package com.markwatson.machine_learning;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class HelloSpark {

  static private List<String> tokenize(String s) {
    return Arrays.asList(s.replaceAll("\\.", " \\. ").replaceAll(",", " , ")
        .replaceAll(";", " ; ").split(" "));
  }

  static public void main(String[] args) {
    JavaSparkContext sc = new JavaSparkContext("local", "Hello Spark");

    JavaRDD<String> lines = sc.textFile("data/test1.txt");
    JavaRDD<String> tokens = lines.flatMap(line -> tokenize(line));
    JavaPairRDD<String, Integer> counts =
        tokens.mapToPair(
            token ->
                new Tuple2<String, Integer>(token.toLowerCase(), 1))
            .reduceByKey((count1, count2) -> count1 + count2);
    Map countMap = counts.collectAsMap();
    System.out.println(countMap);
    List<Tuple2<String, Integer>> collection = counts.collect();
    System.out.println(collection);
  }
}