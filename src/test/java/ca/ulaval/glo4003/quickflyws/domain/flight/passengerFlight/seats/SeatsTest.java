package ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.seats;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.seats.seatCategory.SeatCategory;

public class SeatsTest {

  private static final int NUMBER_OF_BUSINESS_SEATS = 10;
  private static final int NUMBER_OF_REGULAR_SEATS = 5;
  private static final int NUMBER_OF_ECONOMIC_SEATS = 2;
  private static final int NO_SEAT = 0;

  private static final int NUMBER_OF_SEATS_TO_DECREASE = 1;
  private static final int OVERLOAD_NUMBER_OF_SEATS_TO_DECREASE = 100;
  private static final int NO_SEATS_TO_DECREASE = 0;
  
  private static final int NUMBER_OF_SEATS_TO_ADD = 1;
  private static final int OVERLOAD_NUMBER_OF_SEATS_TO_ADD = 100;
  private static final int NO_SEATS_TO_ADD = 0;

  private static final double BUSINESS_SEATS_PRICE = 90.00;
  private static final double REGULAR_SEATS_PRICE = 40.00;
  private static final double ECONOMIC_SEATS_PRICE = 20.00;
  private static final double NO_PRICE = 0;

  private static final SeatCategory ECONOMIC_CATEGORY = SeatCategory.ECONOMIC;
  private static final SeatCategory REGULAR_CATEGORY = SeatCategory.REGULAR;
  private static final SeatCategory BUSINESS_CATEGORY = SeatCategory.BUSINESS;

  private Seats seats;

  @Before
  public void setUp() {
    seats = new Seats();
    seats.createSeatsForFlight(NUMBER_OF_ECONOMIC_SEATS, ECONOMIC_SEATS_PRICE, NUMBER_OF_REGULAR_SEATS, REGULAR_SEATS_PRICE, NUMBER_OF_BUSINESS_SEATS,
        BUSINESS_SEATS_PRICE);
  }

  @Test
  public void gettingNumberSeatsEconomicAvailable_returnEconomicSeatsAvailable() {
    int numberOfEconomicSeats = seats.getNumberSeatsAvailable(ECONOMIC_CATEGORY);

    assertEquals(numberOfEconomicSeats, NUMBER_OF_ECONOMIC_SEATS);
  }

  @Test
  public void gettingNumberSeatsRegularAvailable_returnRegularSeatsAvailable() {
    int numberOfRegularSeats = seats.getNumberSeatsAvailable(REGULAR_CATEGORY);

    assertEquals(numberOfRegularSeats, NUMBER_OF_REGULAR_SEATS);
  }

  @Test
  public void gettingNumberSeatsBusinessAvailable_returnBusinessSeatsAvailable() {
    int numberOfBusinessSeats = seats.getNumberSeatsAvailable(BUSINESS_CATEGORY);

    assertEquals(numberOfBusinessSeats, NUMBER_OF_BUSINESS_SEATS);
  }

  @Test
  public void seatWithEconomicCategory_gettingSeatsPrice_returnEconomicSeatsPrice() {
    double economicPrice = seats.getPriceByCategory(ECONOMIC_CATEGORY);

    assertEquals(economicPrice, ECONOMIC_SEATS_PRICE, 0);
  }

  @Test
  public void flightWithNoEconomic_gettingSeatsPrice_returnZero() {
    Seats seatsNoSeat = new Seats();
    seatsNoSeat.createSeatsForFlight(NO_SEAT, ECONOMIC_SEATS_PRICE, NO_SEAT, REGULAR_SEATS_PRICE, NO_SEAT, BUSINESS_SEATS_PRICE);

    double economicPrice = seatsNoSeat.getPriceByCategory(ECONOMIC_CATEGORY);

    assertEquals(economicPrice, NO_PRICE, 0);
  }

  @Test
  public void seatWithRegularCategory_getSeatsPrice_returnRegularSeatsPrice() {
    double regularPrice = seats.getPriceByCategory(REGULAR_CATEGORY);

    assertEquals(regularPrice, REGULAR_SEATS_PRICE, 0);
  }

