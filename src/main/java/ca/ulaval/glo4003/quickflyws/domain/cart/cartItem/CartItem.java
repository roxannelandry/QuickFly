package ca.ulaval.glo4003.quickflyws.domain.cart.cartItem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import ca.ulaval.glo4003.quickflyws.domain.cart.cartItem.planeTicket.PlaneTicket;
import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.seats.seatCategory.SeatCategory;

public class CartItem {

  private double totalPrice;
  private List<PlaneTicket> planeTickets = new ArrayList<PlaneTicket>();

  public CartItem(List<PlaneTicket> planeTickets) {
    this.planeTickets = planeTickets;
    calculateTotalPrice();
  }

  private void calculateTotalPrice() {
    double tempTotalPrice = 0;
    for (PlaneTicket planeTicket : planeTickets) {
      tempTotalPrice += planeTicket.getPrice();
    }
    totalPrice = tempTotalPrice;
  }

  public void addTickets(List<PlaneTicket> planeTickets) {
    this.planeTickets.addAll(planeTickets);
    calculateTotalPrice();
  }

  public void removeTickets(int numberOfTickets) {
    int numberToRemove = numberOfTickets;
    for (int i = planeTickets.size() - 1; i >= 0; i--) {
      if (numberToRemove > 0) {
        planeTickets.remove(i);
        numberToRemove--;
      }
    }
    calculateTotalPrice();
  }

  public boolean sameAs(CartItem cartItem) {
    if (cartItem.getPlaneTicketSource().equals(getPlaneTicketSource()) && cartItem.getPlaneTicketDestination().equals(getPlaneTicketDestination())
        && cartItem.getPlaneTicketDate().equals(getPlaneTicketDate()) && cartItem.getPlaneTicketCompany().equals(getPlaneTicketCompany())
        && cartItem.getPlaneTicketLuggageWeight() == getPlaneTicketLuggageWeight() && cartItem.getSeatCategory() == getSeatCategory()) {
      return true;
    }
    return false;
  }

  public List<PlaneTicket> getPlaneTickets() {
    return planeTickets;
  }

  public PlaneTicket getFirstPlaneTicket() {
    return planeTickets.get(0);
  }

  public String getPlaneTicketCompany() {
    return planeTickets.get(0).getCompany();
  }

  public LocalDateTime getPlaneTicketDate() {
    return planeTickets.get(0).getDate();
  }

  public String getPlaneTicketDestination() {
    return planeTickets.get(0).getDestination();
  }

  public String getPlaneTicketSource() {
    return planeTickets.get(0).getSource();
  }

  public double getPlaneTicketPrice() {
    return planeTickets.get(0).getPrice();
  }

  public double getPlaneTicketLuggageWeight() {
    return planeTickets.get(0).getLuggageWeight();
  }

  public int getNumberOfTickets() {
    return planeTickets.size();
  }

  public double getTotalPrice() {
    return totalPrice;
  }

  public SeatCategory getSeatCategory() {
    return planeTickets.get(0).getSeatCategory();
  }

  public boolean hasAirCargo() {
    return planeTickets.get(0).hasAirCargo();
  }

  public LocalDateTime getPlaneTicketCargoDate() {
    return planeTickets.get(0).getAirCargoDate();
  }

  public String cartItemToString() {
    String separator = System.getProperty("line.separator");

    String cargo = "";
    if (hasAirCargo()) {
      cargo = "\tAirCargo date : " + getPlaneTicketCargoDate().toLocalDate() + separator;
    }

    String planeTicketInString = "Amount of tickets : " + getNumberOfTickets() + "\tSource : " + getPlaneTicketSource() + "\tDestination : "
        + getPlaneTicketDestination() + "\tDate : " + getPlaneTicketDate().toLocalDate() + "\tDepartureTime : " + getPlaneTicketDate().toLocalTime()
        + "\tCompany : " + getPlaneTicketCompany() + "\tSeat category : " + getSeatCategory() + "\tLuggage Weight : " + getPlaneTicketLuggageWeight()
        + "\tPrice : " + getPlaneTicketPrice() + "$" + separator + cargo;
    return planeTicketInString;
  }
  
  public String getTicketInString() {
    String separator = System.getProperty("line.separator");
    String concatanatedPlaneTickets = "";
    for (PlaneTicket planeTicket : planeTickets) {
      concatanatedPlaneTickets += planeTicket.toString() + separator;
    }
    return concatanatedPlaneTickets;
  }

}