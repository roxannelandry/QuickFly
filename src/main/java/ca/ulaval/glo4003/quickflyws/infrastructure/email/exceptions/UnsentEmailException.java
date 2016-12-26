package ca.ulaval.glo4003.quickflyws.infrastructure.email.exceptions;

@SuppressWarnings("serial")
public class UnsentEmailException extends RuntimeException {
  
  public UnsentEmailException(String message) {
    super(message);
  }
  
}