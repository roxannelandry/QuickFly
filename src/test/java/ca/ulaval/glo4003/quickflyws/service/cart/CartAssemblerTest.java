package ca.ulaval.glo4003.quickflyws.service.cart;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;

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
import ca.ulaval.glo4003.quickflyws.domain.cart.cartItem.planeTicket.PlaneTicket;
import ca.ulaval.glo4003.quickflyws.dto.CartDto;
import ca.ulaval.glo4003.quickflyws.dto.CartItemDtoRequest;
import ca.ulaval.glo4003.quickflyws.dto.CartItemDtoResponse;

@RunWith(MockitoJUnitRunner.class)
public class CartAssemblerTest {

  private static final String USER = "user";
  private static final String FLIGHT_SOURCE = "source";
  private static final String FLIGHT_DESTINATION = "destination";
  private static final String FLIGHT_DATE_STRING = "2016-01-01";
  private static final String FLIGHT_COMPANY = "AirLousse";
  
  private static final LocalDateTime FLIGHT_DATE_IN_LOCAL_DATETIME = LocalDateTime.of(2016, 01, 01, 00, 00);

  private static final int AMOUNT_OF_TICKETS = 10;
  
  private static final double LUGGAGE_WEIGHT = 40.00;
  private static final double FLIGHT_PRICE = 10.00;
  private static final double TOTAL_PRICE = FLIGHT_PRICE * AMOUNT_OF_TICKETS;

  @Mock
  private Cart cart;

  @Mock
  private CartItem cartItem;

  @Mock
  private PlaneTicket planeTicket;

  private CartItemDtoRequest sentCartItemDto;

  private CartItemDtoResponse cartItemDto;

  private CartAssembler cartAssembler;

  @Before
  public void setUp() {
    cartAssembler = new CartAssembler();

    sentCartItemDto = new CartItemDtoRequest();
    sentCartItemDto.user = USER;
    sentCartItemDto.company = FLIGHT_COMPANY;
    sentCartItemDto.date = FLIGHT_DATE_STRING;
    sentCartItemDto.destination = FLIGHT_DESTINATION;
    sentCartItemDto.source = FLIGHT_SOURCE;
    sentCartItemDto.numberOfTickets = AMOUNT_OF_TICKETS;

    cartItemDto = new CartItemDtoResponse();
    cartItemDto.company = FLIGHT_COMPANY;
    cartItemDto.date = FLIGHT_DATE_IN_LOCAL_DATETIME;
    cartItemDto.destination = FLIGHT_DESTINATION;
    cartItemDto.source = FLIGHT_SOURCE;
    cartItemDto.numberOfTickets = AMOUNT_OF_TICKETS;
    cartItemDto.pricePerTicket = FLIGHT_PRICE;
    cartItemDto.totalPrice = TOTAL_PRICE;

    given(planeTicket.getSource()).willReturn(FLIGHT_SOURCE);
    given(planeTicket.getDestination()).willReturn(FLIGHT_DESTINATION);
    given(planeTicket.getDate()).willReturn(FLIGHT_DATE_IN_LOCAL_DATETIME);
    given(planeTicket.getCompany()).willReturn(FLIGHT_COMPANY);
    given(planeTicket.getPrice()).willReturn(FLIGHT_PRICE);
    given(planeTicket.getLuggageWeight()).willReturn(LUGGAGE_WEIGHT);
    given(planeTicket.hasAirCargo()).willReturn(false);

    given(cartItem.getFirstPlaneTicket()).willReturn(planeTicket);
    given(cartItem.getNumberOfTickets()).willReturn(AMOUNT_OF_TICKETS);
    given(cartItem.getTotalPrice()).willReturn(TOTAL_PRICE);

    cartItemDto = new CartItemDtoResponse();
  }

  @Test
  public void cartItemDtoToProcess_assemblingCartFromSentCartItemDto_createNewCart() {
    Cart cart = cartAssembler.assembleCartFromSentCartItemDto(sentCartItemDto);

    assertEquals(cart.getUser(), sentCartItemDto.user);
  }

