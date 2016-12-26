package ca.ulaval.glo4003.quickflyws.api.cart;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.seats.seatCategory.SeatCategory;
import ca.ulaval.glo4003.quickflyws.domain.generalExceptions.BadRequestException;
import ca.ulaval.glo4003.quickflyws.domain.generalExceptions.NotFoundException;
import ca.ulaval.glo4003.quickflyws.dto.CartDto;
import ca.ulaval.glo4003.quickflyws.dto.CartItemDtoRequest;
import ca.ulaval.glo4003.quickflyws.service.cart.CartService;

public class CartResourceApi implements CartResource {

  private CartService cartService;

  public CartResourceApi(CartService cartService) {
    this.cartService = cartService;
  }

  @Override
  public CartDto addCartItem(CartItemDtoRequest cartItemDtoToProcess) {
    try {
      return cartService.addCartItemToCart(cartItemDtoToProcess);
      
    } catch (NotFoundException e) {
      throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build());
      
    } catch (BadRequestException e) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build());
    }
  }

  @Override
  public CartDto updateCartItemInCart(CartItemDtoRequest cartItemDtoToProcess) {
    try {
      return cartService.updateCartItem(cartItemDtoToProcess);

    } catch (NotFoundException e) {
      throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build());
      
    } catch (BadRequestException e) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build());
    }
  }

  @Override
  public CartDto getCart(String user) {
    try {
      return cartService.getCart(user);
      
    } catch (NotFoundException e) {
      throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build());
      
    } catch (BadRequestException e) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build());
    }
  }

  @Override
  public CartDto deleteCartItemInCart(double luggageWeight, String company, String date, String destination, String source, String user, SeatCategory seatCategory) {
    try {
      return cartService.deleteCartItem(luggageWeight, company, date, destination, source, user, seatCategory);
      
    } catch (NotFoundException e) {
      throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build());
      
    } catch (BadRequestException e) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build());
    }
  }

}