package ca.ulaval.glo4003.quickflyws.dto;

import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.weightCategory.WeightCategory;

public class FlightDto {

  public String source;
  public String destination;
  public String date;
  public String company;
  public WeightCategory flightCategory;
  public boolean isAirVivant;
  public boolean hasAirCargo;

  public int seatsEconomicAvailable;
  public double seatsEconomicPrice;
  public int numberOfSeatsEconomic;

  public int seatsRegularAvailable;
  public double seatsRegularPrice;
  public int numberOfSeatsRegular;

  public int seatsBusinessAvailable;
  public double seatsBusinessPrice;
  public int numberOfSeatsBusiness;

  public double exceedingWeightAvailable;
  public double maximumExceedingWeight;

}