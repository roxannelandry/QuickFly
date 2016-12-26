package ca.ulaval.glo4003.quickflyws.service.flight;

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

import ca.ulaval.glo4003.quickflyws.domain.flight.cargoFlight.CargoFlight;
import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.PassengerFlight;
import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.seats.seatCategory.SeatCategory;
import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.weightCategory.WeightCategory;
import ca.ulaval.glo4003.quickflyws.dto.FlightDto;
import ca.ulaval.glo4003.quickflyws.dto.FlightsDto;

@RunWith(MockitoJUnitRunner.class)
public class FlightAssemblerTest {

  private static final String FLIGHT_SOURCE = "source";
  private static final String FLIGHT_DESTINATION = "destination";
  private static final String FLIGHT_COMPANY = "company";
  private static final String CARGO_FLIGHT_SOURCE = "cargo source";
  private static final String CARGO_FLIGHT_DESTINATION = "cargo destination";
  private static final String CARGO_FLIGHT_COMPANY = "cargo company";

  private static final LocalDateTime FLIGHT_DATE_AS_DATETIME = LocalDateTime.of(2016, 01, 01, 13, 00);
  private static final LocalDateTime CARGO_FLIGHT_DATE_AS_DATETIME = LocalDateTime.of(2016, 01, 02, 15, 00);

  private static final boolean IS_NOT_AIR_VIVANT = false;
  private static final boolean HAS_BEEN_FILTER = true;
  private static final boolean HAS_NOT_BEEN_FILTER = false;
  
  private static final int ONE_TICKET = 1;
  private static final int NUMBER_ECONOMIC_SEATS_AVAILABLE = 10;
  private static final int NUMBER_TOTAL_ECONOMIC_SEATS = 15;
  private static final int NUMBER_REGULAR_SEATS_AVAILABLE = 20;
  private static final int NUMBER_TOTAL_REGULAR_SEATS = 20;
  private static final int NUMBER_BUSINESS_SEATS_AVAILABLE = 3;
  private static final int NUMBER_TOTAL_BUSINESS_SEATS = 4;

  private static final double EXCEEDING_LUGGAGE_WEIGHT_AVAILABLE = 10.00;
  private static final double LUGGAGE_WEIGHT = 66;
  private static final double EXTRA_FEES = 10;
  private static final double MAXIMUM_LUGGAGE_WEIGHT_FOR_HEAVY_FLIGHT = 65;
  private static final double ECONOMIC_SEATS_PRICE = 20.0;
  private static final double REGULAR_SEATS_PRICE = 40.00;
  private static final double BUSINESS_SEATS_PRICE = 90.00;

  private static final WeightCategory FLIGHT_CATEGORY_LIGHT = WeightCategory.AIR_LEGER;
  private static final WeightCategory FLIGHT_CATEGORY_HEAVY = WeightCategory.AIR_LOURD;
  
  private static final SeatCategory SEAT_CATEGORY_ECONOMIC = SeatCategory.ECONOMIC;
  private static final SeatCategory SEAT_CATEGORY_REGULAR = SeatCategory.REGULAR;
  private static final SeatCategory SEAT_CATEGORY_BUSINESS = SeatCategory.BUSINESS;

  @Mock
  private PassengerFlight passengerFlight;

  @Mock
  private CargoFlight cargoFlight;

  @Mock
  private FlightPricer flightPricer;

  private FlightDto flightDto;

  private FlightAssembler flightAssembler;

