package ca.ulaval.glo4003.quickflyws.api.cart;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.seats.seatCategory.SeatCategory;
import ca.ulaval.glo4003.quickflyws.dto.CartDto;
import ca.ulaval.glo4003.quickflyws.dto.CartItemDtoRequest;

@Path("/quickfly/cart")
public interface CartResource {

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  CartDto addCartItem(CartItemDtoRequest cartItemDtoToProcess);

  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  CartDto updateCartItemInCart(CartItemDtoRequest cartItemDtoToProcess);

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  CartDto getCart(@QueryParam("user") String user);

  @DELETE
  @Path("/delete")
  @Consumes(MediaType.APPLICATION_JSON)
  CartDto deleteCartItemInCart(@QueryParam("luggageWeight") double luggageWeight, @QueryParam("company") String company,
      @QueryParam("date") String date, @QueryParam("destination") String destination, @QueryParam("source") String source,
      @QueryParam("user") String user, @QueryParam("seatCategory") SeatCategory seatCategory);

}