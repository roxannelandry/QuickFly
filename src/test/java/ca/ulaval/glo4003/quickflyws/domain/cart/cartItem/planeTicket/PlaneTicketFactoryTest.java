package ca.ulaval.glo4003.quickflyws.domain.cart.cartItem.planeTicket;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.seats.seatCategory.SeatCategory;
import ca.ulaval.glo4003.quickflyws.domain.generalExceptions.BadRequestException;

@RunWith(MockitoJUnitRunner.class)
public class PlaneTicketFactoryTest {
  
  private static final String SOURCE = "source";
  private static final String DESTINATION = "destination";
  private static final String COMPANY = "AirLousse";
  
  private static final double LUGGAGE_WEIGHT = 20;
  private static final double PRICE = 90.00;
  
  private static final SeatCategory SEAT_CATEGORY = SeatCategory.REGULAR;
  
  private static final boolean HAS_AIR_CARGO = false;
  
  private static final int POSITIVE_NUMBER_TICKETS = 2;
  private static final int NEGATIVE_NUMBER_TICKETS = -1;
  private static final int NULL_NUMBER_TICKETS = 0;
  
  private static final LocalDateTime DATE = LocalDateTime.of(2015, 12, 15, 12, 00);
  private static final LocalDateTime AIR_CARGO_DATE = null;
  
  private PlaneTicketFactory planeTicketFactory;

  @Before
  public void setUp() {
    planeTicketFactory = new PlaneTicketFactory();
  }

  @Test
  public void planeTicketWithPositiveNumberOfTicket_creatingTickets_returnTicketWithSameInfos() {
    List<PlaneTicket> planeTicket = planeTicketFactory.createTickets(COMPANY, DATE, DESTINATION, SOURCE, PRICE, LUGGAGE_WEIGHT, HAS_AIR_CARGO, AIR_CARGO_DATE, POSITIVE_NUMBER_TICKETS, SEAT_CATEGORY);
  
    assertEquals(planeTicket.get(0).getCompany(), COMPANY);
    assertEquals(planeTicket.get(0).getDate(), DATE);
    assertEquals(planeTicket.get(0).getDestination(), DESTINATION);
    assertEquals(planeTicket.get(0).getSource(), SOURCE);
    assertEquals(planeTicket.get(0).getPrice(), PRICE, 0);
    assertEquals(planeTicket.get(0).getLuggageWeight(), LUGGAGE_WEIGHT, 0);
    assertEquals(planeTicket.get(0).hasAirCargo(), HAS_AIR_CARGO);
    assertEquals(planeTicket.get(0).getSeatCategory(), SEAT_CATEGORY);
    assertEquals(planeTicket.size(), POSITIVE_NUMBER_TICKETS);
  } 
  
  @Test(expected = BadRequestException.class)
  public void planeTicketWithNullNumberOfTicket_creatingTickets_returnTicketWithSameInfos() {
    planeTicketFactory.createTickets(COMPANY, DATE, DESTINATION, SOURCE, PRICE, LUGGAGE_WEIGHT, HAS_AIR_CARGO, AIR_CARGO_DATE, NULL_NUMBER_TICKETS, SEAT_CATEGORY);
  }  
  
  @Test(expected = BadRequestException.class)
  public void planeTicketWithNegativeNumberOfTicket_creatingTickets_returnTicketWithSameInfos() {
    planeTicketFactory.createTickets(COMPANY, DATE, DESTINATION, SOURCE, PRICE, LUGGAGE_WEIGHT, HAS_AIR_CARGO, AIR_CARGO_DATE, NEGATIVE_NUMBER_TICKETS, SEAT_CATEGORY);
  } 
  
}