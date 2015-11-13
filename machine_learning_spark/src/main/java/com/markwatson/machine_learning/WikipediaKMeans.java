/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// This example is derived from the Spark MLlib example:
// in spark-1.3.1/examples/src/main/java/org/apache/spark/examples/mllib/WikipediaKMeans.java

package com.markwatson.machine_learning;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vector;

public class WikipediaKMeans {

  // there are two example files in ./data/ : one with 41 articles and one with 2001
  //private final static String input_file = "wikipedia_41_lines.txt";
  private final static String input_file = "wikipedia_2001_lines.txt";

  private static TextToSparseVector sparseVectorGenerator = new TextToSparseVector();
  private static final Pattern SPACE = Pattern.compile(" ");

  static private String [] tokenizeAndRemoveNoiseWords(String s) {
    return s.toLowerCase().replaceAll("\\.", " \\. ").replaceAll(",", " , ")
        .replaceAll("\\?", " ? ").replaceAll("\n", " ").replaceAll(";", " ; ").split(" ");
  }

  private static class ParsePoint implements Function<String, Vector> {

    @Override
    public Vector call(String line) {
      String[] tok = tokenizeAndRemoveNoiseWords(line);
      return sparseVectorGenerator.tokensToSparseVector(tok);
    }
  }

  public static void main(String[] args) throws IOException {

    int number_of_clusters = 8;
    int iterations = 100;
    int runs = 1;

    JavaSparkContext sc = new JavaSparkContext("local", "WikipediaKMeans");

    JavaRDD<String> lines = sc.textFile("data/" + input_file);

    JavaRDD<Vector> points = lines.map(new ParsePoint());

    KMeansModel model = KMeans.train(points.rdd(), number_of_clusters, iterations, runs, KMeans.K_MEANS_PARALLEL());

    System.out.println("Cluster centers:");
    for (Vector center : model.clusterCenters()) {
      System.out.println("\n " + center);
      String [] bestWords = sparseVectorGenerator.bestWords(center.toArray());
      System.out.println(" bestWords: " + Arrays.asList(bestWords));
    }
    double cost = model.computeCost(points.rdd());
    System.out.println("Cost: " + cost);

    // Print out documents by cluster index. Note: this is really inefficient
    // because I am cycling through the input file number_of_clusters times.
    // In a normal application the cluster index for each document would be saved
    // as metadata for each document. So, please consider the following loop and
    // the method printClusterIndex to be only for pretty-printing the results
    // of this example program:
    for (int i=0; i<number_of_clusters; i++)
      printClusterIndex(i, model);

    sc.stop();
  }

  static private void printClusterIndex(int clusterIndex, KMeansModel model) throws IOException {
    System.out.println("\nDOCUMENTS IN CLUSTER INDEX " + clusterIndex + "\n");
    // re-read each "document* (single line in input file
    // and predict which cluster it belongs to:
    Stream<String> lines2 = Files.lines(Paths.get("data", input_file));
    lines2.forEach(s ->
        {
          String[] parts = s.split("\t");
          String[] tok = tokenizeAndRemoveNoiseWords(parts[1]);
          Vector v = sparseVectorGenerator.tokensToSparseVector(tok);
          int best_cluster_index = model.predict(v);
          if (best_cluster_index == clusterIndex)
            System.out.println("   Article title: " + parts[0]);
        }
    );
    lines2.close();
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
