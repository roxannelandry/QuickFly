package ca.ulaval.glo4003.quickflyws.infrastructure.flight;

import java.time.LocalDateTime;

public interface FlightHashGenerator {

  public int generateFlightHash(String company, LocalDateTime date, String destination, String source);

}