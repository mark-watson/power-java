package com.markwatson.deeplearning;

import org.deeplearning4j.datasets.fetchers.CSVDataFetcher;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by markw on 10/5/15.
 */
public class WisconsinDataFetcher extends CSVDataFetcher {

  public WisconsinDataFetcher() throws FileNotFoundException {
    super(new FileInputStream("data/cleaned_wisconsin_cancer_data.csv"), 9);
  }
  @Override
  public void fetch(int i) {
    super.fetch(i);
  }

}
