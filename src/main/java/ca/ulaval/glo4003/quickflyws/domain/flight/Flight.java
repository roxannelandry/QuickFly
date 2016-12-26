package ca.ulaval.glo4003.quickflyws.domain.flight;

import java.time.LocalDateTime;

import ca.ulaval.glo4003.quickflyws.domain.dateConverterChecker.DateConverterChecker;

public abstract class Flight {

  protected String source;
  protected String destination;
  protected LocalDateTime date;
  protected String company;
  
  protected DateConverterChecker dateChecker;
  
  public Flight(String source, String destination, String date, String company) {
    this.dateChecker = new DateConverterChecker();

    this.source = source;
    this.destination = destination;
    this.date = dateChecker.dateStringToDateTimeObject(date);
    this.company = company;
  }
  
  public String getSource() {
    return source;
  }

  public String getDestination() {
    return destination;
  }

  public LocalDateTime getDate() {
    return date;
  }

  public String getCompany() {
    return company;
  }

}