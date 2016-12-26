package ca.ulaval.glo4003.quickflyws.domain.paymentSystem.exceptions;

import ca.ulaval.glo4003.quickflyws.domain.generalExceptions.BadRequestException;

@SuppressWarnings("serial")
public class CheckoutEmailInvalidException extends BadRequestException {

  public CheckoutEmailInvalidException(String message) {
    super(message);
  }

}