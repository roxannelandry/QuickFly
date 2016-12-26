package ca.ulaval.glo4003.quickflyws.service.paymentSystem;

import java.util.List;

import ca.ulaval.glo4003.quickflyws.domain.cart.Cart;
import ca.ulaval.glo4003.quickflyws.domain.paymentSystem.TransactionDetails;
import ca.ulaval.glo4003.quickflyws.dto.CartItemDtoResponse;
import ca.ulaval.glo4003.quickflyws.dto.TransactionDetailsDto;
import ca.ulaval.glo4003.quickflyws.dto.TransactionToProcessDto;

public class TransactionAssembler {

  public TransactionDetails assembleTransactionDetailsFromDtoAndCart(TransactionToProcessDto transactionToProcessDto, Cart cart) {
    TransactionDetails transactionDetails = new TransactionDetails(transactionToProcessDto.checkoutEmail, transactionToProcessDto.user,
        cart.getCartItems(), cart.getTotalPrice());

    return transactionDetails;
  }

  public TransactionDetailsDto assembleTransactionDetailsDtoFromTransactionDetails(TransactionDetails transactionDetails,
      List<CartItemDtoResponse> cartItemsDto) {
    TransactionDetailsDto transactionDetailsDto = new TransactionDetailsDto();

    transactionDetailsDto.user = transactionDetails.getUser();
    transactionDetailsDto.checkoutEmail = transactionDetails.getCheckoutEmail();
    transactionDetailsDto.cartItems = cartItemsDto;
    transactionDetailsDto.totalPrice = transactionDetails.getTotalPrice();

    return transactionDetailsDto;
  }

}