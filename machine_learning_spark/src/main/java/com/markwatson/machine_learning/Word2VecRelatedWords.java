package com.markwatson.machine_learning;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.feature.Word2Vec;
import org.apache.spark.mllib.feature.Word2VecModel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import scala.Tuple2;

public class Word2VecRelatedWords {
  // Use the example data file form the deeplearning4j.org word2vec example:
  private final static String input_file = "data/raw_sentences.txt";

  static private List<String> tokenizeAndRemoveNoiseWords(String s) {
    return Arrays.asList(s.toLowerCase().replaceAll("\\.", " \\. ").replaceAll(",", " , ")
        .replaceAll("\\?", " ? ").replaceAll("\n", " ").replaceAll(";", " ; ").split(" "))
        .stream().filter((w) -> ! noiseWords.contains(w)).collect(Collectors.toList());
  }

  public static void main(String[] args) throws IOException {
    JavaSparkContext sc = new JavaSparkContext("local", "WikipediaKMeans");

    String sentence = new String(Files.readAllBytes(Paths.get(input_file)));
    List<String> words = tokenizeAndRemoveNoiseWords(sentence);
    List<List<String>> localWords = Arrays.asList(words, words);
    Word2Vec word2vec = new Word2Vec().setVectorSize(10).setSeed(113L);
    JavaRDD<List<String>> rdd_word_list = sc.parallelize(localWords);
    Word2VecModel model = word2vec.fit(rdd_word_list);
    
    Tuple2<String, Object>[] synonyms = model.findSynonyms("day", 15);
    for (Object obj : synonyms) System.err.println("words associated with 'day': " + obj);

    synonyms = model.findSynonyms("children", 15);
    for (Object obj : synonyms) System.err.println("words associated with 'children': " + obj);

    synonyms = model.findSynonyms("people", 15);
    for (Object obj : synonyms) System.err.println("words associated with 'people': " + obj);

    synonyms = model.findSynonyms("three", 15);
    for (Object obj : synonyms) System.err.println("words associated with 'three': " + obj);

    synonyms = model.findSynonyms("man", 15);
    for (Object obj : synonyms) System.err.println("words associated with 'man': " + obj);

    synonyms = model.findSynonyms("women", 15);
    for (Object obj : synonyms) System.err.println("words associated with 'women': " + obj);

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

