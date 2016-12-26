package ca.ulaval.glo4003.quickflyws.service.flight.filterCriteria;

import java.util.List;

import ca.ulaval.glo4003.quickflyws.domain.flight.cargoFlight.CargoFlight;
import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.PassengerFlight;
import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.weightCategory.WeightCategory;

public class WeightCriteria extends SearchResultFilterCriteria {

  private double luggageWeight;
  private boolean airCargoAllowed;
  private List<CargoFlight> cargoFlights;
  private boolean flightsHasBeenFilteredByWeight = false;

  public WeightCriteria() {
  }

  @Override
  public boolean meetCriteria(PassengerFlight passengerFlight) {
    if (passengerFlight.weightIsExceeding(luggageWeight) && passengerFlight.getFlightCategory() != WeightCategory.AIR_LOURD
        && !canBeInAirCargo(luggageWeight, airCargoAllowed)) {
      flightsHasBeenFilteredByWeight = true;
      return false;

    } else {
      return successor.meetCriteria(passengerFlight);
    }
  }

  private boolean canBeInAirCargo(double luggageWeight, boolean airCargoAllowed) {
    if (!airCargoAllowed) {
      return false;
    }

    for (CargoFlight cargoFlight : cargoFlights) {
      if (cargoFlight.hasEnoughSpace(luggageWeight)) {
        return true;
      }
    }
    return false;
  }

  public void setParameters(double luggageWeight, boolean airCargoAllowed, List<CargoFlight> cargoFlights) {
    this.luggageWeight = luggageWeight;
    this.airCargoAllowed = airCargoAllowed;
    this.cargoFlights = cargoFlights;
  }

  public boolean hasBeenFilterByWeight() {
    return flightsHasBeenFilteredByWeight;
  }

  public void reinitializeBooleanForFlightFilteredByWeight() {
    flightsHasBeenFilteredByWeight = false;
  }

}