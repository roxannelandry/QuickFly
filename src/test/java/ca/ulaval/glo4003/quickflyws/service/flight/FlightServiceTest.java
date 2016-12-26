package ca.ulaval.glo4003.quickflyws.service.flight;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ca.ulaval.glo4003.quickflyws.domain.flight.cargoFlight.CargoFlight;
import ca.ulaval.glo4003.quickflyws.domain.flight.exceptions.FlightNotFoundException;
import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.PassengerFlight;
import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.weightCategory.WeightCategory;
import ca.ulaval.glo4003.quickflyws.dto.FlightDto;
import ca.ulaval.glo4003.quickflyws.dto.FlightInfosToUpdateDto;
import ca.ulaval.glo4003.quickflyws.dto.FlightSearchInfos;
import ca.ulaval.glo4003.quickflyws.dto.FlightsDto;

@RunWith(MockitoJUnitRunner.class)
public class FlightServiceTest {
  
  private static final String FLIGHT_SOURCE = "source";
  private static final String FLIGHT_DESTINATION = "destination";
  private static final String FLIGHT_DATE = "2018-01-01 13:00";
  private static final String FLIGHT_DATE_ONLY = "2018-01-01";
  private static final String FLIGHT_COMPANY = "AirLousse";
  
  private static final LocalDateTime FLIGHT_DATE_AS_DATETIME = LocalDateTime.of(2018, 01, 01, 13, 00);
  private static final LocalDateTime FLIGHT_DATE_AS_DATETIME_MIDNIGHT = LocalDateTime.of(2018, 01, 01, 00, 00);
  
  private static final boolean IS_NOT_AIR_VIVANT = false;
  private static final boolean IS_NOT_AIR_CARGO = false;
  private static final boolean IS_AIR_CARGO = true;
  private static final boolean FILTERED_FLIGHT = true;
  private static final boolean NOT_FILTERED_FLIGHT = false;

  private static final boolean IS_SEARCHING_ECONOMIC = true;
  private static final boolean IS_SEARCHING_REGULAR = true;
  private static final boolean IS_SEARCHING_BUSINESS = true;

  private static final int ONE_TICKET = 1;
  
  private static final double LUGGAGE_WEIGHT = 23.5;
  private static final double NEW_LUGGAGE_WEIGHT = 40.0;

  @Mock
  private PassengerFlight passengerFlight;

  @Mock
  private CargoFlight cargoFlight;

  @Mock
  private FlightsDto filteredFlightListDto;

  @Mock
  private FlightRepository flightRepository;

  @Mock
  private FlightAssembler flightAssembler;

  @Mock
  private FlightModificationLogger flightModificationLogger;

  @Mock
  private FlightPricerFactory flightPricerFactory;

  @Mock
  private FlightPricer flightPricer;

  private FlightDto flightDto;

  private FlightInfosToUpdateDto flightToUpdateDto;

  private FlightService flightService;

  @Before
  public void setUp() {
    flightDto = new FlightDto();
    createFlightToUpdateDto();
    flightService = new FlightService(flightRepository, flightAssembler, flightModificationLogger, flightPricerFactory);
  }

  @Test
  public void passengerFlightInfos_findingFlight_delegateToFlightRepositoryToFindPassengerFlight() {
    flightService.findFlight(FLIGHT_COMPANY, FLIGHT_DATE, FLIGHT_DESTINATION, FLIGHT_SOURCE);

    verify(flightRepository).findPassengerFlight(FLIGHT_COMPANY, FLIGHT_DATE_AS_DATETIME, FLIGHT_DESTINATION, FLIGHT_SOURCE);
  }

  @Test
  public void passengerFlightInfos_findingFlight_delegateToFlightAssemblerToAssembleFlightDto() {
    given(flightRepository.findPassengerFlight(FLIGHT_COMPANY, FLIGHT_DATE_AS_DATETIME, FLIGHT_DESTINATION, FLIGHT_SOURCE))
        .willReturn(passengerFlight);

    flightService.findFlight(FLIGHT_COMPANY, FLIGHT_DATE, FLIGHT_DESTINATION, FLIGHT_SOURCE);

    verify(flightAssembler).assembleFlightDtoFromPassengerFlight(passengerFlight);
  }

