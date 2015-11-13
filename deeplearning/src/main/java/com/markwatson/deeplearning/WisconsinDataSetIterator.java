package com.markwatson.deeplearning;

import org.deeplearning4j.datasets.iterator.BaseDatasetIterator;

import java.io.FileNotFoundException;


public class WisconsinDataSetIterator extends BaseDatasetIterator {
  private static final long serialVersionUID = -2023454995728682368L;

  public WisconsinDataSetIterator(int batch, int numExamples) throws FileNotFoundException {
    super(batch, numExamples, new WisconsinDataFetcher());
  }
}