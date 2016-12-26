package ca.ulaval.glo4003.quickflyws.dto;

import java.util.List;

public class TransactionDetailsDto {

  public String user;
  public String checkoutEmail;
  public double totalPrice;
  public List<CartItemDtoResponse> cartItems;

}