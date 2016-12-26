package ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import ca.ulaval.glo4003.quickflyws.domain.flight.exceptions.InvalidWeightException;
import ca.ulaval.glo4003.quickflyws.domain.flight.exceptions.NotEnoughAvailableSeatsException;
import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.seats.seatCategory.SeatCategory;
import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.weightCategory.WeightCategory;

public class MediumFlightTest {

  private static final String FLIGHT_SOURCE = "source";
  private static final String FLIGHT_DESTINATION = "destination";
  private static final String FLIGHT_DATE = "2016-01-01 13:00";
  private static final String FLIGHT_DATE_ONLY = "2016-01-01";
  private static final String FLIGHT_TIME = "13:00";
  private static final String FLIGHT_COMPANY = "AirCanada";
  private static final String NOT_AIR_VIVANT = "no";
  private static final String INVALID_FLIGHT_SOURCE = "invalid source";
  private static final String INVALID_FLIGHT_DESTINATION = "invalid destination";
  
  private static final LocalDateTime DATE_HIGHER_THAN_FLIGHT_DATE_AS_DATETIME = LocalDateTime.of(9999, 01, 01, 13, 00);
  private static final LocalDateTime FLIGHT_DATE_AS_DATETIME = LocalDateTime.of(2016, 01, 01, 13, 00);
  
  private static final boolean IS_NOT_AIR_VIVANT = false;

  private static final int VALID_NUMBER_OF_TICKETS = 8;
  private static final int ONE_TICKET = 1;
  private static final int INVALID_NUMBER_OF_TICKETS = 100;
  private static final int FLIGHT_SEATS_AVAILABLE = 8;
  private static final int NUMBER_OF_ECONOMIC_SEATS = 0;
  private static final int NUMBER_OF_REGULAR_SEATS = 8;
  private static final int NUMBER_OF_BUSINESS_SEATS = 0;

  private static final double VALID_WEIGHT = 20.00;
  private static final double INVALID_WEIGHT = 120.00;
  private static final double NO_EXTRA_FEES = 0;

  private static final double ECONOMIC_SEATS_PRICE = 70.00;
  private static final double REGULAR_SEATS_PRICE = 90.00;
  private static final double BUSINESS_SEATS_PRICE = 150.00;

  private static final SeatCategory SEAT_CATEGORY = SeatCategory.REGULAR;

  private MediumFlight mediumFlight;

  @Before
  public void setUp() {
    mediumFlight = new MediumFlight(FLIGHT_SOURCE, FLIGHT_DESTINATION, FLIGHT_DATE, FLIGHT_COMPANY, NUMBER_OF_ECONOMIC_SEATS, ECONOMIC_SEATS_PRICE,
        NUMBER_OF_REGULAR_SEATS, REGULAR_SEATS_PRICE, NUMBER_OF_BUSINESS_SEATS, BUSINESS_SEATS_PRICE, IS_NOT_AIR_VIVANT);
  }

  @Test
  public void mediumFlightThatHasExceedingWeightAvailable_seeingIfHasEnoughAvailableWeight_returnFalse() {
    assertFalse(mediumFlight.hasEnoughExceedingWeightAvailable(VALID_WEIGHT));
  }

  @Test
  public void luggageThatExceedWeightAndFlightWithNotEnoughSpace_seeingIfIsExceedingWeightAllowed_returnTrue() {
    assertTrue(mediumFlight.weightIsExceeding(INVALID_WEIGHT));
  }

  @Test
  public void luggageThatExceedWeightAndFlightWithExceedingWeighSpace_seeingIfIsExceedingWeightAllowed_returnFalse() {
    assertFalse(mediumFlight.weightIsExceeding(VALID_WEIGHT));
  }

  @Test(expected = InvalidWeightException.class)
  public void validWeight_adjustingMaximumExceedingWeight_throwInvalidWeight() {
    mediumFlight.adjustMaximumExceedingWeight(VALID_WEIGHT);
  }

  @Test
  public void flightInfosThatMatch_verifyingFlightIsMatchingInfo_returnTrue() {
    assertTrue(mediumFlight.areInformationsMatching(FLIGHT_DATE_AS_DATETIME, FLIGHT_DESTINATION, FLIGHT_SOURCE));
  }

