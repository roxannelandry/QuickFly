package ca.ulaval.glo4003.quickflyws.service.flight;

import java.util.ArrayList;
import java.util.List;

import ca.ulaval.glo4003.quickflyws.domain.flight.cargoFlight.CargoFlight;
import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.PassengerFlight;
import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.seats.seatCategory.SeatCategory;
import ca.ulaval.glo4003.quickflyws.dto.FlightDto;
import ca.ulaval.glo4003.quickflyws.dto.FlightsDto;

public class FlightAssembler {

  private static final int ONE_TICKET = 1;

  public FlightDto assembleFlightDtoFromPassengerFlight(PassengerFlight passengerFlight) {
    FlightDto flightDto = new FlightDto();

    flightDto.source = passengerFlight.getSource();
    flightDto.destination = passengerFlight.getDestination();
    flightDto.date = passengerFlight.getDate().toString();
    flightDto.company = passengerFlight.getCompany();

    flightDto.seatsEconomicAvailable = passengerFlight.getNumberOfSeatsAvailable(SeatCategory.ECONOMIC);
    flightDto.seatsEconomicPrice = passengerFlight.getSeatPrice(SeatCategory.ECONOMIC);
    flightDto.numberOfSeatsEconomic = passengerFlight.getNumberEconomicSeats();

    flightDto.seatsRegularAvailable = passengerFlight.getNumberOfSeatsAvailable(SeatCategory.REGULAR);
    flightDto.seatsRegularPrice = passengerFlight.getSeatPrice(SeatCategory.REGULAR);
    flightDto.numberOfSeatsRegular = passengerFlight.getNumberRegularSeats();

    flightDto.seatsBusinessAvailable = passengerFlight.getNumberOfSeatsAvailable(SeatCategory.BUSINESS);
    flightDto.seatsBusinessPrice = passengerFlight.getSeatPrice(SeatCategory.BUSINESS);
    flightDto.numberOfSeatsBusiness = passengerFlight.getNumberBusinessSeats();

    flightDto.flightCategory = passengerFlight.getFlightCategory();
    flightDto.exceedingWeightAvailable = passengerFlight.getExceedingWeightAvailable();
    flightDto.maximumExceedingWeight = passengerFlight.getMaximumExceedingWeight();
    flightDto.isAirVivant = passengerFlight.isAirVivant();

    return flightDto;
  }

  public FlightsDto assembleFlightsDto(List<PassengerFlight> passengerFlights, boolean flightsHasBeenFilteredByWeight) {
    List<FlightDto> listOfFlightDto = convertFlightsListToFligthsDtoList(passengerFlights);

    FlightsDto flightsDto = new FlightsDto();
    flightsDto.listOfFlightDto = listOfFlightDto;
    flightsDto.hasFilteredResult = flightsHasBeenFilteredByWeight;

    return flightsDto;
  }

  private List<FlightDto> convertFlightsListToFligthsDtoList(List<PassengerFlight> passengerFlights) {
    List<FlightDto> flightsDto = new ArrayList<FlightDto>();
    for (PassengerFlight passengerFlight : passengerFlights) {
      FlightDto flightDto = assembleFlightDtoFromPassengerFlight(passengerFlight);
      flightsDto.add(flightDto);
    }

    return flightsDto;
  }

  public FlightsDto assembleFlightsDtoWithExtraFees(List<FlightPricer> flightPricers, boolean flightsHasBeenFilteredByWeight) {
    List<FlightDto> listOfFlightDto = convertFlightsListToFligthsDtoListWithExtraFees(flightPricers);

    FlightsDto flightsDto = new FlightsDto();
    flightsDto.listOfFlightDto = listOfFlightDto;
    flightsDto.hasFilteredResult = flightsHasBeenFilteredByWeight;

    return flightsDto;
  }

  private List<FlightDto> convertFlightsListToFligthsDtoListWithExtraFees(List<FlightPricer> flightPricers) {
    List<FlightDto> flightsDto = new ArrayList<FlightDto>();
    for (FlightPricer flightPricer : flightPricers) {
      FlightDto flightDto = assembleFlightDtoWithExtraFees(flightPricer);
      flightsDto.add(flightDto);
    }

    return flightsDto;
  }

