package ca.ulaval.glo4003.quickflyws.domain.generalExceptions;

@SuppressWarnings("serial")
public class NotFoundException extends RuntimeException {

  public NotFoundException(String message) {
    super(message);
  }

}