  @Test
  public void dateHigherThanFlightDate_verifyingFlightIsMatchingInfo_returnFalse() {
    assertFalse(mediumFlight.areInformationsMatching(DATE_HIGHER_THAN_FLIGHT_DATE_AS_DATETIME, FLIGHT_DESTINATION, FLIGHT_SOURCE));
  }

  @Test
  public void invalidDestinationFlightInfos_verifyingFlightIsMatchingInfo_returnFalse() {
    assertFalse(mediumFlight.areInformationsMatching(FLIGHT_DATE_AS_DATETIME, INVALID_FLIGHT_DESTINATION, FLIGHT_SOURCE));
  }

  @Test
  public void invalidSourceFlightInfos_verifyingFlightIsMatchingInfo_returnFalse() {
    assertFalse(mediumFlight.areInformationsMatching(FLIGHT_DATE_AS_DATETIME, FLIGHT_DESTINATION, INVALID_FLIGHT_SOURCE));
  }

  @Test
  public void numberOfTicketsLessThanSeatAvailable_checkingIfhasEnoughAvailableSeat_returnTrue() {
    assertTrue(mediumFlight.hasAvailableSeatsForThisCategory(SEAT_CATEGORY, VALID_NUMBER_OF_TICKETS));
  }

  @Test
  public void numberOfTicketsMoreThanSeatAvailable_checkingIfhasEnoughAvailableSeat_returnFalse() {
    assertFalse(mediumFlight.hasAvailableSeatsForThisCategory(SEAT_CATEGORY, INVALID_NUMBER_OF_TICKETS));
  }

  @Test(expected = NotEnoughAvailableSeatsException.class)
  public void invalidNumberOfTickets_adjustingQuantityOfAvailableSeat_throwNotEnoughAvailableSeatsException() {
    mediumFlight.adjustQuantityOfAvailableSeat(SEAT_CATEGORY, INVALID_NUMBER_OF_TICKETS);
  }

  @Test
  public void validNumberOfTickets_adjustingQuantityOfAvailableSeat_numberOfSeatsIsAdjusted() {
    mediumFlight.adjustQuantityOfAvailableSeat(SEAT_CATEGORY, VALID_NUMBER_OF_TICKETS);

    assertEquals(mediumFlight.getNumberOfSeatsAvailable(SEAT_CATEGORY), FLIGHT_SEATS_AVAILABLE - VALID_NUMBER_OF_TICKETS);
  }

  @Test
  public void someSeatsAvailable_checkingIfHasSeatsAvailable_returnTrue() {
    assertTrue(mediumFlight.hasAvailableSeats(true, true, true));
  }

  @Test
  public void noSeatsAvailable_checkingIfHasSeatsAvailable_returnFalse() {
    mediumFlight.adjustQuantityOfAvailableSeat(SEAT_CATEGORY, FLIGHT_SEATS_AVAILABLE);

    assertFalse(mediumFlight.hasAvailableSeats(false, true, false));
  }

  @Test
  public void luggageWeightAndNumberOfTicketsLessThanExceedingWeight_checkingIfHasValidWeight_returnTrue() {
    assertTrue(mediumFlight.hasValidWeight(VALID_WEIGHT, ONE_TICKET));
  }

  @Test
  public void luggageWeightAndNumberOfTicketsMoreThanExceedingWeight_checkingIfHasValidWeight_returnFalse() {
    assertFalse(mediumFlight.hasValidWeight(INVALID_WEIGHT, ONE_TICKET));
  }

  @Test
  public void weightLowerThanMaximumWeight_gettingExtraFeesByWeight_returnZeroExtraFees() {
    assertEquals(mediumFlight.getExtraFeesForExceedingWeight(VALID_WEIGHT), NO_EXTRA_FEES, 0);
  }

  @Test
  public void flight_convertingFlightToString_returnStringFormattedWithFlightInfos() {
    String separator = System.getProperty("line.separator");

    String passengerFlightInString = "Flight Details" + separator;
    passengerFlightInString += "Source : " + FLIGHT_SOURCE + separator + "Destination : " + FLIGHT_DESTINATION + separator + "Date : " + FLIGHT_DATE_ONLY + separator
        + "Departure time : " + FLIGHT_TIME + separator + "Company : " + FLIGHT_COMPANY + separator + "AirVivant : " + NOT_AIR_VIVANT + separator;
    passengerFlightInString += "Weight Category : " + WeightCategory.AIR_MOYEN + separator;

    assertEquals(mediumFlight.flightToString(), passengerFlightInString);
  }

}