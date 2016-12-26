package ca.ulaval.glo4003.quickflyws.infrastructure.cart;

import java.util.HashMap;
import java.util.Map;

import ca.ulaval.glo4003.quickflyws.domain.cart.Cart;
import ca.ulaval.glo4003.quickflyws.domain.cart.exceptions.CartNotFoundException;
import ca.ulaval.glo4003.quickflyws.service.cart.CartRepository;

public class CartRepositoryInMemory implements CartRepository {

  private Map<String, Cart> carts = new HashMap<>();

  @Override
  public void save(Cart cart) {
    carts.put(cart.getUser(), cart);
  }

  @Override
  public Cart find(String user) {
    if (carts.containsKey(user)) {
      return carts.get(user);
    }
    throw new CartNotFoundException("There is no item in the cart for this user.");
  }

  @Override
  public void delete(String user) {
    carts.remove(user);
  }

  @Override
  public boolean exist(String user) {
    return carts.containsKey(user);
  }

}