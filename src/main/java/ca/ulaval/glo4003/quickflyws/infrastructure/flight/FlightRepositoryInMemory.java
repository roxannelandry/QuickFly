package ca.ulaval.glo4003.quickflyws.infrastructure.flight;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.ulaval.glo4003.quickflyws.domain.flight.cargoFlight.CargoFlight;
import ca.ulaval.glo4003.quickflyws.domain.flight.exceptions.FlightNotFoundException;
import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.PassengerFlight;
import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.weightCategory.WeightCategory;
import ca.ulaval.glo4003.quickflyws.service.flight.FlightRepository;

public class FlightRepositoryInMemory implements FlightRepository {

  private Map<Integer, PassengerFlight> passengerFlights = new HashMap<>();
  private Map<Integer, CargoFlight> cargoFlights = new HashMap<>();

  private FlightHashGenerator flightHashGenerator;

  public FlightRepositoryInMemory(FlightHashGenerator flightHashGenerator) {
    this.flightHashGenerator = flightHashGenerator;
  }

  @Override
  public void savePassengerFlight(PassengerFlight passengerFlight) {
    int passengerFlightHash = flightHashGenerator.generateFlightHash(passengerFlight.getCompany(), passengerFlight.getDate(), passengerFlight.getDestination(), passengerFlight.getSource());
    passengerFlights.put(passengerFlightHash, passengerFlight);
  }

  @Override
  public void saveCargoFlight(CargoFlight cargoFlight) {
    int cargoFlightHash = flightHashGenerator.generateFlightHash(cargoFlight.getCompany(), cargoFlight.getDate(), cargoFlight.getDestination(), cargoFlight.getSource());
    cargoFlights.put(cargoFlightHash, cargoFlight);
  }

  @Override
  public PassengerFlight findPassengerFlight(String company, LocalDateTime date, String destination, String source) {
    int passengerFlightHash = flightHashGenerator.generateFlightHash(company, date, destination, source);

    if (passengerFlights.containsKey(passengerFlightHash)) {
      return passengerFlights.get(passengerFlightHash);
    }

    throw new FlightNotFoundException("This flight doesn't exist.");
  }

  @Override
  public CargoFlight findCargoFlight(String company, LocalDateTime date, String destination, String source) {
    int cargoFlightHash = flightHashGenerator.generateFlightHash(company, date, destination, source);

    if (cargoFlights.containsKey(cargoFlightHash)) {
      return cargoFlights.get(cargoFlightHash);
    }

    throw new FlightNotFoundException("This cargo flight doesn't exist.");
  }

  @Override
  public List<PassengerFlight> findPassengerFlights(LocalDateTime date, String destination, String source) {
    List<PassengerFlight> passengerFlights = new ArrayList<PassengerFlight>();

    for (PassengerFlight passengerFlight : this.passengerFlights.values()) {
      if (passengerFlight.areInformationsMatching(date, destination, source)) {
        passengerFlights.add(passengerFlight);
      }
    }
    return passengerFlights;
  }

  @Override
  public List<PassengerFlight> findAllPassengerFlights() {
    List<PassengerFlight> passengerFlights = new ArrayList<PassengerFlight>();

    for (PassengerFlight passengerFlight : this.passengerFlights.values()) {
      passengerFlights.add(passengerFlight);
    }

    return passengerFlights;
  }

  @Override
  public List<PassengerFlight> findAirLourdWeightCategoryPassengerFlights() {
    List<PassengerFlight> passengerFlights = new ArrayList<PassengerFlight>();

    for (PassengerFlight passengerFlight : this.passengerFlights.values()) {
      if (passengerFlight.getFlightCategory() == WeightCategory.AIR_LOURD) {
        passengerFlights.add(passengerFlight);
      }
    }
    return passengerFlights;
  }

  public List<CargoFlight> findCargoFlights(String company, LocalDateTime passengerFlightDate, String destination, String source) {
    List<CargoFlight> cargoFlights = new ArrayList<CargoFlight>();

    for (CargoFlight cargoFlight : this.cargoFlights.values()) {
      if (cargoFlight.areInformationsMatching(company, passengerFlightDate, destination, source)) {
        cargoFlights.add(cargoFlight);
      }
    }
    return cargoFlights;
  }

  public List<CargoFlight> findAllCargoFlights() {
    List<CargoFlight> cargoFlights = new ArrayList<CargoFlight>();

    for (CargoFlight cargoFlight : this.cargoFlights.values()) {
      cargoFlights.add(cargoFlight);
    }
    return cargoFlights;
  }

}