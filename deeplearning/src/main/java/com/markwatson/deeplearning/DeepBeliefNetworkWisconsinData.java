package com.markwatson.deeplearning;

import org.deeplearning4j.datasets.iterator.DataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.conf.layers.RBM;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.SplitTestAndTrain;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

import org.apache.commons.io.FileUtils;

/**
 * Train a deep belief network on the University of Wisconsin Cancer Data Set.
 */
public class DeepBeliefNetworkWisconsinData {

  private static Logger log = LoggerFactory.getLogger(DeepBeliefNetworkWisconsinData.class);

  public static void main(String[] args) throws Exception {

    final int numInput = 9;
    int outputNum = 2;
    /**
     * F1 scores as a function of the number of hidden layer units hyper-parameter:
     *
     *    numHidden   F1
     *    ---------   --
     *            2   0.50734
     *            3   0.87283 (best value to use - best result for the smallest network)
     *           13   0.87283
     *          100   0.55987 (over fitting)
     *
     *   Other hyper parameters held constant: batchSize = 648
     */
    int numHidden = 3;
    int numberOfLayers = 3; // input, hidden, output
    int numSamples = 648;

    /**
     * F1 scores as a function of the number of batch size:
     *
     *    batchSize   F1
     *    ---------   --
     *           30   0.50000
     *           64   0.78787
     *          323   0.67123
     *          648   0.87283 (best to process all training vectors in one batch)
     *
     *   Other hyper parameters held constant: numHidden = 3
     */
    int batchSize = 648;

    int iterations = 100;
    int fractionOfDataForTraining = (int) (batchSize * 0.7);
    int seed = 33117;

    DataSetIterator iter = new WisconsinDataSetIterator(batchSize, numSamples);
    DataSet next = iter.next();
    next.normalizeZeroMeanZeroUnitVariance();

    //log.info("shuffle input data (this will randomize the resulting F1 scores for a given set of hyper parameters):");
    //next.shuffle();

    SplitTestAndTrain splitDataSets = next.splitTestAndTrain(fractionOfDataForTraining, new Random(seed));
    DataSet train = splitDataSets.getTrain();
    DataSet test = splitDataSets.getTest();

    MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
        .seed(seed) //use the same random seed
        .iterations(iterations)
        .l1(1e-1).regularization(true).l2(2e-4)
        .list(numberOfLayers - 1) // don't count the input layer
        .layer(0,
            new RBM.Builder(RBM.HiddenUnit.RECTIFIED, RBM.VisibleUnit.GAUSSIAN)
                .nIn(numInput)
                .nOut(numHidden)
                // set variance of random initial weights based on input and output layer size:
                .weightInit(WeightInit.XAVIER)
                .dropOut(0.25)
                .build()
        )
        .layer(1, new OutputLayer.Builder(LossFunctions.LossFunction.MCXENT)
            .nIn(numHidden)
            .nOut(outputNum)
            .activation("softmax")
            .build()
        )
        .build();
    MultiLayerNetwork model = new MultiLayerNetwork(conf);
    model.init();
    model.fit(train);

    log.info("\nEvaluating model:\n");
    Evaluation eval = new Evaluation(outputNum);
    INDArray output = model.output(test.getFeatureMatrix());

    for (int i = 0; i < output.rows(); i++) {
      String target = test.getLabels().getRow(i).toString();
      String predicted = output.getRow(i).toString();
      log.info("target: " + target + " predicted: " + predicted);
    }

    eval.eval(test.getLabels(), output);
    log.info(eval.stats());

    /**
     * Save the model for reuse:
     */
    OutputStream fos = Files.newOutputStream(Paths.get("saved-model.bin"));
    DataOutputStream dos = new DataOutputStream(fos);
    Nd4j.write(model.params(), dos);
    dos.flush();
    dos.close();
    FileUtils.writeStringToFile(new File("conf.json"), model.getLayerWiseConfigurations().toJson());

    /**
     * Load saved model and test again:
     */
    log.info("\nLoad saved model from disk:\n");
    MultiLayerConfiguration confFromJson = MultiLayerConfiguration.fromJson(FileUtils.readFileToString(new File("conf.json")));
    DataInputStream dis = new DataInputStream(new FileInputStream("saved-model.bin"));
    INDArray newParams = Nd4j.read(dis);
    dis.close();
    MultiLayerNetwork savedModel = new MultiLayerNetwork(confFromJson);
    savedModel.init();
    savedModel.setParameters(newParams);

    log.info("\nEvaluating model loaded from disk:\n");
    Evaluation eval2 = new Evaluation(outputNum);
    INDArray output2 = savedModel.output(test.getFeatureMatrix());

    for (int i = 0; i < output2.rows(); i++) {
      String target = test.getLabels().getRow(i).toString();
      String predicted = output.getRow(i).toString();
      log.info("target: " + target + " predicted: " + predicted);
    }

    eval2.eval(test.getLabels(), output2);
    log.info(eval2.stats());
  }
}
