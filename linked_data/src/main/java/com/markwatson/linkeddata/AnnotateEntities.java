package com.markwatson.linkeddata;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by markw on 6/5/15.
 */
public class AnnotateEntities {

  /**
   * Note - this method would be more accurate and more efficient if we properly tokenized text using the tokenization
   *  model in the chapter on natural language processing and matched adjacent words. Here, we will do something
   * simpler: we will add white space around punctuation characters and clean text by removing multiple
   * adjacent spaces, etc. and just match keys in the entity maps against the text.
   *
   * After the following method has been hotspotted, it takes about 10 milliseconds on my laptop
   * to process a few sentences of text - not very efficient.
   *
   * @param text - input text to annotate with DBPedia URIs for entities
   * @return original text with embedded annotations
   */
  static public String annotate(String text) {
    String s = text.replaceAll("\\.", " !! ").replaceAll(",", " ,").replaceAll(";", " ;").replaceAll("  ", " ");
    for (String entity : companyMap.keySet())
      if (s.indexOf(entity) > -1) s = s.replaceAll(entity, entity + " (" + companyMap.get(entity) + ")");
    for (String entity : countryMap.keySet())
      if (s.indexOf(entity) > -1) s = s.replaceAll(entity, entity + " (" + countryMap.get(entity) + ")");
    for (String entity : peopleMap.keySet())
      if (s.indexOf(entity) > -1) s = s.replaceAll(entity, entity + " (" + peopleMap.get(entity) + ")");
    return s.replaceAll(" !!", ".");
  }

  static private Map<String, String> companyMap = new HashMap<>();
  static private Map<String, String> countryMap = new HashMap<>();
  static private Map<String, String> peopleMap = new HashMap<>();
  static private void loadMap(Map<String, String> map, String entityFilePath) {
    try {
      InputStream fis = new FileInputStream(entityFilePath);
      InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
      BufferedReader br = new BufferedReader(isr);
      String line = null;
      while ((line = br.readLine()) != null) {
        int index = line.indexOf('\t');
        if (index > -1) {
          String name = line.substring(0, index);
          String uri = line.substring((index + 1));
          map.put(name, uri);
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  static {
    loadMap(companyMap, "dbpedia_entities/CompanyNamesDbPedia.txt");
    loadMap(countryMap, "dbpedia_entities/CountryNamesDbPedia.txt");
    loadMap(peopleMap, "dbpedia_entities/PeopleDbPedia.txt");
  }
}
