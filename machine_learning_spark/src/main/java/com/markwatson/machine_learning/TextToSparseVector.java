package com.markwatson.machine_learning;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;

/**
 * Copyright Mark Watson 2015. Apache 2 license.
 */
public class TextToSparseVector {

  // The following value might have to be increased for large input texts:
  static private int MAX_WORDS = 80000;

  static private Set<String> noiseWords = new HashSet<>();
  static {
    try {
      Stream<String> lines = Files.lines(Paths.get("data", "stopwords.txt"));
      lines.forEach(s ->
          noiseWords.add(s)
      );
      lines.close();
    } catch (Exception e) { System.err.println(e); }
  }

  private Map<String,Integer> wordMap = new HashMap<>();
  private int startingWordIndex = 0;
  private Map<Integer,String> reverseMap = null;

  public Vector tokensToSparseVector(String[] tokens) {
    List<Integer> indices = new ArrayList();
    for (String token : tokens) {
      String stem = Stemmer.stemWord(token);
      if(! noiseWords.contains(stem) && validWord((stem))) {
        if (! wordMap.containsKey(stem)) {
          wordMap.put(stem, startingWordIndex++);
        }
        indices.add(wordMap.get(stem));
      }
    }
    int[] ind = new int[MAX_WORDS];

    double [] vals = new double[MAX_WORDS];
    for (int i=0, len=indices.size(); i<len; i++) {
      int index = indices.get(i);
      ind[i] = index;
      vals[i] = 1d;
    }
    Vector ret = Vectors.sparse(MAX_WORDS, ind, vals);
    return ret;
  }

  public boolean validWord(String token) {
    if (token.length() < 3)  return false;
    char[] chars = token.toCharArray();
    for (char c : chars) {
      if(!Character.isLetter(c)) return false;
    }
    return true;
  }

  private static int NUM_BEST_WORDS = 20;

  public String [] bestWords(double [] cluster) {
    int [] best = maxKIndex(cluster, NUM_BEST_WORDS);
    String [] ret = new String[NUM_BEST_WORDS];
    if (null == reverseMap) {
      reverseMap = new HashMap<>();
      for(Map.Entry<String,Integer> entry : wordMap.entrySet()){
        reverseMap.put(entry.getValue(), entry.getKey());
      }
    }
    for (int i=0; i<NUM_BEST_WORDS; i++) ret[i] = reverseMap.get(best[i]);
    return ret;
  }
  // following method found on Stack Overflow, written by user3879337
  private  int[] maxKIndex(double[] array, int top_k) {
    double[] max = new double[top_k];
    int[] maxIndex = new int[top_k];
    Arrays.fill(max, Double.NEGATIVE_INFINITY);
    Arrays.fill(maxIndex, -1);

    top: for(int i = 0; i < array.length; i++) {
      for(int j = 0; j < top_k; j++) {
        if(array[i] > max[j]) {
          for(int x = top_k - 1; x > j; x--) {
            maxIndex[x] = maxIndex[x-1]; max[x] = max[x-1];
          }
          maxIndex[j] = i; max[j] = array[i];
          continue top;
        }
      }
    }
    return maxIndex;
  }
}
