package ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ca.ulaval.glo4003.quickflyws.domain.dateConverterChecker.DateConverterChecker;
import ca.ulaval.glo4003.quickflyws.domain.flight.exceptions.InvalidWeightException;
import ca.ulaval.glo4003.quickflyws.domain.flight.exceptions.NotEnoughAvailableSeatsException;
import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.seats.seatCategory.SeatCategory;

@RunWith(MockitoJUnitRunner.class)
public class PassengerFlightTest {

  private static final String FLIGHT_SOURCE = "source";
  private static final String FLIGHT_DESTINATION = "destination";
  private static final String FLIGHT_DATE = "2016-01-01 13:00";
  private static final String FLIGHT_COMPANY = "company";  
  private static final String IS_AIR_VIVANT_TO_STRING = "AirVivant : yes";
  private static final String IS_NO_AIR_VIVANT_TO_STRING = "AirVivant : no";

  private static final boolean IS_AIR_VIVANT = true;
  private static final boolean IS_NOT_AIR_VIVANT = false;
  private static final boolean HAS_NO_CARGO = false;
  
  private static final int NUMBER_OF_ECONOMIC_SEATS = 1;
  private static final int NO_BUSINESS_SEAT = 0;
  private static final int NUMBER_OF_REGULAR_SEATS = 8;
  private static final int NO_REGULAR_SEATS = 0;
  private static final int NUMBER_OF_BUSINESS_SEATS = 1;
  private static final int BUSINESS_SEATS_PRICE = 0;
  private static final int NO_ECONOMIC_SEAT = 0;
  private static final int TICKETS_NUMBER = 10;
  private static final int LITTLE_TICKETS_NUMBER = 1;
  
  private static final double NO_WEIGHT = 0;
  private static final double TOO_BIG_WEIGHT = 100;
  private static final double ECONOMIC_SEATS_PRICE = 0.00;
  private static final double REGULAR_SEATS_PRICE = 90.00;

  @Mock
  private DateConverterChecker dateChecker;

  private PassengerFlight flightSeatsAvailable;

  private PassengerFlight flightNoSeatsAvailable;

  private PassengerFlight lightFlightSeatsAvailable;

  private PassengerFlight lightFlightNoSeatsAvailable;

  @Before
  public void setUp() {
    flightSeatsAvailable = new MediumFlight(FLIGHT_SOURCE, FLIGHT_DESTINATION, FLIGHT_DATE, FLIGHT_COMPANY, NUMBER_OF_ECONOMIC_SEATS,
        ECONOMIC_SEATS_PRICE, NUMBER_OF_REGULAR_SEATS, REGULAR_SEATS_PRICE, NUMBER_OF_BUSINESS_SEATS, BUSINESS_SEATS_PRICE, IS_AIR_VIVANT);
    flightNoSeatsAvailable = new MediumFlight(FLIGHT_SOURCE, FLIGHT_DESTINATION, FLIGHT_DATE, FLIGHT_COMPANY, NO_ECONOMIC_SEAT, ECONOMIC_SEATS_PRICE,
        NO_REGULAR_SEATS, REGULAR_SEATS_PRICE, NO_BUSINESS_SEAT, BUSINESS_SEATS_PRICE, IS_NOT_AIR_VIVANT);
    lightFlightSeatsAvailable = new LightFlight(FLIGHT_SOURCE, FLIGHT_DESTINATION, FLIGHT_DATE, FLIGHT_COMPANY, NO_ECONOMIC_SEAT,
        ECONOMIC_SEATS_PRICE, NUMBER_OF_REGULAR_SEATS, REGULAR_SEATS_PRICE, NO_BUSINESS_SEAT, BUSINESS_SEATS_PRICE, IS_NOT_AIR_VIVANT);
    lightFlightNoSeatsAvailable = new LightFlight(FLIGHT_SOURCE, FLIGHT_DESTINATION, FLIGHT_DATE, FLIGHT_COMPANY, NO_ECONOMIC_SEAT,
        ECONOMIC_SEATS_PRICE, NO_REGULAR_SEATS, REGULAR_SEATS_PRICE, NO_BUSINESS_SEAT, BUSINESS_SEATS_PRICE, IS_NOT_AIR_VIVANT);
  }

  @Test
  public void flightWithAvailableSeats_checkingIfFlightHasAvailableEconomicSeats_returnTrue() {
    boolean result = flightSeatsAvailable.hasAvailableSeats(true, false, false);

    assertTrue(result);
  }

  @Test
  public void flightWithNoAvailableSeats_checkingIfFlightHasAvailableEconomicSeats_returnFalse() {
    boolean result = flightNoSeatsAvailable.hasAvailableSeats(true, false, false);

    assertFalse(result);
  }

