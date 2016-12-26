package ca.ulaval.glo4003.quickflyws.service.flight;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ca.ulaval.glo4003.quickflyws.domain.flight.cargoFlight.CargoFlight;
import ca.ulaval.glo4003.quickflyws.domain.flight.exceptions.InvalidWeightException;
import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.PassengerFlight;
import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.seats.seatCategory.SeatCategory;
import ca.ulaval.glo4003.quickflyws.dto.CartItemDtoRequest;

@RunWith(MockitoJUnitRunner.class)
public class FlightPricerTest {

  private static final String FLIGHT_SOURCE = "source";
  private static final String FLIGHT_DESTINATION = "destination";
  private static final String FLIGHT_COMPANY = "company";
  private static final String CARGO_FLIGHT_SOURCE = "cargo source";
  private static final String CARGO_FLIGHT_DESTINATION = "cargo destination";
  private static final String CARGO_FLIGHT_COMPANY = "cargo company";
  
  private static final LocalDateTime FLIGHT_DATE_AS_DATETIME = LocalDateTime.of(2016, 01, 01, 13, 00);
  private static final LocalDateTime CARGO_FLIGHT_DATE_AS_DATETIME = LocalDateTime.of(2016, 01, 02, 15, 00);
  private static final LocalDateTime CARGO_FLIGHT_LATER_DATE_AS_DATETIME = LocalDateTime.of(2016, 01, 03, 15, 00);
  private static final LocalDateTime CARGO_FLIGHT_TOO_LATE_DATE_AS_DATETIME = LocalDateTime.of(2222, 01, 03, 15, 00);
  
  private static final boolean AIR_CARGO_ALLOWED = true;
  private static final boolean AIR_CARGO_NOT_ALLOWED = false;
  
  private static final int ONE_TICKET = 1;
  private static final int NUMBER_OF_TICKETS = 5;
  private static final int NUMBER_TOTAL_ECONOMIC_SEATS = 15;
  private static final int NUMBER_TOTAL_REGULAR_SEATS = 20;
  private static final int NUMBER_TOTAL_BUSINESS_SEATS = 4;
  
  private static final double EXTRA_FEES = 10;
  private static final double LUGGAGE_WEIGHT = 20;
  private static final double LUGGAGE_OVERWEIGHT = 80;
  private static final double ECONOMIC_SEATS_PRICE = 20.0;
  private static final double REGULAR_SEATS_PRICE = 40.00;
  private static final double BUSINESS_SEATS_PRICE = 90.00;
  private static final double CARGO_FLIGHT_PRICE_BY_WEIGHT = 10.00;

  private static final SeatCategory SEAT_CATEGORY_ECONOMIC = SeatCategory.ECONOMIC;
  private static final SeatCategory SEAT_CATEGORY_REGULAR = SeatCategory.REGULAR;
  private static final SeatCategory SEAT_CATEGORY_BUSINESS = SeatCategory.BUSINESS;

  @Mock
  private PassengerFlight passengerFlight;

  @Mock
  private CargoFlight cargoFlight;

  @Mock
  private CargoFlight anotherCargoFlight;

  private FlightPricer flightPricer;

