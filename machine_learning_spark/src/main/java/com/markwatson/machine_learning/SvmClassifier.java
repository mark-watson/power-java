package com.markwatson.machine_learning;

import org.apache.spark.api.java.JavaDoubleRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.classification.*;
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics;
import scala.Tuple2;

public class SvmClassifier {

  public static double testModel(SVMModel model, double [] features) {
    org.apache.spark.mllib.linalg.Vector inputs = Vectors.dense(features);
    return model.predict(inputs);
  }

  public static void main(String[] args) {
    JavaSparkContext sc = new JavaSparkContext("local", "University of Wisconson Cancer Data");

    // Load and parse the data
    String path = "data/university_of_wisconson_data_.txt";
    JavaRDD<String> data = sc.textFile(path);
    JavaRDD<LabeledPoint> parsedData = data.map(
        new Function<String, LabeledPoint>() {
          public LabeledPoint call(String line) {
            String[] features = line.split(",");
            double label = 0;
            double[] v = new double[features.length - 2];
            for (int i = 0; i < features.length - 2; i++)
              v[i] = Double.parseDouble(features[i + 1]) * 0.09;
            if (features[10].equals("2"))
              label = 0; // benign
            else
              label = 1; // malignant
            return new LabeledPoint(label, Vectors.dense(v));
          }
        }
    );
    // Split initial RDD into two with 70% training data and 30% testing data (13L is a random seed):
    JavaRDD<LabeledPoint>[] splits = parsedData.randomSplit(new double[]{0.7, 0.3}, 13L);
    JavaRDD<LabeledPoint> training = splits[0].cache();
    JavaRDD<LabeledPoint> testing = splits[1];
    training.cache();

    // Building the model
    int numIterations = 500;
    final SVMModel model =
        SVMWithSGD.train(JavaRDD.toRDD(training), numIterations);

    // Evaluate model on training examples and compute training error
    JavaRDD<Tuple2<Double, Double>> valuesAndPreds = testing.map(
        new Function<LabeledPoint, Tuple2<Double, Double>>() {
          public Tuple2<Double, Double> call(LabeledPoint point) {
            double prediction = model.predict(point.features());
            return new Tuple2<Double, Double>(prediction, point.label());
          }
        }
    );
    double MSE = new JavaDoubleRDD(valuesAndPreds.map(
        new Function<Tuple2<Double, Double>, Object>() {
          public Object call(Tuple2<Double, Double> pair) {
            return Math.pow(pair._1() - pair._2(), 2.0);
          }
        }
    ).rdd()).mean();
    System.out.println("Test Data Mean Squared Error = " + MSE);

    // Save and load model and test:
    model.save(sc.sc(), "generated_models");
    SVMModel loaded_model = SVMModel.load(sc.sc(), "generated_models");
    double[] malignant_test_data_1 = {0.81, 0.6, 0.92, 0.8, 0.55, 0.83, 0.88, 0.71, 0.81};
    System.err.println("Should be malignant (close to 1.0): " +
        testModel(loaded_model, malignant_test_data_1));
    double[] benign_test_data_1 = {0.55, 0.25, 0.34, 0.31, 0.29, 0.016, 0.51, 0.01, 0.05};
    System.err.println("Should be benign (close to 0.0): " +
        testModel(loaded_model, benign_test_data_1));
  }
}
