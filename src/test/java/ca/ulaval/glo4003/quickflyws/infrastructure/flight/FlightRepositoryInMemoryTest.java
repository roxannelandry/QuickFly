package ca.ulaval.glo4003.quickflyws.infrastructure.flight;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;

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
import ca.ulaval.glo4003.quickflyws.dto.FlightsDto;

@RunWith(MockitoJUnitRunner.class)
public class FlightRepositoryInMemoryTest {

  private static final String FLIGHT_SOURCE = "source";
  private static final String FLIGHT_DESTINATION = "destination";
  private static final String FLIGHT_COMPANY = "company";
  private static final String WRONG_DESTINATION = "";
  
  private static final LocalDateTime FLIGHT_DATE_AS_DATETIME = LocalDateTime.of(2016, 01, 01, 13, 00);
  
  private static final boolean IS_NOT_AIR_VIVANT = false;

  private static final int VALID_HASH = 1;

  @Mock
  private PassengerFlight passengerFlight;

  @Mock
  private CargoFlight cargoFlight;

  @Mock
  private FlightsDto flightSearchResultDto;

  @Mock
  private FlightHashGenerator flightHashGenerator;

  private FlightRepositoryInMemory flightRepositoryInMemory;

  @Before
  public void setUp() {
    given(flightHashGenerator.generateFlightHash(FLIGHT_COMPANY, FLIGHT_DATE_AS_DATETIME, FLIGHT_SOURCE, FLIGHT_SOURCE)).willReturn(VALID_HASH);
    flightRepositoryInMemory = new FlightRepositoryInMemory(flightHashGenerator);
  }

  @Test
  public void newPassengerFlight_savePassengerFlight_passengerFlightAddedToMemory() {
    given(passengerFlight.getSource()).willReturn(FLIGHT_SOURCE);
    given(passengerFlight.getDestination()).willReturn(FLIGHT_DESTINATION);
    given(passengerFlight.getDate()).willReturn(FLIGHT_DATE_AS_DATETIME);
    given(passengerFlight.getCompany()).willReturn(FLIGHT_COMPANY);
    given(passengerFlight.getFlightCategory()).willReturn(WeightCategory.AIR_LEGER);

    flightRepositoryInMemory.savePassengerFlight(passengerFlight);
    PassengerFlight passengerFlightAdded = flightRepositoryInMemory.findPassengerFlight(FLIGHT_COMPANY, FLIGHT_DATE_AS_DATETIME, FLIGHT_DESTINATION,
        FLIGHT_SOURCE);

    assertEquals(passengerFlight, passengerFlightAdded);
  }

  @Test
  public void newCargoFlight_savingCargoFlight_cargoFlightAddedToMemory() {
    given(cargoFlight.areInformationsMatching(FLIGHT_COMPANY, FLIGHT_DATE_AS_DATETIME, FLIGHT_DESTINATION, FLIGHT_SOURCE)).willReturn(true);

    flightRepositoryInMemory.saveCargoFlight(cargoFlight);
    List<CargoFlight> cargoFlightAdded = flightRepositoryInMemory.findCargoFlights(FLIGHT_COMPANY, FLIGHT_DATE_AS_DATETIME, FLIGHT_DESTINATION,
        FLIGHT_SOURCE);

    assertThat(cargoFlightAdded, org.hamcrest.Matchers.hasItem(cargoFlight));
  }

  @Test
  public void passengerFlightInfos_findingFlight_returnFlightInMemory() {
    setUpFlight(WeightCategory.AIR_LEGER);

    PassengerFlight passengerFlightFound = flightRepositoryInMemory.findPassengerFlight(FLIGHT_COMPANY, FLIGHT_DATE_AS_DATETIME, FLIGHT_DESTINATION,
        FLIGHT_SOURCE);

    assertEquals(passengerFlight, passengerFlightFound);
  }

  @Test(expected = FlightNotFoundException.class)
  public void passengerFlightInfos_findingFlight_throwFlightNotFound() {
    flightRepositoryInMemory.findPassengerFlight(FLIGHT_COMPANY, FLIGHT_DATE_AS_DATETIME, WRONG_DESTINATION, FLIGHT_SOURCE);
  }

