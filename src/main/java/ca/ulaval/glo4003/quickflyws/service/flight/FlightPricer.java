package ca.ulaval.glo4003.quickflyws.service.flight;

import java.time.LocalDateTime;
import java.util.List;

import ca.ulaval.glo4003.quickflyws.domain.flight.cargoFlight.CargoFlight;
import ca.ulaval.glo4003.quickflyws.domain.flight.exceptions.InvalidWeightException;
import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.PassengerFlight;
import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.seats.seatCategory.SeatCategory;
import ca.ulaval.glo4003.quickflyws.dto.CartItemDtoRequest;

public class FlightPricer {

  private double luggageWeight;
  private boolean airCargoAllowed;
  private PassengerFlight passengerFlight;
  private CargoFlight cargoFlight;
  private boolean hasAirCargo;

  public FlightPricer(PassengerFlight passengerFlight, double luggageWeight, boolean airCargoAllowed) {
    this.luggageWeight = luggageWeight;
    this.airCargoAllowed = airCargoAllowed;
    this.passengerFlight = passengerFlight;
    
    hasAirCargo = false;
  }

  public boolean isAirCargoAllowed() {
    return airCargoAllowed;
  }

  public boolean needCargoFlight(int numberOfTickets) {
    return !passengerFlight.hasValidWeight(luggageWeight, numberOfTickets);
  }

  public void associateCargoFlightToPassengerFlight(List<CargoFlight> cargoFlights, int numberOfTickets) {
    if (airCargoAllowed) {
      getTheBestCargoFlight(cargoFlights, numberOfTickets);
    } else {
      throw new InvalidWeightException("Not enough room for your luggage in this flight.");
    }
  }

  private void getTheBestCargoFlight(List<CargoFlight> cargoFlights, int numberOfTickets) {
    CargoFlight cargoFlightToReturn = null;
    LocalDateTime cargotDate = passengerFlight.getDate().plusDays(3);
    for (CargoFlight cargoFlight : cargoFlights) {
      if (cargoFlight.getDate().isBefore(cargotDate) && cargoFlight.hasEnoughSpace(luggageWeight * numberOfTickets)) {
        cargoFlightToReturn = cargoFlight;
        cargotDate = cargoFlight.getDate();
      }
    }
    if (cargoFlightToReturn != null) {
      this.cargoFlight = cargoFlightToReturn;
      this.hasAirCargo = true;
    }
  }

  public double getFlightTotalPrice(SeatCategory seatCategory, int numberOfTickets) {
    double totalPrice = 0;
    if (airCargoAllowed && hasAirCargo) {
      totalPrice = calculatePriceWithCargo(seatCategory, numberOfTickets);
    } else {
      totalPrice = calculatePriceWithoutCargo(seatCategory, numberOfTickets);
    }

    totalPrice = Math.ceil(totalPrice * 100) / 100;
    return totalPrice;
  }

  private double calculatePriceWithCargo(SeatCategory seatCategory, int numberOfTickets) {
    return numberOfTickets * (passengerFlight.getSeatPrice(seatCategory) + cargoFlight.getPriceForWeight(luggageWeight));
  }

  private double calculatePriceWithoutCargo(SeatCategory seatCategory, int numberOfTickets) {
    return numberOfTickets * (passengerFlight.getSeatPrice(seatCategory) + passengerFlight.getExtraFeesForExceedingWeight(luggageWeight));
  }

  public boolean hasAirCargo() {
    return hasAirCargo;
  }

  public PassengerFlight getPassengerFlight() {
    return passengerFlight;
  }

  public CargoFlight getCargoFlight() {
    return cargoFlight;
  }

  public double getLuggageWeight() {
    return luggageWeight;
  }

  public void adjustAvailability(CartItemDtoRequest cartItemDtoToProcess) {
    passengerFlight.checkAvailability(cartItemDtoToProcess.seatCategory, cartItemDtoToProcess.numberOfTickets, hasAirCargo, luggageWeight);
    if (hasAirCargo) {
      cargoFlight.checkAvailability(cartItemDtoToProcess.numberOfTickets, luggageWeight);
      cargoFlight.adjustWeightAvailable(cartItemDtoToProcess.numberOfTickets, luggageWeight);
    }
    passengerFlight.adjustAvailability(cartItemDtoToProcess.seatCategory, cartItemDtoToProcess.numberOfTickets, hasAirCargo, luggageWeight);
  }

}