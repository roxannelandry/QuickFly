package ca.ulaval.glo4003.quickflyws.domain.flight.exceptions;

import ca.ulaval.glo4003.quickflyws.domain.generalExceptions.BadRequestException;

@SuppressWarnings("serial")
public class InvalidSeatCategoryException extends BadRequestException {

  public InvalidSeatCategoryException(String message) {
    super(message);
  }
  
}