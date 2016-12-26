package ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.seats;

import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.seats.seatCategory.SeatCategory;

public class EconomicSeat extends Seat {
   
  public EconomicSeat(double price) {
    super(SeatCategory.ECONOMIC, price);    
  }
  
}