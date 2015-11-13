package com.markwatson.km;

// reference: https://tika.apache.org/

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mbox.MboxParser;
import org.apache.tika.parser.microsoft.ooxml.OOXMLParser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.xmlbeans.XmlException;
import org.xml.sax.SAXException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by markw on 9/17/15.
 */
public class ReadMbox {

  private String getTextContents(InputStream stream) {
    BufferedReader br = new BufferedReader(new InputStreamReader(stream));
    StringBuffer sb = new StringBuffer();
    String subject = "";
    String from = "";
    String to = "";
    try {
      String line = null;
      boolean inText = false;
      while ((line = br.readLine()) != null) {
        //System.err.println("-- line: " + line);
        if (!inText) {
          if (line.startsWith("Subject:")) subject = line.substring(9);
          if (line.startsWith("To:"))      to = line.substring(4);
          if (line.startsWith("From:"))    from = line.substring(6);
        }
        if (line.startsWith("Content-Type: text/plain;")) {
          inText = true;
          br.readLine();
        } else if (inText) {
          if (line.startsWith("-----")) break;
          if (line.startsWith("--_---")) break;
          if (line.startsWith("Content-Type: text/html;")) break;
          sb.append(line + "\n");
        }
      }
    } catch (Exception ex) {
      System.err.println("ERROR: " + ex);
    }
    return "To: " + to + "\nFrom: " + from + "\nSubject: " + subject + "\n" + sb.toString();
  }

  public ReadMbox(String filePath)
      throws IOException, InvalidFormatException, XmlException, TikaException, SAXException {

    FileInputStream inputStream = new FileInputStream(new File(filePath));

    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

    String line = null;
    StringBuffer sb = new StringBuffer();
    try {
      // the outer loop splits each email to a separate string that we will use Tika to parse:
      while ((line = br.readLine()) != null) {
        if (line.startsWith("From ")) {
          if (sb.length() > 0) { // process all but the last email
            String content = sb.toString();
            InputStream stream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
            emails.add(getTextContents(stream));
            sb = new StringBuffer();
          }
        }
        sb.append(line + "\n");
      }
      if (sb.length() > 0) { // process the last email
        String content = sb.toString();
        InputStream stream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        emails.add(getTextContents(stream));
      }

      br.close();
    } catch (Exception ex) {
      System.err.println("ERROR: " + ex);
    }
  }

  public int size() {
    return emails.size();
  }

  public String getEmail(int index) {
    return emails.get(index);
  }

  public List<String> emails = new ArrayList<>();
}
