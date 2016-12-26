package ca.ulaval.glo4003.quickflyws.dto;

public class FlightSearchInfos {

  public String date;
  public String destination;
  public String source;
  public double luggageWeight;
  public boolean wantAirVivant;
  public boolean airCargoAllowed;
  public boolean wantEconomic;
  public boolean wantRegular;
  public boolean wantBusiness;

  public FlightSearchInfos(String date, String destination, String source, double luggageWeight, boolean wantAirVivant, boolean airCargoAllowed,
      boolean wantEconomic, boolean wantRegular, boolean wantBusiness) {
    this.date = date;
    this.destination = destination;
    this.source = source;
    this.luggageWeight = luggageWeight;
    this.wantAirVivant = wantAirVivant;
    this.airCargoAllowed = airCargoAllowed;
    this.wantEconomic = wantEconomic;
    this.wantRegular = wantRegular;
    this.wantBusiness = wantBusiness;
  }
}