  @Before
  public void setUp() {
    flightAssembler = new FlightAssembler();

    flightDto = new FlightDto();
    flightDto.source = FLIGHT_SOURCE;
    flightDto.destination = FLIGHT_DESTINATION;
    flightDto.date = FLIGHT_DATE_AS_DATETIME.toString();
    flightDto.company = FLIGHT_COMPANY;
    flightDto.isAirVivant = IS_NOT_AIR_VIVANT;

    flightDto.seatsEconomicAvailable = NUMBER_ECONOMIC_SEATS_AVAILABLE;
    flightDto.seatsEconomicPrice = ECONOMIC_SEATS_PRICE;
    flightDto.numberOfSeatsEconomic = NUMBER_TOTAL_ECONOMIC_SEATS;

    flightDto.seatsRegularAvailable = NUMBER_REGULAR_SEATS_AVAILABLE;
    flightDto.seatsRegularPrice = REGULAR_SEATS_PRICE;
    flightDto.numberOfSeatsRegular = NUMBER_TOTAL_REGULAR_SEATS;

    flightDto.seatsBusinessAvailable = NUMBER_BUSINESS_SEATS_AVAILABLE;
    flightDto.seatsBusinessPrice = BUSINESS_SEATS_PRICE;
    flightDto.numberOfSeatsBusiness = NUMBER_TOTAL_BUSINESS_SEATS;

    given(passengerFlight.getSource()).willReturn(FLIGHT_SOURCE);
    given(passengerFlight.getDestination()).willReturn(FLIGHT_DESTINATION);
    given(passengerFlight.getDate()).willReturn(FLIGHT_DATE_AS_DATETIME);
    given(passengerFlight.getCompany()).willReturn(FLIGHT_COMPANY);

    given(passengerFlight.getSeatPrice(SEAT_CATEGORY_ECONOMIC)).willReturn(ECONOMIC_SEATS_PRICE);
    given(passengerFlight.getNumberOfSeatsAvailable(SEAT_CATEGORY_ECONOMIC)).willReturn(NUMBER_ECONOMIC_SEATS_AVAILABLE);
    given(passengerFlight.getNumberEconomicSeats()).willReturn(NUMBER_TOTAL_ECONOMIC_SEATS);

    given(passengerFlight.getSeatPrice(SEAT_CATEGORY_REGULAR)).willReturn(REGULAR_SEATS_PRICE);
    given(passengerFlight.getNumberOfSeatsAvailable(SEAT_CATEGORY_REGULAR)).willReturn(NUMBER_REGULAR_SEATS_AVAILABLE);
    given(passengerFlight.getNumberRegularSeats()).willReturn(NUMBER_TOTAL_REGULAR_SEATS);

    given(passengerFlight.getSeatPrice(SEAT_CATEGORY_BUSINESS)).willReturn(BUSINESS_SEATS_PRICE);
    given(passengerFlight.getNumberOfSeatsAvailable(SEAT_CATEGORY_BUSINESS)).willReturn(NUMBER_BUSINESS_SEATS_AVAILABLE);
    given(passengerFlight.getNumberBusinessSeats()).willReturn(NUMBER_TOTAL_BUSINESS_SEATS);

    given(passengerFlight.isAirVivant()).willReturn(IS_NOT_AIR_VIVANT);
  }

  @Test
  public void flightToConvertInDto_assemblingFlightDto_returnFlightDtoWithSameInfos() {
    given(passengerFlight.getFlightCategory()).willReturn(FLIGHT_CATEGORY_LIGHT);

    FlightDto newFlightDto = flightAssembler.assembleFlightDtoFromPassengerFlight(passengerFlight);

    assertEquals(newFlightDto.source, passengerFlight.getSource());
    assertEquals(newFlightDto.destination, passengerFlight.getDestination());
    assertEquals(newFlightDto.date, passengerFlight.getDate().toString());
    assertEquals(newFlightDto.company, passengerFlight.getCompany());

    assertEquals(newFlightDto.seatsEconomicPrice, passengerFlight.getSeatPrice(SEAT_CATEGORY_ECONOMIC), 0);
    assertEquals(newFlightDto.seatsEconomicAvailable, passengerFlight.getNumberOfSeatsAvailable(SEAT_CATEGORY_ECONOMIC));
    assertEquals(newFlightDto.numberOfSeatsEconomic, passengerFlight.getNumberEconomicSeats());

    assertEquals(newFlightDto.seatsRegularPrice, passengerFlight.getSeatPrice(SEAT_CATEGORY_REGULAR), 0);
    assertEquals(newFlightDto.seatsRegularAvailable, passengerFlight.getNumberOfSeatsAvailable(SEAT_CATEGORY_REGULAR));
    assertEquals(newFlightDto.numberOfSeatsRegular, passengerFlight.getNumberRegularSeats());

    assertEquals(newFlightDto.seatsBusinessPrice, passengerFlight.getSeatPrice(SEAT_CATEGORY_BUSINESS), 0);
    assertEquals(newFlightDto.seatsBusinessAvailable, passengerFlight.getNumberOfSeatsAvailable(SEAT_CATEGORY_BUSINESS));
    assertEquals(newFlightDto.numberOfSeatsBusiness, passengerFlight.getNumberBusinessSeats());

    assertEquals(newFlightDto.flightCategory, passengerFlight.getFlightCategory());
    assertEquals(newFlightDto.exceedingWeightAvailable, passengerFlight.getExceedingWeightAvailable(), 0);
    assertEquals(newFlightDto.maximumExceedingWeight, passengerFlight.getMaximumExceedingWeight(), 0);
    assertEquals(newFlightDto.isAirVivant, passengerFlight.isAirVivant());

  }

