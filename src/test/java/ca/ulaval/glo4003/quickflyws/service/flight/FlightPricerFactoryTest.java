package ca.ulaval.glo4003.quickflyws.service.flight;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.PassengerFlight;

@RunWith(MockitoJUnitRunner.class)
public class FlightPricerFactoryTest {
  
  private static final boolean HAS_AIR_CARGO = true;
  
  private static final double LUGGAGE_WEIGHT = 40.00;

  @Mock
  private PassengerFlight passengerFlight;

  private FlightPricerFactory flightPricerFactory;

  @Before
  public void setUp() {
    flightPricerFactory = new FlightPricerFactory();
  }

  @Test
  public void flightWithLuggageWeight_creatingFlightPricerWhileAllowingAirCargo_createFlightPricer() {
    FlightPricer flightPricer = flightPricerFactory.createFlightPricer(passengerFlight, LUGGAGE_WEIGHT, HAS_AIR_CARGO);

    assertEquals(flightPricer.getPassengerFlight(), passengerFlight);
    assertEquals(flightPricer.getLuggageWeight(), LUGGAGE_WEIGHT, 0);
    assertEquals(flightPricer.isAirCargoAllowed(), HAS_AIR_CARGO);
  }

  @Test
  public void listOfFlightWithLuggageWeight_creatingFlightPricersWhileAllowingAirCargo_createListOfFlightPricer() {
    List<PassengerFlight> passengerFlights = new ArrayList<>();
    passengerFlights.add(passengerFlight);

    List<FlightPricer> flightPricers = flightPricerFactory.createFlightPricers(passengerFlights, LUGGAGE_WEIGHT, HAS_AIR_CARGO);

    assertEquals(flightPricers.get(0).getPassengerFlight(), passengerFlight);
    assertEquals(flightPricers.get(0).getLuggageWeight(), LUGGAGE_WEIGHT, 0);
    assertEquals(flightPricers.get(0).isAirCargoAllowed(), HAS_AIR_CARGO);
  }
  
}