  @Test
  public void cargoflightInfos_findingCargoFlight_returnCargoFlightInMemory() {
    flightRepositoryInMemory.saveCargoFlight(cargoFlight);

    CargoFlight cargoFlightFound = flightRepositoryInMemory.findCargoFlight(FLIGHT_COMPANY, FLIGHT_DATE_AS_DATETIME, FLIGHT_DESTINATION,
        FLIGHT_SOURCE);

    assertEquals(cargoFlight, cargoFlightFound);
  }

  @Test(expected = FlightNotFoundException.class)
  public void cargoflightInfos_findingCargoFlight_throwFlightNotFound() {
    flightRepositoryInMemory.findCargoFlight(FLIGHT_COMPANY, FLIGHT_DATE_AS_DATETIME, WRONG_DESTINATION, FLIGHT_SOURCE);
  }

  @Test
  public void nonExistingPassengerFlightInfos_findingFlights_returnNoFlight() {
    List<PassengerFlight> passengerflights = new ArrayList<PassengerFlight>();
    flightRepositoryInMemory.savePassengerFlight(passengerFlight);
    given(passengerFlight.areInformationsMatching(FLIGHT_DATE_AS_DATETIME, FLIGHT_DESTINATION, FLIGHT_SOURCE)).willReturn(false);

    passengerflights = flightRepositoryInMemory.findPassengerFlights(FLIGHT_DATE_AS_DATETIME, FLIGHT_DESTINATION, FLIGHT_SOURCE);

    assertTrue(passengerflights.isEmpty());
  }

  @Test
  public void passengerFlightInfos_findingFlights_returnFlights() {
    List<PassengerFlight> passengerflights = new ArrayList<PassengerFlight>();
    flightRepositoryInMemory.savePassengerFlight(passengerFlight);
    given(passengerFlight.areInformationsMatching(FLIGHT_DATE_AS_DATETIME, FLIGHT_DESTINATION, FLIGHT_SOURCE)).willReturn(true);

    passengerflights = flightRepositoryInMemory.findPassengerFlights(FLIGHT_DATE_AS_DATETIME, FLIGHT_DESTINATION, FLIGHT_SOURCE);

    assertThat(passengerflights, org.hamcrest.Matchers.hasItem(passengerFlight));
  }

  @Test
  public void noPassengerFlightSaved_findingAllFlights_returnNoFlight() {
    List<PassengerFlight> passengerflights = flightRepositoryInMemory.findAllPassengerFlights();

    assertTrue(passengerflights.isEmpty());
  }

  @Test
  public void somePassengerFlightSaved_findingAllFlights_returnAllFlightsSaved() {
    List<PassengerFlight> passengerflights = new ArrayList<PassengerFlight>();
    flightRepositoryInMemory.savePassengerFlight(passengerFlight);

    passengerflights = flightRepositoryInMemory.findAllPassengerFlights();

    assertThat(passengerflights, org.hamcrest.Matchers.hasItem(passengerFlight));
  }

  @Test
  public void noPassengerFlightSaved_findingAirLourdWeightCategoryFlights_returnNoFlight() {
    List<PassengerFlight> passengerflights = flightRepositoryInMemory.findAirLourdWeightCategoryPassengerFlights();

    assertTrue(passengerflights.isEmpty());
  }

  @Test
  public void somePassengerFlightSaved_findingAirLourdWeightCategoryFlights_returnOnlyAirLourdCategoryFlights() {
    List<PassengerFlight> passengerflights = new ArrayList<PassengerFlight>();
    flightRepositoryInMemory.savePassengerFlight(passengerFlight);
    willReturn(WeightCategory.AIR_LOURD).given(passengerFlight).getFlightCategory();

    passengerflights = flightRepositoryInMemory.findAirLourdWeightCategoryPassengerFlights();

    assertThat(passengerflights, org.hamcrest.Matchers.hasItem(passengerFlight));
  }