  @Test
  public void listOfFlightsThatWasFiltered_assemblingFlightsDto_returnFlightsDtoWithSameInfos() {
    given(passengerFlight.getFlightCategory()).willReturn(FLIGHT_CATEGORY_HEAVY);
    given(passengerFlight.getExceedingWeightAvailable()).willReturn(EXCEEDING_LUGGAGE_WEIGHT_AVAILABLE);
    given(passengerFlight.getMaximumExceedingWeight()).willReturn(MAXIMUM_LUGGAGE_WEIGHT_FOR_HEAVY_FLIGHT);

    List<PassengerFlight> flights = new ArrayList<>();
    flights.add(passengerFlight);

    FlightsDto flightsDto = flightAssembler.assembleFlightsDto(flights, HAS_BEEN_FILTER);

    assertTrue(flightsDto.hasFilteredResult);
    assertEquals(flightsDto.listOfFlightDto.get(0).company, FLIGHT_COMPANY);
    assertEquals(flightsDto.listOfFlightDto.get(0).date, FLIGHT_DATE_AS_DATETIME.toString());
    assertEquals(flightsDto.listOfFlightDto.get(0).destination, FLIGHT_DESTINATION);
    assertEquals(flightsDto.listOfFlightDto.get(0).source, FLIGHT_SOURCE);

    assertEquals(flightsDto.listOfFlightDto.get(0).seatsEconomicPrice, ECONOMIC_SEATS_PRICE, 0);
    assertEquals(flightsDto.listOfFlightDto.get(0).seatsEconomicAvailable, NUMBER_ECONOMIC_SEATS_AVAILABLE);
    assertEquals(flightsDto.listOfFlightDto.get(0).numberOfSeatsEconomic, NUMBER_TOTAL_ECONOMIC_SEATS);

    assertEquals(flightsDto.listOfFlightDto.get(0).seatsRegularPrice, REGULAR_SEATS_PRICE, 0);
    assertEquals(flightsDto.listOfFlightDto.get(0).seatsRegularAvailable, NUMBER_REGULAR_SEATS_AVAILABLE);
    assertEquals(flightsDto.listOfFlightDto.get(0).numberOfSeatsRegular, NUMBER_TOTAL_REGULAR_SEATS);

    assertEquals(flightsDto.listOfFlightDto.get(0).seatsBusinessPrice, BUSINESS_SEATS_PRICE, 0);
    assertEquals(flightsDto.listOfFlightDto.get(0).seatsBusinessAvailable, NUMBER_BUSINESS_SEATS_AVAILABLE);
    assertEquals(flightsDto.listOfFlightDto.get(0).numberOfSeatsBusiness, NUMBER_TOTAL_BUSINESS_SEATS);

    assertEquals(flightsDto.listOfFlightDto.get(0).flightCategory, FLIGHT_CATEGORY_HEAVY);
    assertEquals(flightsDto.listOfFlightDto.get(0).exceedingWeightAvailable, EXCEEDING_LUGGAGE_WEIGHT_AVAILABLE, 0);
    assertEquals(flightsDto.listOfFlightDto.get(0).maximumExceedingWeight, MAXIMUM_LUGGAGE_WEIGHT_FOR_HEAVY_FLIGHT, 0);
    assertEquals(flightsDto.listOfFlightDto.get(0).isAirVivant, IS_NOT_AIR_VIVANT);
  }

