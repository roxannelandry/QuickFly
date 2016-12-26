package ca.ulaval.glo4003.quickflyws.service.cart;

import ca.ulaval.glo4003.quickflyws.domain.cart.Cart;

public interface CartRepository {

  void save(Cart cart);

  Cart find(String user);

  void delete(String user);

  boolean exist(String user);
  
}