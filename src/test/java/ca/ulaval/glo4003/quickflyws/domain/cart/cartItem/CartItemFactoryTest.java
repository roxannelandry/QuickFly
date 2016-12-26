package ca.ulaval.glo4003.quickflyws.domain.cart.cartItem;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ca.ulaval.glo4003.quickflyws.domain.cart.cartItem.planeTicket.PlaneTicketFactory;
import ca.ulaval.glo4003.quickflyws.domain.flight.cargoFlight.CargoFlight;
import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.PassengerFlight;
import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.seats.seatCategory.SeatCategory;
import ca.ulaval.glo4003.quickflyws.dto.CartItemDtoRequest;
import ca.ulaval.glo4003.quickflyws.service.flight.FlightPricer;

@RunWith(MockitoJUnitRunner.class)
public class CartItemFactoryTest {

  private static final String COMPANY = "AirLousse";
  private static final String DESTINATION = "destination";
  private static final String SOURCE = "source";

  private static final int NUMBER_OF_TICKETS = 10;

  private static final SeatCategory SEAT_CATEGORY = SeatCategory.REGULAR;

  private static final double PRICE = 80.00;
  private static final double LUGGAGE_WEIGHT = 40.00;

  private static final boolean HAS_AIR_CARGO = true;
  
  private static final LocalDateTime CARGO_DATE_AS_DATETIME = LocalDateTime.of(2016, 01, 02, 13, 00);
  private static final LocalDateTime DATE_AS_DATETIME = LocalDateTime.of(2016, 01, 01, 13, 00);

  @Mock
  private FlightPricer flightPricer;

  @Mock
  private PassengerFlight passengerFlight;

  @Mock
  private CargoFlight cargoFlight;

  private PlaneTicketFactory planeTicketFactory;

  private CartItemFactory cartItemFactory;

  private CartItemDtoRequest cartItemDtoRequest;

  @Before
  public void setUp() {
    planeTicketFactory = new PlaneTicketFactory();
    cartItemFactory = new CartItemFactory(planeTicketFactory);

    cartItemDtoRequest = new CartItemDtoRequest();
    cartItemDtoRequest.seatCategory = SEAT_CATEGORY;
    cartItemDtoRequest.numberOfTickets = NUMBER_OF_TICKETS;

    given(flightPricer.getPassengerFlight()).willReturn(passengerFlight);
    given(flightPricer.getCargoFlight()).willReturn(cargoFlight);

    given(passengerFlight.getCompany()).willReturn(COMPANY);
    given(passengerFlight.getSource()).willReturn(SOURCE);
    given(passengerFlight.getDate()).willReturn(DATE_AS_DATETIME);
    given(passengerFlight.getDestination()).willReturn(DESTINATION);

    given(flightPricer.getFlightTotalPrice(SEAT_CATEGORY, NUMBER_OF_TICKETS)).willReturn(PRICE);
    given(flightPricer.getLuggageWeight()).willReturn(LUGGAGE_WEIGHT);
    given(flightPricer.hasAirCargo()).willReturn(HAS_AIR_CARGO);

    given(cargoFlight.getDate()).willReturn(CARGO_DATE_AS_DATETIME);
  }

  @Test
  public void cartItemInfos_creatingNewCartItem_returnCartItemWithSameInfos() {
    CartItem cartItem = cartItemFactory.createNewCartItem(flightPricer, cartItemDtoRequest);

    assertEquals(cartItem.getPlaneTicketCompany(), COMPANY);
    assertEquals(cartItem.getPlaneTicketDate(), DATE_AS_DATETIME);
    assertEquals(cartItem.getPlaneTicketDestination(), DESTINATION);
    assertEquals(cartItem.getPlaneTicketSource(), SOURCE);
    assertEquals(cartItem.getPlaneTicketPrice(), PRICE, 0);
    assertEquals(cartItem.getPlaneTicketLuggageWeight(), LUGGAGE_WEIGHT, 0);
    assertEquals(cartItem.getNumberOfTickets(), NUMBER_OF_TICKETS);
    assertEquals(cartItem.getSeatCategory(), SEAT_CATEGORY);
  }

}