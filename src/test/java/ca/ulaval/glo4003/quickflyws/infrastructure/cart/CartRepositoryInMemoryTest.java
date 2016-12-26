package ca.ulaval.glo4003.quickflyws.infrastructure.cart;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.willReturn;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ca.ulaval.glo4003.quickflyws.domain.cart.Cart;
import ca.ulaval.glo4003.quickflyws.domain.cart.exceptions.CartNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class CartRepositoryInMemoryTest {

  private static final String USER = "user";

  @Mock
  private Cart cart;

  @Mock
  private Cart cartWithSameUserId;

  private CartRepositoryInMemory cartRepositoryInMemory;

  @Before
  public void setUp() {
    cartRepositoryInMemory = new CartRepositoryInMemory();
  }

  @Test
  public void cart_savingCart_cartIsAddedToRepository() {
    willReturn(USER).given(cart).getUser();

    cartRepositoryInMemory.save(cart);

    assertEquals(cart, cartRepositoryInMemory.find(USER));
  }

  @Test
  public void alreadyExistingCart_savingCart_cartIsUpdatedInRepository() {
    willReturn(USER).given(cart).getUser();
    willReturn(USER).given(cartWithSameUserId).getUser();

    cartRepositoryInMemory.save(cart);
    cartRepositoryInMemory.save(cartWithSameUserId);

    assertEquals(cartRepositoryInMemory.find(USER), cartWithSameUserId);
  }

  @Test(expected = CartNotFoundException.class)
  public void newCart_findingCart_throwCartNotFound() {
    cartRepositoryInMemory.find(USER);
  }

  @Test
  public void cartInRepository_gettingCart_returnCart() {
    addCartToRepositoryForTest();

    Cart cartReturn = cartRepositoryInMemory.find(USER);

    assertEquals(cart, cartReturn);
  }

  @Test(expected = CartNotFoundException.class)
  public void existingUserWithCart_deletingCart_deleteCartFromRepository() {
    addCartToRepositoryForTest();

    cartRepositoryInMemory.delete(USER);

    cartRepositoryInMemory.find(USER);
  }

  @Test
  public void userHavingCart_seeingIfCartExist_returnTrue() {
    addCartToRepositoryForTest();

    assertTrue(cartRepositoryInMemory.exist(USER));
  }

  @Test
  public void userDoNotHaveCart_seeingIfCartExist_returnFalse() {
    assertFalse(cartRepositoryInMemory.exist(USER));
  }

  private void addCartToRepositoryForTest() {
    willReturn(USER).given(cart).getUser();

    cartRepositoryInMemory.save(cart);
  }

}