  @Test
  public void passengerFlightInfos_findingFlight_returnFlightDto() {
    given(flightRepository.findPassengerFlight(FLIGHT_COMPANY, FLIGHT_DATE_AS_DATETIME, FLIGHT_DESTINATION, FLIGHT_SOURCE))
        .willReturn(passengerFlight);
    given(flightAssembler.assembleFlightDtoFromPassengerFlight(passengerFlight)).willReturn(flightDto);

    FlightDto newFlightDto = flightService.findFlight(FLIGHT_COMPANY, FLIGHT_DATE, FLIGHT_DESTINATION, FLIGHT_SOURCE);

    assertEquals(newFlightDto, flightDto);
  }

  @Test(expected = FlightNotFoundException.class)
  public void passengerFlightInfosWithNoResult_findingFlight_throwFlightNotFoundException() {
    willThrow(FlightNotFoundException.class).given(flightRepository).findPassengerFlight(FLIGHT_COMPANY, FLIGHT_DATE_AS_DATETIME, FLIGHT_DESTINATION,
        FLIGHT_SOURCE);

    flightService.findFlight(FLIGHT_COMPANY, FLIGHT_DATE, FLIGHT_DESTINATION, FLIGHT_SOURCE);
  }

  @Test
  public void passengerFlightsInfos_findingFlights_delegateToFlightRepositoryToFindPassengersFlights() {
    FlightSearchInfos flightSearchInfos = new FlightSearchInfos(FLIGHT_DATE_ONLY, FLIGHT_DESTINATION, FLIGHT_SOURCE, LUGGAGE_WEIGHT,
        IS_NOT_AIR_VIVANT, IS_NOT_AIR_CARGO, IS_SEARCHING_ECONOMIC, IS_SEARCHING_REGULAR, IS_SEARCHING_BUSINESS);
    flightService.findFlights(flightSearchInfos);

    verify(flightRepository).findPassengerFlights(FLIGHT_DATE_AS_DATETIME_MIDNIGHT, FLIGHT_DESTINATION, FLIGHT_SOURCE);
  }

  @Test
  public void flightsInfos_findingFlights_delegateToFlightPricerFactoryToCreateFlightPriceofThePassengerFlight() {
    List<PassengerFlight> flights = addMockFlightToListOfFlight();
    given(flightRepository.findPassengerFlights(FLIGHT_DATE_AS_DATETIME_MIDNIGHT, FLIGHT_DESTINATION, FLIGHT_SOURCE)).willReturn(flights);

    FlightSearchInfos flightSearchInfos = new FlightSearchInfos(FLIGHT_DATE_ONLY, FLIGHT_DESTINATION, FLIGHT_SOURCE, LUGGAGE_WEIGHT,
        IS_NOT_AIR_VIVANT, IS_NOT_AIR_CARGO, IS_SEARCHING_ECONOMIC, IS_SEARCHING_REGULAR, IS_SEARCHING_BUSINESS);
    flightService.findFlights(flightSearchInfos);

    verify(flightPricerFactory).createFlightPricers(flights, LUGGAGE_WEIGHT, IS_NOT_AIR_CARGO);
  }

  @Test
  public void flightsInfos_findingFlights_delegateToFlightAssemblerToGetFilteredFlightListDto() {
    List<PassengerFlight> flights = addMockFlightToListOfFlight();
    given(flightRepository.findPassengerFlights(FLIGHT_DATE_AS_DATETIME_MIDNIGHT, FLIGHT_DESTINATION, FLIGHT_SOURCE)).willReturn(flights);

    List<FlightPricer> flightPricers = new ArrayList<>();
    flightPricers.add(flightPricer);
    given(flightPricerFactory.createFlightPricers(flights, LUGGAGE_WEIGHT, IS_NOT_AIR_CARGO)).willReturn(flightPricers);

    FlightSearchInfos flightSearchInfos = new FlightSearchInfos(FLIGHT_DATE_ONLY, FLIGHT_DESTINATION, FLIGHT_SOURCE, LUGGAGE_WEIGHT,
        IS_NOT_AIR_VIVANT, IS_NOT_AIR_CARGO, IS_SEARCHING_ECONOMIC, IS_SEARCHING_REGULAR, IS_SEARCHING_BUSINESS);

    flightService.findFlights(flightSearchInfos);

    verify(flightAssembler).assembleFlightsDtoWithExtraFees(flightPricers, NOT_FILTERED_FLIGHT);
  }

