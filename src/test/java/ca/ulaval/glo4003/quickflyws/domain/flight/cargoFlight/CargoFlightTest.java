package ca.ulaval.glo4003.quickflyws.domain.flight.cargoFlight;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ca.ulaval.glo4003.quickflyws.domain.dateConverterChecker.DateConverterChecker;
import ca.ulaval.glo4003.quickflyws.domain.flight.exceptions.InvalidWeightException;

@RunWith(MockitoJUnitRunner.class)
public class CargoFlightTest {

  private static final String FLIGHT_SOURCE = "source";
  private static final String FLIGHT_DESTINATION = "destination";
  private static final String FLIGHT_DATE = "2016-01-01 13:00";
  private static final String FLIGHT_COMPANY = "company";
  private static final String NO_RESULT_FLIGHT_SOURCE = "no result for this source";
  private static final String NO_RESULT_FLIGHT_DESTINATION = "no result for this destination";
  private static final String NO_RESULT_FLIGHT_COMPANY = "no result for this company";
  
  private static final double FLIGHT_PRICE_BY_WEIGHT = 10.00;
  private static final double FLIGHT_LUGGAGE_WEIGHT_CAPACITY = 200.00;
  private static final double LUGGAGE_WEIGHT = 40.00;
  private static final double INVALID_LUGGAGE_WEIGHT = 4000.00;

  private static final int NUMBER_OF_TICKETS = 5;

  private static final LocalDateTime FLIGHT_DATE_AS_DATETIME = LocalDateTime.of(2016, 01, 01, 13, 00);
  private static final LocalDateTime NO_RESULT_FLIGHT_DATE_AS_DATETIME = LocalDateTime.of(0001, 01, 01, 00, 00);

  @Mock
  private DateConverterChecker dateChecker;

  private CargoFlight cargoFlight;

  @Before
  public void setUp() {
    cargoFlight = new CargoFlight(FLIGHT_SOURCE, FLIGHT_DESTINATION, FLIGHT_DATE, FLIGHT_COMPANY, FLIGHT_PRICE_BY_WEIGHT,
        FLIGHT_LUGGAGE_WEIGHT_CAPACITY);
  }

  @Test
  public void luggageWeight_gettingPriceForWeight_calculatedPrice() {
    double totalPrice = cargoFlight.getPriceForWeight(LUGGAGE_WEIGHT);

    assertEquals(totalPrice, LUGGAGE_WEIGHT * FLIGHT_PRICE_BY_WEIGHT, 0);
  }

  @Test
  public void flightInfosThatMatch_verifyingFlightIsMatchingInfo_returnTrue() {
    assertTrue(cargoFlight.areInformationsMatching(FLIGHT_COMPANY, FLIGHT_DATE_AS_DATETIME, FLIGHT_DESTINATION, FLIGHT_SOURCE));
  }

  @Test
  public void flightInfosThatDoesntMatch_verifyingFlightIsMatchingInfo_returnFalse() {
    assertFalse(cargoFlight.areInformationsMatching(NO_RESULT_FLIGHT_COMPANY, FLIGHT_DATE_AS_DATETIME, FLIGHT_DESTINATION, FLIGHT_SOURCE));
  }

  @Test
  public void dateHigherThanFlightDate_verifyingFlightIsMatchingInfo_returnFalse() {
    assertFalse(cargoFlight.areInformationsMatching(FLIGHT_COMPANY, NO_RESULT_FLIGHT_DATE_AS_DATETIME, FLIGHT_DESTINATION, FLIGHT_SOURCE));
  }

  @Test
  public void invalidDestinationFlightInfos_verifyingFlightIsMatchingInfo_returnFalse() {
    assertFalse(cargoFlight.areInformationsMatching(FLIGHT_COMPANY, FLIGHT_DATE_AS_DATETIME, NO_RESULT_FLIGHT_DESTINATION, FLIGHT_SOURCE));
  }

  @Test
  public void invalidSourceFlightInfos_verifyingFlightIsMatchingInfo_returnFalse() {
    assertFalse(cargoFlight.areInformationsMatching(FLIGHT_COMPANY, FLIGHT_DATE_AS_DATETIME, FLIGHT_DESTINATION, NO_RESULT_FLIGHT_SOURCE));
  }

  @Test
  public void luggageWeight_seeIfHasEnoughSpaceForLuggages_returnTrue() {
    assertTrue(cargoFlight.hasEnoughSpace(LUGGAGE_WEIGHT));
  }

  @Test
  public void luggageWeight_seeIfHasEnoughSpaceForLuggages_returnFalse() {
    assertFalse(cargoFlight.hasEnoughSpace(INVALID_LUGGAGE_WEIGHT));
  }

  @Test
  public void luggageWeightAndNumberOfTickets_addLuggagesToTheCargoFlight_adjustWeightAvailableInCargo() {
    cargoFlight.adjustWeightAvailable(NUMBER_OF_TICKETS, LUGGAGE_WEIGHT);
    double expectedAvailableLuggageCapacity = FLIGHT_LUGGAGE_WEIGHT_CAPACITY - (NUMBER_OF_TICKETS * LUGGAGE_WEIGHT);

    assertEquals(cargoFlight.getAvailableSpace(), expectedAvailableLuggageCapacity, 0);
  }

  @Test(expected = InvalidWeightException.class)
  public void excessiveLuggageWeightAndNumberOfTickets_addingLuggagesToTheCargoFlight_adjustWeightAvailableInCargo() {
    cargoFlight.adjustWeightAvailable(NUMBER_OF_TICKETS, INVALID_LUGGAGE_WEIGHT);
  }

  @Test(expected = InvalidWeightException.class)
  public void excessiveLuggageWeightAndNumberOfTickets_checkingAvailabitlity_adjustWeightAvailableInCargo() {
    cargoFlight.checkAvailability(NUMBER_OF_TICKETS, INVALID_LUGGAGE_WEIGHT);
  }

}