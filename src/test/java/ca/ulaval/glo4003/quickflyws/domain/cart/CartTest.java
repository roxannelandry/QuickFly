package ca.ulaval.glo4003.quickflyws.domain.cart;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ca.ulaval.glo4003.quickflyws.domain.cart.cartItem.CartItem;
import ca.ulaval.glo4003.quickflyws.domain.cart.exceptions.CartNotFoundException;
import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.seats.seatCategory.SeatCategory;

@RunWith(MockitoJUnitRunner.class)
public class CartTest {

  private static final String LOGGED_USER = "something@hotmail.com";
  private static final String FLIGHT_SOURCE = "source";
  private static final String FLIGHT_DESTINATION = "destination";
  private static final String FLIGHT_COMPANY = "AirLousse";
  private static final String ALTERNATE_FLIGHT_SOURCE = "another source";
  private static final String ALTERNATE_FLIGHT_DESTINATION = "another destination";
  private static final String ALTERNATE_FLIGHT_COMPANY = "AirGras";

  private static final SeatCategory SEAT_CATEGORY = SeatCategory.REGULAR;

  private static final int FLIGHT_TICKETS = 10;
  private static final int DIFFERENCE = 5;

  private static final double FLIGHT_PRICE = 10.00;
  private static final double LUGGAGE_WEIGHT = 40.00;
  private static final double ALTERNATE_LUGGAGE_WEIGHT = 30.00;
  
  private static final LocalDateTime FLIGHT_DATE = LocalDateTime.of(2016, 01, 01, 00, 00);
  private static final LocalDateTime ALTERNATE_FLIGHT_DATE = LocalDateTime.of(2015, 01, 01, 00, 00);

  @Mock
  private CartItem cartItemInCart;

  @Mock
  private CartItem cartItemGiven;

  private Cart cart;

  @Before
  public void setUp() {
    given(cartItemGiven.getPlaneTicketCompany()).willReturn(FLIGHT_COMPANY);
    given(cartItemGiven.getPlaneTicketDate()).willReturn(FLIGHT_DATE);
    given(cartItemGiven.getPlaneTicketDestination()).willReturn(FLIGHT_DESTINATION);
    given(cartItemGiven.getPlaneTicketSource()).willReturn(FLIGHT_SOURCE);
    given(cartItemGiven.getPlaneTicketPrice()).willReturn(FLIGHT_PRICE);
    given(cartItemGiven.getNumberOfTickets()).willReturn(FLIGHT_TICKETS);
    given(cartItemGiven.getTotalPrice()).willReturn(FLIGHT_TICKETS * FLIGHT_PRICE);

    given(cartItemInCart.getPlaneTicketCompany()).willReturn(FLIGHT_COMPANY);
    given(cartItemInCart.getPlaneTicketDate()).willReturn(FLIGHT_DATE);
    given(cartItemInCart.getPlaneTicketDestination()).willReturn(FLIGHT_DESTINATION);
    given(cartItemInCart.getPlaneTicketSource()).willReturn(FLIGHT_SOURCE);
    given(cartItemInCart.getPlaneTicketPrice()).willReturn(FLIGHT_PRICE);
    given(cartItemInCart.getNumberOfTickets()).willReturn(FLIGHT_TICKETS);
    given(cartItemInCart.getTotalPrice()).willReturn(FLIGHT_TICKETS * FLIGHT_PRICE);
  }

  @Test
  public void cartItemToAdd_addingNewCartItem_delegateToCartItemToVerifyIfIsSameAsExistingCartItemsInCart() {
    cart = new Cart(LOGGED_USER);

    cart.addCartItem(cartItemGiven);

    verify(cartItemInCart, never()).sameAs(cartItemGiven);
  }

  @Test
  public void cartItemToAdd_addingNewCartItem_delegateToCartItemToGetNumberOfTickets() {
    cart = new Cart(LOGGED_USER);

    cart.addCartItem(cartItemGiven);

    verify(cartItemGiven).getNumberOfTickets();
  }

  @Test
  public void cartItemToAdd_addingNewCartItem_cartItemIsAddedToCart() {
    cart = new Cart(LOGGED_USER);

    cart.addCartItem(cartItemGiven);

    assertEquals(cart.getCartItems().size(), 1);
  }

  @Test
  public void cartItemToAdd_addingNewCartItem_addNumberOfTicketsOfItemInCart() {
    cart = new Cart(LOGGED_USER);

    cart.addCartItem(cartItemGiven);

    assertEquals(cart.getNumberOfItems(), cartItemGiven.getNumberOfTickets());
  }