  @Test
  public void flightsInfos_findingFlights_returnFilteredFlightListDto() {
    List<PassengerFlight> flights = addMockFlightToListOfFlight();
    List<FlightDto> flightsDto = addMockFlightDtoToListOfFlightDto();
    filteredFlightListDto = initiateflightSearchResultDtoWithoutFiltering(flightsDto);

    List<FlightPricer> flightPricers = new ArrayList<>();
    flightPricers.add(flightPricer);

    given(flightRepository.findPassengerFlights(FLIGHT_DATE_AS_DATETIME_MIDNIGHT, FLIGHT_DESTINATION, FLIGHT_SOURCE)).willReturn(flights);
    given(flightPricerFactory.createFlightPricers(flights, LUGGAGE_WEIGHT, IS_NOT_AIR_CARGO)).willReturn(flightPricers);
    given(flightAssembler.assembleFlightsDtoWithExtraFees(flightPricers, NOT_FILTERED_FLIGHT)).willReturn(filteredFlightListDto);

    FlightSearchInfos flightSearchInfos = new FlightSearchInfos(FLIGHT_DATE_ONLY, FLIGHT_DESTINATION, FLIGHT_SOURCE, LUGGAGE_WEIGHT,
        IS_NOT_AIR_VIVANT, IS_NOT_AIR_CARGO, IS_SEARCHING_ECONOMIC, IS_SEARCHING_REGULAR, IS_SEARCHING_BUSINESS);

    FlightsDto newFlightSearchResultDto = flightService.findFlights(flightSearchInfos);

    assertThat(newFlightSearchResultDto.listOfFlightDto, org.hamcrest.Matchers.hasItem(flightDto));
    assertEquals(newFlightSearchResultDto.hasFilteredResult, NOT_FILTERED_FLIGHT);
  }

  @Test
  public void noValidFlightForInfos_findingFlights_returnEmptyFilteredFlightListDto() {
    List<PassengerFlight> flights = new ArrayList<>();
    List<FlightDto> flightsDto = new ArrayList<>();
    filteredFlightListDto = initiateflightSearchResultDtoWithoutFiltering(flightsDto);

    List<FlightPricer> flightPricers = new ArrayList<>();
    flightPricers.add(flightPricer);

    given(flightRepository.findPassengerFlights(FLIGHT_DATE_AS_DATETIME_MIDNIGHT, FLIGHT_DESTINATION, FLIGHT_SOURCE)).willReturn(flights);
    given(flightPricerFactory.createFlightPricers(flights, LUGGAGE_WEIGHT, IS_NOT_AIR_CARGO)).willReturn(flightPricers);
    given(flightAssembler.assembleFlightsDtoWithExtraFees(flightPricers, NOT_FILTERED_FLIGHT)).willReturn(filteredFlightListDto);

    FlightSearchInfos flightSearchInfos = new FlightSearchInfos(FLIGHT_DATE_ONLY, FLIGHT_DESTINATION, FLIGHT_SOURCE, LUGGAGE_WEIGHT,
        IS_NOT_AIR_VIVANT, IS_NOT_AIR_CARGO, IS_SEARCHING_ECONOMIC, IS_SEARCHING_REGULAR, IS_SEARCHING_BUSINESS);
    FlightsDto newFlightSearchResultDto = flightService.findFlights(flightSearchInfos);

    assertTrue(newFlightSearchResultDto.listOfFlightDto.isEmpty());
    assertEquals(newFlightSearchResultDto.hasFilteredResult, NOT_FILTERED_FLIGHT);
  }