  @Test
  public void flightWithNoRegular_gettingSeatsPrice_returnZero() {
    Seats seatsNoSeat = new Seats();
    seatsNoSeat.createSeatsForFlight(NO_SEAT, ECONOMIC_SEATS_PRICE, NO_SEAT, REGULAR_SEATS_PRICE, NO_SEAT, BUSINESS_SEATS_PRICE);

    double regularPrice = seatsNoSeat.getPriceByCategory(REGULAR_CATEGORY);

    assertEquals(regularPrice, NO_PRICE, 0);
  }

  @Test
  public void seatWithBusinessCategory_gettingSeatsPrice_returnBusinessSeatsPrice() {
    double businessPrice = seats.getPriceByCategory(BUSINESS_CATEGORY);

    assertEquals(businessPrice, BUSINESS_SEATS_PRICE, 0);
  }

  @Test
  public void flightWithNoBusiness_gettingSeatsPrice_returnZero() {
    Seats seatsNoSeat = new Seats();
    seatsNoSeat.createSeatsForFlight(NO_SEAT, ECONOMIC_SEATS_PRICE, NO_SEAT, REGULAR_SEATS_PRICE, NO_SEAT, BUSINESS_SEATS_PRICE);

    double businessPrice = seatsNoSeat.getPriceByCategory(BUSINESS_CATEGORY);

    assertEquals(businessPrice, NO_PRICE, 0);
  }

  @Test
  public void seatEconomicCategoryNumberOfSeatsToDecrease_decreasingNumberOfAvailableSeats_availableSeatOfTheCategoryIsDecrease() {
    seats.decreaseNumberOfAvailableSeats(ECONOMIC_CATEGORY, NUMBER_OF_SEATS_TO_DECREASE);

    assertEquals(seats.getNumberSeatsAvailable(ECONOMIC_CATEGORY), NUMBER_OF_ECONOMIC_SEATS - NUMBER_OF_SEATS_TO_DECREASE, 0);
  }

  @Test
  public void seatRegularCategoryNumberOfSeatsToDecrease_decreasingNumberOfAvailableSeats_availableSeatOfTheCategoryIsDecrease() {
    seats.decreaseNumberOfAvailableSeats(REGULAR_CATEGORY, NUMBER_OF_SEATS_TO_DECREASE);

    assertEquals(seats.getNumberSeatsAvailable(REGULAR_CATEGORY), NUMBER_OF_REGULAR_SEATS - NUMBER_OF_SEATS_TO_DECREASE, 0);
  }

  @Test
  public void seatBusinessCategoryNumberOfSeatsToDecrease_decreasingNumberOfAvailableSeats_availableSeatOfTheCategoryIsDecrease() {
    seats.decreaseNumberOfAvailableSeats(BUSINESS_CATEGORY, NUMBER_OF_SEATS_TO_DECREASE);

    assertEquals(seats.getNumberSeatsAvailable(BUSINESS_CATEGORY), NUMBER_OF_BUSINESS_SEATS - NUMBER_OF_SEATS_TO_DECREASE, 0);
  }

  @Test
  public void seatRegularCategoryOverloadNumberOfSeatsToDecrease_decreasingNumberOfAvailableSeats_availableSeatOfTheCategoryIsDecrease() {
    seats.decreaseNumberOfAvailableSeats(REGULAR_CATEGORY, OVERLOAD_NUMBER_OF_SEATS_TO_DECREASE);

    assertEquals(seats.getNumberSeatsAvailable(REGULAR_CATEGORY), 0, 0);
  }

  @Test
  public void seatRegularCategoryNoNumberOfSeatsToDecrease_decreasingNumberOfAvailableSeats_noChangeIsDone() {
    seats.decreaseNumberOfAvailableSeats(REGULAR_CATEGORY, NO_SEATS_TO_DECREASE);

    assertEquals(seats.getNumberSeatsAvailable(REGULAR_CATEGORY), NUMBER_OF_REGULAR_SEATS, 0);
  }

  @Test
  public void seatEconomicCategoryNumberOfSeatsToAdd_addingReleasedSeats_availableSeatOfTheCategoryIsAdded() {
    seats.decreaseNumberOfAvailableSeats(ECONOMIC_CATEGORY, NUMBER_OF_SEATS_TO_DECREASE);

    seats.addReleasedSeats(ECONOMIC_CATEGORY, NUMBER_OF_SEATS_TO_ADD);

    assertEquals(seats.getNumberSeatsAvailable(ECONOMIC_CATEGORY), NUMBER_OF_ECONOMIC_SEATS - NUMBER_OF_SEATS_TO_DECREASE + NUMBER_OF_SEATS_TO_ADD,
        0);
  }

