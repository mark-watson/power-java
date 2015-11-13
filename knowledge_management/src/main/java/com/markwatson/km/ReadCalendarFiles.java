package com.markwatson.km;

// refenerence: https://github.com/ical4j/ical4j

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ReadCalendarFiles {

  public ReadCalendarFiles(String filePath) throws IOException, ParserException {
    Map<String, String> calendarEntry = null;
    FileInputStream fin = new FileInputStream(filePath);
    CalendarBuilder builder = new CalendarBuilder();
    net.fortuna.ical4j.model.Calendar calendar = builder.build(fin);
    for (Iterator i = calendar.getComponents().iterator(); i.hasNext(); ) {
      Component component = (Component) i.next();
      if (component.getName().equalsIgnoreCase("VEVENT")) {
        calendarEntry = new HashMap<>();
        for (Iterator j = component.getProperties().iterator(); j.hasNext(); ) {
          net.fortuna.ical4j.model.Property property = (Property) j.next();
          calendarEntry.put(property.getName(), property.getValue());
        }
        calendarEntries.add(calendarEntry);
      }
    }
  }

  public int size() { return calendarEntries.size(); }
  public Map<String, String> getCalendarEntry(int index) { return calendarEntries.get(index); }

  /**
   * List of calendar entries where each entry is a Map<String, String>
   *
   */
  private List<Map<String, String>> calendarEntries = new ArrayList<>();

  public static void main(String[] args) throws IOException, ParserException {
    new ReadCalendarFiles("GoogleTakeout/Calendar/Mark Watson.ics");
  }
}
