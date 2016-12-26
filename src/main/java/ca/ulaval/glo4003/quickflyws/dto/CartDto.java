package ca.ulaval.glo4003.quickflyws.dto;

import java.util.List;

public class CartDto {

  public String user;
  public int numberOfItems;
  public List<CartItemDtoResponse> cartItems;
  public double totalPrice;

}