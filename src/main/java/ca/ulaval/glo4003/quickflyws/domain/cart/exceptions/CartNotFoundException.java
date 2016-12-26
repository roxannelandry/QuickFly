package ca.ulaval.glo4003.quickflyws.domain.cart.exceptions;

import ca.ulaval.glo4003.quickflyws.domain.generalExceptions.NotFoundException;

@SuppressWarnings("serial")
public class CartNotFoundException extends NotFoundException {

  public CartNotFoundException(String message) {
    super(message);
  }
  
}