  @Before
  public void setUp() {
    given(passengerFlight.getSource()).willReturn(FLIGHT_SOURCE);
    given(passengerFlight.getDestination()).willReturn(FLIGHT_DESTINATION);
    given(passengerFlight.getDate()).willReturn(FLIGHT_DATE_AS_DATETIME);
    given(passengerFlight.getCompany()).willReturn(FLIGHT_COMPANY);
    given(passengerFlight.getExtraFeesForExceedingWeight(LUGGAGE_OVERWEIGHT)).willReturn(EXTRA_FEES);

    given(passengerFlight.getSeatPrice(SEAT_CATEGORY_ECONOMIC)).willReturn(ECONOMIC_SEATS_PRICE);
    given(passengerFlight.getNumberEconomicSeats()).willReturn(NUMBER_TOTAL_ECONOMIC_SEATS);

    given(passengerFlight.getSeatPrice(SEAT_CATEGORY_REGULAR)).willReturn(REGULAR_SEATS_PRICE);
    given(passengerFlight.getNumberRegularSeats()).willReturn(NUMBER_TOTAL_REGULAR_SEATS);

    given(passengerFlight.getSeatPrice(SEAT_CATEGORY_BUSINESS)).willReturn(BUSINESS_SEATS_PRICE);
    given(passengerFlight.getNumberBusinessSeats()).willReturn(NUMBER_TOTAL_BUSINESS_SEATS);

    given(cargoFlight.getSource()).willReturn(CARGO_FLIGHT_SOURCE);
    given(cargoFlight.getDestination()).willReturn(CARGO_FLIGHT_DESTINATION);
    given(cargoFlight.getDate()).willReturn(CARGO_FLIGHT_DATE_AS_DATETIME);
    given(cargoFlight.getCompany()).willReturn(CARGO_FLIGHT_COMPANY);
    given(cargoFlight.getPriceForWeight(LUGGAGE_WEIGHT)).willReturn(CARGO_FLIGHT_PRICE_BY_WEIGHT);
    given(cargoFlight.hasEnoughSpace(LUGGAGE_WEIGHT)).willReturn(true);
    given(cargoFlight.hasEnoughSpace(LUGGAGE_WEIGHT * NUMBER_OF_TICKETS)).willReturn(true);
    given(cargoFlight.hasEnoughSpace(LUGGAGE_OVERWEIGHT)).willReturn(true);
    given(cargoFlight.hasEnoughSpace(LUGGAGE_OVERWEIGHT * NUMBER_OF_TICKETS)).willReturn(true);
  }

  @Test
  public void numberOfTickets_checkingIfNeedAirCargo_returnTrue() {
    given(passengerFlight.hasValidWeight(LUGGAGE_WEIGHT, NUMBER_OF_TICKETS)).willReturn(false);

    flightPricer = new FlightPricer(passengerFlight, LUGGAGE_WEIGHT, AIR_CARGO_ALLOWED);

    assertTrue(flightPricer.needCargoFlight(NUMBER_OF_TICKETS));
  }

  @Test
  public void tooManyTickets_checkingIfNeedAirCargo_returnFalse() {
    given(passengerFlight.hasValidWeight(LUGGAGE_WEIGHT, NUMBER_OF_TICKETS)).willReturn(true);

    flightPricer = new FlightPricer(passengerFlight, LUGGAGE_WEIGHT, AIR_CARGO_ALLOWED);

    assertFalse(flightPricer.needCargoFlight(NUMBER_OF_TICKETS));
  }

  @Test(expected = InvalidWeightException.class)
  public void flightPricerThatCannotHaveACargoFlight_associatingAirCargoToFlightPassengerForOneTicket_throwInvalidWeightException() {
    flightPricer = new FlightPricer(passengerFlight, LUGGAGE_WEIGHT, AIR_CARGO_NOT_ALLOWED);
    List<CargoFlight> cargoFlights = new ArrayList<>();

    flightPricer.associateCargoFlightToPassengerFlight(cargoFlights, ONE_TICKET);
  }

  @Test
  public void emtpyListOfFlightCargo_associatingAirCargoToFlightPassengerForOneTicket_flightPricerHasNoFlightCargo() {
    flightPricer = new FlightPricer(passengerFlight, LUGGAGE_WEIGHT, AIR_CARGO_ALLOWED);

    List<CargoFlight> cargoFlights = new ArrayList<>();
    flightPricer.associateCargoFlightToPassengerFlight(cargoFlights, ONE_TICKET);

    assertFalse(flightPricer.hasAirCargo());
  }

  @Test
  public void listOfOneFlightCargo_associatingAirCargoToFlightPassengerForOneTicket_flightPricerKeepThatFlightCargo() {
    flightPricer = new FlightPricer(passengerFlight, LUGGAGE_WEIGHT, AIR_CARGO_ALLOWED);

    List<CargoFlight> cargoFlights = new ArrayList<>();
    cargoFlights.add(cargoFlight);
    flightPricer.associateCargoFlightToPassengerFlight(cargoFlights, ONE_TICKET);

    assertEquals(flightPricer.getCargoFlight(), cargoFlight);
    assertTrue(flightPricer.hasAirCargo());
  }

