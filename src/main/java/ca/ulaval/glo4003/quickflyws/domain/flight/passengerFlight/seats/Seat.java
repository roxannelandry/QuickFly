package ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.seats;

import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.seats.seatCategory.SeatCategory;

public abstract class Seat {
  
  private SeatCategory seatCategory;
  private double price;
  private boolean isAvailable;
  
  public Seat(SeatCategory seatCategory, double price) {
    this.seatCategory = seatCategory;
    this.price = price;
    this.isAvailable = true;
  }

  public boolean categoryIs(SeatCategory seatCategory) {
    return this.seatCategory.equals(seatCategory);
  }

  public void setAvailability(boolean isAvailable) {
    this.isAvailable = isAvailable;
  }

  public boolean isAvailable() {
    return isAvailable;
  }

  public double getPrice() {
    return price;
  }
  
}