  @Test
  public void seatRegularCategoryNumberOfSeatsToAdd_addingReleasedSeats_availableSeatOfTheCategoryIsAdded() {
    seats.decreaseNumberOfAvailableSeats(REGULAR_CATEGORY, NUMBER_OF_SEATS_TO_DECREASE);

    seats.addReleasedSeats(REGULAR_CATEGORY, NUMBER_OF_SEATS_TO_ADD);

    assertEquals(seats.getNumberSeatsAvailable(REGULAR_CATEGORY), NUMBER_OF_REGULAR_SEATS - NUMBER_OF_SEATS_TO_DECREASE + NUMBER_OF_SEATS_TO_ADD, 0);
  }

  @Test
  public void seatBusinessCategoryNumberOfSeatsToAdd_addingReleasedSeats_availableSeatOfTheCategoryIsAdded() {
    seats.decreaseNumberOfAvailableSeats(BUSINESS_CATEGORY, NUMBER_OF_SEATS_TO_DECREASE);

    seats.addReleasedSeats(BUSINESS_CATEGORY, NUMBER_OF_SEATS_TO_ADD);

    assertEquals(seats.getNumberSeatsAvailable(BUSINESS_CATEGORY), NUMBER_OF_BUSINESS_SEATS - NUMBER_OF_SEATS_TO_DECREASE + NUMBER_OF_SEATS_TO_ADD,
        0);
  }

  @Test
  public void seatRegularCategoryOverloadNumberOfSeatsToAdd_addingReleasedSeats_availableSeatOfTheCategoryIsCropToMaximumSeats() {
    seats.addReleasedSeats(REGULAR_CATEGORY, OVERLOAD_NUMBER_OF_SEATS_TO_ADD);

    assertEquals(seats.getNumberSeatsAvailable(REGULAR_CATEGORY), NUMBER_OF_REGULAR_SEATS, 0);
  }

  @Test
  public void seatBusinessCategoryOverloadNumberOfSeatsToAdd_addingReleasedSeats_availableSeatOfTheCategoryIsCropToMaximumSeats() {
    seats.addReleasedSeats(BUSINESS_CATEGORY, OVERLOAD_NUMBER_OF_SEATS_TO_ADD);

    assertEquals(seats.getNumberSeatsAvailable(BUSINESS_CATEGORY), NUMBER_OF_BUSINESS_SEATS, 0);
  }

  @Test
  public void seatEconomicCategoryOverloadNumberOfSeatsToAdd_addingReleasedSeats_availableSeatOfTheCategoryIsCropToMaximumSeats() {
    seats.addReleasedSeats(ECONOMIC_CATEGORY, OVERLOAD_NUMBER_OF_SEATS_TO_ADD);

    assertEquals(seats.getNumberSeatsAvailable(ECONOMIC_CATEGORY), NUMBER_OF_ECONOMIC_SEATS, 0);
  }

  @Test
  public void seatRegularCategoryNoNumberOfSeatsToAdd_addingNumberOfAvailableSeats_noChangeIsDone() {
    seats.addReleasedSeats(REGULAR_CATEGORY, NO_SEATS_TO_ADD);

    assertEquals(seats.getNumberSeatsAvailable(REGULAR_CATEGORY), NUMBER_OF_REGULAR_SEATS, 0);
  }

  @Test
  public void seatEconomicCategory_gettingTotalNumberSeats_numberOfSeatsOfThisCategory() {
    assertEquals(seats.getTotalNumberSeats(ECONOMIC_CATEGORY), NUMBER_OF_ECONOMIC_SEATS);
  }

  @Test
  public void seatRegularCategory_gettingTotalNumberSeats_numberOfSeatsOfThisCategory() {
    assertEquals(seats.getTotalNumberSeats(REGULAR_CATEGORY), NUMBER_OF_REGULAR_SEATS);
  }

  @Test
  public void seatBusinessCategory_gettingTotalNumberSeats_numberOfSeatsOfThisCategory() {
    assertEquals(seats.getTotalNumberSeats(BUSINESS_CATEGORY), NUMBER_OF_BUSINESS_SEATS);
  }

}