  @Test
  public void listOfOneFlightCargo_associatingAirCargoToFlightPassengerForMultipleTicket_flightPricerKeepThatFlightCargo() {
    flightPricer = new FlightPricer(passengerFlight, LUGGAGE_WEIGHT, AIR_CARGO_ALLOWED);

    List<CargoFlight> cargoFlights = new ArrayList<>();
    cargoFlights.add(cargoFlight);
    flightPricer.associateCargoFlightToPassengerFlight(cargoFlights, NUMBER_OF_TICKETS);

    assertEquals(flightPricer.getCargoFlight(), cargoFlight);
    assertTrue(flightPricer.hasAirCargo());
  }

  @Test
  public void listOfOneFlightCargoNotWithinThreeDays_associatingAirCargoToFlightPassengerForMultipleTicket_flightPricerKeepNoCargoFligth() {
    flightPricer = new FlightPricer(passengerFlight, LUGGAGE_WEIGHT, AIR_CARGO_ALLOWED);
    given(cargoFlight.getDate()).willReturn(CARGO_FLIGHT_TOO_LATE_DATE_AS_DATETIME);

    List<CargoFlight> cargoFlights = new ArrayList<>();
    cargoFlights.add(cargoFlight);
    flightPricer.associateCargoFlightToPassengerFlight(cargoFlights, NUMBER_OF_TICKETS);

    assertFalse(flightPricer.hasAirCargo());

  }

  @Test
  public void listOfFlightCargo_associatingAirCargoToFlightPassengerForMultipleTicket_flightPricerKeepThatFlightCargo() {
    flightPricer = new FlightPricer(passengerFlight, LUGGAGE_WEIGHT, AIR_CARGO_ALLOWED);

    given(anotherCargoFlight.getDate()).willReturn(CARGO_FLIGHT_LATER_DATE_AS_DATETIME);
    given(anotherCargoFlight.hasEnoughSpace(LUGGAGE_WEIGHT * NUMBER_OF_TICKETS)).willReturn(true);

    List<CargoFlight> cargoFlights = new ArrayList<>();
    cargoFlights.add(anotherCargoFlight);
    cargoFlights.add(cargoFlight);
    flightPricer.associateCargoFlightToPassengerFlight(cargoFlights, NUMBER_OF_TICKETS);

    assertEquals(flightPricer.getCargoFlight(), cargoFlight);
    assertTrue(flightPricer.hasAirCargo());
  }

  @Test
  public void flightPassengerInfos_creatingFlightPricer_hasNoCargo() {
    flightPricer = new FlightPricer(passengerFlight, LUGGAGE_WEIGHT, AIR_CARGO_NOT_ALLOWED);

    FlightPricer newFlightPricer = new FlightPricer(passengerFlight, LUGGAGE_WEIGHT, AIR_CARGO_NOT_ALLOWED);

    assertFalse(newFlightPricer.hasAirCargo());
  }

  @Test
  public void flightPassengerWithNoCargoAndNoExtraWeight_calculatingPriceForAnEconomicSeat_returnCalculatedFlightPrice() {
    flightPricer = new FlightPricer(passengerFlight, LUGGAGE_WEIGHT, AIR_CARGO_NOT_ALLOWED);

    double calculatedPrice = flightPricer.getFlightTotalPrice(SEAT_CATEGORY_ECONOMIC, ONE_TICKET);

    assertEquals(ECONOMIC_SEATS_PRICE, calculatedPrice, 0);
  }

  @Test
  public void flightPassengerWithNoCargoAndNoExtraWeight_calculatingPriceForARegularSeat_returnCalculatedFlightPrice() {
    flightPricer = new FlightPricer(passengerFlight, LUGGAGE_WEIGHT, AIR_CARGO_NOT_ALLOWED);

    double calculatedPrice = flightPricer.getFlightTotalPrice(SEAT_CATEGORY_REGULAR, ONE_TICKET);

    assertEquals(REGULAR_SEATS_PRICE, calculatedPrice, 0);
  }

  @Test
  public void flightPassengerWithNoCargoAndNoExtraWeight_calculatingPriceForABusinessSeat_returnCalculatedFlightPrice() {
    flightPricer = new FlightPricer(passengerFlight, LUGGAGE_WEIGHT, AIR_CARGO_NOT_ALLOWED);

    double calculatedPrice = flightPricer.getFlightTotalPrice(SEAT_CATEGORY_BUSINESS, ONE_TICKET);

    assertEquals(BUSINESS_SEATS_PRICE, calculatedPrice, 0);
  }

