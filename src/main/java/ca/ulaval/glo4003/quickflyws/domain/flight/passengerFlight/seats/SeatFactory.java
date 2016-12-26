package ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.seats;

import ca.ulaval.glo4003.quickflyws.domain.flight.exceptions.InvalidSeatCategoryException;
import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.seats.seatCategory.SeatCategory;

public class SeatFactory {

  public Seat createSeat(SeatCategory seatCategory, double price) {
    switch (seatCategory) {
      case ECONOMIC:
        return new EconomicSeat(price);
      case REGULAR:
        return new RegularSeat(price);
      case BUSINESS:
        return new BusinessSeat(price);
      default:
        throw new InvalidSeatCategoryException("This seat category does not exist in the system.");
    }
  }
  
}