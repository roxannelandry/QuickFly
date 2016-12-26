package ca.ulaval.glo4003.quickflyws.domain.cart.cartItem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ca.ulaval.glo4003.quickflyws.domain.cart.cartItem.planeTicket.PlaneTicket;
import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.seats.seatCategory.SeatCategory;

@RunWith(MockitoJUnitRunner.class)
public class CartItemTest {

  private static final String FLIGHT_SOURCE = "source";
  private static final String FLIGHT_DESTINATION = "destination";
  private static final String FLIGHT_COMPANY = "AirLousse";
  private static final String ALTERNATE_FLIGHT_SOURCE = "another source";
  private static final String ALTERNATE_FLIGHT_DESTINATION = "another destination";
  private static final String ALTERNATE_FLIGHT_COMPANY = "AirGras";

  private static final SeatCategory ALTERNATE_FLIGHT_SEAT_CATEGORY = SeatCategory.ECONOMIC;
  private static final SeatCategory FLIGHT_SEAT_CATEGORY = SeatCategory.REGULAR;

  private static final double FLIGHT_PRICE = 10.00;
  private static final double ALTERNATE_FLIGHT_LUGGAGE_WEIGHT = 20.00;
  private static final double FLIGHT_LUGGAGE_WEIGHT = 10.00;

  private static final int AMOUNT_OF_TICKETS = 2;
  private static final int TOTAL_AMOUNT_OF_TICKETS = 4;
  
  private static final LocalDateTime FLIGHT_DATE = LocalDateTime.of(2016, 01, 01, 00, 00);
  private static final LocalDateTime AIR_CARGO_DATE = LocalDateTime.of(2016, 01, 01, 00, 00);
  private static final LocalDateTime ALTERNATE_FLIGHT_DATE = LocalDateTime.of(2017, 01, 01, 00, 00);

  @Mock
  private PlaneTicket ticket;
  
  @Mock
  private PlaneTicket ticketWithCargo;

  @Mock
  private PlaneTicket alternateTicket;

  private List<PlaneTicket> tickets;
  
  private List<PlaneTicket> ticketsWithCargo;

  private CartItem cartItem;
  
  private CartItem cartItemWithCargo;

  @Before
  public void setUp() {
    given(ticket.getCompany()).willReturn(FLIGHT_COMPANY);
    given(ticket.getDate()).willReturn(FLIGHT_DATE);
    given(ticket.getDestination()).willReturn(FLIGHT_DESTINATION);
    given(ticket.getSource()).willReturn(FLIGHT_SOURCE);
    given(ticket.getPrice()).willReturn(FLIGHT_PRICE);
    given(ticket.getLuggageWeight()).willReturn(FLIGHT_LUGGAGE_WEIGHT);
    given(ticket.getSeatCategory()).willReturn(FLIGHT_SEAT_CATEGORY);

    tickets = new ArrayList<>();
    tickets.add(ticket);

    cartItem = new CartItem(tickets);
    
    given(ticketWithCargo.getCompany()).willReturn(FLIGHT_COMPANY);
    given(ticketWithCargo.getDate()).willReturn(FLIGHT_DATE);
    given(ticketWithCargo.getDestination()).willReturn(FLIGHT_DESTINATION);
    given(ticketWithCargo.getSource()).willReturn(FLIGHT_SOURCE);
    given(ticketWithCargo.getPrice()).willReturn(FLIGHT_PRICE);
    given(ticketWithCargo.getLuggageWeight()).willReturn(FLIGHT_LUGGAGE_WEIGHT);
    given(ticketWithCargo.getSeatCategory()).willReturn(FLIGHT_SEAT_CATEGORY);
    given(ticketWithCargo.hasAirCargo()).willReturn(true);
    given(ticketWithCargo.getAirCargoDate()).willReturn(AIR_CARGO_DATE);

    ticketsWithCargo = new ArrayList<>();
    ticketsWithCargo.add(ticketWithCargo);
    
    cartItemWithCargo = new CartItem(ticketsWithCargo);
  }

  @Test
  public void cartItem_creatingCartItem_calculateTotalPrice() {
    CartItem newCartItem = new CartItem(tickets);

    assertEquals(newCartItem.getTotalPrice(), FLIGHT_PRICE, 0);
  }

  @Test
  public void listOfTicketsToAdd_addingTickets_calculateTotalPrice() {
    List<PlaneTicket> listOfTickets = new ArrayList<>();
    listOfTickets.add(ticket);
    listOfTickets.add(ticket);
    listOfTickets.add(ticket);
    cartItem.addTickets(listOfTickets);

    assertEquals(cartItem.getTotalPrice(), FLIGHT_PRICE * TOTAL_AMOUNT_OF_TICKETS, 0);
  }

