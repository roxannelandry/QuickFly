package ca.ulaval.glo4003.quickflyws.service.cart;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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

@RunWith(MockitoJUnitRunner.class)
public class CartServiceTest {
  
  private static final String FLIGHT_SOURCE = "source";
  private static final String FLIGHT_DESTINATION = "destination";
  private static final String FLIGHT_DATE_IN_STRING = "2016-01-01 00:00";
  private static final String FLIGHT_COMPANY = "AirLousse";
  private static final String USER = "user";

  private static final LocalDateTime AIR_CARGO_DATE = LocalDateTime.of(2016, 01, 01, 00, 00);
  private static final LocalDateTime FLIGHT_DATE_IN_LOCAL_DATETIME = LocalDateTime.of(2016, 01, 01, 00, 00);
  
  private static final boolean HAS_NO_AIR_CARGO = false;
  private static final boolean HAS_AIR_CARGO = true;
  private static final boolean HAS_VALID_WEIGHT = true;

  private static final int FLIGHT_TICKETS = 10;
  private static final int HIGHER_AMOUNT_FLIGHT_TICKETS = 20;
  private static final int SMALLER_AMOUNT_FLIGHT_TICKETS = 5;

  private static final double FLIGHT_PRICE = 10.00;
  private static final double LUGGAGE_WEIGHT = 40.00;
  
  private static final SeatCategory SEAT_CATEGORY = SeatCategory.REGULAR;

  @Mock
  private CartRepository cartRepository;

  @Mock
  private FlightRepository flightRepository;

  @Mock
  private CartAssembler cartAssembler;

  @Mock
  private CartItemFactory cartItemFactory;

  @Mock
  private PlaneTicketFactory planeTicketFactory;

  @Mock
  private CartItem cartItem;

  @Mock
  private PlaneTicket planeTicket;

  @Mock
  private Cart cart;

  @Mock
  private PassengerFlight passengerFlight;

  @Mock
  private CargoFlight cargoFlight;

  @Mock
  private FlightPricer flightPricer;

  @Mock
  private FlightPricerFactory flightPricerFactory;

  private List<PlaneTicket> planeTickets;

  private CartDto cartDto;

  private CartItemDtoRequest cartItemDtoToProcess;

  private CartService cartService;

  private DateConverterChecker dateChecker = new DateConverterChecker();

  @Before
  public void setUp() {
    cartService = new CartService(cartRepository, flightRepository, cartAssembler, cartItemFactory, planeTicketFactory, flightPricerFactory);
    mockSetUp();

  }

  @Test
  public void cartItem_addingCartItem_delegateToFlightRepositoryToFindFlight() {
    LocalDateTime dateTime = dateChecker.dateStringToDateTimeObject(cartItemDtoToProcess.date);

    cartService.addCartItemToCart(cartItemDtoToProcess);

    verify(flightRepository).findPassengerFlight(cartItemDtoToProcess.company, dateTime, cartItemDtoToProcess.destination,
        cartItemDtoToProcess.source);
  }

  @Test
  public void cartItem_addingCartItem_delegateToCartItemFactory() {
    cartService.addCartItemToCart(cartItemDtoToProcess);

    verify(cartItemFactory).createNewCartItem(flightPricer, cartItemDtoToProcess);

  }

  @Test
  public void cartItem_addingCartItem_delegateToCartRepositoryToVerifyIfCartExist() {
    cartService.addCartItemToCart(cartItemDtoToProcess);

    verify(cartRepository).exist(USER);
  }

  @Test
  public void cartItem_addingCartItem_delegateToCartRepositoryToFind() {
    cartService.addCartItemToCart(cartItemDtoToProcess);

    verify(cartRepository).find(USER);
  }

  @Test
  public void cartItem_addingCartItem_delegateToCartAssemblerToCreateCart() {
    given(cartRepository.exist(USER)).willReturn(false);

    cartService.addCartItemToCart(cartItemDtoToProcess);

    verify(cartAssembler).assembleCartFromSentCartItemDto(cartItemDtoToProcess);
  }

  @Test
  public void cartItem_addingCartItem_delegateToCartToAddItem() {
    cartService.addCartItemToCart(cartItemDtoToProcess);

    verify(cart).addCartItem(cartItem);
  }

  @Test
  public void cartItem_addingCartItem_delegateToCartRepositoryToSave() {
    cartService.addCartItemToCart(cartItemDtoToProcess);

    verify(cartRepository).save(cart);
  }

