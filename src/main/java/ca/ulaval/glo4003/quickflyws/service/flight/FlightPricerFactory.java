package ca.ulaval.glo4003.quickflyws.service.flight;

import java.util.ArrayList;
import java.util.List;

import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.PassengerFlight;

public class FlightPricerFactory {
  
  public FlightPricer createFlightPricer(PassengerFlight passengerFlight, double luggageWeight, boolean airCargoAllowed) {
    return new FlightPricer(passengerFlight, luggageWeight, airCargoAllowed);
  }

  public List<FlightPricer> createFlightPricers(List<PassengerFlight> passengerFlights, double luggageWeight, boolean airCargoAllowed) {
    List<FlightPricer> flightPricers = new ArrayList<>();
    for (PassengerFlight passengerFlight : passengerFlights) {
      flightPricers.add(new FlightPricer(passengerFlight, luggageWeight, airCargoAllowed));
    }
    return flightPricers;
  }
  
}