  @Test
  public void listOfFlightsThatWasNotFiltered_assemblingFlightsDto_returnFlightsDtoWithSameInfos() {
    given(passengerFlight.getFlightCategory()).willReturn(FLIGHT_CATEGORY_HEAVY);
    given(passengerFlight.getExceedingWeightAvailable()).willReturn(EXCEEDING_LUGGAGE_WEIGHT_AVAILABLE);
    given(passengerFlight.getMaximumExceedingWeight()).willReturn(MAXIMUM_LUGGAGE_WEIGHT_FOR_HEAVY_FLIGHT);

    List<PassengerFlight> flights = new ArrayList<>();
    flights.add(passengerFlight);

    FlightsDto flightsDto = flightAssembler.assembleFlightsDto(flights, HAS_NOT_BEEN_FILTER);

    assertFalse(flightsDto.hasFilteredResult);
    assertEquals(flightsDto.listOfFlightDto.get(0).company, FLIGHT_COMPANY);
    assertEquals(flightsDto.listOfFlightDto.get(0).date, FLIGHT_DATE_AS_DATETIME.toString());
    assertEquals(flightsDto.listOfFlightDto.get(0).destination, FLIGHT_DESTINATION);
    assertEquals(flightsDto.listOfFlightDto.get(0).source, FLIGHT_SOURCE);

    assertEquals(flightsDto.listOfFlightDto.get(0).seatsEconomicPrice, ECONOMIC_SEATS_PRICE, 0);
    assertEquals(flightsDto.listOfFlightDto.get(0).seatsEconomicAvailable, NUMBER_ECONOMIC_SEATS_AVAILABLE);
    assertEquals(flightsDto.listOfFlightDto.get(0).numberOfSeatsEconomic, NUMBER_TOTAL_ECONOMIC_SEATS);

    assertEquals(flightsDto.listOfFlightDto.get(0).seatsRegularPrice, REGULAR_SEATS_PRICE, 0);
    assertEquals(flightsDto.listOfFlightDto.get(0).seatsRegularAvailable, NUMBER_REGULAR_SEATS_AVAILABLE);
    assertEquals(flightsDto.listOfFlightDto.get(0).numberOfSeatsRegular, NUMBER_TOTAL_REGULAR_SEATS);

    assertEquals(flightsDto.listOfFlightDto.get(0).seatsBusinessPrice, BUSINESS_SEATS_PRICE, 0);
    assertEquals(flightsDto.listOfFlightDto.get(0).seatsBusinessAvailable, NUMBER_BUSINESS_SEATS_AVAILABLE);
    assertEquals(flightsDto.listOfFlightDto.get(0).numberOfSeatsBusiness, NUMBER_TOTAL_BUSINESS_SEATS);

    assertEquals(flightsDto.listOfFlightDto.get(0).flightCategory, FLIGHT_CATEGORY_HEAVY);
    assertEquals(flightsDto.listOfFlightDto.get(0).exceedingWeightAvailable, EXCEEDING_LUGGAGE_WEIGHT_AVAILABLE, 0);
    assertEquals(flightsDto.listOfFlightDto.get(0).maximumExceedingWeight, MAXIMUM_LUGGAGE_WEIGHT_FOR_HEAVY_FLIGHT, 0);
    assertEquals(flightsDto.listOfFlightDto.get(0).isAirVivant, IS_NOT_AIR_VIVANT);
  }