  @Test
  public void cartItem_addingCartItem_delegateToCartAssemblerToCreateCartDto() {
    cartService.addCartItemToCart(cartItemDtoToProcess);

    verify(cartAssembler).assembleDtoFromCart(cart);
  }

  @Test
  public void cartItem_addingCartItem_returnCartDto() {
    CartDto requestedCartDto = cartService.addCartItemToCart(cartItemDtoToProcess);

    assertEquals(cartDto, requestedCartDto);
  }

  @Test
  public void cartItemWithCargo_addingCartItem_delegateToFlightRepositoryToFindFlightsCargo() {
    cartItemDtoToProcess.airCargoAllowed = true;
    List<CargoFlight> cargoFlights = new ArrayList<>();
    cargoFlights.add(cargoFlight);

    given(flightPricer.needCargoFlight(FLIGHT_TICKETS)).willReturn(true);
    given(flightPricer.hasAirCargo()).willReturn(true);
    given(flightPricer.getCargoFlight()).willReturn(cargoFlight);
    given(flightRepository.findCargoFlights(FLIGHT_COMPANY, FLIGHT_DATE_IN_LOCAL_DATETIME, FLIGHT_DESTINATION, FLIGHT_SOURCE))
        .willReturn(cargoFlights);

    cartService.addCartItemToCart(cartItemDtoToProcess);

    verify(flightRepository).findCargoFlights(FLIGHT_COMPANY, FLIGHT_DATE_IN_LOCAL_DATETIME, FLIGHT_DESTINATION, FLIGHT_SOURCE);
  }

  @Test
  public void cartItemWithCargo_addingCartItem_delegateToFlightPricerToAssociateCargoToPassengerFlight() {
    cartItemDtoToProcess.airCargoAllowed = true;
    List<CargoFlight> cargoFlights = new ArrayList<>();
    cargoFlights.add(cargoFlight);

    given(flightPricer.needCargoFlight(FLIGHT_TICKETS)).willReturn(true);
    given(flightPricer.hasAirCargo()).willReturn(true);
    given(flightPricer.getCargoFlight()).willReturn(cargoFlight);
    given(flightRepository.findCargoFlights(FLIGHT_COMPANY, FLIGHT_DATE_IN_LOCAL_DATETIME, FLIGHT_DESTINATION, FLIGHT_SOURCE))
        .willReturn(cargoFlights);

    cartService.addCartItemToCart(cartItemDtoToProcess);

    verify(flightPricer).associateCargoFlightToPassengerFlight(cargoFlights, FLIGHT_TICKETS);
  }

  @Test
  public void cartItemWithCargo_addingCartItem_delegateToFlightPricerToAdjustInformationWhenAddCartItem() {
    cartItemDtoToProcess.airCargoAllowed = true;
    List<CargoFlight> cargoFlights = new ArrayList<>();
    cargoFlights.add(cargoFlight);

    given(flightPricer.needCargoFlight(FLIGHT_TICKETS)).willReturn(true);
    given(flightPricer.hasAirCargo()).willReturn(true);
    given(flightPricer.getCargoFlight()).willReturn(cargoFlight);
    given(flightPricer.getLuggageWeight()).willReturn(LUGGAGE_WEIGHT);
    given(flightRepository.findCargoFlights(FLIGHT_COMPANY, FLIGHT_DATE_IN_LOCAL_DATETIME, FLIGHT_DESTINATION, FLIGHT_SOURCE))
        .willReturn(cargoFlights);

    cartService.addCartItemToCart(cartItemDtoToProcess);

    verify(flightPricer).adjustAvailability(cartItemDtoToProcess);
  }

  @Test
  public void cartItem_updatingCartItem_delegateToFlightRepositoryToFindFlight() {
    cartItemDtoToProcess.numberOfTickets = HIGHER_AMOUNT_FLIGHT_TICKETS;

    cartService.addCartItemToCart(cartItemDtoToProcess);

    verify(flightRepository, atLeastOnce()).findPassengerFlight(FLIGHT_COMPANY, FLIGHT_DATE_IN_LOCAL_DATETIME, FLIGHT_DESTINATION, FLIGHT_SOURCE);
  }

  @Test
  public void cartItem_updatingCartItem_delegateToCartRepositoryToFind() {
    cartItemDtoToProcess.numberOfTickets = HIGHER_AMOUNT_FLIGHT_TICKETS;

    cartService.updateCartItem(cartItemDtoToProcess);

    verify(cartRepository).find(USER);
  }