  private FlightDto assembleFlightDtoWithExtraFees(FlightPricer flightPricer) {
    FlightDto flightDto = new FlightDto();

    flightDto.source = flightPricer.getPassengerFlight().getSource();
    flightDto.destination = flightPricer.getPassengerFlight().getDestination();
    flightDto.date = flightPricer.getPassengerFlight().getDate().toString();
    flightDto.company = flightPricer.getPassengerFlight().getCompany();
    flightDto.flightCategory = flightPricer.getPassengerFlight().getFlightCategory();
    flightDto.exceedingWeightAvailable = flightPricer.getPassengerFlight().getExceedingWeightAvailable();
    flightDto.maximumExceedingWeight = flightPricer.getPassengerFlight().getMaximumExceedingWeight();
    flightDto.isAirVivant = flightPricer.getPassengerFlight().isAirVivant();

    flightDto.seatsEconomicAvailable = flightPricer.getPassengerFlight().getNumberOfSeatsAvailable(SeatCategory.ECONOMIC);
    flightDto.seatsEconomicPrice = flightPricer.getFlightTotalPrice(SeatCategory.ECONOMIC, ONE_TICKET);
    flightDto.numberOfSeatsEconomic = flightPricer.getPassengerFlight().getNumberEconomicSeats();

    flightDto.seatsRegularAvailable = flightPricer.getPassengerFlight().getNumberOfSeatsAvailable(SeatCategory.REGULAR);
    flightDto.seatsRegularPrice = flightPricer.getFlightTotalPrice(SeatCategory.REGULAR, ONE_TICKET);
    flightDto.numberOfSeatsRegular = flightPricer.getPassengerFlight().getNumberRegularSeats();

    flightDto.seatsBusinessAvailable = flightPricer.getPassengerFlight().getNumberOfSeatsAvailable(SeatCategory.BUSINESS);
    flightDto.seatsBusinessPrice = flightPricer.getFlightTotalPrice(SeatCategory.BUSINESS, ONE_TICKET);
    flightDto.numberOfSeatsBusiness = flightPricer.getPassengerFlight().getNumberBusinessSeats();

    flightDto.hasAirCargo = flightPricer.hasAirCargo();

    return flightDto;
  }

  public FlightsDto assembleAllFlightsDto(List<PassengerFlight> passengerFlights, List<CargoFlight> cargoFlights) {
    List<FlightDto> listOfFlightDto = convertAllFlightsListToFligthsDtoList(passengerFlights, cargoFlights);
    FlightsDto flightsDto = new FlightsDto();
    flightsDto.listOfFlightDto = listOfFlightDto;

    return flightsDto;

  }

  private List<FlightDto> convertAllFlightsListToFligthsDtoList(List<PassengerFlight> passengerFlights, List<CargoFlight> cargoFlights) {
    List<FlightDto> flightsDto = new ArrayList<FlightDto>();
    for (PassengerFlight passengerFlight : passengerFlights) {
      FlightDto flightDto = assembleFlightDtoFromPassengerFlight(passengerFlight);
      flightsDto.add(flightDto);
    }
    for (CargoFlight cargoFlight : cargoFlights) {
      FlightDto flightDto = assembleFlightDtoFromCargoFlight(cargoFlight);
      flightsDto.add(flightDto);
    }
    return flightsDto;
  }

  private FlightDto assembleFlightDtoFromCargoFlight(CargoFlight cargoFlight) {
    FlightDto flightDto = new FlightDto();
    flightDto.source = cargoFlight.getSource();
    flightDto.destination = cargoFlight.getDestination();
    flightDto.date = cargoFlight.getDate().toString();
    flightDto.company = cargoFlight.getCompany();
    flightDto.maximumExceedingWeight = cargoFlight.getMaximumWeight();
    return flightDto;
  }

}