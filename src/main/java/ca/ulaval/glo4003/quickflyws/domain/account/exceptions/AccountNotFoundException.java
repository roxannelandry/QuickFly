package ca.ulaval.glo4003.quickflyws.domain.account.exceptions;

import ca.ulaval.glo4003.quickflyws.domain.generalExceptions.NotFoundException;

@SuppressWarnings("serial")
public class AccountNotFoundException extends NotFoundException {

  public AccountNotFoundException(String message) {
    super(message);
  }

}