  @Test
  public void flightInfos_findingFlights_filterFlightsWithLowerWeightAllowed() {
    List<PassengerFlight> flights = addMockFlightToFilterForWeightToListOfFlight();
    List<FlightDto> flightsDto = new ArrayList<>();
    filteredFlightListDto = initiateflightSearchResultDtoWithFiltering(flightsDto);

    List<FlightPricer> flightPricers = new ArrayList<>();
    flightPricers.add(flightPricer);

    given(flightRepository.findPassengerFlights(FLIGHT_DATE_AS_DATETIME_MIDNIGHT, FLIGHT_DESTINATION, FLIGHT_SOURCE)).willReturn(flights);
    given(flightPricerFactory.createFlightPricers(flights, LUGGAGE_WEIGHT, IS_NOT_AIR_CARGO)).willReturn(flightPricers);
    given(flightAssembler.assembleFlightsDtoWithExtraFees(flightPricers, FILTERED_FLIGHT)).willReturn(filteredFlightListDto);

    FlightSearchInfos flightSearchInfos = new FlightSearchInfos(FLIGHT_DATE_ONLY, FLIGHT_DESTINATION, FLIGHT_SOURCE, LUGGAGE_WEIGHT,
        IS_NOT_AIR_VIVANT, IS_NOT_AIR_CARGO, IS_SEARCHING_ECONOMIC, IS_SEARCHING_REGULAR, IS_SEARCHING_BUSINESS);

    FlightsDto newFlightSearchResultDto = flightService.findFlights(flightSearchInfos);

    assertTrue(newFlightSearchResultDto.listOfFlightDto.isEmpty());
    assertEquals(newFlightSearchResultDto.hasFilteredResult, FILTERED_FLIGHT);
  }

  @Test
  public void flightInfos_findingFlights_filterFlightsWithEnoughSeatsAvailable() {
    List<PassengerFlight> flights = addMockFlightToFilterForSeatsToListOfFlight();
    List<FlightDto> flightsDto = new ArrayList<>();
    filteredFlightListDto = initiateflightSearchResultDtoWithoutFiltering(flightsDto);

    List<FlightPricer> flightPricers = new ArrayList<>();
    flightPricers.add(flightPricer);

    given(flightRepository.findPassengerFlights(FLIGHT_DATE_AS_DATETIME_MIDNIGHT, FLIGHT_DESTINATION, FLIGHT_SOURCE)).willReturn(flights);
    given(flightPricerFactory.createFlightPricers(flights, LUGGAGE_WEIGHT, IS_NOT_AIR_CARGO)).willReturn(flightPricers);
    given(flightAssembler.assembleFlightsDtoWithExtraFees(flightPricers, NOT_FILTERED_FLIGHT)).willReturn(filteredFlightListDto);

    FlightSearchInfos flightSearchInfos = new FlightSearchInfos(FLIGHT_DATE_ONLY, FLIGHT_DESTINATION, FLIGHT_SOURCE, LUGGAGE_WEIGHT,
        IS_NOT_AIR_VIVANT, IS_NOT_AIR_CARGO, IS_SEARCHING_ECONOMIC, IS_SEARCHING_REGULAR, IS_SEARCHING_BUSINESS);
    FlightsDto newFlightSearchResultDto = flightService.findFlights(flightSearchInfos);

    assertTrue(newFlightSearchResultDto.listOfFlightDto.isEmpty());
    assertEquals(newFlightSearchResultDto.hasFilteredResult, NOT_FILTERED_FLIGHT);
  }

  @Test
  public void requestToGetAllFlights_findingAllFlights_delegateToRepositoryToFindAllFlights() {
    flightService.findAllFlights();

    verify(flightRepository).findAllPassengerFlights();
    verify(flightRepository).findAllCargoFlights();
  }

  @Test
  public void requestToGetAllFlights_findingAllFlights_delegateToAssemblerToCreateAllFlightsDto() {

    List<PassengerFlight> flights = new ArrayList<PassengerFlight>();
    List<CargoFlight> cargoFlights = new ArrayList<CargoFlight>();
    flights.add(passengerFlight);
    cargoFlights.add(cargoFlight);
    given(flightRepository.findAllPassengerFlights()).willReturn(flights);
    given(flightRepository.findAllCargoFlights()).willReturn(cargoFlights);

    flightService.findAllFlights();

    verify(flightAssembler).assembleAllFlightsDto(flights, cargoFlights);
  }