  @Test
  public void flightPassengerWithCargoAndNoExtraWeight_calculatingPriceForAnEconomicSeat_returnCalculatedFlightPrice() {
    flightPricer = new FlightPricer(passengerFlight, LUGGAGE_WEIGHT, AIR_CARGO_ALLOWED);

    List<CargoFlight> cargoFlights = new ArrayList<>();
    cargoFlights.add(cargoFlight);
    flightPricer.associateCargoFlightToPassengerFlight(cargoFlights, ONE_TICKET);

    double calculatedPrice = flightPricer.getFlightTotalPrice(SEAT_CATEGORY_ECONOMIC, ONE_TICKET);

    assertEquals(ECONOMIC_SEATS_PRICE + CARGO_FLIGHT_PRICE_BY_WEIGHT, calculatedPrice, 0);
  }

  @Test
  public void flightPassengerWithCargoAndNoExtraWeight_calculatingPriceForARegularSeat_returnCalculatedFlightPrice() {
    flightPricer = new FlightPricer(passengerFlight, LUGGAGE_WEIGHT, AIR_CARGO_ALLOWED);

    List<CargoFlight> cargoFlights = new ArrayList<>();
    cargoFlights.add(cargoFlight);
    flightPricer.associateCargoFlightToPassengerFlight(cargoFlights, ONE_TICKET);

    double calculatedPrice = flightPricer.getFlightTotalPrice(SEAT_CATEGORY_REGULAR, ONE_TICKET);

    assertEquals(REGULAR_SEATS_PRICE + CARGO_FLIGHT_PRICE_BY_WEIGHT, calculatedPrice, 0);
  }

  @Test
  public void flightPassengerWithCargoAndNoExtraWeight_calculatingPriceForABusinessSeat_returnCalculatedFlightPrice() {
    flightPricer = new FlightPricer(passengerFlight, LUGGAGE_WEIGHT, AIR_CARGO_ALLOWED);

    List<CargoFlight> cargoFlights = new ArrayList<>();
    cargoFlights.add(cargoFlight);
    flightPricer.associateCargoFlightToPassengerFlight(cargoFlights, ONE_TICKET);

    double calculatedPrice = flightPricer.getFlightTotalPrice(SEAT_CATEGORY_BUSINESS, ONE_TICKET);

    assertEquals(BUSINESS_SEATS_PRICE + CARGO_FLIGHT_PRICE_BY_WEIGHT, calculatedPrice, 0);
  }

  @Test
  public void flightPassengerWithCargoAndExtraWeight_calculatingPriceForAnEconomicSeat_returnCalculatedFlightPrice() {
    flightPricer = new FlightPricer(passengerFlight, LUGGAGE_OVERWEIGHT, AIR_CARGO_ALLOWED);
    given(cargoFlight.getPriceForWeight(LUGGAGE_OVERWEIGHT)).willReturn(CARGO_FLIGHT_PRICE_BY_WEIGHT);
    given(cargoFlight.hasEnoughSpace(LUGGAGE_OVERWEIGHT)).willReturn(true);
    List<CargoFlight> cargoFlights = new ArrayList<>();
    cargoFlights.add(cargoFlight);
    flightPricer.associateCargoFlightToPassengerFlight(cargoFlights, ONE_TICKET);

    double calculatedPrice = flightPricer.getFlightTotalPrice(SEAT_CATEGORY_ECONOMIC, ONE_TICKET);

    assertEquals(ECONOMIC_SEATS_PRICE + CARGO_FLIGHT_PRICE_BY_WEIGHT, calculatedPrice, 0);
  }

  @Test
  public void flightPassengerWithNoCargoAndExtraWeight_calculatingPriceForAnEconomicSeat_returnCalculatedFlightPrice() {
    flightPricer = new FlightPricer(passengerFlight, LUGGAGE_OVERWEIGHT, AIR_CARGO_NOT_ALLOWED);

    double calculatedPrice = flightPricer.getFlightTotalPrice(SEAT_CATEGORY_ECONOMIC, ONE_TICKET);

    assertEquals(ECONOMIC_SEATS_PRICE + EXTRA_FEES, calculatedPrice, 0);
  }

