package ca.ulaval.glo4003.quickflyws.service.paymentSystem;

import java.util.List;

import ca.ulaval.glo4003.quickflyws.domain.cart.Cart;
import ca.ulaval.glo4003.quickflyws.domain.paymentSystem.TransactionDetails;
import ca.ulaval.glo4003.quickflyws.dto.CartItemDtoResponse;
import ca.ulaval.glo4003.quickflyws.dto.TransactionDetailsDto;
import ca.ulaval.glo4003.quickflyws.dto.TransactionToProcessDto;
import ca.ulaval.glo4003.quickflyws.service.cart.CartAssembler;
import ca.ulaval.glo4003.quickflyws.service.cart.CartRepository;

public class PaymentSystemService {

  private TransactionLogger transactionLogger;
  private CartRepository cartRepository;
  private TransactionAssembler transactionAssembler;
  private CartAssembler cartAssembler;
  private TransactionDetailsSender transactionDetailsSender;

  public PaymentSystemService(TransactionLogger transactionLogger, CartRepository cartRepository, TransactionAssembler transactionAssembler,
      CartAssembler cartAssembler, TransactionDetailsSender transactionDetailsSender) {
    this.transactionLogger = transactionLogger;
    this.transactionAssembler = transactionAssembler;
    this.cartAssembler = cartAssembler;
    this.transactionDetailsSender = transactionDetailsSender;
    this.cartRepository = cartRepository;
  }

  public TransactionDetailsDto payTicketsInCart(TransactionToProcessDto transactionToProcessDto) {
    Cart cart = cartRepository.find(transactionToProcessDto.user);
    TransactionDetails transactionDetails = transactionAssembler.assembleTransactionDetailsFromDtoAndCart(transactionToProcessDto, cart);

    transactionLogger.log(transactionDetails);
    transactionDetailsSender.sendTransactionDetails(transactionDetails);

    cartRepository.delete(transactionDetails.getUser());

    List<CartItemDtoResponse> cartItemsDto = cartAssembler.assembleListOfCartItemDtoFromListOfCartItem(transactionDetails.getCartItems());

    TransactionDetailsDto transactionDetailsDto = transactionAssembler.assembleTransactionDetailsDtoFromTransactionDetails(transactionDetails, cartItemsDto);
    return transactionDetailsDto;
  }

}