  @Test
  public void flightWithAvailableSeats_checkingIfFlightHasAvailableRegularSeats_returnTrue() {
    boolean result = flightSeatsAvailable.hasAvailableSeats(false, true, false);

    assertTrue(result);
  }

  @Test
  public void flightWithNoAvailableSeats_checkingIfFlightHasAvailableRegularSeats_returnFalse() {
    boolean result = flightNoSeatsAvailable.hasAvailableSeats(false, true, false);

    assertFalse(result);
  }

  @Test
  public void flightWithAvailableSeats_checkingIfFlightHasAvailableBusinessSeats_returnTrue() {
    boolean result = flightSeatsAvailable.hasAvailableSeats(false, false, true);

    assertTrue(result);
  }

  @Test
  public void flightWithNoAvailableSeats_checkingIfFlightHasAvailableBusinessSeats_returnFalse() {
    boolean result = flightNoSeatsAvailable.hasAvailableSeats(false, false, true);

    assertFalse(result);
  }

  @Test
  public void flightWithAvailableSeats_checkingIfFlightHasAvailableAnySeats_returnTrue() {
    boolean result = flightSeatsAvailable.hasAvailableSeats(true, true, true);

    assertTrue(result);
  }

  @Test
  public void flightWithNoAvailableSeats_checkingIfFlightHasAvailableAnySeats_returnFalse() {
    boolean result = flightNoSeatsAvailable.hasAvailableSeats(true, true, true);

    assertFalse(result);
  }

  @Test
  public void lightWithNoAvailableSeats_hasAvailableSeats_returnFalse() {
    boolean result = lightFlightNoSeatsAvailable.hasAvailableSeats(false, false, false);

    assertFalse(result);
  }

  @Test
  public void lightFlightWithFalseFilter_checkingIfFlightHasAvailableSeats_alwaysReturnTrueIfHaveRegularSeatsNoMatterFilter() {
    boolean result = lightFlightSeatsAvailable.hasAvailableSeats(false, false, false);

    assertTrue(result);
  }

  @Test
  public void lightFlightWithTrueFilter_checkingIfFlightHasAvailableSeats_alwaysReturnTrueIfHaveRegularSeatsNoMatterFilter() {
    boolean result = lightFlightSeatsAvailable.hasAvailableSeats(true, true, true);

    assertTrue(result);
  }

  @Test
  public void seatCategoryAndNumberOfTicketsNegative_adjustingQuantityOfAvailableSeat_addReleasedSeats() {
    flightSeatsAvailable.adjustQuantityOfAvailableSeat(SeatCategory.ECONOMIC, 1);

    flightSeatsAvailable.adjustQuantityOfAvailableSeat(SeatCategory.ECONOMIC, -1);

    assertEquals(flightSeatsAvailable.getNumberOfSeatsAvailable(SeatCategory.ECONOMIC), NUMBER_OF_ECONOMIC_SEATS);
  }

  @Test
  public void isAirVivantTrue_convertingIsAirVivantToString_returnYesForIsAirVivant() {
    assertEquals(flightSeatsAvailable.isAirVivantToString(), IS_AIR_VIVANT_TO_STRING);
  }

  @Test
  public void isAirVivantFalse_convertingIsAirVivantToString_returnNoForIsAirVivant() {
    assertEquals(flightNoSeatsAvailable.isAirVivantToString(), IS_NO_AIR_VIVANT_TO_STRING);
  }

  @Test(expected = NotEnoughAvailableSeatsException.class)
  public void seatCategoryAndNumberOfTicketsTooHigh_adjustingQuantityOfAvailableSeat_throwNotEnoughAvailableSeat() {
    flightNoSeatsAvailable.adjustQuantityOfAvailableSeat(SeatCategory.ECONOMIC, TICKETS_NUMBER);
  }

  @Test(expected = NotEnoughAvailableSeatsException.class)
  public void noSeatAvailable_adjustingAvailability_throwNotEnoughAvailableSeat() {
    flightNoSeatsAvailable.adjustAvailability(SeatCategory.ECONOMIC, TICKETS_NUMBER, IS_AIR_VIVANT, NO_WEIGHT);
  }

  @Test(expected = NotEnoughAvailableSeatsException.class)
  public void noSeatAvailable_checkingForAvailability_throwNotEnoughAvailableSeat() {
    flightNoSeatsAvailable.checkAvailability(SeatCategory.ECONOMIC, TICKETS_NUMBER, HAS_NO_CARGO, NO_WEIGHT);
  }

  @Test(expected = InvalidWeightException.class)
  public void notEnoughRoom_checkingForAvailability_throwInvalidWeight() {
    flightSeatsAvailable.checkAvailability(SeatCategory.REGULAR, LITTLE_TICKETS_NUMBER, HAS_NO_CARGO, TOO_BIG_WEIGHT);
  }

}