  @Test
  public void requestToGetAllHeavyFlights_findingAirLourdWeightCategoryFlights_delegateToRepositoryToFindAllAirLourdWeightCategoryFlights() {
    flightService.findAirLourdWeightCategoryFlights();

    verify(flightRepository).findAirLourdWeightCategoryPassengerFlights();
  }

  @Test
  public void requestToGetAllHeavyFlights_findingAirLourdWeightCategoryFlights_delegateToAssemblerToCreateFlightsDto() {
    List<PassengerFlight> flights = flightRepository.findAirLourdWeightCategoryPassengerFlights();

    flightService.findAirLourdWeightCategoryFlights();

    verify(flightAssembler).assembleFlightsDto(flights, false);
  }

  @Test
  public void flightToUpdateDto_updatingFlightParameters_delegateToRepository() {
    willReturn(passengerFlight).given(flightRepository).findPassengerFlight(FLIGHT_COMPANY, FLIGHT_DATE_AS_DATETIME, FLIGHT_DESTINATION,
        FLIGHT_SOURCE);
    flightService.updateFlightParameters(flightToUpdateDto);

    verify(flightRepository).findPassengerFlight(flightToUpdateDto.company, FLIGHT_DATE_AS_DATETIME, flightToUpdateDto.destination,
        flightToUpdateDto.source);
  }

  @Test
  public void flightToUpdateDto_updatingFlightParameters_callFlightadjustMaximumExceedingWeight() {
    willReturn(passengerFlight).given(flightRepository).findPassengerFlight(FLIGHT_COMPANY, FLIGHT_DATE_AS_DATETIME, FLIGHT_DESTINATION,
        FLIGHT_SOURCE);
    flightService.updateFlightParameters(flightToUpdateDto);

    verify(passengerFlight).adjustMaximumExceedingWeight(NEW_LUGGAGE_WEIGHT);
  }

  @Test
  public void flightToUpdateDto_updatingFlightParameters_saveNewParametersInRepository() {
    willReturn(passengerFlight).given(flightRepository).findPassengerFlight(FLIGHT_COMPANY, FLIGHT_DATE_AS_DATETIME, FLIGHT_DESTINATION,
        FLIGHT_SOURCE);
    flightService.updateFlightParameters(flightToUpdateDto);

    verify(flightRepository).savePassengerFlight(passengerFlight);
  }

  @Test
  public void flightToUpdateDto_updatingFlightParameters_logModificationInLogger() {
    willReturn(passengerFlight).given(flightRepository).findPassengerFlight(FLIGHT_COMPANY, FLIGHT_DATE_AS_DATETIME, FLIGHT_DESTINATION,
        FLIGHT_SOURCE);
    flightService.updateFlightParameters(flightToUpdateDto);

    verify(flightModificationLogger).log(passengerFlight);
  }
  

  @Test
  public void flightsInfosAndAllowAirCargoAndEnoughLuggage_findingFlights_returnFilteredFlightListDto() {
    List<PassengerFlight> flights = addMockFlightToFilterForWeightToListOfFlight();
    List<FlightDto> flightsDto = addMockFlightDtoToListOfFlightDto();
    List<CargoFlight> cargoFlights = addMockCargoFlightToListOfCargoFlights();
    filteredFlightListDto = initiateflightSearchResultDtoWithoutFiltering(flightsDto);
    List<FlightPricer> flightPricers = addMockFlightPricerToListOfFlightPricer();

    given(flightRepository.findPassengerFlights(FLIGHT_DATE_AS_DATETIME_MIDNIGHT, FLIGHT_DESTINATION, FLIGHT_SOURCE)).willReturn(flights);
    given(flightRepository.findCargoFlights(FLIGHT_COMPANY, FLIGHT_DATE_AS_DATETIME, FLIGHT_DESTINATION, FLIGHT_SOURCE)).willReturn(cargoFlights);
    given(flightPricerFactory.createFlightPricers(flights, LUGGAGE_WEIGHT, IS_AIR_CARGO)).willReturn(flightPricers);
    given(flightAssembler.assembleFlightsDtoWithExtraFees(flightPricers, NOT_FILTERED_FLIGHT)).willReturn(filteredFlightListDto);
    given(cargoFlight.hasEnoughSpace(LUGGAGE_WEIGHT)).willReturn(true);
    given(passengerFlight.getFlightCategory()).willReturn(WeightCategory.AIR_LEGER);

    FlightSearchInfos flightSearchInfos = new FlightSearchInfos(FLIGHT_DATE_ONLY, FLIGHT_DESTINATION, FLIGHT_SOURCE, LUGGAGE_WEIGHT,
        IS_NOT_AIR_VIVANT, IS_AIR_CARGO, IS_SEARCHING_ECONOMIC, IS_SEARCHING_REGULAR, IS_SEARCHING_BUSINESS);

    FlightsDto newFlightSearchResultDto = flightService.findFlights(flightSearchInfos);

    assertThat(newFlightSearchResultDto.listOfFlightDto, org.hamcrest.Matchers.hasItem(flightDto));
    assertEquals(newFlightSearchResultDto.hasFilteredResult, NOT_FILTERED_FLIGHT);
  }

