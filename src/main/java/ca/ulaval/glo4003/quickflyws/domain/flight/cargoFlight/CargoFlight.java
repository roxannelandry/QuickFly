package ca.ulaval.glo4003.quickflyws.domain.flight.cargoFlight;

import java.time.LocalDateTime;

import ca.ulaval.glo4003.quickflyws.domain.flight.Flight;
import ca.ulaval.glo4003.quickflyws.domain.flight.exceptions.InvalidWeightException;

public class CargoFlight extends Flight {

  private double priceByWeight;
  private double availableLuggageCapacity;
  private double maximumLuggageCapacity;

  public CargoFlight(String source, String destination, String date, String company, double priceByWeight, double availableLuggageCapacity) {
    super(source, destination, date, company);

    this.priceByWeight = priceByWeight;
    this.availableLuggageCapacity = availableLuggageCapacity;
    this.maximumLuggageCapacity = availableLuggageCapacity;
  }

  public boolean areInformationsMatching(String company, LocalDateTime passengerFlightDate, String destination, String source) {
    boolean flightMatch = this.company.equals(company) && cargoDateInAcceptedInterval(passengerFlightDate) && this.destination.equals(destination)
        && this.source.equals(source);
    return flightMatch;
  }

  private boolean cargoDateInAcceptedInterval(LocalDateTime passengerFlightDate) {
    return dateChecker.dateIsWithinThreeDays(date, passengerFlightDate);
  }

  public boolean hasEnoughSpace(double luggageWeight) {
    return luggageWeight <= availableLuggageCapacity;
  }

  public void adjustWeightAvailable(int numberOfTickets, double luggageWeight) {
    double newCapacity = availableLuggageCapacity - (luggageWeight * numberOfTickets);
    if (newCapacity < 0) {
      throw new InvalidWeightException("The capacity weight can not be a negative value.");
    }
    this.availableLuggageCapacity = newCapacity;
  }

  public double getPriceForWeight(double luggageWeight) {
    return priceByWeight * luggageWeight;
  }

  public double getMaximumWeight() {
    return maximumLuggageCapacity;
  }

  protected double getAvailableSpace() {
    return availableLuggageCapacity;
  }

  public void checkAvailability(int numberOfTickets, double luggageWeight) {
    double newCapacity = availableLuggageCapacity - (luggageWeight * numberOfTickets);
    if (newCapacity < 0) {
      throw new InvalidWeightException("The capacity weight can not be a negative value.");
    }
  }

}