  @Test
  public void cartItem_updatingCartItem_delegateToCartToFindCartItem() {
    cartItemDtoToProcess.numberOfTickets = HIGHER_AMOUNT_FLIGHT_TICKETS;
    LocalDateTime dateTime = dateChecker.dateStringToDateTimeObject(cartItemDtoToProcess.date);

    cartService.updateCartItem(cartItemDtoToProcess);

    verify(cart).findCartItem(cartItemDtoToProcess.luggageWeight, cartItemDtoToProcess.company, dateTime, cartItemDtoToProcess.destination,
        cartItemDtoToProcess.source, cartItemDtoToProcess.seatCategory);
  }

  @Test
  public void cartItem_updatingCartItem_delegateToPlaneTicketFactory() {
    cartItemDtoToProcess.numberOfTickets = HIGHER_AMOUNT_FLIGHT_TICKETS;

    cartService.updateCartItem(cartItemDtoToProcess);

    verify(planeTicketFactory, atLeastOnce()).createTickets(FLIGHT_COMPANY, FLIGHT_DATE_IN_LOCAL_DATETIME, FLIGHT_DESTINATION, FLIGHT_SOURCE,
        FLIGHT_PRICE, LUGGAGE_WEIGHT, HAS_NO_AIR_CARGO, AIR_CARGO_DATE, HIGHER_AMOUNT_FLIGHT_TICKETS - FLIGHT_TICKETS, SEAT_CATEGORY);
  }

  @Test
  public void cartItem_updatingCartItem_delegateToCartItemToAddTickets() {
    cartItemDtoToProcess.numberOfTickets = HIGHER_AMOUNT_FLIGHT_TICKETS;

    cartService.updateCartItem(cartItemDtoToProcess);

    verify(cartItem, atLeastOnce()).addTickets(planeTickets);
  }

  @Test
  public void cartItem_updatingCartItem_delegateToCartItemToRemoveTickets() {
    cartItemDtoToProcess.numberOfTickets = SMALLER_AMOUNT_FLIGHT_TICKETS;

    cartService.updateCartItem(cartItemDtoToProcess);

    verify(cartItem, atLeastOnce()).removeTickets(FLIGHT_TICKETS - SMALLER_AMOUNT_FLIGHT_TICKETS);
  }

  @Test
  public void cartItem_updatingCartItem_delegateToCartToUpdateItem() {
    cartItemDtoToProcess.numberOfTickets = HIGHER_AMOUNT_FLIGHT_TICKETS;

    cartService.updateCartItem(cartItemDtoToProcess);

    verify(cart).updateCartItem(cartItem, HIGHER_AMOUNT_FLIGHT_TICKETS - FLIGHT_TICKETS);
  }

  @Test
  public void cartItem_updatingCartItem_delegateToCartAssemblerToCreateCartDto() {
    cartItemDtoToProcess.numberOfTickets = HIGHER_AMOUNT_FLIGHT_TICKETS;

    cartService.updateCartItem(cartItemDtoToProcess);

    verify(cartAssembler).assembleDtoFromCart(cart);
  }

  @Test
  public void cartItem_updatingCartItem_returnCartDto() {
    cartItemDtoToProcess.numberOfTickets = HIGHER_AMOUNT_FLIGHT_TICKETS;

    CartDto requestedCartDto = cartService.updateCartItem(cartItemDtoToProcess);

    assertEquals(cartDto, requestedCartDto);
  }

  @Test
  public void cartItemWithCargo_updatingCartItem_delegateToFlightRepositoryToFindCargoFlight() {
    given(cargoFlight.hasEnoughSpace((HIGHER_AMOUNT_FLIGHT_TICKETS - FLIGHT_TICKETS) * LUGGAGE_WEIGHT)).willReturn(true);
    given(cartItem.hasAirCargo()).willReturn(true);

    given(flightRepository.findCargoFlight(FLIGHT_COMPANY, FLIGHT_DATE_IN_LOCAL_DATETIME, FLIGHT_DESTINATION, FLIGHT_SOURCE)).willReturn(cargoFlight);
    cartItemDtoToProcess.numberOfTickets = HIGHER_AMOUNT_FLIGHT_TICKETS;

    cartService.updateCartItem(cartItemDtoToProcess);

    verify(flightRepository).findCargoFlight(FLIGHT_COMPANY, FLIGHT_DATE_IN_LOCAL_DATETIME, FLIGHT_DESTINATION, FLIGHT_SOURCE);
  }