  @Test
  public void onlyLightWeightPassengerFlightSaved_findingAirLourdWeightCategoryFlights_returnNoFlights() {
    List<PassengerFlight> passengerflights = new ArrayList<PassengerFlight>();
    flightRepositoryInMemory.savePassengerFlight(passengerFlight);
    willReturn(WeightCategory.AIR_LEGER).given(passengerFlight).getFlightCategory();

    passengerflights = flightRepositoryInMemory.findAirLourdWeightCategoryPassengerFlights();

    assertTrue(passengerflights.isEmpty());
  }

  @Test
  public void onlyMediumWeightPassengerFlightSaved_findingAirLourdWeightCategoryFlights_returnNoFlights() {
    List<PassengerFlight> passengerflights = new ArrayList<PassengerFlight>();
    flightRepositoryInMemory.savePassengerFlight(passengerFlight);
    willReturn(WeightCategory.AIR_MOYEN).given(passengerFlight).getFlightCategory();

    passengerflights = flightRepositoryInMemory.findAirLourdWeightCategoryPassengerFlights();

    assertTrue(passengerflights.isEmpty());
  }

  @Test
  public void passengerFlightDate_findingCargoFlights_returnAllCargoFlightsThatRespect3DaysDelay() {
    given(cargoFlight.areInformationsMatching(FLIGHT_COMPANY, FLIGHT_DATE_AS_DATETIME, FLIGHT_DESTINATION, FLIGHT_SOURCE)).willReturn(true);

    List<CargoFlight> cargoFlights = new ArrayList<CargoFlight>();
    flightRepositoryInMemory.saveCargoFlight(cargoFlight);

    cargoFlights = flightRepositoryInMemory.findCargoFlights(FLIGHT_COMPANY, FLIGHT_DATE_AS_DATETIME, FLIGHT_DESTINATION, FLIGHT_SOURCE);

    assertThat(cargoFlights, org.hamcrest.Matchers.hasItem(cargoFlight));
  }

  @Test
  public void passengerFlightDateWithNoCargoAssociated_findingCargoFlights_returnNoCargoFlight() {
    given(cargoFlight.areInformationsMatching(FLIGHT_COMPANY, FLIGHT_DATE_AS_DATETIME, FLIGHT_DESTINATION, FLIGHT_SOURCE)).willReturn(false);

    List<CargoFlight> cargoFlights = new ArrayList<CargoFlight>();
    flightRepositoryInMemory.saveCargoFlight(cargoFlight);

    cargoFlights = flightRepositoryInMemory.findCargoFlights(FLIGHT_COMPANY, FLIGHT_DATE_AS_DATETIME, FLIGHT_DESTINATION, FLIGHT_SOURCE);

    assertTrue(cargoFlights.isEmpty());
  }

  @Test
  public void noCargoFlightSaved_findingAllCargoFlights_returnNoFlight() {
    List<CargoFlight> cargoFlights = flightRepositoryInMemory.findAllCargoFlights();

    assertTrue(cargoFlights.isEmpty());
  }

  @Test
  public void someCargoFlightSaved_findingAllCargoFlights_returnAllFlightsSaved() {
    List<CargoFlight> cargoFlights = new ArrayList<CargoFlight>();
    flightRepositoryInMemory.saveCargoFlight(cargoFlight);

    cargoFlights = flightRepositoryInMemory.findAllCargoFlights();

    assertThat(cargoFlights, org.hamcrest.Matchers.hasItem(cargoFlight));
  }

  private void setUpFlight(WeightCategory flightCategory) {
    given(passengerFlight.getSource()).willReturn(FLIGHT_SOURCE);
    given(passengerFlight.getDestination()).willReturn(FLIGHT_DESTINATION);
    given(passengerFlight.getDate()).willReturn(FLIGHT_DATE_AS_DATETIME);
    given(passengerFlight.getFlightCategory()).willReturn(flightCategory);
    given(passengerFlight.isAirVivant()).willReturn(IS_NOT_AIR_VIVANT);

    flightRepositoryInMemory.savePassengerFlight(passengerFlight);
  }

}