package ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.seats;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.seats.seatCategory.SeatCategory;

public class SeatTest {
 
  private static final double BUSINESS_SEATS_PRICE = 90.00;
  private static final double REGULAR_SEATS_PRICE = 40.00;
  private static final double ECONOMIC_SEATS_PRICE = 20.00;
  
  private static final SeatCategory ECONOMIC_CATEGORY = SeatCategory.ECONOMIC;
  private static final SeatCategory REGULAR_CATEGORY = SeatCategory.REGULAR;
  private static final SeatCategory BUSINESS_CATEGORY = SeatCategory.BUSINESS;
    
  private Seat seat;
  
  @Test
  public void seatWithEconomicCategory_findingIfSeatHasEconomicCategory_returnTrue(){
    seat = new EconomicSeat(ECONOMIC_SEATS_PRICE);
    
    assertTrue(seat.categoryIs(ECONOMIC_CATEGORY));
  }
  
  @Test
  public void seatWithEconomicCategory_findingIfSeatHasRegularCategory_returnFalse(){
    seat = new EconomicSeat(ECONOMIC_SEATS_PRICE);
    
    assertFalse(seat.categoryIs(REGULAR_CATEGORY));
  }
  
  @Test
  public void seatWithEconomicCategory_findingIfSeatHasBusinessCategory_returnFalse(){
    seat = new EconomicSeat(ECONOMIC_SEATS_PRICE);
    
    assertFalse(seat.categoryIs(BUSINESS_CATEGORY));
  }
  
  @Test
  public void seatWithRegularCategory_findingIfSeatHasEconomicCategory_returnFalse(){
    seat = new RegularSeat(REGULAR_SEATS_PRICE);
    
    assertFalse(seat.categoryIs(ECONOMIC_CATEGORY));
  }
  
  @Test
  public void seatWithRegularCategory_findingIfSeatHasRegularCategory_returnTrue(){
    seat = new RegularSeat(REGULAR_SEATS_PRICE);
    
    assertTrue(seat.categoryIs(REGULAR_CATEGORY));
  }
  
  @Test
  public void seatWithRegularCategory_findingIfSeatHasBusinessCategory_returnFalse(){
    seat = new RegularSeat(REGULAR_SEATS_PRICE);
    
    assertFalse(seat.categoryIs(BUSINESS_CATEGORY));
  }
  
  @Test
  public void seatWithBusinessCategory_findingIfSeatHasEconomicCategory_returnFalse(){
    seat = new BusinessSeat(BUSINESS_SEATS_PRICE);
    
    assertFalse(seat.categoryIs(ECONOMIC_CATEGORY));
  }
  
  @Test
  public void seatWithBusinessCategory_findingIfSeatHasRegularCategory_returnFalse(){
    seat = new BusinessSeat(BUSINESS_SEATS_PRICE);
    
    assertFalse(seat.categoryIs(REGULAR_CATEGORY));
  }
  
  @Test
  public void seatWithBusinessCategory_findingIfSeatHasBusinessCategory_returnTrue(){
    seat = new BusinessSeat(BUSINESS_SEATS_PRICE);
    
    assertTrue(seat.categoryIs(BUSINESS_CATEGORY));
  }
  
}