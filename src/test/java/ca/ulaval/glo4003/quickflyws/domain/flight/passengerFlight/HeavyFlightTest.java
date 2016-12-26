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

public class HeavyFlightTest {

  private static final String FLIGHT_SOURCE = "source";
  private static final String FLIGHT_DESTINATION = "destination";
  private static final String FLIGHT_DATE = "2016-01-01 13:00";
  private static final String FLIGHT_DATE_ONLY = "2016-01-01";
  private static final String FLIGHT_TIME = "13:00";
  private static final String FLIGHT_COMPANY = "AirLousse";
  private static final String INVALID_FLIGHT_SOURCE = "invalid source";
  private static final String INVALID_FLIGHT_DESTINATION = "invalid destination";
  private static final String NOT_AIR_VIVANT = "no";

  private static final LocalDateTime DATE_HIGHER_THAN_FLIGHT_DATE_AS_DATETIME = LocalDateTime.of(9999, 01, 01, 13, 00);
  private static final LocalDateTime FLIGHT_DATE_AS_DATETIME = LocalDateTime.of(2016, 01, 01, 13, 00);

  private static final boolean IS_NOT_AIR_VIVANT = false;
  
  private static final int VALID_NUMBER_OF_TICKETS = 2;
  private static final int ONE_TICKET = 1;
  private static final int INVALID_NUMBER_OF_TICKETS = 11;
  private static final int FLIGHT_SEATS_AVAILABLE = 8;
  private static final int NUMBER_OF_ECONOMIC_SEATS = 5;
  private static final int NUMBER_OF_REGULAR_SEATS = 8;
  private static final int NUMBER_OF_BUSINESS_SEATS = 7;

  private static final double EXCEEDING_WEIGHT_PRICE = 10.00;
  private static final double FLIGHT_MAXIMUM_EXCEEDING_WEIGHT = 35.00;
  private static final double VALID_WEIGHT = 20.00;
  private static final double INVALID_WEIGHT = 120.00;
  private static final double NEGATIVE_WEIGHT = -1;
  private static final double HIGHER_VALID_WEIGHT = 40.00;
  private static final double NO_WEIGHT = 0;
  private static final double OVERWEIGHT_BY_ONE = 66;
  private static final double NO_EXTRA_FEES = 0;
  private static final double ONE_EXTRA_FEES = 10;

  private static final double ECONOMIC_SEATS_PRICE = 70.00;
  private static final double REGULAR_SEATS_PRICE = 90.00;
  private static final double BUSINESS_SEATS_PRICE = 150.00;
  
  private static final SeatCategory SEAT_CATEGORY = SeatCategory.REGULAR;

  private HeavyFlight heavyFlight;

  @Before
  public void setUp() {
    heavyFlight = new HeavyFlight(FLIGHT_SOURCE, FLIGHT_DESTINATION, FLIGHT_DATE, FLIGHT_COMPANY, NUMBER_OF_ECONOMIC_SEATS, ECONOMIC_SEATS_PRICE,
        NUMBER_OF_REGULAR_SEATS, REGULAR_SEATS_PRICE, NUMBER_OF_BUSINESS_SEATS, BUSINESS_SEATS_PRICE, FLIGHT_MAXIMUM_EXCEEDING_WEIGHT,
        EXCEEDING_WEIGHT_PRICE, IS_NOT_AIR_VIVANT);
  }

  @Test
  public void heavyFlightThatHasExceedingWeightAvailable_seeingIfHasEnoughAvailableWeight_returnTrue() {
    assertTrue(heavyFlight.hasEnoughExceedingWeightAvailable(VALID_WEIGHT));
  }

  @Test
  public void heavyFlightThatHasNotExceedingWeightAvailable_seeingIfHasEnoughAvailableWeight_returnFalse() {
    assertFalse(heavyFlight.hasEnoughExceedingWeightAvailable(INVALID_WEIGHT));
  }

  @Test
  public void luggageThatExceedWeightAndFlightWithNotEnoughSpace_seeingIfIsExceedingWeightAllowed_returnTrue() {
    assertTrue(heavyFlight.weightIsExceeding(INVALID_WEIGHT));
  }

  @Test
  public void luggageThatExceedWeightAndFlightWithExceedingWeighSpace_seeingIfIsExceedingWeightAllowed_returnFalse() {
    assertFalse(heavyFlight.weightIsExceeding(VALID_WEIGHT));
  }

