package ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.seats;

import java.util.ArrayList;
import java.util.List;

import ca.ulaval.glo4003.quickflyws.domain.flight.exceptions.InvalidSeatCategoryException;
import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.seats.seatCategory.SeatCategory;

public class Seats {

  private List<Seat> seats;

  private SeatFactory seatFactory = new SeatFactory();

  public void createSeatsForFlight(int numberTotalOfSeatsEconomic, double seatsEconomicPrice, int numberTotalOfSeatsRegular, double seatsRegularPrice,
      int numberTotalOfSeatsBusiness, double seatsBusinessPrice) {
    seats = new ArrayList<Seat>();

    for (int i = 0; i < numberTotalOfSeatsEconomic; i++) {
      seats.add(seatFactory.createSeat(SeatCategory.ECONOMIC, seatsEconomicPrice));
    }

    for (int i = 0; i < numberTotalOfSeatsRegular; i++) {
      seats.add(seatFactory.createSeat(SeatCategory.REGULAR, seatsRegularPrice));
    }

    for (int i = 0; i < numberTotalOfSeatsBusiness; i++) {
      seats.add(seatFactory.createSeat(SeatCategory.BUSINESS, seatsBusinessPrice));
    }
  }

  public void addReleasedSeats(SeatCategory seatCategory, int numberOfTicketsToRelease) {
    for (Seat seat : seats) {
      if (numberOfTicketsToRelease == 0) {
        break;
      }
      if (seat.categoryIs(seatCategory) && !seat.isAvailable()) {
        seat.setAvailability(true);
        numberOfTicketsToRelease++;
      }
    }
  }

  public void decreaseNumberOfAvailableSeats(SeatCategory seatCategory, int numberOfTickets) {
    for (Seat seat : seats) {
      if (numberOfTickets == 0) {
        break;
      }
      if (seat.categoryIs(seatCategory) && seat.isAvailable()) {
        seat.setAvailability(false);
        numberOfTickets--;
      }
    }
  }

  public int getNumberSeatsAvailable(SeatCategory seatCategory) {
    int seatsCounter = 0;
    for (Seat seat : seats) {
      if (seat.categoryIs(seatCategory) && seat.isAvailable()) {
        seatsCounter++;
      }
    }
    return seatsCounter;
  }

  public double getPriceByCategory(SeatCategory seatCategory) {
    switch (seatCategory) {
    case ECONOMIC:
      return getEconomicSeatsPrice();
    case REGULAR:
      return getRegularSeatsPrice();
    case BUSINESS:
      return getBusinessSeatsPrice();
    default:
      throw new InvalidSeatCategoryException("This seat category does not exist in the system.");
    }
  }

  private double getEconomicSeatsPrice() {
    for (Seat seat : seats) {
      if (seat.categoryIs(SeatCategory.ECONOMIC)) {
        return seat.getPrice();
      }
    }
    return 0;
  }

  private double getRegularSeatsPrice() {
    for (Seat seat : seats) {
      if (seat.categoryIs(SeatCategory.REGULAR)) {
        return seat.getPrice();
      }
    }
    return 0;
  }

  private double getBusinessSeatsPrice() {
    for (Seat seat : seats) {
      if (seat.categoryIs(SeatCategory.BUSINESS)) {
        return seat.getPrice();
      }
    }
    return 0;
  }

  public int getTotalNumberSeats(SeatCategory seatCategory) {
    int counter = 0;
    for (Seat seat : seats) {
      if (seat.categoryIs(seatCategory)) {
        counter++;
      }
    }
    return counter;
  }

}