  @Test
  public void cartItemToAdd_addingNewCartItem_cartTotalPriceIsCalculated() {
    cart = new Cart(LOGGED_USER);

    cart.addCartItem(cartItemGiven);

    assertEquals(cart.getTotalPrice(), cartItemGiven.getTotalPrice(), 0);
  }

  @Test
  public void cartItemToAdd_addingExistingCartItem_delegateToCartItemToVerifyIfIsSameAsExistingCartItemsInCart() {
    given(cartItemInCart.sameAs(cartItemGiven)).willReturn(true);
    cart = new Cart(LOGGED_USER);
    cart.addCartItem(cartItemInCart);

    cart.addCartItem(cartItemGiven);

    verify(cartItemInCart).sameAs(cartItemGiven);
  }

  @Test
  public void cartItemToAdd_addExistingCartItem_delegateToCartItemInCartToAddTickets() {
    given(cartItemInCart.sameAs(cartItemGiven)).willReturn(true);
    cart = new Cart(LOGGED_USER);
    cart.addCartItem(cartItemInCart);

    cart.addCartItem(cartItemGiven);

    verify(cartItemInCart).addTickets(cartItemGiven.getPlaneTickets());
  }

  @Test
  public void cartItemToAdd_addingExistingCartItem_delegateToCartItemToGetNumberOfTickets() {
    given(cartItemInCart.sameAs(cartItemGiven)).willReturn(true);
    cart = new Cart(LOGGED_USER);
    cart.addCartItem(cartItemInCart);

    cart.addCartItem(cartItemGiven);

    verify(cartItemGiven).getNumberOfTickets();
  }

  @Test
  public void cartItemToAdd_addingExistingCartItem_cartItemsSizeIsTheSame() {
    given(cartItemInCart.sameAs(cartItemGiven)).willReturn(true);
    cart = new Cart(LOGGED_USER);
    cart.addCartItem(cartItemInCart);

    cart.addCartItem(cartItemGiven);

    assertEquals(cart.getCartItems().size(), 1);
  }

  @Test
  public void cartItemToAdd_addingExistingCartItem_numberOfItemsIsTheSumOfBothCartItem() {
    given(cartItemInCart.sameAs(cartItemGiven)).willReturn(true);
    cart = new Cart(LOGGED_USER);
    cart.addCartItem(cartItemInCart);

    cart.addCartItem(cartItemGiven);

    assertEquals(cart.getNumberOfItems(), cartItemInCart.getNumberOfTickets() + cartItemGiven.getNumberOfTickets());
  }

  @Test
  public void cartItemToAdd_addingExistingCartItem_cartTotalPriceIsCalculated() {
    given(cartItemInCart.sameAs(cartItemGiven)).willReturn(true);
    given(cartItemInCart.getTotalPrice()).willReturn(FLIGHT_TICKETS * FLIGHT_PRICE + FLIGHT_TICKETS * FLIGHT_PRICE);

    cart = new Cart(LOGGED_USER);
    cart.addCartItem(cartItemInCart);

    cart.addCartItem(cartItemGiven);

    assertEquals(cart.getTotalPrice(), cartItemInCart.getTotalPrice(), 0);
  }

  @Test
  public void cartItemToAdd_addingNewCartItemDifferentOfExistingCartItem_delegateToCartItemToVerifyIfIsSameAsExistingCartItemsInCart() {
    given(cartItemInCart.sameAs(cartItemGiven)).willReturn(false);
    given(cartItemGiven.getPlaneTicketDestination()).willReturn(FLIGHT_SOURCE);
    given(cartItemGiven.getPlaneTicketSource()).willReturn(FLIGHT_DESTINATION);

    cart = new Cart(LOGGED_USER);
    cart.addCartItem(cartItemInCart);

    cart.addCartItem(cartItemGiven);

    verify(cartItemInCart).sameAs(cartItemGiven);
  }

  @Test
  public void cartItemToAdd_addingNewCartItemDifferentOfExistingCartItem_delegateToCartItemToGetNumberOfTicket() {
    given(cartItemInCart.sameAs(cartItemGiven)).willReturn(false);
    given(cartItemGiven.getPlaneTicketDestination()).willReturn(FLIGHT_SOURCE);
    given(cartItemGiven.getPlaneTicketSource()).willReturn(FLIGHT_DESTINATION);

    cart = new Cart(LOGGED_USER);
    cart.addCartItem(cartItemInCart);

    cart.addCartItem(cartItemGiven);

    verify(cartItemGiven).getNumberOfTickets();
  }

