package ca.ulaval.glo4003.quickflyws.domain.cart.cartItem;

import java.time.LocalDateTime;
import java.util.List;

import ca.ulaval.glo4003.quickflyws.domain.cart.cartItem.planeTicket.PlaneTicket;
import ca.ulaval.glo4003.quickflyws.domain.cart.cartItem.planeTicket.PlaneTicketFactory;
import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.seats.seatCategory.SeatCategory;
import ca.ulaval.glo4003.quickflyws.dto.CartItemDtoRequest;
import ca.ulaval.glo4003.quickflyws.service.flight.FlightPricer;

public class CartItemFactory {

  private PlaneTicketFactory planeTicketFactory;

  public CartItemFactory(PlaneTicketFactory planeTicketFactory) {
    this.planeTicketFactory = planeTicketFactory;
  }

  public CartItem createNewCartItem(FlightPricer flightPricer, CartItemDtoRequest cartItemDtoRequest) {
    String company = flightPricer.getPassengerFlight().getCompany();
    String source = flightPricer.getPassengerFlight().getSource();
    LocalDateTime date = flightPricer.getPassengerFlight().getDate();
    String destination = flightPricer.getPassengerFlight().getDestination();
    SeatCategory seatCategory = cartItemDtoRequest.seatCategory;
    double price = flightPricer.getFlightTotalPrice(seatCategory, cartItemDtoRequest.numberOfTickets);
    double luggageWeight = flightPricer.getLuggageWeight();
    boolean hasAirCargo = flightPricer.hasAirCargo();
    LocalDateTime airCargoDate = flightPricer.getPassengerFlight().getDate();
    if (hasAirCargo) {
      airCargoDate = flightPricer.getCargoFlight().getDate();
    }

    List<PlaneTicket> planeTickets = planeTicketFactory.createTickets(company, date, destination, source, price, luggageWeight, hasAirCargo,
        airCargoDate, cartItemDtoRequest.numberOfTickets, seatCategory);

    CartItem newCartItem = new CartItem(planeTickets);
    return newCartItem;
  }

}