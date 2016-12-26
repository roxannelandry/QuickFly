package ca.ulaval.glo4003.quickflyws.domain.paymentSystem;

import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.ulaval.glo4003.quickflyws.domain.cart.cartItem.CartItem;
import ca.ulaval.glo4003.quickflyws.domain.paymentSystem.exceptions.CheckoutEmailInvalidException;

public class TransactionDetails {

  private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("##########.##");
  
  private String user;
  private String checkoutEmail;
  private double totalPrice;
  private List<CartItem> cartItems;

  public TransactionDetails(String checkoutEmail, String user, List<CartItem> cartItems, double totalPrice) {
    this.user = user;
    this.checkoutEmail = checkoutEmail;
    this.cartItems = cartItems;
    this.totalPrice = totalPrice;
    
    ensureCheckoutEmailIsValid(checkoutEmail);
  }
  
  private void ensureCheckoutEmailIsValid(String checkoutEmail) {
    if (!emailIsValid(checkoutEmail)) {
      throw new CheckoutEmailInvalidException("The email entered is invalid.");
    }
  }

  private boolean emailIsValid(String checkoutEmail) {
    if (checkoutEmail != null) {
      Pattern pattern = Pattern.compile(".+@.+\\.[a-z]+");
      Matcher matcher = pattern.matcher(checkoutEmail);
      return matcher.matches();
    }
    
    return false;
  }

  public String getUser() {
    return user;
  }

  public String getCheckoutEmail() {
    return checkoutEmail;
  }

  public List<CartItem> getCartItems() {
    return cartItems;
  }

  public double getTotalPrice() {
    return totalPrice;
  }
  
  public String transactionDetailsToString() {
    String separator = System.getProperty("line.separator");
    String formatedString = "Confirmation buy for : " + checkoutEmail + separator + separator;
    for (CartItem cartItem : cartItems) {
      formatedString += cartItem.cartItemToString() + separator;
    }
    
    formatedString += "Total price : " + DECIMAL_FORMAT.format(totalPrice) + "$" + separator + separator;
    return formatedString;
  }

  public String ticketsDetailsToString() {
    String separator = System.getProperty("line.separator");
    String formatedString = "";
    for (CartItem cartItem : cartItems) {
      formatedString += cartItem.getTicketInString() + separator;
    }
    
    return formatedString;
  }

}