package ca.ulaval.glo4003.quickflyws.domain.flight.exceptions;

import ca.ulaval.glo4003.quickflyws.domain.generalExceptions.BadRequestException;

@SuppressWarnings("serial")
public class InvalidWeightException extends BadRequestException {

  public InvalidWeightException(String message) {
    super(message);
  }

}