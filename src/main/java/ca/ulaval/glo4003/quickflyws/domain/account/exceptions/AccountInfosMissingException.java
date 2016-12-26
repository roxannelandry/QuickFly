package ca.ulaval.glo4003.quickflyws.domain.account.exceptions;

import ca.ulaval.glo4003.quickflyws.domain.generalExceptions.BadRequestException;

@SuppressWarnings("serial")
public class AccountInfosMissingException extends BadRequestException {

  public AccountInfosMissingException(String message) {
    super(message);
  }

}