  @Test
  public void cartItemWithCargo_updatingCartItem_delegateToCargoFlightToAdjustAvailableSpace() {
    given(cargoFlight.hasEnoughSpace((HIGHER_AMOUNT_FLIGHT_TICKETS - FLIGHT_TICKETS) * LUGGAGE_WEIGHT)).willReturn(true);
    given(cartItem.hasAirCargo()).willReturn(true);

    given(flightRepository.findCargoFlight(FLIGHT_COMPANY, FLIGHT_DATE_IN_LOCAL_DATETIME, FLIGHT_DESTINATION, FLIGHT_SOURCE)).willReturn(cargoFlight);
    cartItemDtoToProcess.numberOfTickets = HIGHER_AMOUNT_FLIGHT_TICKETS;

    cartService.updateCartItem(cartItemDtoToProcess);

    verify(cargoFlight).adjustWeightAvailable(HIGHER_AMOUNT_FLIGHT_TICKETS - FLIGHT_TICKETS, LUGGAGE_WEIGHT);
  }

  @Test
  public void cartItem_gettingCart_delegateToCartRepositoryToFind() {
    cartService.getCart(USER);

    verify(cartRepository).find(USER);
  }

  @Test
  public void cartItem_getCart_delegateToCartAssemblerToCreateReturnedCartItemDto() {
    cartService.getCart(USER);

    verify(cartAssembler).assembleDtoFromCart(cart);
  }

  @Test
  public void cartItem_gettingCart_returnCartDto() {
    CartDto requestedCartDto = cartService.getCart(USER);

    assertEquals(cartDto, requestedCartDto);
  }

  @Test
  public void cartItem_deletingCartItem_delegateToCartRepositoryToFind() {
    cartService.deleteCartItem(LUGGAGE_WEIGHT, FLIGHT_COMPANY, FLIGHT_DATE_IN_STRING, FLIGHT_DESTINATION, FLIGHT_SOURCE, USER, SEAT_CATEGORY);

    verify(cartRepository).find(USER);
  }

  @Test
  public void cartItem_deletingCartItem_delegateToCartToFindCartItem() {
    LocalDateTime dateTime = dateChecker.dateStringToDateTimeObject(cartItemDtoToProcess.date);

    cartService.deleteCartItem(LUGGAGE_WEIGHT, FLIGHT_COMPANY, FLIGHT_DATE_IN_STRING, FLIGHT_DESTINATION, FLIGHT_SOURCE, USER, SEAT_CATEGORY);

    verify(cart).findCartItem(cartItemDtoToProcess.luggageWeight, cartItemDtoToProcess.company, dateTime, cartItemDtoToProcess.destination,
        cartItemDtoToProcess.source, cartItemDtoToProcess.seatCategory);
  }

  @Test
  public void cartItem_deletingCartItem_delegateToFlightRepositoryToFind() {
    cartService.deleteCartItem(LUGGAGE_WEIGHT, FLIGHT_COMPANY, FLIGHT_DATE_IN_STRING, FLIGHT_DESTINATION, FLIGHT_SOURCE, USER, SEAT_CATEGORY);

    verify(flightRepository).findPassengerFlight(FLIGHT_COMPANY, FLIGHT_DATE_IN_LOCAL_DATETIME, FLIGHT_DESTINATION, FLIGHT_SOURCE);
  }

  @Test
  public void cartItem_deletingCartItem_delegateToCartToDeleteCartItem() {
    cartService.deleteCartItem(LUGGAGE_WEIGHT, FLIGHT_COMPANY, FLIGHT_DATE_IN_STRING, FLIGHT_DESTINATION, FLIGHT_SOURCE, USER, SEAT_CATEGORY);

    verify(cart).deleteCartItem(cartItem);
  }

  @Test
  public void cartItem_deletingCartItem_delegateToCartAssemblerToCreateCartDto() {
    cartService.deleteCartItem(LUGGAGE_WEIGHT, FLIGHT_COMPANY, FLIGHT_DATE_IN_STRING, FLIGHT_DESTINATION, FLIGHT_SOURCE, USER, SEAT_CATEGORY);

    verify(cartAssembler).assembleDtoFromCart(cart);
  }

