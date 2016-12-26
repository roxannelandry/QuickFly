package ca.ulaval.glo4003.quickflyws.service.flight;

import java.time.LocalDateTime;
import java.util.List;

import ca.ulaval.glo4003.quickflyws.domain.flight.cargoFlight.CargoFlight;
import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.PassengerFlight;

public interface FlightRepository {

  void savePassengerFlight(PassengerFlight passengerFlight);

  void saveCargoFlight(CargoFlight cargoFlight);

  PassengerFlight findPassengerFlight(String company, LocalDateTime date, String destination, String source);

  CargoFlight findCargoFlight(String company, LocalDateTime date, String destination, String source);

  List<PassengerFlight> findPassengerFlights(LocalDateTime date, String destination, String source);

  List<CargoFlight> findCargoFlights(String company, LocalDateTime passengerFlightDate, String destination, String source);

  List<PassengerFlight> findAllPassengerFlights();

  List<CargoFlight> findAllCargoFlights();

  List<PassengerFlight> findAirLourdWeightCategoryPassengerFlights();

}