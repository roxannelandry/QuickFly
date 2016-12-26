package ca.ulaval.glo4003.quickflyws.domain.flight.exceptions;

import ca.ulaval.glo4003.quickflyws.domain.generalExceptions.NotFoundException;

@SuppressWarnings("serial")
public class FlightNotFoundException extends NotFoundException {

  public FlightNotFoundException(String message) {
    super(message);
  }

}