  @Test
  public void cartItemToAdd_addNewCartItemDifferentOfExistingCartItem_cartItemIsAddedToCart() {
    given(cartItemInCart.sameAs(cartItemGiven)).willReturn(false);
    given(cartItemGiven.getPlaneTicketDestination()).willReturn(FLIGHT_SOURCE);
    given(cartItemGiven.getPlaneTicketSource()).willReturn(FLIGHT_DESTINATION);

    cart = new Cart(LOGGED_USER);
    cart.addCartItem(cartItemInCart);

    cart.addCartItem(cartItemGiven);

    assertEquals(cart.getCartItems().size(), 2);
  }

  @Test
  public void cartItemToAdd_addingNewCartItemDifferentOfExistingCartItem_addNumberOfTicketsOfItemInCart() {
    given(cartItemInCart.sameAs(cartItemGiven)).willReturn(false);
    given(cartItemGiven.getPlaneTicketDestination()).willReturn(FLIGHT_SOURCE);
    given(cartItemGiven.getPlaneTicketSource()).willReturn(FLIGHT_DESTINATION);

    cart = new Cart(LOGGED_USER);
    cart.addCartItem(cartItemInCart);

    cart.addCartItem(cartItemGiven);

    assertEquals(cart.getNumberOfItems(), cartItemInCart.getNumberOfTickets() + cartItemGiven.getNumberOfTickets());
  }

  @Test
  public void cartItemToAdd_addingNewCartItemDifferentOfExistingCartItem_cartTotalPriceIsCalculated() {
    given(cartItemInCart.sameAs(cartItemGiven)).willReturn(false);
    given(cartItemGiven.getPlaneTicketDestination()).willReturn(FLIGHT_SOURCE);
    given(cartItemGiven.getPlaneTicketSource()).willReturn(FLIGHT_DESTINATION);

    cart = new Cart(LOGGED_USER);
    cart.addCartItem(cartItemInCart);

    cart.addCartItem(cartItemGiven);

    assertEquals(cart.getTotalPrice(), cartItemInCart.getTotalPrice() + cartItemGiven.getTotalPrice(), 0);
  }

  @Test
  public void cartItemToUpdate_updatingCartItem_delegateToCartItemToVerifyIfIsSameAsExistingCartItemsInCart() {
    given(cartItemInCart.sameAs(cartItemGiven)).willReturn(true);
    given(cartItemGiven.getNumberOfTickets()).willReturn(FLIGHT_TICKETS + DIFFERENCE);

    cart = new Cart(LOGGED_USER);
    cart.addCartItem(cartItemInCart);

    cart.updateCartItem(cartItemGiven, DIFFERENCE);

    verify(cartItemInCart).sameAs(cartItemGiven);
  }

  @Test
  public void cartItemToUpdate_updatingCartItem_updateCartItemInCart() {
    given(cartItemInCart.sameAs(cartItemGiven)).willReturn(true);
    given(cartItemGiven.getNumberOfTickets()).willReturn(FLIGHT_TICKETS + DIFFERENCE);

    cart = new Cart(LOGGED_USER);
    cart.addCartItem(cartItemInCart);

    cart.updateCartItem(cartItemGiven, DIFFERENCE);

    assertEquals(cart.getCartItems().get(0).getNumberOfTickets(), FLIGHT_TICKETS + DIFFERENCE);
  }

  @Test
  public void cartItemToUpdate_updatingCartItem_adjustNumberOfTicketsOfItemInCart() {
    given(cartItemInCart.sameAs(cartItemGiven)).willReturn(true);
    given(cartItemGiven.getNumberOfTickets()).willReturn(FLIGHT_TICKETS + DIFFERENCE);

    cart = new Cart(LOGGED_USER);
    cart.addCartItem(cartItemInCart);

    cart.updateCartItem(cartItemGiven, DIFFERENCE);

    assertEquals(cart.getNumberOfItems(), FLIGHT_TICKETS + DIFFERENCE);
  }

  @Test
  public void cartItemToUpdate_updatingCartItem_cartTotalPriceIsCalculated() {
    given(cartItemInCart.sameAs(cartItemGiven)).willReturn(true);
    given(cartItemGiven.getNumberOfTickets()).willReturn(FLIGHT_TICKETS + DIFFERENCE);

    cart = new Cart(LOGGED_USER);
    cart.addCartItem(cartItemInCart);

    cart.updateCartItem(cartItemGiven, DIFFERENCE);

    assertEquals(cart.getTotalPrice(), cartItemGiven.getTotalPrice(), 0);
  }

  @Test(expected = CartNotFoundException.class)
  public void cartItemToUpdate_updatingCartItem_throwCartNotFound() {
    given(cartItemInCart.sameAs(cartItemGiven)).willReturn(false);
    given(cartItemGiven.getPlaneTicketDestination()).willReturn(FLIGHT_SOURCE);
    given(cartItemGiven.getPlaneTicketSource()).willReturn(FLIGHT_DESTINATION);
    given(cartItemGiven.getNumberOfTickets()).willReturn(FLIGHT_TICKETS + DIFFERENCE);

    cart = new Cart(LOGGED_USER);
    cart.addCartItem(cartItemInCart);

    cart.updateCartItem(cartItemGiven, DIFFERENCE);
  }