  @Test
  public void listOfFlightsPricersThatWasFiltered_assemblingFlightsDtoWithExtraFees_flightsDtoIsCreatedAndPriceHasExtraFees() {
    given(passengerFlight.getSource()).willReturn(FLIGHT_SOURCE);
    given(passengerFlight.getDestination()).willReturn(FLIGHT_DESTINATION);
    given(passengerFlight.getDate()).willReturn(FLIGHT_DATE_AS_DATETIME);
    given(passengerFlight.getCompany()).willReturn(FLIGHT_COMPANY);
    given(passengerFlight.getFlightCategory()).willReturn(FLIGHT_CATEGORY_HEAVY);
    given(passengerFlight.getExceedingWeightAvailable()).willReturn(EXCEEDING_LUGGAGE_WEIGHT_AVAILABLE);
    given(passengerFlight.getMaximumExceedingWeight()).willReturn(MAXIMUM_LUGGAGE_WEIGHT_FOR_HEAVY_FLIGHT);
    given(passengerFlight.getExtraFeesForExceedingWeight(LUGGAGE_WEIGHT)).willReturn(EXTRA_FEES);

    given(flightPricer.getPassengerFlight()).willReturn(passengerFlight);
    given(flightPricer.getFlightTotalPrice(SEAT_CATEGORY_ECONOMIC, ONE_TICKET)).willReturn(ECONOMIC_SEATS_PRICE + EXTRA_FEES);
    given(flightPricer.getFlightTotalPrice(SEAT_CATEGORY_REGULAR, ONE_TICKET)).willReturn(REGULAR_SEATS_PRICE + EXTRA_FEES);
    given(flightPricer.getFlightTotalPrice(SEAT_CATEGORY_BUSINESS, ONE_TICKET)).willReturn(BUSINESS_SEATS_PRICE + EXTRA_FEES);

    List<FlightPricer> flights = new ArrayList<>();
    flights.add(flightPricer);

    FlightsDto flightsDto = flightAssembler.assembleFlightsDtoWithExtraFees(flights, HAS_BEEN_FILTER);

    assertTrue(flightsDto.hasFilteredResult);
    assertEquals(flightsDto.listOfFlightDto.get(0).company, FLIGHT_COMPANY);
    assertEquals(flightsDto.listOfFlightDto.get(0).date, FLIGHT_DATE_AS_DATETIME.toString());
    assertEquals(flightsDto.listOfFlightDto.get(0).destination, FLIGHT_DESTINATION);
    assertEquals(flightsDto.listOfFlightDto.get(0).source, FLIGHT_SOURCE);

    assertEquals(flightsDto.listOfFlightDto.get(0).seatsEconomicPrice, ECONOMIC_SEATS_PRICE + EXTRA_FEES, 0);
    assertEquals(flightsDto.listOfFlightDto.get(0).seatsEconomicAvailable, NUMBER_ECONOMIC_SEATS_AVAILABLE);
    assertEquals(flightsDto.listOfFlightDto.get(0).numberOfSeatsEconomic, NUMBER_TOTAL_ECONOMIC_SEATS);

    assertEquals(flightsDto.listOfFlightDto.get(0).seatsRegularPrice, REGULAR_SEATS_PRICE + EXTRA_FEES, 0);
    assertEquals(flightsDto.listOfFlightDto.get(0).seatsRegularAvailable, NUMBER_REGULAR_SEATS_AVAILABLE);
    assertEquals(flightsDto.listOfFlightDto.get(0).numberOfSeatsRegular, NUMBER_TOTAL_REGULAR_SEATS);

    assertEquals(flightsDto.listOfFlightDto.get(0).seatsBusinessPrice, BUSINESS_SEATS_PRICE + EXTRA_FEES, 0);
    assertEquals(flightsDto.listOfFlightDto.get(0).seatsBusinessAvailable, NUMBER_BUSINESS_SEATS_AVAILABLE);
    assertEquals(flightsDto.listOfFlightDto.get(0).numberOfSeatsBusiness, NUMBER_TOTAL_BUSINESS_SEATS);

    assertEquals(flightsDto.listOfFlightDto.get(0).flightCategory, FLIGHT_CATEGORY_HEAVY);
    assertEquals(flightsDto.listOfFlightDto.get(0).exceedingWeightAvailable, EXCEEDING_LUGGAGE_WEIGHT_AVAILABLE, 0);
    assertEquals(flightsDto.listOfFlightDto.get(0).maximumExceedingWeight, MAXIMUM_LUGGAGE_WEIGHT_FOR_HEAVY_FLIGHT, 0);
    assertEquals(flightsDto.listOfFlightDto.get(0).isAirVivant, IS_NOT_AIR_VIVANT);

  }

