package ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.seats;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.seats.seatCategory.SeatCategory;

public class SeatFactoryTest {
  
  private static final double BUSINESS_SEATS_PRICE = 90.00;
  private static final double REGULAR_SEATS_PRICE = 40.00;
  private static final double ECONOMIC_SEATS_PRICE = 20.00;

  private static final SeatCategory BUSINESS_SEAT_CATEGORY = SeatCategory.BUSINESS;
  private static final SeatCategory REGULAR_SEAT_CATEGORY = SeatCategory.REGULAR;
  private static final SeatCategory ECONOMIC_SEAT_CATEGORY = SeatCategory.ECONOMIC;
    
  private SeatFactory seatFactory;
  
  @Before
  public void setUp() {
    seatFactory = new SeatFactory();
  }
  
  @Test
  public void businessCategoryAndPrice_creatingSeatBusiness_returnSeatCreated() {
    Seat businessSeats = seatFactory.createSeat(BUSINESS_SEAT_CATEGORY, BUSINESS_SEATS_PRICE);
    
    assertTrue(businessSeats.categoryIs(BUSINESS_SEAT_CATEGORY));
    assertEquals(businessSeats.getPrice(), BUSINESS_SEATS_PRICE, 0);
  }
  
  @Test
  public void regularCategoryAndPrice_creatingSeatRegular_returnSeatCreated() {
    Seat businessSeats = seatFactory.createSeat(REGULAR_SEAT_CATEGORY, REGULAR_SEATS_PRICE);
    
    assertTrue(businessSeats.categoryIs(REGULAR_SEAT_CATEGORY));
    assertEquals(businessSeats.getPrice(), REGULAR_SEATS_PRICE, 0);
  }
  
  @Test
  public void economicCategoryAndPrice_creatingSeatEconomic_returnSeatCreated() {
    Seat businessSeats = seatFactory.createSeat(ECONOMIC_SEAT_CATEGORY, ECONOMIC_SEATS_PRICE);
    
    assertTrue(businessSeats.categoryIs(ECONOMIC_SEAT_CATEGORY));
    assertEquals(businessSeats.getPrice(), ECONOMIC_SEATS_PRICE, 0);
  }
  
}