  @Test
  public void flightsInfosAndAllowAirCargoAndHasTooMuchLuggage_findingFlights_returnFilteredFlightListDto() {
    List<PassengerFlight> flights = addMockFlightToFilterForWeightToListOfFlight();
    List<FlightDto> flightsDto = new ArrayList<>();
    List<CargoFlight> cargoFlights = addMockCargoFlightToListOfCargoFlights();
    filteredFlightListDto = initiateflightSearchResultDtoWithFiltering(flightsDto);
    List<FlightPricer> flightPricers = new ArrayList<>();

    given(flightRepository.findPassengerFlights(FLIGHT_DATE_AS_DATETIME_MIDNIGHT, FLIGHT_DESTINATION, FLIGHT_SOURCE)).willReturn(flights);
    given(flightRepository.findCargoFlights(FLIGHT_COMPANY, FLIGHT_DATE_AS_DATETIME, FLIGHT_DESTINATION, FLIGHT_SOURCE)).willReturn(cargoFlights);
    given(flightAssembler.assembleFlightsDtoWithExtraFees(flightPricers, FILTERED_FLIGHT)).willReturn(filteredFlightListDto);
    given(cargoFlight.hasEnoughSpace(LUGGAGE_WEIGHT)).willReturn(false);
    given(passengerFlight.getFlightCategory()).willReturn(WeightCategory.AIR_LEGER);

    FlightSearchInfos flightSearchInfos = new FlightSearchInfos(FLIGHT_DATE_ONLY, FLIGHT_DESTINATION, FLIGHT_SOURCE, LUGGAGE_WEIGHT,
        IS_NOT_AIR_VIVANT, IS_AIR_CARGO, IS_SEARCHING_ECONOMIC, IS_SEARCHING_REGULAR, IS_SEARCHING_BUSINESS);
    FlightsDto newFlightSearchResultDto = flightService.findFlights(flightSearchInfos);

    assertTrue(newFlightSearchResultDto.listOfFlightDto.isEmpty());
    assertEquals(newFlightSearchResultDto.hasFilteredResult, FILTERED_FLIGHT);
  }