  @Test
  public void numberOfTicketstoRemove_removingTickets_calculateTotalPrice() {
    List<PlaneTicket> listOfTickets = new ArrayList<>();
    listOfTickets.add(ticket);
    listOfTickets.add(ticket);
    listOfTickets.add(ticket);
    cartItem.addTickets(listOfTickets);

    cartItem.removeTickets(AMOUNT_OF_TICKETS);

    assertEquals(cartItem.getTotalPrice(), FLIGHT_PRICE * (TOTAL_AMOUNT_OF_TICKETS - AMOUNT_OF_TICKETS), 0);
  }

  @Test
  public void flightInfosSameAsCartItem_comparingThemWithCartItem_returnTrue() {
    CartItem newCartItem = new CartItem(tickets);

    assertTrue(cartItem.sameAs(newCartItem));
  }

  @Test
  public void flightInfosSourceDifferentFromCartItem_comparingThemWithCartItem_returnFalse() {
    given(alternateTicket.getCompany()).willReturn(FLIGHT_COMPANY);
    given(alternateTicket.getDate()).willReturn(FLIGHT_DATE);
    given(alternateTicket.getDestination()).willReturn(FLIGHT_DESTINATION);
    given(alternateTicket.getSource()).willReturn(ALTERNATE_FLIGHT_SOURCE);
    given(alternateTicket.getPrice()).willReturn(FLIGHT_PRICE);
    given(alternateTicket.getLuggageWeight()).willReturn(FLIGHT_LUGGAGE_WEIGHT);
    given(alternateTicket.getSeatCategory()).willReturn(FLIGHT_SEAT_CATEGORY);
    List<PlaneTicket> listOfTickets = new ArrayList<>();
    listOfTickets.add(alternateTicket);

    CartItem newCartItem = new CartItem(listOfTickets);

    assertFalse(cartItem.sameAs(newCartItem));
  }

  @Test
  public void flightInfosDestinationDifferentFromCartItem_comparingThemWithCartItem_returnFalse() {
    given(alternateTicket.getCompany()).willReturn(FLIGHT_COMPANY);
    given(alternateTicket.getDate()).willReturn(FLIGHT_DATE);
    given(alternateTicket.getDestination()).willReturn(ALTERNATE_FLIGHT_DESTINATION);
    given(alternateTicket.getSource()).willReturn(FLIGHT_SOURCE);
    given(alternateTicket.getPrice()).willReturn(FLIGHT_PRICE);
    given(alternateTicket.getLuggageWeight()).willReturn(FLIGHT_LUGGAGE_WEIGHT);
    given(alternateTicket.getSeatCategory()).willReturn(FLIGHT_SEAT_CATEGORY);
    List<PlaneTicket> listOfTickets = new ArrayList<>();
    listOfTickets.add(alternateTicket);

    CartItem newCartItem = new CartItem(listOfTickets);

    assertFalse(cartItem.sameAs(newCartItem));
  }

  @Test
  public void flightInfosCompanyDifferentFromCartItem_comparingThemWithCartItem_returnFalse() {
    given(alternateTicket.getCompany()).willReturn(ALTERNATE_FLIGHT_COMPANY);
    given(alternateTicket.getDate()).willReturn(FLIGHT_DATE);
    given(alternateTicket.getDestination()).willReturn(FLIGHT_DESTINATION);
    given(alternateTicket.getSource()).willReturn(FLIGHT_SOURCE);
    given(alternateTicket.getPrice()).willReturn(FLIGHT_PRICE);
    given(alternateTicket.getLuggageWeight()).willReturn(FLIGHT_LUGGAGE_WEIGHT);
    given(alternateTicket.getSeatCategory()).willReturn(FLIGHT_SEAT_CATEGORY);
    List<PlaneTicket> listOfTickets = new ArrayList<>();
    listOfTickets.add(alternateTicket);

    CartItem newCartItem = new CartItem(listOfTickets);

    assertFalse(cartItem.sameAs(newCartItem));
  }

  @Test
  public void flightInfosSeatCategoryDifferentFromCartItem_comparingThemWithCartItem_returnFalse() {
    given(alternateTicket.getCompany()).willReturn(FLIGHT_COMPANY);
    given(alternateTicket.getDate()).willReturn(FLIGHT_DATE);
    given(alternateTicket.getDestination()).willReturn(FLIGHT_DESTINATION);
    given(alternateTicket.getSource()).willReturn(FLIGHT_SOURCE);
    given(alternateTicket.getPrice()).willReturn(FLIGHT_PRICE);
    given(alternateTicket.getLuggageWeight()).willReturn(FLIGHT_LUGGAGE_WEIGHT);
    given(alternateTicket.getSeatCategory()).willReturn(ALTERNATE_FLIGHT_SEAT_CATEGORY);
    List<PlaneTicket> listOfTickets = new ArrayList<>();
    listOfTickets.add(alternateTicket);

    CartItem newCartItem = new CartItem(listOfTickets);

    assertFalse(cartItem.sameAs(newCartItem));
  }

