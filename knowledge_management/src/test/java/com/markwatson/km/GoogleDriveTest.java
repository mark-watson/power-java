package com.markwatson.km;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.fortuna.ical4j.data.ParserException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.tika.exception.TikaException;
import org.apache.xmlbeans.XmlException;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Unit test for chapter on knowledge management
 */
public class GoogleDriveTest extends TestCase {

  public GoogleDriveTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    return new TestSuite(GoogleDriveTest.class);
  }

  public void testDocxToText() throws InvalidFormatException, XmlException, IOException, TikaException, SAXException, ParserException {
    String contents = PoiMicrosoftFileReader.DocxToText("GoogleTakeout/Drive/booktest/Test document 1.docx");
    System.err.println("Contents of test Word docx file:\n\n" + contents + "\n");
  }
  public void testExcelReader() throws InvalidFormatException, XmlException, IOException, TikaException, SAXException, ParserException {
    ExcelData spreadsheet = PoiMicrosoftFileReader.readXlsx("GoogleTakeout/Drive/booktest/Test spreadsheet 1.xlsx");
    System.err.println("Contents of test spreadsheet:\n\n" + spreadsheet);
  }
  public void testCalendarFileReader() throws InvalidFormatException, XmlException, IOException, TikaException, SAXException, ParserException {
    ReadCalendarFiles calendar = new ReadCalendarFiles("GoogleTakeout/Calendar/Mark Watson.ics");
    System.err.println("\n\nTest iCal calendar file has " + calendar.size() + " calendar entries.");
    for (int i=0, size=calendar.size(); i<size; i++) {
      Map<String,String> entry = calendar.getCalendarEntry(i);
      System.err.println("\n\tNext calendar entry:");
      System.err.println("\n\t\tDTSAMP:\t" + entry.getOrDefault("DTSTAMP", ""));
      System.err.println("\n\t\tSUMMARY:\t" + entry.getOrDefault("SUMMARY", ""));
      System.err.println("\n\t\tDESCRIPTION:\t" + entry.getOrDefault("DESCRIPTION", ""));
    }
  }
  public void testMboxFileReader() throws InvalidFormatException, XmlException, IOException, TikaException, SAXException, ParserException {
    ReadMbox mbox = new ReadMbox("GoogleTakeout/Mail/bookexample.mbox");
    System.err.println("\nMBOX size = " + mbox.size());
    for (int i=0, size=mbox.size(); i<size; i++) {
      System.err.println("\n* * next email:\n\n" + mbox.getEmail(i) + "\n");
    }
  }
}

