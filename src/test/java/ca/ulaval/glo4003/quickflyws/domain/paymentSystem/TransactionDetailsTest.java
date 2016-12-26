package ca.ulaval.glo4003.quickflyws.domain.paymentSystem;

import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ca.ulaval.glo4003.quickflyws.domain.cart.cartItem.CartItem;
import ca.ulaval.glo4003.quickflyws.domain.paymentSystem.exceptions.CheckoutEmailInvalidException;

@RunWith(MockitoJUnitRunner.class)
public class TransactionDetailsTest {

  private static final String INVALID_EMAIL = "invalidemailhotmail.com";
  private static final String NO_EMAIL = null;
  private static final String VALID_EMAIL = "email@hotmail.com";
  private static final String USER = "user";

  private static final int NUMBER_OF_TICKETS = 10;
  
  private static final double TOTAL_PRICE = 10.00;

  @Mock
  private CartItem cartItem;

  private TransactionDetails transactionDetails;

  @Before
  public void setUp() {
    willReturn(NUMBER_OF_TICKETS).given(cartItem).getNumberOfTickets();
  }

  @Test(expected = CheckoutEmailInvalidException.class)
  public void transactionWithNoCheckoutEmail_verifyingInfos_throwCheckoutEmailInvalid() {
    List<CartItem> cartItems = new ArrayList<CartItem>();
    cartItems.add(cartItem);

    new TransactionDetails(NO_EMAIL, USER, cartItems, TOTAL_PRICE);
  }

  @Test(expected = CheckoutEmailInvalidException.class)
  public void transactionWithInvalidInfos_verifyingInfos_throwCheckoutEmailInvalid() {
    List<CartItem> cartItems = new ArrayList<CartItem>();
    cartItems.add(cartItem);

    new TransactionDetails(INVALID_EMAIL, USER, cartItems, TOTAL_PRICE);
  }

  @Test(expected = CheckoutEmailInvalidException.class)
  public void transactionWithValidInfos_verifyingInfos_throwCheckoutEmailInvalid() {
    List<CartItem> cartItems = new ArrayList<CartItem>();
    cartItems.add(cartItem);

    new TransactionDetails(INVALID_EMAIL, USER, cartItems, TOTAL_PRICE);
  }

  @Test
  public void cartItem_convertingTransactionDetailsToString_willCallCartItemToStringForEachItem() {
    List<CartItem> cartItems = new ArrayList<CartItem>();
    cartItems.add(cartItem);
    transactionDetails = new TransactionDetails(VALID_EMAIL, USER, cartItems, TOTAL_PRICE);

    transactionDetails.transactionDetailsToString();

    verify(cartItem, times(cartItems.size())).cartItemToString();
  }

  @Test
  public void cartItem_convertingTicketsDetailsToString_willCallCartItemPrintTicketsEachItem() {
    List<CartItem> cartItems = new ArrayList<CartItem>();
    cartItems.add(cartItem);
    transactionDetails = new TransactionDetails(VALID_EMAIL, USER, cartItems, TOTAL_PRICE);

    transactionDetails.ticketsDetailsToString();

    verify(cartItem, times(cartItems.size())).getTicketInString();
  }

}