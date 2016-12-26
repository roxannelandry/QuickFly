package ca.ulaval.glo4003.quickflyws.dto;

import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.seats.seatCategory.SeatCategory;

public class CartItemDtoRequest {

  public String user;
  public String company;
  public String destination;
  public String source;
  public String date;
  public double luggageWeight;
  public int numberOfTickets;
  public boolean airCargoAllowed;
  public SeatCategory seatCategory;
  
}