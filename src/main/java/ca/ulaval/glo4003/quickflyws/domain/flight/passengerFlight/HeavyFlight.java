package ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight;

import ca.ulaval.glo4003.quickflyws.domain.flight.exceptions.InvalidWeightException;
import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.weightCategory.WeightCategory;

public class HeavyFlight extends PassengerFlight {

  private static final double MAXIMUM_WEIGHT = 65.0;

  private double exceedingWeightAvailable;
  private double maximumExceedingWeight;
  private double extraFeeForExceedingWeight;

  private WeightCategory flightWeightCategory;

  public HeavyFlight(String source, String destination, String date, String company, int seatsEconomic, double seatsEconomicPrice, int seatsRegular,
      double seatsRegularPrice, int seatsBusiness, double seatsBusinessPrice, double exceedingWeightAvailable, double exceedingWeightFee,
      boolean isAirVivant) {
    super(source, destination, date, company, seatsEconomic, seatsEconomicPrice, seatsRegular, seatsRegularPrice, seatsBusiness, seatsBusinessPrice,
        isAirVivant);

    flightWeightCategory = WeightCategory.AIR_LOURD;
    this.exceedingWeightAvailable = exceedingWeightAvailable;
    maximumExceedingWeight = exceedingWeightAvailable;
    extraFeeForExceedingWeight = exceedingWeightFee;
  }

  @Override
  public boolean hasEnoughExceedingWeightAvailable(double luggageWeight) {
    return luggageWeight <= exceedingWeightAvailable;
  }

  @Override
  public boolean weightIsExceeding(double luggageWeight) {
    return luggageWeight > MAXIMUM_WEIGHT + exceedingWeightAvailable;
  }

  @Override
  public void adjustExceedingWeightAvailable(int numberOfTickets, double luggageWeight) {
    if (luggageWeight > MAXIMUM_WEIGHT) {
      double weightAvailable = getExceedingWeightAvailable() - numberOfTickets * (luggageWeight - MAXIMUM_WEIGHT);
      if (weightAvailable < 0) {
        throw new InvalidWeightException("There is no room available for this luggage");
      }
      exceedingWeightAvailable = Math.min(weightAvailable, maximumExceedingWeight);
    }
  }

  @Override
  public void adjustMaximumExceedingWeight(double maximumExceedingWeight) {
    if (maximumExceedingWeight < 0) {
      throw new InvalidWeightException("The maximum exceeding weight can not be a negative value.");
    }

    double weightDifference = maximumExceedingWeight - this.maximumExceedingWeight;
    exceedingWeightAvailable += weightDifference;
    exceedingWeightAvailable = Math.max(exceedingWeightAvailable, 0);

    this.maximumExceedingWeight = maximumExceedingWeight;
  }

  public boolean hasValidWeight(double luggageWeight, int numberOfTickets) {
    if (luggageWeight > MAXIMUM_WEIGHT && !hasEnoughExceedingWeightAvailable(numberOfTickets * (luggageWeight - MAXIMUM_WEIGHT))) {
      return false;
    }
    return true;
  }

  @Override
  public WeightCategory getFlightCategory() {
    return flightWeightCategory;
  }

  @Override
  public double getExtraFeesForExceedingWeight(double luggageWeight) {
    if (luggageWeight > MAXIMUM_WEIGHT) {
      return extraFeeForExceedingWeight * (luggageWeight - MAXIMUM_WEIGHT);
    }
    return 0;
  }

  @Override
  public double getMaximumExceedingWeight() {
    return maximumExceedingWeight;
  }

  @Override
  public double getExceedingWeightAvailable() {
    return exceedingWeightAvailable;
  }

  @Override
  public String flightToString() {
    String separator = System.getProperty("line.separator");
    String passengerFlightInString = "Flight Details" + separator;

    passengerFlightInString += "Source : " + source + separator + "Destination : " + destination + separator + "Date : " + date.toLocalDate()
        + separator + "Departure time : " + date.toLocalTime() + separator + "Company : " + company + separator + isAirVivantToString() + separator;
    passengerFlightInString += "Weight Category : " + flightWeightCategory + separator;
    passengerFlightInString += "Exceeding Weight Available For Luggage : " + exceedingWeightAvailable + separator + "Maximum Exceeding Weight : "
        + maximumExceedingWeight + separator;

    return passengerFlightInString;
  }

}