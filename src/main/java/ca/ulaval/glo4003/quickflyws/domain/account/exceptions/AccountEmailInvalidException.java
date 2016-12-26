package ca.ulaval.glo4003.quickflyws.domain.account.exceptions;

import ca.ulaval.glo4003.quickflyws.domain.generalExceptions.BadRequestException;

@SuppressWarnings("serial")
public class AccountEmailInvalidException extends BadRequestException {

  public AccountEmailInvalidException(String message) {
    super(message);
  }

}