  @Test
  public void flightsInfosAndAllowAirCargo_findingFlights_returnFlightListDto() {
    List<PassengerFlight> flights = addMockFlightToListOfFlight();
    List<FlightDto> flightsDto = addMockFlightDtoToListOfFlightDto();
    List<CargoFlight> cargoFlights = addMockCargoFlightToListOfCargoFlights();
    filteredFlightListDto = initiateflightSearchResultDtoWithoutFiltering(flightsDto);
    List<FlightPricer> flightPricers = addMockFlightPricerToListOfFlightPricer();

    given(flightRepository.findPassengerFlights(FLIGHT_DATE_AS_DATETIME_MIDNIGHT, FLIGHT_DESTINATION, FLIGHT_SOURCE)).willReturn(flights);
    given(flightRepository.findCargoFlights(FLIGHT_COMPANY, FLIGHT_DATE_AS_DATETIME_MIDNIGHT, FLIGHT_DESTINATION, FLIGHT_SOURCE))
        .willReturn(cargoFlights);
    given(flightPricerFactory.createFlightPricers(flights, LUGGAGE_WEIGHT, IS_AIR_CARGO)).willReturn(flightPricers);
    given(flightAssembler.assembleFlightsDtoWithExtraFees(flightPricers, NOT_FILTERED_FLIGHT)).willReturn(filteredFlightListDto);
    given(cargoFlight.hasEnoughSpace(LUGGAGE_WEIGHT)).willReturn(true);

    FlightSearchInfos flightSearchInfos = new FlightSearchInfos(FLIGHT_DATE_ONLY, FLIGHT_DESTINATION, FLIGHT_SOURCE, LUGGAGE_WEIGHT,
        IS_NOT_AIR_VIVANT, IS_AIR_CARGO, IS_SEARCHING_ECONOMIC, IS_SEARCHING_REGULAR, IS_SEARCHING_BUSINESS);

    FlightsDto newFlightSearchResultDto = flightService.findFlights(flightSearchInfos);

    assertThat(newFlightSearchResultDto.listOfFlightDto, org.hamcrest.Matchers.hasItem(flightDto));
    assertEquals(newFlightSearchResultDto.hasFilteredResult, NOT_FILTERED_FLIGHT);
  }

  @Test
  public void flightsInfosAndAllowAirCargo_findingFlightsAndNoAirCargoExist_returnFlightListDto() {
    List<PassengerFlight> flights = addMockFlightToListOfFlight();
    List<FlightDto> flightsDto = addMockFlightDtoToListOfFlightDto();
    List<CargoFlight> cargoFlights = new ArrayList<>();
    filteredFlightListDto = initiateflightSearchResultDtoWithoutFiltering(flightsDto);
    List<FlightPricer> flightPricers = addMockFlightPricerToListOfFlightPricer();

    given(flightRepository.findPassengerFlights(FLIGHT_DATE_AS_DATETIME_MIDNIGHT, FLIGHT_DESTINATION, FLIGHT_SOURCE)).willReturn(flights);
    given(flightRepository.findCargoFlights(FLIGHT_COMPANY, FLIGHT_DATE_AS_DATETIME_MIDNIGHT, FLIGHT_DESTINATION, FLIGHT_SOURCE))
        .willReturn(cargoFlights);
    given(flightPricerFactory.createFlightPricers(flights, LUGGAGE_WEIGHT, IS_AIR_CARGO)).willReturn(flightPricers);
    given(flightAssembler.assembleFlightsDtoWithExtraFees(flightPricers, NOT_FILTERED_FLIGHT)).willReturn(filteredFlightListDto);
    given(cargoFlight.hasEnoughSpace(LUGGAGE_WEIGHT)).willReturn(true);

    FlightSearchInfos flightSearchInfos = new FlightSearchInfos(FLIGHT_DATE_ONLY, FLIGHT_DESTINATION, FLIGHT_SOURCE, LUGGAGE_WEIGHT,
        IS_NOT_AIR_VIVANT, IS_AIR_CARGO, IS_SEARCHING_ECONOMIC, IS_SEARCHING_REGULAR, IS_SEARCHING_BUSINESS);

    FlightsDto newFlightSearchResultDto = flightService.findFlights(flightSearchInfos);

    assertThat(newFlightSearchResultDto.listOfFlightDto, org.hamcrest.Matchers.hasItem(flightDto));
    assertEquals(newFlightSearchResultDto.hasFilteredResult, NOT_FILTERED_FLIGHT);
  }

  private List<PassengerFlight> addMockFlightToListOfFlight() {
    given(passengerFlight.getCompany()).willReturn(FLIGHT_COMPANY);
    given(passengerFlight.getDate()).willReturn(FLIGHT_DATE_AS_DATETIME);
    given(passengerFlight.getDestination()).willReturn(FLIGHT_DESTINATION);
    given(passengerFlight.getSource()).willReturn(FLIGHT_SOURCE);
    given(passengerFlight.weightIsExceeding(LUGGAGE_WEIGHT)).willReturn(false);
    given(passengerFlight.hasAvailableSeats(IS_SEARCHING_ECONOMIC, IS_SEARCHING_REGULAR, IS_SEARCHING_BUSINESS)).willReturn(true);
    List<PassengerFlight> flights = new ArrayList<>();
    flights.add(passengerFlight);
    return flights;
  }

