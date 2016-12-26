package ca.ulaval.glo4003.quickflyws.service.paymentSystem;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ca.ulaval.glo4003.quickflyws.domain.cart.Cart;
import ca.ulaval.glo4003.quickflyws.domain.cart.cartItem.CartItem;
import ca.ulaval.glo4003.quickflyws.domain.cart.exceptions.CartNotFoundException;
import ca.ulaval.glo4003.quickflyws.domain.paymentSystem.TransactionDetails;
import ca.ulaval.glo4003.quickflyws.dto.CartItemDtoResponse;
import ca.ulaval.glo4003.quickflyws.dto.TransactionDetailsDto;
import ca.ulaval.glo4003.quickflyws.dto.TransactionToProcessDto;
import ca.ulaval.glo4003.quickflyws.infrastructure.email.exceptions.UnsentEmailException;
import ca.ulaval.glo4003.quickflyws.infrastructure.paymentSystem.emailService.TransactionDetailsSenderByEmail;
import ca.ulaval.glo4003.quickflyws.service.cart.CartAssembler;
import ca.ulaval.glo4003.quickflyws.service.cart.CartRepository;

@RunWith(MockitoJUnitRunner.class)
public class PaymentSystemServiceTest {

  private static final String USER = "user@hotmail.com";
  private static final String CHECKOUT_EMAIL = "checkout@hotmail.com";

  private static final double TOTAL_PRICE = 10.00;

  @Mock
  private TransactionLogger transactionLogger;

  @Mock
  private CartRepository cartRepository;

  @Mock
  private CartAssembler cartAssembler;

  @Mock
  private TransactionAssembler transactionAssembler;

  @Mock
  private TransactionDetailsSenderByEmail emailService;

  @Mock
  private Cart cart;

  @Mock
  private CartItem cartItem;

  @Mock
  private TransactionDetails transactionDetails;

  private TransactionToProcessDto transactionToProcessDto;

  private TransactionDetailsDto transactionDetailsDto;

  private PaymentSystemService paymentSystemService;

  @Before
  public void setUp() {
    paymentSystemService = new PaymentSystemService(transactionLogger, cartRepository, transactionAssembler, cartAssembler, emailService);

    List<CartItem> cartItems = new ArrayList<CartItem>();
    List<CartItemDtoResponse> cartItemsDto = new ArrayList<CartItemDtoResponse>();

    transactionToProcessDto = new TransactionToProcessDto();
    transactionToProcessDto.user = USER;

    transactionDetailsDto = new TransactionDetailsDto();
    transactionDetailsDto.user = USER;
    transactionDetailsDto.checkoutEmail = CHECKOUT_EMAIL;
    transactionDetailsDto.cartItems = cartItemsDto;
    transactionDetailsDto.totalPrice = TOTAL_PRICE;

    given(cartRepository.find(USER)).willReturn(cart);
    given(cart.getCartItems()).willReturn(cartItems);
    given(transactionAssembler.assembleTransactionDetailsFromDtoAndCart(transactionToProcessDto, cart)).willReturn(transactionDetails);
    given(transactionDetails.getUser()).willReturn(USER);
    given(transactionDetails.getCartItems()).willReturn(cartItems);
    given(cartAssembler.assembleListOfCartItemDtoFromListOfCartItem(cartItems)).willReturn(cartItemsDto);
    given(transactionAssembler.assembleTransactionDetailsDtoFromTransactionDetails(transactionDetails, cartItemsDto))
        .willReturn(transactionDetailsDto);
  }

  @Test
  public void transactionToProcessDto_payingTicketsInCart_delegateToCartRepositoryToFindCart() {
    paymentSystemService.payTicketsInCart(transactionToProcessDto);

    verify(cartRepository).find(USER);
  }

  @Test
  public void transactionToProcessDto_payingTicketsInCart_delegateToTransactionAssemblerToCreateTransactionD() {
    List<CartItem> cartItems = new ArrayList<CartItem>();
    given(cart.getCartItems()).willReturn(cartItems);

    paymentSystemService.payTicketsInCart(transactionToProcessDto);

    verify(transactionAssembler).assembleTransactionDetailsFromDtoAndCart(transactionToProcessDto, cart);
  }

  @Test
  public void transactionToProcessDto_payingTicketsInCart_delegateToTransactionLogger() {
    paymentSystemService.payTicketsInCart(transactionToProcessDto);

    verify(transactionLogger).log(transactionDetails);
  }

  @Test
  public void transactionToProcessDto_payingTicketsInCart_delegateToEmailService() {
    paymentSystemService.payTicketsInCart(transactionToProcessDto);

    verify(emailService).sendTransactionDetails(transactionDetails);
  }

  @Test
  public void transactionToProcessDto_payingTicketsInCart_delegateToCartRepositoryToDeleteCart() {
    paymentSystemService.payTicketsInCart(transactionToProcessDto);

    verify(cartRepository).delete(USER);
  }

  @Test
  public void transactionToProcessDto_payingTicketsInCart_delegateToCartAssembler() {
    List<CartItem> cartItems = new ArrayList<CartItem>();

    given(cart.getCartItems()).willReturn(cartItems);
    given(transactionAssembler.assembleTransactionDetailsFromDtoAndCart(transactionToProcessDto, cart)).willReturn(transactionDetails);
    given(transactionDetails.getCartItems()).willReturn(cartItems);

    paymentSystemService.payTicketsInCart(transactionToProcessDto);

    verify(cartAssembler).assembleListOfCartItemDtoFromListOfCartItem(cartItems);
  }

  @Test
  public void transactionToProcessDto_payingTicketsInCart_delegateToTransactionAssemblerToCreateTransactionDetailsDto() {
    List<CartItem> cartItems = new ArrayList<CartItem>();
    List<CartItemDtoResponse> cartItemsDto = new ArrayList<CartItemDtoResponse>();
    given(cart.getCartItems()).willReturn(cartItems);

    given(cartAssembler.assembleListOfCartItemDtoFromListOfCartItem(cartItems)).willReturn(cartItemsDto);

    paymentSystemService.payTicketsInCart(transactionToProcessDto);

    verify(transactionAssembler).assembleTransactionDetailsDtoFromTransactionDetails(transactionDetails, cartItemsDto);
  }

  @Test
  public void transactionToProcessDto_payingTicketsInCart_returnTransactionDetailsDto() {
    TransactionDetailsDto RequestedTransactionDetailsDto = paymentSystemService.payTicketsInCart(transactionToProcessDto);

    assertEquals(RequestedTransactionDetailsDto, transactionDetailsDto);
  }

  @Test(expected = CartNotFoundException.class)
  public void transactionToProcessDtoWithNonExistantCart_payingTicketsInCart_throwCartNotFound() {
    willThrow(CartNotFoundException.class).given(cartRepository).find(USER);

    paymentSystemService.payTicketsInCart(transactionToProcessDto);
  }

  @Test(expected = UnsentEmailException.class)
  public void transactionToProcessDtoWithNonExistantCart_payingTicketsInCart_throwUnsentEmail() {
    willThrow(UnsentEmailException.class).given(emailService).sendTransactionDetails(transactionDetails);

    paymentSystemService.payTicketsInCart(transactionToProcessDto);
  }

}