  @Test
  public void cartItem_deletingCartItem_returnCartDto() {
    CartDto requestedCartDto = cartService.deleteCartItem(LUGGAGE_WEIGHT, FLIGHT_COMPANY, FLIGHT_DATE_IN_STRING, FLIGHT_DESTINATION, FLIGHT_SOURCE,
        USER, SEAT_CATEGORY);

    assertEquals(cartDto, requestedCartDto);
  }

  @Test
  public void cartItemWithCargo_deletingCartItem_delegateToFlightRepositoryToFindCargoFlight() {
    given(cargoFlight.hasEnoughSpace((HIGHER_AMOUNT_FLIGHT_TICKETS - FLIGHT_TICKETS) * LUGGAGE_WEIGHT)).willReturn(true);
    given(cartItem.hasAirCargo()).willReturn(true);

    given(flightRepository.findCargoFlight(FLIGHT_COMPANY, FLIGHT_DATE_IN_LOCAL_DATETIME, FLIGHT_DESTINATION, FLIGHT_SOURCE)).willReturn(cargoFlight);
    cartItemDtoToProcess.numberOfTickets = HIGHER_AMOUNT_FLIGHT_TICKETS;

    cartService.deleteCartItem(LUGGAGE_WEIGHT, FLIGHT_COMPANY, FLIGHT_DATE_IN_STRING, FLIGHT_DESTINATION, FLIGHT_SOURCE, USER, SEAT_CATEGORY);

    verify(flightRepository).findCargoFlight(FLIGHT_COMPANY, FLIGHT_DATE_IN_LOCAL_DATETIME, FLIGHT_DESTINATION, FLIGHT_SOURCE);
  }

  @Test
  public void cartItemWithCargo_deletingCartItem_delegateToCargoFlightToAdjustAvailableSpace() {
    given(cargoFlight.hasEnoughSpace((HIGHER_AMOUNT_FLIGHT_TICKETS - FLIGHT_TICKETS) * LUGGAGE_WEIGHT)).willReturn(true);
    given(cartItem.hasAirCargo()).willReturn(true);

    given(flightRepository.findCargoFlight(FLIGHT_COMPANY, FLIGHT_DATE_IN_LOCAL_DATETIME, FLIGHT_DESTINATION, FLIGHT_SOURCE)).willReturn(cargoFlight);
    cartItemDtoToProcess.numberOfTickets = HIGHER_AMOUNT_FLIGHT_TICKETS;

    cartService.deleteCartItem(LUGGAGE_WEIGHT, FLIGHT_COMPANY, FLIGHT_DATE_IN_STRING, FLIGHT_DESTINATION, FLIGHT_SOURCE, USER, SEAT_CATEGORY);

    verify(cargoFlight).adjustWeightAvailable(HIGHER_AMOUNT_FLIGHT_TICKETS - FLIGHT_TICKETS, -LUGGAGE_WEIGHT);
  }