  @Test
  public void cartItemToDelete_deletingCartItem_delegateToCartItemToGetNumberOfTicket() {
    given(cartItemInCart.sameAs(cartItemGiven)).willReturn(true);
    cart = new Cart(LOGGED_USER);
    cart.addCartItem(cartItemInCart);

    cart.deleteCartItem(cartItemGiven);

    verify(cartItemInCart, atLeastOnce()).getNumberOfTickets();
  }

  @Test
  public void cartItemToDelete_deletingCartItem_adjustNumberOfTicketsOfItemInCart() {
    given(cartItemInCart.sameAs(cartItemGiven)).willReturn(true);

    cart = new Cart(LOGGED_USER);
    cart.addCartItem(cartItemInCart);

    cart.deleteCartItem(cartItemGiven);

    assertEquals(cart.getNumberOfItems(), 0);
  }

  @Test
  public void cartItemToDelete_deletingCartItem_cartTotalPriceIsCalculated() {
    given(cartItemInCart.sameAs(cartItemGiven)).willReturn(true);
    given(cartItemGiven.getNumberOfTickets()).willReturn(FLIGHT_TICKETS + DIFFERENCE);

    cart = new Cart(LOGGED_USER);
    cart.addCartItem(cartItemInCart);

    cart.deleteCartItem(cartItemGiven);

    assertEquals(cart.getTotalPrice(), 0, 0);
  }

  @Test(expected = CartNotFoundException.class)
  public void cartItemInfos_searchingNonExistingCartItem_throwNoCartFound() {
    cart = new Cart(LOGGED_USER);

    cart.findCartItem(LUGGAGE_WEIGHT, FLIGHT_COMPANY, FLIGHT_DATE, FLIGHT_DESTINATION, FLIGHT_SOURCE, SEAT_CATEGORY);
  }

  @Test(expected = CartNotFoundException.class)
  public void cartItemInfosWithNonExistingCompany_searchingNonExistingCartItem_throwNoCartFound() {
    cart = new Cart(LOGGED_USER);
    cart.addCartItem(cartItemInCart);

    cart.findCartItem(LUGGAGE_WEIGHT, ALTERNATE_FLIGHT_COMPANY, FLIGHT_DATE, FLIGHT_DESTINATION, FLIGHT_SOURCE, SEAT_CATEGORY);
  }

  @Test(expected = CartNotFoundException.class)
  public void cartItemInfosWithNonExistingDate_searchingNonExistingCartItem_throwNoCartFound() {
    cart = new Cart(LOGGED_USER);
    cart.addCartItem(cartItemInCart);

    cart.findCartItem(LUGGAGE_WEIGHT, FLIGHT_COMPANY, ALTERNATE_FLIGHT_DATE, FLIGHT_DESTINATION, FLIGHT_SOURCE, SEAT_CATEGORY);
  }

  @Test(expected = CartNotFoundException.class)
  public void cartItemInfosWithNonExistingDestination_searchingNonExistingCartItem_throwNoCartFound() {
    cart = new Cart(LOGGED_USER);
    cart.addCartItem(cartItemInCart);

    cart.findCartItem(LUGGAGE_WEIGHT, FLIGHT_COMPANY, FLIGHT_DATE, ALTERNATE_FLIGHT_DESTINATION, FLIGHT_SOURCE, SEAT_CATEGORY);
  }

  @Test(expected = CartNotFoundException.class)
  public void cartItemInfosWithNonExistingSource_searchingNonExistingCartItem_throwNoCartFound() {
    cart = new Cart(LOGGED_USER);
    cart.addCartItem(cartItemInCart);

    cart.findCartItem(LUGGAGE_WEIGHT, FLIGHT_COMPANY, FLIGHT_DATE, FLIGHT_DESTINATION, ALTERNATE_FLIGHT_SOURCE, SEAT_CATEGORY);
  }

  @Test(expected = CartNotFoundException.class)
  public void cartItemInfosWithNonLuggageWeight_searchingNonExistingCartItem_throwNoCartFound() {
    cart = new Cart(LOGGED_USER);
    cart.addCartItem(cartItemInCart);

    cart.findCartItem(ALTERNATE_LUGGAGE_WEIGHT, FLIGHT_COMPANY, FLIGHT_DATE, FLIGHT_DESTINATION, FLIGHT_SOURCE, SEAT_CATEGORY);
  }

}