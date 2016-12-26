package ca.ulaval.glo4003.quickflyws.domain.cart.cartItem.planeTicket;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.seats.seatCategory.SeatCategory;
import ca.ulaval.glo4003.quickflyws.domain.generalExceptions.BadRequestException;

public class PlaneTicketFactory {

  private AtomicInteger ticketId = new AtomicInteger();

  public List<PlaneTicket> createTickets(String company, LocalDateTime date, String destination, String source, double price, double luggageWeight,
      boolean hasAirCargo, LocalDateTime airCargoDate, int numberOfTicketsWanted, SeatCategory seatCategory) {
    if (numberOfTicketsWanted > 0) {
      List<PlaneTicket> tickets = new ArrayList<PlaneTicket>();
      for (int i = 0; i < numberOfTicketsWanted; i++) {
        int id = getNextUniqueIndex();
        tickets.add(new PlaneTicket(id, company, date, destination, source, price, luggageWeight, hasAirCargo, airCargoDate, seatCategory));
      }
      return tickets;

    } else {
      throw new BadRequestException("Can't ask for negative or null number of tickets.");
    }
  }

  private int getNextUniqueIndex() {
    return ticketId.incrementAndGet();
  }

}