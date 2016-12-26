package ca.ulaval.glo4003.quickflyws.domain.cart.cartItem.planeTicket;

import java.time.LocalDateTime;

import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.seats.seatCategory.SeatCategory;
import ca.ulaval.glo4003.quickflyws.domain.generalExceptions.NotFoundException;

public class PlaneTicket {

  private int ticketId;
  private String company;
  private LocalDateTime date;
  private String destination;
  private String source;
  private double price;
  private double luggageWeight;
  private boolean hasAirCargo;
  private LocalDateTime airCargoDate;
  private SeatCategory seatCategory;

  public PlaneTicket(int ticketId, String company, LocalDateTime date, String destination, String source, double price, double luggageWeight,
      boolean hasAirCargo, LocalDateTime airCargoDate, SeatCategory seatCategory) {
    this.ticketId = ticketId;
    this.company = company;
    this.date = date;
    this.destination = destination;
    this.source = source;
    this.price = price;
    this.luggageWeight = luggageWeight;
    this.hasAirCargo = hasAirCargo;
    this.airCargoDate = airCargoDate;
    this.seatCategory = seatCategory;
  }

  public int getTicketId() {
    return ticketId;
  }

  public double getPrice() {
    return price;
  }

  public String getCompany() {
    return company;
  }

  public LocalDateTime getDate() {
    return date;
  }

  public String getDestination() {
    return destination;
  }

  public String getSource() {
    return source;
  }

  public double getLuggageWeight() {
    return luggageWeight;
  }

  public SeatCategory getSeatCategory() {
    return seatCategory;
  }
  
  public boolean hasAirCargo() {
    return hasAirCargo;
  }

  public LocalDateTime getAirCargoDate() {
    if (hasAirCargo) {
      return airCargoDate;
    }
    throw new NotFoundException("This ticket does not have an AirCargo tied to it");
  }

  @Override
  public String toString() {
    String separator = System.getProperty("line.separator");
    String cargo = "";
    if (hasAirCargo) {
      cargo = "\tAirCargo date : " + airCargoDate.toLocalDate();
    }
    return "Ticket : " + ticketId + "\tFrom : " + source + "\tTo : " + destination + "\tOn : " + date.toLocalDate() + "\tAt : " + date.toLocalTime()
        + "\tBy : " + company + "\tSeat category : " + seatCategory + "\tWith an amount of luggages of : " + luggageWeight + " lbs" + "\tFor : "
        + price + "$" + cargo + separator;
  }

}