  @Test
  public void flightPassengerWithCargoAndExtraWeightAndMultipleTickets_calculatingPriceForARegularSeat_returnCalculatedFlightPrice() {
    flightPricer = new FlightPricer(passengerFlight, LUGGAGE_OVERWEIGHT, AIR_CARGO_NOT_ALLOWED);

    double calculatedPrice = flightPricer.getFlightTotalPrice(SEAT_CATEGORY_REGULAR, NUMBER_OF_TICKETS);

    assertEquals(NUMBER_OF_TICKETS * (REGULAR_SEATS_PRICE + EXTRA_FEES), calculatedPrice, 0);
  }

  @Test
  public void flightPassengerWithCargoAndExtraWeightAndMultipleTickets_calculatingPriceForABusinessSeat_returnCalculatedFlightPrice() {
    flightPricer = new FlightPricer(passengerFlight, LUGGAGE_OVERWEIGHT, AIR_CARGO_NOT_ALLOWED);

    double calculatedPrice = flightPricer.getFlightTotalPrice(SEAT_CATEGORY_BUSINESS, NUMBER_OF_TICKETS);

    assertEquals(NUMBER_OF_TICKETS * (BUSINESS_SEATS_PRICE + EXTRA_FEES), calculatedPrice, 0);
  }

  @Test
  public void adjustingAvailability_delegateToPassengerFlightCheckAvailability() {
    CartItemDtoRequest cartItemDtoToProcess = new CartItemDtoRequest();
    flightPricer = new FlightPricer(passengerFlight, LUGGAGE_OVERWEIGHT, AIR_CARGO_NOT_ALLOWED);

    flightPricer.adjustAvailability(cartItemDtoToProcess);

    verify(passengerFlight).checkAvailability(cartItemDtoToProcess.seatCategory, cartItemDtoToProcess.numberOfTickets, AIR_CARGO_NOT_ALLOWED,
        LUGGAGE_OVERWEIGHT);
  }

  @Test
  public void hasAirCargo_adjustingAvailability_delegateToCargoFlightCheckAvailability() {
    CartItemDtoRequest cartItemDtoToProcess = new CartItemDtoRequest();
    flightPricer = new FlightPricer(passengerFlight, LUGGAGE_WEIGHT, AIR_CARGO_ALLOWED);

    List<CargoFlight> cargoFlights = new ArrayList<>();
    cargoFlights.add(cargoFlight);
    flightPricer.associateCargoFlightToPassengerFlight(cargoFlights, ONE_TICKET);

    flightPricer.adjustAvailability(cartItemDtoToProcess);

    verify(cargoFlight).checkAvailability(cartItemDtoToProcess.numberOfTickets, LUGGAGE_WEIGHT);
  }

  @Test
  public void hasAirCargo_adjustingAvailability_delegateToCargoFlightAdjustWeightAvailable() {
    CartItemDtoRequest cartItemDtoToProcess = new CartItemDtoRequest();
    flightPricer = new FlightPricer(passengerFlight, LUGGAGE_WEIGHT, AIR_CARGO_ALLOWED);

    List<CargoFlight> cargoFlights = new ArrayList<>();
    cargoFlights.add(cargoFlight);
    flightPricer.associateCargoFlightToPassengerFlight(cargoFlights, ONE_TICKET);

    flightPricer.adjustAvailability(cartItemDtoToProcess);

    verify(cargoFlight).adjustWeightAvailable(cartItemDtoToProcess.numberOfTickets, LUGGAGE_WEIGHT);
  }

  @Test
  public void adjustingAvailability_delegateToPassengerFlightAdjustAvailability() {
    CartItemDtoRequest cartItemDtoToProcess = new CartItemDtoRequest();
    flightPricer = new FlightPricer(passengerFlight, LUGGAGE_OVERWEIGHT, AIR_CARGO_NOT_ALLOWED);

    flightPricer.adjustAvailability(cartItemDtoToProcess);

    verify(passengerFlight).adjustAvailability(cartItemDtoToProcess.seatCategory, cartItemDtoToProcess.numberOfTickets, AIR_CARGO_NOT_ALLOWED,
        LUGGAGE_OVERWEIGHT);
  }
  
}