  private List<FlightDto> addMockFlightDtoToListOfFlightDto() {
    List<FlightDto> flightsDto = new ArrayList<>();
    flightsDto.add(flightDto);
    return flightsDto;
  }

  private FlightsDto initiateflightSearchResultDtoWithoutFiltering(List<FlightDto> flightsDto) {
    filteredFlightListDto = new FlightsDto();
    filteredFlightListDto.listOfFlightDto = flightsDto;
    filteredFlightListDto.hasFilteredResult = false;
    return filteredFlightListDto;
  }

  private List<PassengerFlight> addMockFlightToFilterForWeightToListOfFlight() {
    given(passengerFlight.getCompany()).willReturn(FLIGHT_COMPANY);
    given(passengerFlight.getDate()).willReturn(FLIGHT_DATE_AS_DATETIME);
    given(passengerFlight.getDestination()).willReturn(FLIGHT_DESTINATION);
    given(passengerFlight.getSource()).willReturn(FLIGHT_SOURCE);
    given(passengerFlight.weightIsExceeding(LUGGAGE_WEIGHT)).willReturn(true);
    given(passengerFlight.hasAvailableSeats(IS_SEARCHING_ECONOMIC, IS_SEARCHING_REGULAR, IS_SEARCHING_BUSINESS)).willReturn(true);
    List<PassengerFlight> flights = new ArrayList<>();
    flights.add(passengerFlight);
    return flights;
  }

  private List<PassengerFlight> addMockFlightToFilterForSeatsToListOfFlight() {
    given(passengerFlight.getCompany()).willReturn(FLIGHT_COMPANY);
    given(passengerFlight.getDate()).willReturn(FLIGHT_DATE_AS_DATETIME);
    given(passengerFlight.getDestination()).willReturn(FLIGHT_DESTINATION);
    given(passengerFlight.getSource()).willReturn(FLIGHT_SOURCE);
    given(passengerFlight.weightIsExceeding(LUGGAGE_WEIGHT)).willReturn(false);
    given(passengerFlight.hasAvailableSeats(IS_SEARCHING_ECONOMIC, IS_SEARCHING_REGULAR, IS_SEARCHING_BUSINESS)).willReturn(false);
    List<PassengerFlight> flights = new ArrayList<>();
    flights.add(passengerFlight);
    return flights;
  }

  private List<CargoFlight> addMockCargoFlightToListOfCargoFlights() {
    List<CargoFlight> cargoFlights = new ArrayList<>();
    cargoFlights.add(cargoFlight);
    return cargoFlights;
  }

  private List<FlightPricer> addMockFlightPricerToListOfFlightPricer() {
    List<FlightPricer> flightPricers = new ArrayList<>();
    given(flightPricer.needCargoFlight(ONE_TICKET)).willReturn(true);
    given(flightPricer.getPassengerFlight()).willReturn(passengerFlight);
    flightPricers.add(flightPricer);
    return flightPricers;
  }

  private FlightsDto initiateflightSearchResultDtoWithFiltering(List<FlightDto> flightsDto) {
    filteredFlightListDto = new FlightsDto();
    filteredFlightListDto.listOfFlightDto = flightsDto;
    filteredFlightListDto.hasFilteredResult = true;
    return filteredFlightListDto;
  }

  private void createFlightToUpdateDto() {
    flightToUpdateDto = new FlightInfosToUpdateDto();
    flightToUpdateDto.company = FLIGHT_COMPANY;
    flightToUpdateDto.date = FLIGHT_DATE;
    flightToUpdateDto.destination = FLIGHT_DESTINATION;
    flightToUpdateDto.maximumExceedingWeight = NEW_LUGGAGE_WEIGHT;
    flightToUpdateDto.source = FLIGHT_SOURCE;
  }
  
}