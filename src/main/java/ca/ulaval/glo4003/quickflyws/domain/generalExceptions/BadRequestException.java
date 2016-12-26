package ca.ulaval.glo4003.quickflyws.domain.generalExceptions;

@SuppressWarnings("serial")
public class BadRequestException extends RuntimeException {

  public BadRequestException(String message) {
    super(message);
  }

}