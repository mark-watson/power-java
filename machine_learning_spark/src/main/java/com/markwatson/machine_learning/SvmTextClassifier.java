package com.markwatson.machine_learning;

import org.apache.spark.api.java.JavaDoubleRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.classification.SVMModel;
import org.apache.spark.mllib.classification.SVMWithSGD;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.regression.LabeledPoint;
import scala.Tuple2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Created by markw on 11/25/15.
 */
public class SvmTextClassifier {
  private final static String input_file = "test_classification.txt";

  private static TextToSparseVector sparseVectorGenerator = new TextToSparseVector();
  private static final Pattern SPACE = Pattern.compile(" ");

  static private String [] tokenizeAndRemoveNoiseWords(String s) {
    return s.toLowerCase().replaceAll("\\.", " \\. ").replaceAll(",", " , ")
        .replaceAll("\\?", " ? ").replaceAll("\n", " ").replaceAll(";", " ; ").split(" ");
  }

  static private Map<String, Integer> label_to_index = new HashMap<>();
  static private int label_index_count = 0;
  static private Map<String, String> map_to_print_original_text = new HashMap<>();

  private static class ParsePoint implements Function<String, LabeledPoint> {

    @Override
    public LabeledPoint call(String line) {
      String [] data_split = line.split("\t");
      String label = data_split[0];
      Integer label_index = label_to_index.get(label);
      if (null == label_index) {
        label_index = label_index_count;
        label_to_index.put(label, label_index_count++);
      }
      Vector tok = sparseVectorGenerator.tokensToSparseVector(tokenizeAndRemoveNoiseWords(data_split[1]));
      //System.out.println("** " + line + " : " + label_index + ":" + tok.compressed());
      // Save original text, indexed by compressed features, for later display (for debug ony):
      map_to_print_original_text.put(tok.compressed().toString(), line);
      return new LabeledPoint(label_index, tok);
    }
  }

  public static void main(String[] args) throws IOException {

    JavaSparkContext sc = new JavaSparkContext("local", "WikipediaKMeans");

    JavaRDD<String> lines = sc.textFile("data/" + input_file);

    JavaRDD<LabeledPoint> points = lines.map(new ParsePoint());

    // Split initial RDD into two with 70% training data and 30% testing data (13L is a random seed):
    JavaRDD<LabeledPoint>[] splits = points.randomSplit(new double[]{0.7, 0.3}, 13L);
    JavaRDD<LabeledPoint> training = splits[0].cache();
    JavaRDD<LabeledPoint> testing = splits[1];
    training.cache();

    // Building the model
    int numIterations = 500;
    final SVMModel model =
        SVMWithSGD.train(JavaRDD.toRDD(training), numIterations);
    model.clearThreshold();
    // Evaluate model on testing examples and compute training error
    JavaRDD<Tuple2<Double, Double>> valuesAndPreds = testing.map(
        new Function<LabeledPoint, Tuple2<Double, Double>>() {
          public Tuple2<Double, Double> call(LabeledPoint point) {
            double prediction = model.predict(point.features());
            System.out.println(" ++ prediction: " + prediction + " original: " + map_to_print_original_text.get(point.features().compressed().toString()));
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

    sc.stop();
  }

  static private Set<String> noiseWords = new HashSet<>();
  static {
    try {
      Stream<String> lines = Files.lines(Paths.get("data", "stopwords.txt"));
      lines.forEach(s -> noiseWords.add(s));
      lines.close();
    } catch (Exception e) { System.err.println(e); }
  }

}
