package ca.ulaval.glo4003.quickflyws.service.cart;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import ca.ulaval.glo4003.quickflyws.domain.cart.Cart;
import ca.ulaval.glo4003.quickflyws.domain.cart.cartItem.CartItem;
import ca.ulaval.glo4003.quickflyws.domain.cart.cartItem.CartItemFactory;
import ca.ulaval.glo4003.quickflyws.domain.cart.cartItem.planeTicket.PlaneTicket;
import ca.ulaval.glo4003.quickflyws.domain.cart.cartItem.planeTicket.PlaneTicketFactory;
import ca.ulaval.glo4003.quickflyws.domain.dateConverterChecker.DateConverterChecker;
import ca.ulaval.glo4003.quickflyws.domain.flight.cargoFlight.CargoFlight;
import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.PassengerFlight;
import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.seats.seatCategory.SeatCategory;
import ca.ulaval.glo4003.quickflyws.dto.CartDto;
import ca.ulaval.glo4003.quickflyws.dto.CartItemDtoRequest;
import ca.ulaval.glo4003.quickflyws.service.flight.FlightPricer;
import ca.ulaval.glo4003.quickflyws.service.flight.FlightPricerFactory;
import ca.ulaval.glo4003.quickflyws.service.flight.FlightRepository;

public class CartService {

  private CartRepository cartRepository;
  private CartAssembler cartAssembler;
  private FlightRepository flightRepository;
  private CartItemFactory cartItemFactory;
  private PlaneTicketFactory planeTicketFactory;
  private DateConverterChecker dateChecker;
  private FlightPricerFactory flightPricerFactory;

  public CartService(CartRepository cartRepository, FlightRepository flightRepository, CartAssembler cartAssembler, CartItemFactory cartItemFactory,
      PlaneTicketFactory planeTicketFactory, FlightPricerFactory flightPricerFactory) {
    this.cartRepository = cartRepository;
    this.cartAssembler = cartAssembler;
    this.flightRepository = flightRepository;
    this.cartItemFactory = cartItemFactory;
    this.planeTicketFactory = planeTicketFactory;
    this.flightPricerFactory = flightPricerFactory;

    this.dateChecker = new DateConverterChecker();
  }

  public CartDto addCartItemToCart(CartItemDtoRequest cartItemDtoToProcess) {
    LocalDateTime dateTime = dateChecker.dateStringToDateTimeObject(cartItemDtoToProcess.date);

    PassengerFlight passengerFlight = flightRepository.findPassengerFlight(cartItemDtoToProcess.company, dateTime, cartItemDtoToProcess.destination,
        cartItemDtoToProcess.source);

    FlightPricer flightPricer = flightPricerFactory.createFlightPricer(passengerFlight, cartItemDtoToProcess.luggageWeight,
        cartItemDtoToProcess.airCargoAllowed);

    if (flightPricer.needCargoFlight(cartItemDtoToProcess.numberOfTickets)) {
      List<CargoFlight> cargoFlights = flightRepository.findCargoFlights(flightPricer.getPassengerFlight().getCompany(),
          flightPricer.getPassengerFlight().getDate(), flightPricer.getPassengerFlight().getDestination(),
          flightPricer.getPassengerFlight().getSource());
      flightPricer.associateCargoFlightToPassengerFlight(cargoFlights, cartItemDtoToProcess.numberOfTickets);
    }

    flightPricer.adjustAvailability(cartItemDtoToProcess);

    CartItem cartItemToAdd = cartItemFactory.createNewCartItem(flightPricer, cartItemDtoToProcess);

    Cart cart = getCartToAddCartItem(cartItemDtoToProcess);

    cart.addCartItem(cartItemToAdd);

    cartRepository.save(cart);

    return cartAssembler.assembleDtoFromCart(cart);
  }

