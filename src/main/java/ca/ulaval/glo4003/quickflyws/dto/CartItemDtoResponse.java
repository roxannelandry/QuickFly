package ca.ulaval.glo4003.quickflyws.dto;

import java.time.LocalDateTime;

import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.seats.seatCategory.SeatCategory;

public class CartItemDtoResponse {

  public String source;
  public String destination;
  public LocalDateTime date;
  public String company;
  public int numberOfTickets;
  public double luggageWeight;
  public double pricePerTicket;
  public double totalPrice;
  public boolean hasAirCargo;
  public LocalDateTime airCargoDate;
  public SeatCategory seatCategory;
  
}