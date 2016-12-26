package ca.ulaval.glo4003.quickflyws.infrastructure.hashGenerator;

import java.time.LocalDateTime;

import ca.ulaval.glo4003.quickflyws.infrastructure.flight.FlightHashGenerator;

public class FlightHashGeneratorInMemory implements FlightHashGenerator {

  public int generateFlightHash(String company, LocalDateTime date, String destination, String source) {
    String flightInformationsToBeHashed = company + destination + date.toString() + source;
    int flightHash = flightInformationsToBeHashed.hashCode();

    return flightHash;
  }

}