  @Test
  public void listOfFlightsPricersThatWasNotFiltered_assemblingFlightsDtoWithExtraFees_flightsDtoIsCreatedAndPriceHasExtraFees() {
    given(passengerFlight.getSource()).willReturn(FLIGHT_SOURCE);
    given(passengerFlight.getDestination()).willReturn(FLIGHT_DESTINATION);
    given(passengerFlight.getDate()).willReturn(FLIGHT_DATE_AS_DATETIME);
    given(passengerFlight.getCompany()).willReturn(FLIGHT_COMPANY);
    given(passengerFlight.getFlightCategory()).willReturn(FLIGHT_CATEGORY_HEAVY);
    given(passengerFlight.getExceedingWeightAvailable()).willReturn(EXCEEDING_LUGGAGE_WEIGHT_AVAILABLE);
    given(passengerFlight.getMaximumExceedingWeight()).willReturn(MAXIMUM_LUGGAGE_WEIGHT_FOR_HEAVY_FLIGHT);
    given(passengerFlight.getExtraFeesForExceedingWeight(LUGGAGE_WEIGHT)).willReturn(EXTRA_FEES);

    given(flightPricer.getPassengerFlight()).willReturn(passengerFlight);
    given(flightPricer.getFlightTotalPrice(SEAT_CATEGORY_ECONOMIC, ONE_TICKET)).willReturn(ECONOMIC_SEATS_PRICE + EXTRA_FEES);
    given(flightPricer.getFlightTotalPrice(SEAT_CATEGORY_REGULAR, ONE_TICKET)).willReturn(REGULAR_SEATS_PRICE + EXTRA_FEES);
    given(flightPricer.getFlightTotalPrice(SEAT_CATEGORY_BUSINESS, ONE_TICKET)).willReturn(BUSINESS_SEATS_PRICE + EXTRA_FEES);

    List<FlightPricer> flights = new ArrayList<>();
    flights.add(flightPricer);

    FlightsDto flightsDto = flightAssembler.assembleFlightsDtoWithExtraFees(flights, HAS_NOT_BEEN_FILTER);

    assertFalse(flightsDto.hasFilteredResult);
    assertEquals(flightsDto.listOfFlightDto.get(0).company, FLIGHT_COMPANY);
    assertEquals(flightsDto.listOfFlightDto.get(0).date, FLIGHT_DATE_AS_DATETIME.toString());
    assertEquals(flightsDto.listOfFlightDto.get(0).destination, FLIGHT_DESTINATION);
    assertEquals(flightsDto.listOfFlightDto.get(0).source, FLIGHT_SOURCE);

    assertEquals(flightsDto.listOfFlightDto.get(0).seatsEconomicPrice, ECONOMIC_SEATS_PRICE + EXTRA_FEES, 0);
    assertEquals(flightsDto.listOfFlightDto.get(0).seatsEconomicAvailable, NUMBER_ECONOMIC_SEATS_AVAILABLE);
    assertEquals(flightsDto.listOfFlightDto.get(0).numberOfSeatsEconomic, NUMBER_TOTAL_ECONOMIC_SEATS);

    assertEquals(flightsDto.listOfFlightDto.get(0).seatsRegularPrice, REGULAR_SEATS_PRICE + EXTRA_FEES, 0);
    assertEquals(flightsDto.listOfFlightDto.get(0).seatsRegularAvailable, NUMBER_REGULAR_SEATS_AVAILABLE);
    assertEquals(flightsDto.listOfFlightDto.get(0).numberOfSeatsRegular, NUMBER_TOTAL_REGULAR_SEATS);

    assertEquals(flightsDto.listOfFlightDto.get(0).seatsBusinessPrice, BUSINESS_SEATS_PRICE + EXTRA_FEES, 0);
    assertEquals(flightsDto.listOfFlightDto.get(0).seatsBusinessAvailable, NUMBER_BUSINESS_SEATS_AVAILABLE);
    assertEquals(flightsDto.listOfFlightDto.get(0).numberOfSeatsBusiness, NUMBER_TOTAL_BUSINESS_SEATS);

    assertEquals(flightsDto.listOfFlightDto.get(0).flightCategory, FLIGHT_CATEGORY_HEAVY);
    assertEquals(flightsDto.listOfFlightDto.get(0).exceedingWeightAvailable, EXCEEDING_LUGGAGE_WEIGHT_AVAILABLE, 0);
    assertEquals(flightsDto.listOfFlightDto.get(0).maximumExceedingWeight, MAXIMUM_LUGGAGE_WEIGHT_FOR_HEAVY_FLIGHT, 0);
    assertEquals(flightsDto.listOfFlightDto.get(0).isAirVivant, IS_NOT_AIR_VIVANT);

  }

