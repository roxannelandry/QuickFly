
package ca.ulaval.glo4003.quickflyws.api.cart;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

import javax.ws.rs.WebApplicationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.seats.seatCategory.SeatCategory;
import ca.ulaval.glo4003.quickflyws.domain.generalExceptions.BadRequestException;
import ca.ulaval.glo4003.quickflyws.domain.generalExceptions.NotFoundException;
import ca.ulaval.glo4003.quickflyws.dto.CartDto;
import ca.ulaval.glo4003.quickflyws.dto.CartItemDtoRequest;
import ca.ulaval.glo4003.quickflyws.service.cart.CartService;

@RunWith(MockitoJUnitRunner.class)
public class CartResourceApiTest {

  private static final String USER_EMAIL = "something@hotmail.com";
  private static final String FLIGHT_SOURCE = "source";
  private static final String FLIGHT_DESTINATION = "destination";
  private static final String FLIGHT_DATE = "2017-01-01";
  private static final String FLIGHT_COMPANY = "company";

  private static final SeatCategory SEAT_CATEGORY = SeatCategory.REGULAR;

  private static final double LUGGAGE_WEIGHT = 40.00;

  @Mock
  private CartService cartService;

  private CartItemDtoRequest cartItemDtoRequest;

  private CartDto cartDto;

  private CartResource cartResource;

  @Before
  public void setUp() {
    cartResource = new CartResourceApi(cartService);
    cartItemDtoRequest = new CartItemDtoRequest();
    cartItemDtoRequest.user = USER_EMAIL;
    cartItemDtoRequest.company = FLIGHT_COMPANY;
    cartItemDtoRequest.destination = FLIGHT_DESTINATION;
    cartItemDtoRequest.source = FLIGHT_SOURCE;
    cartItemDtoRequest.date = FLIGHT_DATE;
    cartItemDtoRequest.luggageWeight = LUGGAGE_WEIGHT;
    cartItemDtoRequest.seatCategory = SeatCategory.REGULAR;
  }

  @Test
  public void cartItemDtoRequest_addingCartItem_delegateToService() {
    cartResource.addCartItem(cartItemDtoRequest);

    verify(cartService).addCartItemToCart(cartItemDtoRequest);
  }

  @Test
  public void cartItemDtoRequest_addingCartItem_returnCartDto() {
    given(cartService.addCartItemToCart(cartItemDtoRequest)).willReturn(cartDto);

    CartDto cartDtoResult = cartResource.addCartItem(cartItemDtoRequest);

    assertEquals(cartDtoResult, cartDto);
  }

  @Test(expected = WebApplicationException.class)
  public void notFoundException_addingCartItem_throwWebApplication() {
    willThrow(new NotFoundException("Flight not found.")).given(cartService).addCartItemToCart(cartItemDtoRequest);

    cartResource.addCartItem(cartItemDtoRequest);
  }

  @Test(expected = WebApplicationException.class)
  public void badRequestException_addingCartItem_throwWebApplication() {
    willThrow(new BadRequestException("Invalid informations.")).given(cartService).addCartItemToCart(cartItemDtoRequest);

    cartResource.addCartItem(cartItemDtoRequest);
  }

  @Test
  public void cartItemDtoRequest_updatingCartItem_delegateToService() {
    cartResource.updateCartItemInCart(cartItemDtoRequest);

    verify(cartService).updateCartItem(cartItemDtoRequest);
  }

  @Test
  public void cartItemDtoRequest_updatingCartItem_returnCartDto() {
    given(cartService.updateCartItem(cartItemDtoRequest)).willReturn(cartDto);

    CartDto cartDtoResult = cartResource.updateCartItemInCart(cartItemDtoRequest);

    assertEquals(cartDtoResult, cartDto);
  }

  @Test(expected = WebApplicationException.class)
  public void notFoundException_updatingCartItem_throwWebApplication() {
    willThrow(new NotFoundException("Item not found.")).given(cartService).updateCartItem(cartItemDtoRequest);

    cartResource.updateCartItemInCart(cartItemDtoRequest);
  }

