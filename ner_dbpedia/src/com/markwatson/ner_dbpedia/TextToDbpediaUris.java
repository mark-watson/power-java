package com.markwatson.ner_dbpedia;

public class TextToDbpediaUris {
  private TextToDbpediaUris() {
  }

  public TextToDbpediaUris(String text) {
    String[] tokens = tokenize(text + " . . .");
    String s = "";
    for (int i = 0, size = tokens.length - 2; i < size; i++) {
      String n2gram = tokens[i] + " " + tokens[i + 1];
      String n3gram = n2gram + " " + tokens[i + 2];
      // check for 3grams:
      if ((s = NerMaps.broadcastNetworks.get(n3gram)) != null) {
        log("broadcastNetwork", i, i + 2, n3gram, s);
        i += 2;
        continue;
      }
      if ((s = NerMaps.cityNames.get(n3gram)) != null) {
        log("city", i, i + 2, n3gram, s);
        i += 2;
        continue;
      }
      if ((s = NerMaps.companyames.get(n3gram)) != null) {
        log("company", i, i + 2, n3gram, s);
        i += 2;
        continue;
      }
      if ((s = NerMaps.countryNames.get(n3gram)) != null) {
        log("country", i, i + 2, n3gram, s);
        i += 2;
        continue;
      }
      if ((s = NerMaps.musicGroupNames.get(n3gram)) != null) {
        log("musicGroup", i, i + 2, n3gram, s);
        i += 2;
        continue;
      }
      if ((s = NerMaps.personNames.get(n3gram)) != null) {
        log("person", i, i + 2, n3gram, s);
        i += 2;
        continue;
      }
      if ((s = NerMaps.politicalPartyNames.get(n3gram)) != null) {
        log("politicalParty", i, i + 2, n3gram, s);
        i += 2;
        continue;
      }
      if ((s = NerMaps.tradeUnionNames.get(n3gram)) != null) {
        log("tradeUnion", i, i + 2, n3gram, s);
        i += 2;
        continue;
      }
      if ((s = NerMaps.universityNames.get(n3gram)) != null) {
        log("unitersity", i, i + 2, n3gram, s);
        i += 2;
        continue;
      }

      // check for 2grams:
      if ((s = NerMaps.broadcastNetworks.get(n2gram)) != null) {
        log("broadCastNetwork", i, i + 1, n2gram, s);
        i += 1;
        continue;
      }
      if ((s = NerMaps.cityNames.get(n2gram)) != null) {
        log("city", i, i + 1, n2gram, s);
        i += 1;
        continue;
      }
      if ((s = NerMaps.companyames.get(n2gram)) != null) {
        log("company", i, i + 1, n2gram, s);
        i += 1;
        continue;
      }
      if ((s = NerMaps.countryNames.get(n2gram)) != null) {
        log("country", i, i + 1, n2gram, s);
        i += 1;
        continue;
      }
      if ((s = NerMaps.musicGroupNames.get(n2gram)) != null) {
        log("musicGroup", i, i + 1, n2gram, s);
        i += 1;
        continue;
      }
      if ((s = NerMaps.personNames.get(n2gram)) != null) {
        log("person", i, i + 1, n2gram, s);
        i += 1;
        continue;
      }
      if ((s = NerMaps.politicalPartyNames.get(n2gram)) != null) {
        log("politicalParty", i, i + 1, n2gram, s);
        i += 1;
        continue;
      }
      if ((s = NerMaps.tradeUnionNames.get(n2gram)) != null) {
        log("tradeUnion", i, i + 1, n2gram, s);
        i += 1;
        continue;
      }
      if ((s = NerMaps.universityNames.get(n2gram)) != null) {
        log("unitersity", i, i + 1, n2gram, s);
        i += 1;
        continue;
      }

      // check for 1grams:
      if ((s = NerMaps.broadcastNetworks.get(tokens[i])) != null) {
        log("broadCastNetwork", i, i + 1, tokens[i], s);
        continue;
      }
      if ((s = NerMaps.cityNames.get(tokens[i])) != null) {
        log("city", i, i + 1, tokens[i], s);
        continue;
      }
      if ((s = NerMaps.companyames.get(tokens[i])) != null) {
        log("company", i, i + 1, tokens[i], s);
        continue;
      }
      if ((s = NerMaps.countryNames.get(tokens[i])) != null) {
        log("country", i, i + 1, tokens[i], s);
        continue;
      }
      if ((s = NerMaps.musicGroupNames.get(tokens[i])) != null) {
        log("musicGroup", i, i + 1, tokens[i], s);
        continue;
      }
      if ((s = NerMaps.personNames.get(tokens[i])) != null) {
        log("person", i, i + 1, tokens[i], s);
        continue;
      }
      if ((s = NerMaps.politicalPartyNames.get(tokens[i])) != null) {
        log("politicalParty", i, i + 1, tokens[i], s);
        continue;
      }
      if ((s = NerMaps.tradeUnionNames.get(tokens[i])) != null) {
        log("tradeUnion", i, i + 1, tokens[i], s);
        continue;
      }
      if ((s = NerMaps.universityNames.get(tokens[i])) != null) {
        log("unitersity", i, i + 1, tokens[i], s);
      }
    }
  }

  /** In your applications you will want to subclass TextToDbpediaUris
   * and override <strong>log</strong> to do something with the
   * DBPedia entities that you have identified.
   * @param nerType
   * @param index1
   * @param index2
   * @param ngram
   * @param uri
   */
  void log(String nerType, int index1, int index2, String ngram, String uri) {
    System.out.println(nerType + "\t" + index1 + "\t" + index2 + "\t" + ngram + "\t" + uri);
  }

  private String[] tokenize(String s) {
    return s.replaceAll("\\.", " \\. ").replaceAll(",", " , ")
        .replaceAll("\\?", " ? ").replaceAll("\n", " ").replaceAll(";", " ; ").split(" ");
  }
}