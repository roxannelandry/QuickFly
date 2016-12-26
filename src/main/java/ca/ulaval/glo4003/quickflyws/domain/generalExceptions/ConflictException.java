package ca.ulaval.glo4003.quickflyws.domain.generalExceptions;

@SuppressWarnings("serial")
public class ConflictException extends RuntimeException {

  public ConflictException(String message) {
    super(message);
  }

}