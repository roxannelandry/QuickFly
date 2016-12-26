package ca.ulaval.glo4003.quickflyws.infrastructure.hashGenerator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import ca.ulaval.glo4003.quickflyws.infrastructure.flight.FlightHashGenerator;

@RunWith(MockitoJUnitRunner.class)
public class FlightHashGeneratorTest {

  private static final String FLIGHT_SOURCE = "source";
  private static final String FLIGHT_DESTINATION = "destination";
  private static final String FLIGHT_COMPANY = "company";

  private static final String ANOTHER_FLIGHT_SOURCE = "another source";
  private static final LocalDateTime FLIGHT_DATE_AS_DATETIME = LocalDateTime.of(2016, 01, 01, 13, 00);

  private FlightHashGenerator flightHashGenerator;

  @Before
  public void setUp() {
    flightHashGenerator = new FlightHashGeneratorInMemory();
  }

  @Test
  public void flightInformations_generatingHashTwice_hashIsTheSame() {
    int firstHash = flightHashGenerator.generateFlightHash(FLIGHT_COMPANY, FLIGHT_DATE_AS_DATETIME, FLIGHT_DESTINATION, FLIGHT_SOURCE);
    int secondHash = flightHashGenerator.generateFlightHash(FLIGHT_COMPANY, FLIGHT_DATE_AS_DATETIME, FLIGHT_DESTINATION, FLIGHT_SOURCE);

    assertEquals(firstHash, secondHash);
  }

  @Test
  public void twoDifferentFlightsInformations_generatingHashForBoth_hashesAreDifferent() {
    int firstHash = flightHashGenerator.generateFlightHash(FLIGHT_COMPANY, FLIGHT_DATE_AS_DATETIME, FLIGHT_DESTINATION, FLIGHT_SOURCE);
    int secondHash = flightHashGenerator.generateFlightHash(FLIGHT_COMPANY, FLIGHT_DATE_AS_DATETIME, FLIGHT_DESTINATION, ANOTHER_FLIGHT_SOURCE);

    assertNotEquals(firstHash, secondHash);
  }

}