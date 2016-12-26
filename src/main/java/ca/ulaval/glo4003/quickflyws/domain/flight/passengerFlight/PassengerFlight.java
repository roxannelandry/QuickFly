package ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight;

import java.time.LocalDateTime;

import ca.ulaval.glo4003.quickflyws.domain.flight.Flight;
import ca.ulaval.glo4003.quickflyws.domain.flight.exceptions.InvalidWeightException;
import ca.ulaval.glo4003.quickflyws.domain.flight.exceptions.NotEnoughAvailableSeatsException;
import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.seats.Seats;
import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.seats.seatCategory.SeatCategory;
import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.weightCategory.WeightCategory;

public abstract class PassengerFlight extends Flight {

  protected boolean isAirVivant;
  protected Seats seats = new Seats();

  public PassengerFlight(String source, String destination, String date, String company, int seatsEconomic, double seatsEconomicPrice,
      int seatsRegular, double seatsRegularPrice, int seatsBusiness, double seatsBusinessPrice, boolean isAirVivant) {
    super(source, destination, date, company);

    this.isAirVivant = isAirVivant;
    this.seats.createSeatsForFlight(seatsEconomic, seatsEconomicPrice, seatsRegular, seatsRegularPrice, seatsBusiness, seatsBusinessPrice);
  }

  public abstract WeightCategory getFlightCategory();

  public abstract boolean weightIsExceeding(double luggageWeight);

  public abstract boolean hasEnoughExceedingWeightAvailable(double luggageWeight);

  public abstract double getExceedingWeightAvailable();

  public abstract double getMaximumExceedingWeight();

  public abstract void adjustExceedingWeightAvailable(int numberOfTickets, double luggageWeight);

  public abstract void adjustMaximumExceedingWeight(double exceedingWeightAvailable);

  public abstract double getExtraFeesForExceedingWeight(double weight);

  public abstract boolean hasValidWeight(double luggageWeight, int numberOfTickets);

  public abstract String flightToString();

  public boolean hasAvailableSeats(boolean wantEconomic, boolean wantRegular, boolean wantBusiness) {
    if (wantEconomic && seats.getNumberSeatsAvailable(SeatCategory.ECONOMIC) > 0) {
      return true;
    } else if (wantRegular && seats.getNumberSeatsAvailable(SeatCategory.REGULAR) > 0) {
      return true;
    } else if (wantBusiness && seats.getNumberSeatsAvailable(SeatCategory.BUSINESS) > 0) {
      return true;
    } else if (getFlightCategory().equals(WeightCategory.AIR_LEGER) && seats.getNumberSeatsAvailable(SeatCategory.REGULAR) > 0) {
      return true;
    }
    return false;
  }

  public void adjustQuantityOfAvailableSeat(SeatCategory seatCategory, int numberOfTickets) {
    if (!hasAvailableSeatsForThisCategory(seatCategory, numberOfTickets)) {
      throw new NotEnoughAvailableSeatsException("Not enough tickets for this flights, please enter a new amount of tickets");
    }
    if (numberOfTickets > 0) {
      seats.decreaseNumberOfAvailableSeats(seatCategory, numberOfTickets);
    } else if (numberOfTickets < 0) {
      seats.addReleasedSeats(seatCategory, numberOfTickets);
    }
  }

  public void adjustAvailability(SeatCategory seatCategory, int numberOfTickets, boolean hasAirCargo, double luggageWeight) {
    adjustQuantityOfAvailableSeat(seatCategory, numberOfTickets);
    if (!hasAirCargo) {
      adjustExceedingWeightAvailable(numberOfTickets, luggageWeight);
    }
  }

  public void checkAvailability(SeatCategory seatCategory, int numberOfTickets, boolean hasAirCargo, double luggageWeight) {
    if (!hasAvailableSeatsForThisCategory(seatCategory, numberOfTickets)) {
      throw new NotEnoughAvailableSeatsException("Not enough tickets for this flight");
    }
    if (!hasAirCargo && !hasValidWeight(luggageWeight, numberOfTickets)) {
      throw new InvalidWeightException("Not enough room for your luggage in this flight.");
    }

  }

  public boolean areInformationsMatching(LocalDateTime date, String destination, String source) {
    boolean flightMatch = this.source.equals(source) && this.destination.equals(destination) && dateIsEarlierThanFlightDate(date, this.date);
    return flightMatch;
  }

  private boolean dateIsEarlierThanFlightDate(LocalDateTime date, LocalDateTime flightDate) {
    return dateChecker.firstDateIsEqualOrEarlier(date, flightDate);
  }

  public boolean isAirVivant() {
    return isAirVivant;
  }

  public boolean hasAvailableSeatsForThisCategory(SeatCategory seatCategory, int numberOfTickets) {
    return getNumberOfSeatsAvailable(seatCategory) >= numberOfTickets;
  }

  public int getNumberOfSeatsAvailable(SeatCategory seatCategory) {
    return seats.getNumberSeatsAvailable(seatCategory);
  }

  public int getNumberEconomicSeats() {
    return seats.getTotalNumberSeats(SeatCategory.ECONOMIC);
  }

  public int getNumberRegularSeats() {
    return seats.getTotalNumberSeats(SeatCategory.REGULAR);
  }

  public int getNumberBusinessSeats() {
    return seats.getTotalNumberSeats(SeatCategory.BUSINESS);
  }

  public double getSeatPrice(SeatCategory seatCategory) {
    return seats.getPriceByCategory(seatCategory);
  }

  protected String isAirVivantToString() {
    String airVivantToString;
    if (isAirVivant) {
      airVivantToString = "AirVivant : yes";
    } else {
      airVivantToString = "AirVivant : no";
    }
    return airVivantToString;
  }

}