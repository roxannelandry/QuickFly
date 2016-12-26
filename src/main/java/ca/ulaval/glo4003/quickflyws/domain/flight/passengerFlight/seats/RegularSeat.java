package ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.seats;

import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.seats.seatCategory.SeatCategory;

public class RegularSeat extends Seat {
  
  public RegularSeat(double price) {
    super(SeatCategory.REGULAR, price);
  }
  
}