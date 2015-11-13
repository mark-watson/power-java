package com.markwatson.km;

import java.io.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.microsoft.ooxml.OOXMLParser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.xmlbeans.XmlException;
import org.xml.sax.SAXException;

public class PoiMicrosoftFileReader {

  static private boolean DEBUG_PRINT_META_DATA = false;

  public static String DocxToText(String docxFilePath)
      throws IOException, InvalidFormatException, XmlException, TikaException {
    String ret = "";
    FileInputStream fis = new FileInputStream(docxFilePath);
    Tika tika = new Tika();
    ret = tika.parseToString(fis);
    fis.close();
    return ret;
  }

  public static ExcelData readXlsx(String xlsxFilePath)
      throws IOException, InvalidFormatException, XmlException, TikaException, SAXException {
    BodyContentHandler bcHandler = new BodyContentHandler();
    Metadata metadata = new Metadata();
    FileInputStream inputStream = new FileInputStream(new File(xlsxFilePath));
    ParseContext pcontext = new ParseContext();
    OOXMLParser parser = new OOXMLParser();
    parser.parse(inputStream, bcHandler, metadata, pcontext);
    if (DEBUG_PRINT_META_DATA) {
      System.err.println("Metadata:");
      for (String name : metadata.names())
        System.out.println(name + "\t:\t" + metadata.get(name));
    }
    ExcelData spreedsheet = new ExcelData(bcHandler.toString());
    return spreedsheet;
  }
}
