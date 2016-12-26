package ca.ulaval.glo4003.quickflyws.domain.flight.exceptions;

import ca.ulaval.glo4003.quickflyws.domain.generalExceptions.BadRequestException;

@SuppressWarnings("serial")
public class NotEnoughAvailableSeatsException extends BadRequestException {

  public NotEnoughAvailableSeatsException(String message) {
    super(message);
  }
  
}