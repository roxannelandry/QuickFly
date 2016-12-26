package ca.ulaval.glo4003.quickflyws.domain.cart.cartItem.planeTicket;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.seats.seatCategory.SeatCategory;
import ca.ulaval.glo4003.quickflyws.domain.generalExceptions.NotFoundException;

public class PlaneTicketTest {

  private static final int TICKET_ID = 30;

  private static final String COMPANY = "AirLousse";
  private static final String DESTINATION = "destination";
  private static final String SOURCE = "source";
  
  private static final boolean HAS_NO_AIR_CARGO = false;
  private static final boolean HAS_AIR_CARGO = true;

  private static final SeatCategory SEAT_CATEGORY = SeatCategory.REGULAR;

  private static final double PRICE = 80.00;
  private static final double LUGGAGE_WEIGHT = 40.00;
  
  private static final LocalDateTime AIR_CARGO_DATE = LocalDateTime.of(1999, 01, 01, 13, 00);
  private static final LocalDateTime FLIGHT_DATE_AS_DATETIME = LocalDateTime.of(1999, 01, 01, 13, 00);

  private PlaneTicket planeTicketWithNoCargo;
  private PlaneTicket planeTicketWithCargo;

  @Before
  public void setUp() {
    planeTicketWithNoCargo = new PlaneTicket(TICKET_ID, COMPANY, FLIGHT_DATE_AS_DATETIME, DESTINATION, SOURCE, PRICE, LUGGAGE_WEIGHT,
        HAS_NO_AIR_CARGO, AIR_CARGO_DATE, SEAT_CATEGORY);
    planeTicketWithCargo = new PlaneTicket(TICKET_ID, COMPANY, FLIGHT_DATE_AS_DATETIME, DESTINATION, SOURCE, PRICE, LUGGAGE_WEIGHT, HAS_AIR_CARGO,
        AIR_CARGO_DATE, SEAT_CATEGORY);
  }

  @Test
  public void requestToGetAirCargoDate_gettingExistingAirCargoDate_returnAirCargoDate() {
    LocalDateTime airCargoDate = planeTicketWithCargo.getAirCargoDate();

    assertEquals(AIR_CARGO_DATE, airCargoDate);
  }

  @Test(expected = NotFoundException.class)
  public void requestToGetAirCargoDate_gettingNonExistingAirCargoDate_throwNotFoundException() {
    planeTicketWithNoCargo.getAirCargoDate();
  }

  @Test
  public void requestToGetPlaneTicketInString_convertingPlaneTicketToStringWithNoCargo_returnPlaneTicketAsString() {
    String separator = System.getProperty("line.separator");

    String planeTicketAsString = "Ticket : " + planeTicketWithNoCargo.getTicketId() + "\tFrom : " + planeTicketWithNoCargo.getSource() + "\tTo : "
        + planeTicketWithNoCargo.getDestination() + "\tOn : " + planeTicketWithNoCargo.getDate().toLocalDate() + "\tAt : "
        + planeTicketWithNoCargo.getDate().toLocalTime() + "\tBy : " + planeTicketWithNoCargo.getCompany() + "\tSeat category : "
        + planeTicketWithNoCargo.getSeatCategory() + "\tWith an amount of luggages of : " + planeTicketWithNoCargo.getLuggageWeight() + " lbs"
        + "\tFor : " + planeTicketWithNoCargo.getPrice() + "$" + separator;

    assertEquals(planeTicketWithNoCargo.toString(), planeTicketAsString);
  }

  @Test
  public void requestToGetPlaneTicketInString_convertingPlaneTicketToStringWithCargo_returnPlaneTicketAsString() {
    String separator = System.getProperty("line.separator");

    String planeTicketAsString = "Ticket : " + planeTicketWithCargo.getTicketId() + "\tFrom : " + planeTicketWithCargo.getSource() + "\tTo : "
        + planeTicketWithCargo.getDestination() + "\tOn : " + planeTicketWithCargo.getDate().toLocalDate() + "\tAt : "
        + planeTicketWithCargo.getDate().toLocalTime() + "\tBy : " + planeTicketWithCargo.getCompany() + "\tSeat category : "
        + planeTicketWithCargo.getSeatCategory() + "\tWith an amount of luggages of : " + planeTicketWithCargo.getLuggageWeight() + " lbs"
        + "\tFor : " + planeTicketWithCargo.getPrice() + "$" + "\tAirCargo date : " + planeTicketWithCargo.getAirCargoDate().toLocalDate()
        + separator;

    assertEquals(planeTicketWithCargo.toString(), planeTicketAsString);
  }

}