  private void mockSetUp() {
    cartItemDtoToProcess = new CartItemDtoRequest();
    cartItemDtoToProcess.user = USER;
    cartItemDtoToProcess.company = FLIGHT_COMPANY;
    cartItemDtoToProcess.date = FLIGHT_DATE_IN_STRING;
    cartItemDtoToProcess.destination = FLIGHT_DESTINATION;
    cartItemDtoToProcess.source = FLIGHT_SOURCE;
    cartItemDtoToProcess.numberOfTickets = FLIGHT_TICKETS;
    cartItemDtoToProcess.seatCategory = SEAT_CATEGORY;
    cartItemDtoToProcess.luggageWeight = LUGGAGE_WEIGHT;
    cartItemDtoToProcess.airCargoAllowed = false;

    cartDto = new CartDto();

    given(passengerFlight.getCompany()).willReturn(FLIGHT_COMPANY);
    given(passengerFlight.getDate()).willReturn(FLIGHT_DATE_IN_LOCAL_DATETIME);
    given(passengerFlight.getDestination()).willReturn(FLIGHT_DESTINATION);
    given(passengerFlight.getSource()).willReturn(FLIGHT_SOURCE);
    given(passengerFlight.getSeatPrice(SEAT_CATEGORY)).willReturn(FLIGHT_PRICE);
    given(passengerFlight.hasAvailableSeatsForThisCategory(SEAT_CATEGORY, FLIGHT_TICKETS)).willReturn(true);
    given(passengerFlight.hasAvailableSeatsForThisCategory(SEAT_CATEGORY, HIGHER_AMOUNT_FLIGHT_TICKETS)).willReturn(true);
    given(passengerFlight.hasAvailableSeatsForThisCategory(SEAT_CATEGORY, SMALLER_AMOUNT_FLIGHT_TICKETS - FLIGHT_TICKETS)).willReturn(true);
    given(passengerFlight.hasValidWeight(LUGGAGE_WEIGHT, FLIGHT_TICKETS)).willReturn(true);
    given(passengerFlight.hasValidWeight(LUGGAGE_WEIGHT, HIGHER_AMOUNT_FLIGHT_TICKETS)).willReturn(true);
    given(passengerFlight.hasValidWeight(LUGGAGE_WEIGHT, SMALLER_AMOUNT_FLIGHT_TICKETS - FLIGHT_TICKETS)).willReturn(true);

    given(cart.findCartItem(LUGGAGE_WEIGHT, FLIGHT_COMPANY, FLIGHT_DATE_IN_LOCAL_DATETIME, FLIGHT_DESTINATION, FLIGHT_SOURCE, SEAT_CATEGORY))
        .willReturn(cartItem);

    given(planeTicket.getCompany()).willReturn(FLIGHT_COMPANY);
    given(planeTicket.getDate()).willReturn(FLIGHT_DATE_IN_LOCAL_DATETIME);
    given(planeTicket.getDestination()).willReturn(FLIGHT_DESTINATION);
    given(planeTicket.getSource()).willReturn(FLIGHT_SOURCE);
    given(planeTicket.getSeatCategory()).willReturn(SEAT_CATEGORY);

    given(cartItem.hasAirCargo()).willReturn(false);
    given(cartItem.getFirstPlaneTicket()).willReturn(planeTicket);
    given(cartItem.getNumberOfTickets()).willReturn(FLIGHT_TICKETS);
    given(cartItem.getPlaneTicketCompany()).willReturn(FLIGHT_COMPANY);
    given(cartItem.getPlaneTicketDate()).willReturn(FLIGHT_DATE_IN_LOCAL_DATETIME);
    given(cartItem.getPlaneTicketDestination()).willReturn(FLIGHT_DESTINATION);
    given(cartItem.getPlaneTicketSource()).willReturn(FLIGHT_SOURCE);
    given(cartItem.getSeatCategory()).willReturn(SEAT_CATEGORY);
    given(cartItem.getPlaneTicketPrice()).willReturn(FLIGHT_PRICE);
    given(cartItem.getPlaneTicketLuggageWeight()).willReturn(LUGGAGE_WEIGHT);
    given(cartItem.getPlaneTicketCargoDate()).willReturn(FLIGHT_DATE_IN_LOCAL_DATETIME);

    given(cartAssembler.assembleCartFromSentCartItemDto(cartItemDtoToProcess)).willReturn(cart);
    given(cartAssembler.assembleDtoFromCart(cart)).willReturn(cartDto);

    given(cartRepository.exist(USER)).willReturn(true);
    given(cartRepository.find(USER)).willReturn(cart);

    given(flightRepository.findPassengerFlight(FLIGHT_COMPANY, FLIGHT_DATE_IN_LOCAL_DATETIME, FLIGHT_DESTINATION, FLIGHT_SOURCE))
        .willReturn(passengerFlight);

    given(cartItemFactory.createNewCartItem(flightPricer, cartItemDtoToProcess)).willReturn(cartItem);

    given(flightPricerFactory.createFlightPricer(passengerFlight, LUGGAGE_WEIGHT, HAS_NO_AIR_CARGO)).willReturn(flightPricer);
    given(flightPricerFactory.createFlightPricer(passengerFlight, LUGGAGE_WEIGHT, HAS_AIR_CARGO)).willReturn(flightPricer);
    given(flightPricer.getPassengerFlight()).willReturn(passengerFlight);
    given(flightPricer.getPassengerFlight().hasValidWeight(LUGGAGE_WEIGHT, FLIGHT_TICKETS)).willReturn(HAS_VALID_WEIGHT);
    given(flightPricer.isAirCargoAllowed()).willReturn(HAS_NO_AIR_CARGO);

    planeTickets = new ArrayList<>();
    given(planeTicketFactory.createTickets(FLIGHT_COMPANY, FLIGHT_DATE_IN_LOCAL_DATETIME, FLIGHT_DESTINATION, FLIGHT_SOURCE, FLIGHT_PRICE,
        LUGGAGE_WEIGHT, HAS_NO_AIR_CARGO, AIR_CARGO_DATE, FLIGHT_TICKETS, SEAT_CATEGORY)).willReturn(planeTickets);
  }

}