  @Test
  public void cart_assemblingDtoFromCart_createCartDto() {
    given(cart.getUser()).willReturn(USER);
    given(cart.getNumberOfItems()).willReturn(AMOUNT_OF_TICKETS);
    given(cart.getTotalPrice()).willReturn(TOTAL_PRICE);

    List<CartItem> cartItems = new ArrayList<>();
    cartItems.add(cartItem);
    given(cart.getCartItems()).willReturn(cartItems);

    List<CartItemDtoResponse> cartItemsDto = new ArrayList<>();
    cartItemsDto.add(cartItemDto);

    CartDto cartDto = cartAssembler.assembleDtoFromCart(cart);

    assertEquals(cartDto.user, cart.getUser());
    assertEquals(cartDto.numberOfItems, cart.getNumberOfItems());
    assertEquals(cartDto.totalPrice, cart.getTotalPrice(), 0);
    assertEquals(cartDto.cartItems.size(), cartItemsDto.size());
  }

  @Test
  public void listOfCartItemsWithNoCargo_assemblingListOfCartItemDtoFromListOfCartItem_createListOfCartItemsDto() {
    List<CartItem> cartItems = new ArrayList<>();
    cartItems.add(cartItem);
    List<CartItemDtoResponse> cartItemsDto = new ArrayList<>();
    cartItemsDto.add(cartItemDto);

    List<CartItemDtoResponse> listOfCartItemDto = cartAssembler.assembleListOfCartItemDtoFromListOfCartItem(cartItems);

    assertEquals(listOfCartItemDto.get(0).company, FLIGHT_COMPANY);
    assertEquals(listOfCartItemDto.get(0).date, FLIGHT_DATE_IN_LOCAL_DATETIME);
    assertEquals(listOfCartItemDto.get(0).destination, FLIGHT_DESTINATION);
    assertEquals(listOfCartItemDto.get(0).source, FLIGHT_SOURCE);
    assertEquals(listOfCartItemDto.get(0).pricePerTicket, FLIGHT_PRICE, 0);
    assertEquals(listOfCartItemDto.get(0).luggageWeight, LUGGAGE_WEIGHT, 0);
    assertEquals(listOfCartItemDto.get(0).numberOfTickets, AMOUNT_OF_TICKETS);
    assertEquals(listOfCartItemDto.get(0).totalPrice, TOTAL_PRICE, 0);
    assertFalse(listOfCartItemDto.get(0).hasAirCargo);
  }

  @Test
  public void listOfCartItemsWithCargo_assemblingListOfCartItemDtoFromListOfCartItem_createListOfCartItemsDto() {
    List<CartItem> cartItems = new ArrayList<>();
    cartItems.add(cartItem);
    List<CartItemDtoResponse> cartItemsDto = new ArrayList<>();
    cartItemsDto.add(cartItemDto);

    given(planeTicket.hasAirCargo()).willReturn(true);
    given(planeTicket.getAirCargoDate()).willReturn(FLIGHT_DATE_IN_LOCAL_DATETIME);

    List<CartItemDtoResponse> listOfCartItemDto = cartAssembler.assembleListOfCartItemDtoFromListOfCartItem(cartItems);

    assertEquals(listOfCartItemDto.get(0).company, FLIGHT_COMPANY);
    assertEquals(listOfCartItemDto.get(0).date, FLIGHT_DATE_IN_LOCAL_DATETIME);
    assertEquals(listOfCartItemDto.get(0).destination, FLIGHT_DESTINATION);
    assertEquals(listOfCartItemDto.get(0).source, FLIGHT_SOURCE);
    assertEquals(listOfCartItemDto.get(0).pricePerTicket, FLIGHT_PRICE, 0);
    assertEquals(listOfCartItemDto.get(0).luggageWeight, LUGGAGE_WEIGHT, 0);
    assertEquals(listOfCartItemDto.get(0).numberOfTickets, AMOUNT_OF_TICKETS);
    assertEquals(listOfCartItemDto.get(0).totalPrice, TOTAL_PRICE, 0);
    assertTrue(listOfCartItemDto.get(0).hasAirCargo);
    assertEquals(listOfCartItemDto.get(0).airCargoDate, FLIGHT_DATE_IN_LOCAL_DATETIME);
  }

}