  @Test(expected = WebApplicationException.class)
  public void badRequestException_updatingCartItem_throwWebApplication() {
    willThrow(new BadRequestException("Invalid informations")).given(cartService).updateCartItem(cartItemDtoRequest);

    cartResource.updateCartItemInCart(cartItemDtoRequest);
  }

  @Test
  public void requestFromUserToGetCart_gettingCart_delegateToService() {
    cartResource.getCart(USER_EMAIL);

    verify(cartService).getCart(USER_EMAIL);
  }

  @Test
  public void requestFromUserToGetCart_gettingCart_returnCartDto() {
    given(cartService.getCart(USER_EMAIL)).willReturn(cartDto);

    CartDto cartDtoResult = cartResource.getCart(USER_EMAIL);

    assertEquals(cartDtoResult, cartDto);
  }

  @Test(expected = WebApplicationException.class)
  public void notFoundException_getCart_throwWebApplication() {
    willThrow(new NotFoundException("Cart not found.")).given(cartService).getCart(USER_EMAIL);

    cartResource.getCart(USER_EMAIL);
  }

  @Test(expected = WebApplicationException.class)
  public void badRequestException_gettingCart_throwWebApplication() {
    willThrow(new BadRequestException("Invalid informations")).given(cartService).getCart(USER_EMAIL);

    cartResource.getCart(USER_EMAIL);
  }

  @Test
  public void cartItemDtoRequest_deletingCartItem_delegateToService() {
    cartResource.deleteCartItemInCart(cartItemDtoRequest.luggageWeight, cartItemDtoRequest.company, cartItemDtoRequest.date,
        cartItemDtoRequest.destination, cartItemDtoRequest.source, cartItemDtoRequest.user, cartItemDtoRequest.seatCategory);

    verify(cartService).deleteCartItem(LUGGAGE_WEIGHT, FLIGHT_COMPANY, FLIGHT_DATE, FLIGHT_DESTINATION, FLIGHT_SOURCE, USER_EMAIL, SEAT_CATEGORY);
  }

  @Test
  public void cartItemDtoRequest_deletingCartItem_returnCartDto() {
    given(cartService.deleteCartItem(LUGGAGE_WEIGHT, FLIGHT_COMPANY, FLIGHT_DATE, FLIGHT_DESTINATION, FLIGHT_SOURCE, USER_EMAIL, SEAT_CATEGORY))
        .willReturn(cartDto);

    CartDto cartDtoResult = cartResource.deleteCartItemInCart(cartItemDtoRequest.luggageWeight, cartItemDtoRequest.company, cartItemDtoRequest.date,
        cartItemDtoRequest.destination, cartItemDtoRequest.source, cartItemDtoRequest.user, cartItemDtoRequest.seatCategory);

    assertEquals(cartDtoResult, cartDto);
  }

  @Test(expected = WebApplicationException.class)
  public void notFoundException_deletingCartItem_throwWebApplication() {
    willThrow(new NotFoundException("Item not found.")).given(cartService).deleteCartItem(LUGGAGE_WEIGHT, FLIGHT_COMPANY, FLIGHT_DATE,
        FLIGHT_DESTINATION, FLIGHT_SOURCE, USER_EMAIL, SEAT_CATEGORY);

    cartResource.deleteCartItemInCart(cartItemDtoRequest.luggageWeight, cartItemDtoRequest.company, cartItemDtoRequest.date,
        cartItemDtoRequest.destination, cartItemDtoRequest.source, cartItemDtoRequest.user, cartItemDtoRequest.seatCategory);
  }

  @Test(expected = WebApplicationException.class)
  public void badRequestException_deletingCartItem_throwWebApplication() {
    willThrow(new BadRequestException("Invalid informations")).given(cartService).deleteCartItem(LUGGAGE_WEIGHT, FLIGHT_COMPANY, FLIGHT_DATE,
        FLIGHT_DESTINATION, FLIGHT_SOURCE, USER_EMAIL, SEAT_CATEGORY);

    cartResource.deleteCartItemInCart(cartItemDtoRequest.luggageWeight, cartItemDtoRequest.company, cartItemDtoRequest.date,
        cartItemDtoRequest.destination, cartItemDtoRequest.source, cartItemDtoRequest.user, cartItemDtoRequest.seatCategory);
  }

}