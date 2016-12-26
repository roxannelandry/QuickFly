package ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight;

import ca.ulaval.glo4003.quickflyws.domain.flight.exceptions.InvalidWeightException;
import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.weightCategory.WeightCategory;

public class MediumFlight extends PassengerFlight {

  private static final double MAXIMUM_WEIGHT = 42.0;

  private WeightCategory flightWeightCategory;

  public MediumFlight(String source, String destination, String date, String company, int seatsEconomic, double seatsEconomicPrice, int seatsRegular,
      double seatsRegularPrice, int seatsBusiness, double seatsBusinessPrice, boolean isAirVivant) {
    super(source, destination, date, company, seatsEconomic, seatsEconomicPrice, seatsRegular, seatsRegularPrice, seatsBusiness, seatsBusinessPrice,
        isAirVivant);

    this.flightWeightCategory = WeightCategory.AIR_MOYEN;
  }

  @Override
  public boolean hasEnoughExceedingWeightAvailable(double luggageWeight) {
    return false;
  }

  @Override
  public boolean weightIsExceeding(double luggageWeight) {
    return luggageWeight > MAXIMUM_WEIGHT;
  }

  @Override
  public void adjustExceedingWeightAvailable(int numberOfTickets, double luggageWeight) {
  }

  @Override
  public boolean hasValidWeight(double luggageWeight, int numberOfTickets) {
    if (weightIsExceeding(luggageWeight)) {
      return false;
    }
    return true;
  }

  @Override
  public double getExceedingWeightAvailable() {
    return 0;
  }

  @Override
  public WeightCategory getFlightCategory() {
    return flightWeightCategory;
  }

  @Override
  public double getMaximumExceedingWeight() {
    return 0;
  }

  @Override
  public void adjustMaximumExceedingWeight(double exceedingWeightAvailable) {
    throw new InvalidWeightException("You can not set an exceeding weight on a medium flight.");
  }

  @Override
  public double getExtraFeesForExceedingWeight(double luggageWeight) {
    return 0;
  }

  @Override
  public String flightToString() {
    String separator = System.getProperty("line.separator");
    String passengerFlightInString = "Flight Details" + separator;
    passengerFlightInString += "Source : " + source + separator + "Destination : " + destination + separator + "Date : " + date.toLocalDate()
        + separator + "Departure time : " + date.toLocalTime() + separator + "Company : " + company + separator + isAirVivantToString() + separator;
    passengerFlightInString += "Weight Category : " + flightWeightCategory + separator;

    return passengerFlightInString;
  }

}