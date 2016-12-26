package ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.seats;

import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.seats.seatCategory.SeatCategory;

public class BusinessSeat extends Seat {
  
  public BusinessSeat(double price) {
    super(SeatCategory.BUSINESS, price);
  }

}