  @Test
  public void flightInfosDateDifferentFromCartItem_comparingThemWithCartItem_returnFalse() {
    given(alternateTicket.getCompany()).willReturn(FLIGHT_COMPANY);
    given(alternateTicket.getDate()).willReturn(ALTERNATE_FLIGHT_DATE);
    given(alternateTicket.getDestination()).willReturn(FLIGHT_DESTINATION);
    given(alternateTicket.getSource()).willReturn(FLIGHT_SOURCE);
    given(alternateTicket.getPrice()).willReturn(FLIGHT_PRICE);
    given(alternateTicket.getLuggageWeight()).willReturn(FLIGHT_LUGGAGE_WEIGHT);
    given(alternateTicket.getSeatCategory()).willReturn(FLIGHT_SEAT_CATEGORY);
    List<PlaneTicket> listOfTickets = new ArrayList<>();
    listOfTickets.add(alternateTicket);

    CartItem newCartItem = new CartItem(listOfTickets);

    assertFalse(cartItem.sameAs(newCartItem));
  }

  @Test
  public void flightInfosLuggageWeightDifferentFromCartItem_comparingThemWithCartItem_returnFalse() {
    given(alternateTicket.getCompany()).willReturn(FLIGHT_COMPANY);
    given(alternateTicket.getDate()).willReturn(FLIGHT_DATE);
    given(alternateTicket.getDestination()).willReturn(FLIGHT_DESTINATION);
    given(alternateTicket.getSource()).willReturn(FLIGHT_SOURCE);
    given(alternateTicket.getPrice()).willReturn(FLIGHT_PRICE);
    given(alternateTicket.getLuggageWeight()).willReturn(ALTERNATE_FLIGHT_LUGGAGE_WEIGHT);
    given(alternateTicket.getSeatCategory()).willReturn(FLIGHT_SEAT_CATEGORY);
    List<PlaneTicket> listOfTickets = new ArrayList<>();
    listOfTickets.add(alternateTicket);

    CartItem newCartItem = new CartItem(listOfTickets);

    assertFalse(cartItem.sameAs(newCartItem));
  }

  @Test
  public void cartItem_convertingCartItemToString_returnCartItemAsString() {
    String separator = System.getProperty("line.separator");

    String cartItemAsString = "Amount of tickets : " + cartItem.getNumberOfTickets() + "\tSource : " + cartItem.getPlaneTicketSource()
        + "\tDestination : " + cartItem.getPlaneTicketDestination() + "\tDate : " + cartItem.getPlaneTicketDate().toLocalDate() + "\tDepartureTime : "
        + cartItem.getPlaneTicketDate().toLocalTime() + "\tCompany : " + cartItem.getPlaneTicketCompany() + "\tSeat category : "
        + cartItem.getSeatCategory() + "\tLuggage Weight : " + cartItem.getPlaneTicketLuggageWeight() + "\tPrice : " + cartItem.getPlaneTicketPrice()
        + "$" + separator;

    assertEquals(cartItem.cartItemToString(), cartItemAsString);
  }
  
  @Test
  public void cartItemWithCargo_convertingCartItemToString_returnCartItemAsString() {
    String separator = System.getProperty("line.separator");

    String cartItemAsString = "Amount of tickets : " + cartItemWithCargo.getNumberOfTickets() + "\tSource : " + cartItemWithCargo.getPlaneTicketSource()
        + "\tDestination : " + cartItemWithCargo.getPlaneTicketDestination() + "\tDate : " + cartItemWithCargo.getPlaneTicketDate().toLocalDate() + "\tDepartureTime : "
        + cartItemWithCargo.getPlaneTicketDate().toLocalTime() + "\tCompany : " + cartItemWithCargo.getPlaneTicketCompany() + "\tSeat category : "
        + cartItemWithCargo.getSeatCategory() + "\tLuggage Weight : " + cartItemWithCargo.getPlaneTicketLuggageWeight() + "\tPrice : " + cartItemWithCargo.getPlaneTicketPrice()
        + "$" + separator + "\tAirCargo date : " + cartItemWithCargo.getPlaneTicketCargoDate().toLocalDate() + separator;

    assertEquals(cartItemWithCargo.cartItemToString(), cartItemAsString);
  }

}