package ca.ulaval.glo4003.quickflyws.domain.account.exceptions;

import ca.ulaval.glo4003.quickflyws.domain.generalExceptions.ConflictException;

@SuppressWarnings("serial")
public class AccountAlreadyExistException extends ConflictException {

  public AccountAlreadyExistException(String message) {
    super(message);
  }

}