  @Test(expected = InvalidWeightException.class)
  public void negativeWeight_adjustingExceedingWeight_throwInvalidWeight() {
    heavyFlight.adjustExceedingWeightAvailable(VALID_NUMBER_OF_TICKETS, INVALID_WEIGHT);
  }

  @Test
  public void validWeight_adjustingExceedingWeight_exceedingWeightHasTheNewValue() {
    double previousExceedingWeight = heavyFlight.getExceedingWeightAvailable();

    heavyFlight.adjustExceedingWeightAvailable(ONE_TICKET, OVERWEIGHT_BY_ONE);

    assertEquals(heavyFlight.getExceedingWeightAvailable(), previousExceedingWeight - 1, 0);
  }

  @Test
  public void validWeightHigherThanMaximumPermitted_adjustingExceedingWeight_maximumWeightIsTheNewValue() {
    heavyFlight.adjustExceedingWeightAvailable(ONE_TICKET, HIGHER_VALID_WEIGHT);

    assertEquals(heavyFlight.getExceedingWeightAvailable(), heavyFlight.getMaximumExceedingWeight(), 0);
  }

  @Test
  public void noWeight_adjustingExceedingWeight_maximumWeightIsTheNewValue() {
    heavyFlight.adjustExceedingWeightAvailable(ONE_TICKET, NO_WEIGHT);

    assertEquals(heavyFlight.getExceedingWeightAvailable(), heavyFlight.getMaximumExceedingWeight(), 0);
  }

  @Test(expected = InvalidWeightException.class)
  public void negativeWeight_adjustingMaximumExceedingWeight_throwInvalidWeight() {
    heavyFlight.adjustMaximumExceedingWeight(NEGATIVE_WEIGHT);
  }

  @Test
  public void validWeight_adjustingMaximumExceedingWeight_exceedingMaximumWeightHasTheNewValue() {
    heavyFlight.adjustMaximumExceedingWeight(VALID_WEIGHT);

    assertEquals(heavyFlight.getMaximumExceedingWeight(), VALID_WEIGHT, 0);
  }

  @Test
  public void validWeight_adjustingMaximumExceedingWeightAndExceedingWeightAvaibleBecomeNegative_exceedingWeightAvailableClampToZero() {
    heavyFlight.adjustExceedingWeightAvailable(ONE_TICKET, VALID_WEIGHT);
    heavyFlight.adjustMaximumExceedingWeight(NO_WEIGHT);

    assertEquals(heavyFlight.getExceedingWeightAvailable(), NO_WEIGHT, 0);
  }

  @Test
  public void validWeight_adjustingMaximumExceedingWeight_exceedingWeightAvailableHasTheNewValue() {
    heavyFlight.adjustMaximumExceedingWeight(HIGHER_VALID_WEIGHT);

    assertEquals(heavyFlight.getExceedingWeightAvailable(), HIGHER_VALID_WEIGHT, 0);
  }

  @Test(expected = NotEnoughAvailableSeatsException.class)
  public void invalidNumberOfTickets_adjustingQuantityOfAvailableSeat_throwNotEnoughAvailableSeatsException() {
    heavyFlight.adjustQuantityOfAvailableSeat(SEAT_CATEGORY, INVALID_NUMBER_OF_TICKETS);
  }

  @Test
  public void validNumberOfTickets_adjustingQuantityOfAvailableSeat_numberOfSeatsIsAdjusted() {
    heavyFlight.adjustQuantityOfAvailableSeat(SEAT_CATEGORY, VALID_NUMBER_OF_TICKETS);

    assertEquals(heavyFlight.getNumberOfSeatsAvailable(SEAT_CATEGORY), FLIGHT_SEATS_AVAILABLE - VALID_NUMBER_OF_TICKETS);
  }

  @Test
  public void flightInfosThatMatch_verifyingFlightIsMatchingInfo_returnTrue() {
    assertTrue(heavyFlight.areInformationsMatching(FLIGHT_DATE_AS_DATETIME, FLIGHT_DESTINATION, FLIGHT_SOURCE));
  }

  @Test
  public void dateHigherThanFlightDate_verifyingFlightIsMatchingInfo_returnFalse() {
    assertFalse(heavyFlight.areInformationsMatching(DATE_HIGHER_THAN_FLIGHT_DATE_AS_DATETIME, FLIGHT_DESTINATION, FLIGHT_SOURCE));
  }

