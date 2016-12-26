package ca.ulaval.glo4003.quickflyws.service.flight;

import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.PassengerFlight;

public interface FlightModificationLogger {

  void log(PassengerFlight passengerFlight);

}