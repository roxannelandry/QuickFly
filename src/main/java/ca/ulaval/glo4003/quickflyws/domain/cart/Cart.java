package ca.ulaval.glo4003.quickflyws.domain.cart;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import ca.ulaval.glo4003.quickflyws.domain.cart.cartItem.CartItem;
import ca.ulaval.glo4003.quickflyws.domain.cart.exceptions.CartNotFoundException;
import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.seats.seatCategory.SeatCategory;

public class Cart {

  private static final int INITIAL_POSITION = -1;
  
  private String user;
  private int numberOfTickets;
  private double totalPrice;
  
  private List<CartItem> cartItems = new ArrayList<CartItem>();

  public Cart(String user) {
    this.user = user;
    this.numberOfTickets = 0;
    this.totalPrice = 0;
  }

  public void addCartItem(CartItem cartItem) {
    if (alreadyContainsItem(cartItem)) {
      addCartItemToExistingCartItem(cartItem);
    } else {
      addNewCartItem(cartItem);
    }
  }

  private boolean alreadyContainsItem(CartItem item) {
    for (CartItem cartItem : cartItems) {
      if (cartItem.sameAs(item)) {
        return true;
      }
    }
    return false;
  }

  private void addNewCartItem(CartItem cartItem) {
    cartItems.add(cartItem);
    numberOfTickets += cartItem.getNumberOfTickets();
    calculateTotalPrice();
  }

  private void addCartItemToExistingCartItem(CartItem cartItem) {
    CartItem cartItemInCart = findCartItem(cartItem.getPlaneTicketLuggageWeight(), cartItem.getPlaneTicketCompany(), cartItem.getPlaneTicketDate(),
        cartItem.getPlaneTicketDestination(), cartItem.getPlaneTicketSource(), cartItem.getSeatCategory());

    cartItemInCart.addTickets(cartItem.getPlaneTickets());

    numberOfTickets += cartItem.getNumberOfTickets();
    calculateTotalPrice();
  }

  public void updateCartItem(CartItem cartItem, int differenceInNumberOfTickets) {
    int position = findPositionOfCartItem(cartItem);

    cartItems.set(position, cartItem);
    numberOfTickets += differenceInNumberOfTickets;

    calculateTotalPrice();
  }

  private int findPositionOfCartItem(CartItem cartItem) {
    int position = INITIAL_POSITION;
    for (int i = 0; i < cartItems.size(); i++) {
      if (cartItems.get(i).sameAs(cartItem)) {
        position = i;
      }
    }
    
    if (position == INITIAL_POSITION) {
      throw new CartNotFoundException("This cart item doesn't exist.");
    }
    return position;
  }

  public void deleteCartItem(CartItem cartItem) {
    CartItem cartItemToDelete = findCartItem(cartItem.getPlaneTicketLuggageWeight(), cartItem.getPlaneTicketCompany(), cartItem.getPlaneTicketDate(),
        cartItem.getPlaneTicketDestination(), cartItem.getPlaneTicketSource(), cartItem.getSeatCategory());

    int numberOfItemToRemove = cartItemToDelete.getNumberOfTickets();
    cartItems.remove(cartItemToDelete);

    numberOfTickets -= numberOfItemToRemove;
    calculateTotalPrice();
  }

  public CartItem findCartItem(double luggageWeight, String company, LocalDateTime date, String destination, String source, SeatCategory seatCategory) {
    CartItem cartItemToReturn = null;
    for (CartItem item : cartItems) {
      if (item.getPlaneTicketCompany().equals(company) && item.getPlaneTicketDate().equals(date)
          && item.getPlaneTicketDestination().equals(destination) && item.getPlaneTicketSource().equals(source)
          && item.getPlaneTicketLuggageWeight() == luggageWeight && item.getSeatCategory() == seatCategory) {
        cartItemToReturn = item;
      }
    }
    
    if (cartItemToReturn == null) {
      throw new CartNotFoundException("This cart item doesn't exist.");
    }
    
    return cartItemToReturn;
  }

  private void calculateTotalPrice() {
    double totalPrice = 0;
    for (CartItem cartItem : cartItems) {
      totalPrice += cartItem.getTotalPrice();
    }
    
    this.totalPrice = totalPrice;
  }

  public String getUser() {
    return user;
  }

  public int getNumberOfItems() {
    return numberOfTickets;
  }

  public List<CartItem> getCartItems() {
    return cartItems;
  }

  public double getTotalPrice() {
    return totalPrice;
  }

}