package ca.ulaval.glo4003.quickflyws.service.flight;

import java.time.LocalDateTime;
import java.util.List;

import ca.ulaval.glo4003.quickflyws.domain.dateConverterChecker.DateConverterChecker;
import ca.ulaval.glo4003.quickflyws.domain.flight.cargoFlight.CargoFlight;
import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.PassengerFlight;
import ca.ulaval.glo4003.quickflyws.dto.FlightDto;
import ca.ulaval.glo4003.quickflyws.dto.FlightInfosToUpdateDto;
import ca.ulaval.glo4003.quickflyws.dto.FlightSearchInfos;
import ca.ulaval.glo4003.quickflyws.dto.FlightsDto;
import ca.ulaval.glo4003.quickflyws.service.flight.filterCriteria.LivingLuggageCriteria;
import ca.ulaval.glo4003.quickflyws.service.flight.filterCriteria.SeatsCriteria;
import ca.ulaval.glo4003.quickflyws.service.flight.filterCriteria.WeightCriteria;

public class FlightService {

  private FlightRepository flightRepository;
  private FlightAssembler flightAssembler;
  private FlightModificationLogger flightModificationLogger;
  private FlightPricerFactory flightPricerFactory;

  private WeightCriteria weightCriteria;
  private SeatsCriteria seatsCriteria;
  private LivingLuggageCriteria livingLuggageCriteria;
  private DateConverterChecker dateChecker;

  public FlightService(FlightRepository flightRepository, FlightAssembler flightAssembler, FlightModificationLogger flightModificationLogger,
      FlightPricerFactory flightPricerFactory) {
    this.flightRepository = flightRepository;
    this.flightAssembler = flightAssembler;
    this.flightModificationLogger = flightModificationLogger;
    this.flightPricerFactory = flightPricerFactory;

    this.weightCriteria = new WeightCriteria();
    this.seatsCriteria = new SeatsCriteria();
    this.livingLuggageCriteria = new LivingLuggageCriteria();
    this.dateChecker = new DateConverterChecker();

    weightCriteria.setSuccessor(seatsCriteria);
    seatsCriteria.setSuccessor(livingLuggageCriteria);
  }

  public FlightDto findFlight(String company, String date, String destination, String source) {
    LocalDateTime dateTime = dateChecker.dateStringToDateTimeObject(date);

    PassengerFlight passengerFlight = flightRepository.findPassengerFlight(company, dateTime, destination, source);

    return flightAssembler.assembleFlightDtoFromPassengerFlight(passengerFlight);
  }

  public FlightsDto findFlights(FlightSearchInfos flightSearchInfos) {
    LocalDateTime dateTime = dateChecker.dateAddHoursAndConvertToDateTimeObject(flightSearchInfos.date);
    weightCriteria.reinitializeBooleanForFlightFilteredByWeight();

    List<PassengerFlight> passengerFlights = flightRepository.findPassengerFlights(dateTime, flightSearchInfos.destination, flightSearchInfos.source);

    for (int i = passengerFlights.size() - 1; i >= 0; i--) {
      List<CargoFlight> cargoFlights = flightRepository.findCargoFlights(passengerFlights.get(i).getCompany(), passengerFlights.get(i).getDate(),
          passengerFlights.get(i).getDestination(), passengerFlights.get(i).getSource());

      SetFilterParameters(flightSearchInfos, cargoFlights);

      if (!weightCriteria.meetCriteria(passengerFlights.get(i))) {
        passengerFlights.remove(i);
      }
    }

    List<FlightPricer> flightPricers = flightPricerFactory.createFlightPricers(passengerFlights, flightSearchInfos.luggageWeight,
        flightSearchInfos.airCargoAllowed);

    if (flightSearchInfos.airCargoAllowed) {
      joinBestCargoFlightToPassengerFlights(flightPricers);
    }

    return flightAssembler.assembleFlightsDtoWithExtraFees(flightPricers, weightCriteria.hasBeenFilterByWeight());
  }

  private void SetFilterParameters(FlightSearchInfos flightSearchInfos, List<CargoFlight> cargoFlights) {
    weightCriteria.setParameters(flightSearchInfos.luggageWeight, flightSearchInfos.airCargoAllowed, cargoFlights);
    seatsCriteria.setParameters(flightSearchInfos.wantEconomic, flightSearchInfos.wantRegular, flightSearchInfos.wantBusiness);
    livingLuggageCriteria.setParameters(flightSearchInfos.wantAirVivant);
  }

  private void joinBestCargoFlightToPassengerFlights(List<FlightPricer> flightsPricers) {
    int numberOfTickets = 1;
    for (FlightPricer flightPricer : flightsPricers) {
      if (flightPricer.needCargoFlight(numberOfTickets)) {
        List<CargoFlight> cargoFlights = flightRepository.findCargoFlights(flightPricer.getPassengerFlight().getCompany(),
            flightPricer.getPassengerFlight().getDate(), flightPricer.getPassengerFlight().getDestination(),
            flightPricer.getPassengerFlight().getSource());
        flightPricer.associateCargoFlightToPassengerFlight(cargoFlights, numberOfTickets);
      }
    }
  }

  public FlightsDto findAllFlights() {
    List<PassengerFlight> passengerFlights = flightRepository.findAllPassengerFlights();
    List<CargoFlight> cargoFlights = flightRepository.findAllCargoFlights();
    return flightAssembler.assembleAllFlightsDto(passengerFlights, cargoFlights);
  }

  public FlightsDto findAirLourdWeightCategoryFlights() {
    List<PassengerFlight> passengerFlights = flightRepository.findAirLourdWeightCategoryPassengerFlights();
    return flightAssembler.assembleFlightsDto(passengerFlights, false);
  }

  public void updateFlightParameters(FlightInfosToUpdateDto flightInfosToUpdateDto) {
    LocalDateTime dateTime = dateChecker.dateStringToDateTimeObject(flightInfosToUpdateDto.date);

    PassengerFlight passengerFlight = flightRepository.findPassengerFlight(flightInfosToUpdateDto.company, dateTime,
        flightInfosToUpdateDto.destination, flightInfosToUpdateDto.source);

    passengerFlight.adjustMaximumExceedingWeight(flightInfosToUpdateDto.maximumExceedingWeight);

    flightRepository.savePassengerFlight(passengerFlight);

    flightModificationLogger.log(passengerFlight);
  }

}