  @Test
  public void invalidDestinationFlightInfos_verifyingFlightIsMatchingInfo_returnFalse() {
    assertFalse(heavyFlight.areInformationsMatching(FLIGHT_DATE_AS_DATETIME, INVALID_FLIGHT_DESTINATION, FLIGHT_SOURCE));
  }

  @Test
  public void invalidSourceFlightInfos_verifyingFlightIsMatchingInfo_returnFalse() {
    assertFalse(heavyFlight.areInformationsMatching(FLIGHT_DATE_AS_DATETIME, FLIGHT_DESTINATION, INVALID_FLIGHT_SOURCE));
  }

  @Test
  public void numberOfTicketsLessThanSeatAvailable_checkingIfhasEnoughAvailableSeat_returnTrue() {
    assertTrue(heavyFlight.hasAvailableSeatsForThisCategory(SEAT_CATEGORY, VALID_NUMBER_OF_TICKETS));
  }

  @Test
  public void numberOfTicketsMoreThanSeatAvailable_checkingIfhasEnoughAvailableSeat_returnFalse() {
    assertFalse(heavyFlight.hasAvailableSeatsForThisCategory(SEAT_CATEGORY, INVALID_NUMBER_OF_TICKETS));
  }

  @Test
  public void someSeatsAvailable_checkingIfHasSeatsAvailable_returnTrue() {
    assertTrue(heavyFlight.hasAvailableSeats(true, true, true));
  }

  @Test
  public void noSeatsAvailable_checkingIfHasSeatsAvailable_returnFalse() {
    heavyFlight.adjustQuantityOfAvailableSeat(SEAT_CATEGORY, FLIGHT_SEATS_AVAILABLE);

    assertFalse(heavyFlight.hasAvailableSeats(false, true, false));
  }

  @Test
  public void NoLuggageWeightAndNumberOfTicketsLessThanExceedingWeight_checkingIfHasValidWeight_returnTrue() {
    assertTrue(heavyFlight.hasValidWeight(NO_WEIGHT, ONE_TICKET));
  }

  @Test
  public void luggageWeightAndNumberOfTicketsLessThanExceedingWeight_checkingIfHasValidWeight_returnTrue() {
    assertTrue(heavyFlight.hasValidWeight(VALID_WEIGHT, ONE_TICKET));
  }

  @Test
  public void luggageWeightAndNumberOfTicketsHigherThanExceedingWeight_checkingIfHasValidWeight_returnFalse() {
    assertFalse(heavyFlight.hasValidWeight(INVALID_WEIGHT, VALID_NUMBER_OF_TICKETS));
  }
  
  @Test
  public void weightLowerThanMaximumWeight_gettingExtraFeesByWeight_returnZeroExtraFees() {
    assertEquals(heavyFlight.getExtraFeesForExceedingWeight(NO_WEIGHT), NO_EXTRA_FEES, 0);
  }

  @Test
  public void weightBiggerThanMaximumWeight_gettingExtraFeesByWeight_returnExtraFees() {
    assertEquals(heavyFlight.getExtraFeesForExceedingWeight(OVERWEIGHT_BY_ONE), ONE_EXTRA_FEES, 0);
  }

  @Test
  public void flight_convertingFlightToString_returnStringFormattedWithFlightInfos() {
    String separator = System.getProperty("line.separator");

    String passengerFlightInString = "Flight Details" + separator;
    passengerFlightInString += "Source : " + FLIGHT_SOURCE + separator + "Destination : " + FLIGHT_DESTINATION + separator + "Date : " + FLIGHT_DATE_ONLY + separator
        + "Departure time : " + FLIGHT_TIME + separator + "Company : " + FLIGHT_COMPANY + separator + "AirVivant : " + NOT_AIR_VIVANT + separator;
    ;
    passengerFlightInString += "Weight Category : " + WeightCategory.AIR_LOURD + separator;
    passengerFlightInString += "Exceeding Weight Available For Luggage : " + FLIGHT_MAXIMUM_EXCEEDING_WEIGHT + separator + "Maximum Exceeding Weight : "
        + FLIGHT_MAXIMUM_EXCEEDING_WEIGHT + separator;

    assertEquals(heavyFlight.flightToString(), passengerFlightInString);
  }

}