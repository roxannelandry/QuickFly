package ca.ulaval.glo4003.quickflyws.service.cart;

import java.util.ArrayList;
import java.util.List;

import ca.ulaval.glo4003.quickflyws.domain.cart.Cart;
import ca.ulaval.glo4003.quickflyws.domain.cart.cartItem.CartItem;
import ca.ulaval.glo4003.quickflyws.domain.cart.cartItem.planeTicket.PlaneTicket;
import ca.ulaval.glo4003.quickflyws.dto.CartDto;
import ca.ulaval.glo4003.quickflyws.dto.CartItemDtoRequest;
import ca.ulaval.glo4003.quickflyws.dto.CartItemDtoResponse;

public class CartAssembler {

  public Cart assembleCartFromSentCartItemDto(CartItemDtoRequest sentCartItemDto) {
    return new Cart(sentCartItemDto.user);
  }

  public List<CartItemDtoResponse> assembleListOfCartItemDtoFromListOfCartItem(List<CartItem> cartItems) {
    List<CartItemDtoResponse> cartItemsDto = new ArrayList<>();

    for (CartItem item : cartItems) {
      CartItemDtoResponse cartItemDto = new CartItemDtoResponse();
      
      PlaneTicket planeTicket = item.getFirstPlaneTicket();
      cartItemDto.source = planeTicket.getSource();
      cartItemDto.destination = planeTicket.getDestination();
      cartItemDto.date = planeTicket.getDate();
      cartItemDto.company = planeTicket.getCompany();
      cartItemDto.pricePerTicket = Math.ceil((planeTicket.getPrice()) * 100)/100;
      cartItemDto.luggageWeight = planeTicket.getLuggageWeight();
      cartItemDto.numberOfTickets = item.getNumberOfTickets();
      cartItemDto.totalPrice = item.getTotalPrice();
      cartItemDto.hasAirCargo = planeTicket.hasAirCargo();
      cartItemDto.seatCategory = item.getSeatCategory();

      if (cartItemDto.hasAirCargo) {
        cartItemDto.airCargoDate = planeTicket.getAirCargoDate();
      }

      cartItemsDto.add(cartItemDto);
    }
    return cartItemsDto;
  }

  public CartDto assembleDtoFromCart(Cart cart) {
    CartDto cartDto = new CartDto();
    
    List<CartItemDtoResponse> cartItemsDto = assembleListOfCartItemDtoFromListOfCartItem(cart.getCartItems());
    cartDto.user = cart.getUser();
    cartDto.numberOfItems = cart.getNumberOfItems();
    cartDto.totalPrice = cart.getTotalPrice();
    cartDto.cartItems = cartItemsDto;
    
    return cartDto;
  }

}