  public CartDto updateCartItem(CartItemDtoRequest cartItemDtoToProcess) {
    LocalDateTime dateTime = dateChecker.dateStringToDateTimeObject(cartItemDtoToProcess.date);

    PassengerFlight passengerFlight = flightRepository.findPassengerFlight(cartItemDtoToProcess.company, dateTime, cartItemDtoToProcess.destination,
        cartItemDtoToProcess.source);

    Cart cart = cartRepository.find(cartItemDtoToProcess.user);

    CartItem cartItemToUpdate = cart.findCartItem(cartItemDtoToProcess.luggageWeight, passengerFlight.getCompany(), passengerFlight.getDate(),
        passengerFlight.getDestination(), passengerFlight.getSource(), cartItemDtoToProcess.seatCategory);

    int differenceInNumberOfTickets = cartItemDtoToProcess.numberOfTickets - cartItemToUpdate.getNumberOfTickets();

    passengerFlight.checkAvailability(cartItemDtoToProcess.seatCategory, differenceInNumberOfTickets, cartItemToUpdate.hasAirCargo(),
        cartItemDtoToProcess.luggageWeight);

    CargoFlight cargoFlight = null;
    if (cartItemToUpdate.hasAirCargo()) {
      cargoFlight = flightRepository.findCargoFlight(passengerFlight.getCompany(), cartItemToUpdate.getPlaneTicketCargoDate(),
          passengerFlight.getDestination(), passengerFlight.getSource());
      cargoFlight.checkAvailability(differenceInNumberOfTickets, cartItemToUpdate.getPlaneTicketLuggageWeight());
      cargoFlight.adjustWeightAvailable(differenceInNumberOfTickets, cartItemToUpdate.getPlaneTicketLuggageWeight());

    }
    passengerFlight.adjustAvailability(cartItemDtoToProcess.seatCategory, differenceInNumberOfTickets, cartItemToUpdate.hasAirCargo(),
        cartItemDtoToProcess.luggageWeight);

    cartItemToUpdate = updateTicketsInCartItem(cartItemToUpdate, differenceInNumberOfTickets);
    cart.updateCartItem(cartItemToUpdate, differenceInNumberOfTickets);

    return cartAssembler.assembleDtoFromCart(cart);
  }

  private CartItem updateTicketsInCartItem(CartItem cartItemToUpdate, int differenceInNumberOfTickets) {
    List<PlaneTicket> tickets = new ArrayList<>();

    LocalDateTime luggageDepartureTime = cartItemToUpdate.getPlaneTicketDate();

    if (cartItemToUpdate.hasAirCargo()) {
      luggageDepartureTime = cartItemToUpdate.getPlaneTicketCargoDate();
    }

    if (differenceInNumberOfTickets > 0) {
      tickets = planeTicketFactory.createTickets(cartItemToUpdate.getPlaneTicketCompany(), cartItemToUpdate.getPlaneTicketDate(),
          cartItemToUpdate.getPlaneTicketDestination(), cartItemToUpdate.getPlaneTicketSource(), cartItemToUpdate.getPlaneTicketPrice(),
          cartItemToUpdate.getPlaneTicketLuggageWeight(), cartItemToUpdate.hasAirCargo(), luggageDepartureTime, differenceInNumberOfTickets,
          cartItemToUpdate.getSeatCategory());

      cartItemToUpdate.addTickets(tickets);
    } else {
      cartItemToUpdate.removeTickets(Math.abs(differenceInNumberOfTickets));
    }

    return cartItemToUpdate;
  }

  public CartDto getCart(String user) {
    Cart cart = cartRepository.find(user);

    return cartAssembler.assembleDtoFromCart(cart);
  }

  public CartDto deleteCartItem(double luggageWeight, String company, String date, String destination, String source, String user,
      SeatCategory seatCategory) {
    Cart cart = cartRepository.find(user);

    LocalDateTime dateTime = dateChecker.dateStringToDateTimeObject(date);

    CartItem cartItemToDelete = cart.findCartItem(luggageWeight, company, dateTime, destination, source, seatCategory);

    PassengerFlight passengerFlight = flightRepository.findPassengerFlight(cartItemToDelete.getPlaneTicketCompany(),
        cartItemToDelete.getPlaneTicketDate(), cartItemToDelete.getPlaneTicketDestination(), cartItemToDelete.getPlaneTicketSource());

    passengerFlight.adjustAvailability(cartItemToDelete.getSeatCategory(), -(cartItemToDelete.getNumberOfTickets()), cartItemToDelete.hasAirCargo(),
        cartItemToDelete.getFirstPlaneTicket().getLuggageWeight());

    if (cartItemToDelete.hasAirCargo()) {
      CargoFlight cargoFlight = flightRepository.findCargoFlight(passengerFlight.getCompany(), cartItemToDelete.getPlaneTicketCargoDate(),
          passengerFlight.getDestination(), passengerFlight.getSource());

      cargoFlight.adjustWeightAvailable(cartItemToDelete.getNumberOfTickets(), -(cartItemToDelete.getPlaneTicketLuggageWeight()));
    }

    cart.deleteCartItem(cartItemToDelete);

    return cartAssembler.assembleDtoFromCart(cart);
  }

  private Cart getCartToAddCartItem(CartItemDtoRequest sentCartItemDto) {
    if (cartRepository.exist(sentCartItemDto.user)) {
      return cartRepository.find(sentCartItemDto.user);
    } else {
      return cartAssembler.assembleCartFromSentCartItemDto(sentCartItemDto);
    }
  }

}