  @Test
  public void aListOfPassengerFlightAndAListOfCargoFlight_assemblingAllFlightsDto_returnListContainingFlightsDtoOfBothLists() {
    given(passengerFlight.getFlightCategory()).willReturn(FLIGHT_CATEGORY_HEAVY);
    given(passengerFlight.getExceedingWeightAvailable()).willReturn(EXCEEDING_LUGGAGE_WEIGHT_AVAILABLE);
    given(passengerFlight.getMaximumExceedingWeight()).willReturn(MAXIMUM_LUGGAGE_WEIGHT_FOR_HEAVY_FLIGHT);

    given(cargoFlight.getSource()).willReturn(CARGO_FLIGHT_SOURCE);
    given(cargoFlight.getDestination()).willReturn(CARGO_FLIGHT_DESTINATION);
    given(cargoFlight.getDate()).willReturn(CARGO_FLIGHT_DATE_AS_DATETIME);
    given(cargoFlight.getCompany()).willReturn(CARGO_FLIGHT_COMPANY);
    given(cargoFlight.getMaximumWeight()).willReturn(MAXIMUM_LUGGAGE_WEIGHT_FOR_HEAVY_FLIGHT);

    List<PassengerFlight> passengerFlights = new ArrayList<>();
    passengerFlights.add(passengerFlight);
    List<CargoFlight> cargoFlights = new ArrayList<>();
    cargoFlights.add(cargoFlight);

    FlightsDto flightsDto = flightAssembler.assembleAllFlightsDto(passengerFlights, cargoFlights);

    assertEquals(flightsDto.listOfFlightDto.get(0).company, FLIGHT_COMPANY);
    assertEquals(flightsDto.listOfFlightDto.get(0).date, FLIGHT_DATE_AS_DATETIME.toString());
    assertEquals(flightsDto.listOfFlightDto.get(0).destination, FLIGHT_DESTINATION);
    assertEquals(flightsDto.listOfFlightDto.get(0).source, FLIGHT_SOURCE);

    assertEquals(flightsDto.listOfFlightDto.get(0).seatsEconomicPrice, ECONOMIC_SEATS_PRICE, 0);
    assertEquals(flightsDto.listOfFlightDto.get(0).seatsEconomicAvailable, NUMBER_ECONOMIC_SEATS_AVAILABLE);
    assertEquals(flightsDto.listOfFlightDto.get(0).numberOfSeatsEconomic, NUMBER_TOTAL_ECONOMIC_SEATS);

    assertEquals(flightsDto.listOfFlightDto.get(0).seatsRegularPrice, REGULAR_SEATS_PRICE, 0);
    assertEquals(flightsDto.listOfFlightDto.get(0).seatsRegularAvailable, NUMBER_REGULAR_SEATS_AVAILABLE);
    assertEquals(flightsDto.listOfFlightDto.get(0).numberOfSeatsRegular, NUMBER_TOTAL_REGULAR_SEATS);

    assertEquals(flightsDto.listOfFlightDto.get(0).seatsBusinessPrice, BUSINESS_SEATS_PRICE, 0);
    assertEquals(flightsDto.listOfFlightDto.get(0).seatsBusinessAvailable, NUMBER_BUSINESS_SEATS_AVAILABLE);
    assertEquals(flightsDto.listOfFlightDto.get(0).numberOfSeatsBusiness, NUMBER_TOTAL_BUSINESS_SEATS);

    assertEquals(flightsDto.listOfFlightDto.get(0).flightCategory, FLIGHT_CATEGORY_HEAVY);
    assertEquals(flightsDto.listOfFlightDto.get(0).exceedingWeightAvailable, EXCEEDING_LUGGAGE_WEIGHT_AVAILABLE, 0);
    assertEquals(flightsDto.listOfFlightDto.get(0).maximumExceedingWeight, MAXIMUM_LUGGAGE_WEIGHT_FOR_HEAVY_FLIGHT, 0);
    assertEquals(flightsDto.listOfFlightDto.get(0).isAirVivant, IS_NOT_AIR_VIVANT);

    assertEquals(flightsDto.listOfFlightDto.get(1).company, CARGO_FLIGHT_COMPANY);
    assertEquals(flightsDto.listOfFlightDto.get(1).date, CARGO_FLIGHT_DATE_AS_DATETIME.toString());
    assertEquals(flightsDto.listOfFlightDto.get(1).destination, CARGO_FLIGHT_DESTINATION);
    assertEquals(flightsDto.listOfFlightDto.get(1).source, CARGO_FLIGHT_SOURCE);
    assertEquals(flightsDto.listOfFlightDto.get(1).maximumExceedingWeight, MAXIMUM_LUGGAGE_WEIGHT_FOR_HEAVY_FLIGHT, 0);
  }

}