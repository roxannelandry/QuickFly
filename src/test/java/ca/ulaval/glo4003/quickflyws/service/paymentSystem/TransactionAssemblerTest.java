package ca.ulaval.glo4003.quickflyws.service.paymentSystem;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ca.ulaval.glo4003.quickflyws.domain.cart.Cart;
import ca.ulaval.glo4003.quickflyws.domain.cart.cartItem.CartItem;
import ca.ulaval.glo4003.quickflyws.domain.paymentSystem.TransactionDetails;
import ca.ulaval.glo4003.quickflyws.dto.CartItemDtoResponse;
import ca.ulaval.glo4003.quickflyws.dto.TransactionDetailsDto;
import ca.ulaval.glo4003.quickflyws.dto.TransactionToProcessDto;

@RunWith(MockitoJUnitRunner.class)
public class TransactionAssemblerTest {

  private static final String VALID_EMAIL = "email@hotmail.com";
  private static final String USER = "user";
  
  private static final double TOTAL_PRICE = 100.00;

  @Mock
  private TransactionDetails transactionDetails;

  @Mock
  private CartItem cartItem;

  @Mock
  private Cart cart;

  private CartItemDtoResponse cartItemDto;

  private TransactionToProcessDto transactionToProcessDto;

  private TransactionAssembler transactionAssembler;

  @Before
  public void setUp() {
    transactionAssembler = new TransactionAssembler();

    transactionToProcessDto = new TransactionToProcessDto();
    transactionToProcessDto.user = USER;
    transactionToProcessDto.checkoutEmail = VALID_EMAIL;

  }

  @Test
  public void transactionToProcessDto_assemblingTransactionDetailsFromDtoAndCartItems_createNewTransaction() {
    List<CartItem> cartItems = new ArrayList<>();
    cartItems.add(cartItem);

    given(cart.getCartItems()).willReturn(cartItems);
    given(cart.getTotalPrice()).willReturn(TOTAL_PRICE);

    TransactionDetails newTransactionDetails = transactionAssembler.assembleTransactionDetailsFromDtoAndCart(transactionToProcessDto, cart);

    assertEquals(newTransactionDetails.getUser(), USER);
    assertEquals(newTransactionDetails.getCheckoutEmail(), VALID_EMAIL);
    assertEquals(newTransactionDetails.getCartItems(), cartItems);
    assertEquals(newTransactionDetails.getTotalPrice(), TOTAL_PRICE, 0);
  }

  @Test
  public void transactionDetailsProcessedAndListOfCartItemDto_assemblingTransactionDetailsDtoFromTransactionDetails_createTransactionDetailsDto() {
    List<CartItemDtoResponse> cartItemsDto = new ArrayList<>();

    cartItemDto = new CartItemDtoResponse();
    cartItemsDto.add(cartItemDto);

    given(transactionDetails.getUser()).willReturn(USER);
    given(transactionDetails.getCheckoutEmail()).willReturn(VALID_EMAIL);
    given(transactionDetails.getTotalPrice()).willReturn(TOTAL_PRICE);

    TransactionDetailsDto newTransactionDetailsDto = transactionAssembler.assembleTransactionDetailsDtoFromTransactionDetails(transactionDetails,
        cartItemsDto);

    assertEquals(newTransactionDetailsDto.user, USER);
    assertEquals(newTransactionDetailsDto.checkoutEmail, VALID_EMAIL);
    assertEquals(newTransactionDetailsDto.cartItems, cartItemsDto);
    assertEquals(newTransactionDetailsDto.totalPrice, TOTAL_PRICE, 0);
  }

}