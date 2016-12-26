package ca.ulaval.glo4003.quickflyws.api.flight;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import javax.ws.rs.WebApplicationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ca.ulaval.glo4003.quickflyws.domain.generalExceptions.BadRequestException;
import ca.ulaval.glo4003.quickflyws.domain.generalExceptions.NotFoundException;
import ca.ulaval.glo4003.quickflyws.dto.FlightDto;
import ca.ulaval.glo4003.quickflyws.dto.FlightInfosToUpdateDto;
import ca.ulaval.glo4003.quickflyws.dto.FlightSearchInfos;
import ca.ulaval.glo4003.quickflyws.dto.FlightsDto;
import ca.ulaval.glo4003.quickflyws.service.flight.FlightService;
import jersey.repackaged.com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class FlightResourceApiTest {

  private static final String FLIGHT_SOURCE = "source";
  private static final String FLIGHT_DESTINATION = "destination";
  private static final String FLIGHT_DATE = "2016-01-01";
  private static final String FLIGHT_COMPANY = "AirGras";
  
  private static final boolean IS_AIR_VIVANT = true;
  private static final boolean IS_AIR_CARGO = true;
  private static final boolean IS_ECONOMIC = true;
  private static final boolean IS_REGULAR = true;
  private static final boolean IS_BUSINESS = true;

  private static final double LUGGAGE_WEIGHT = 23.5;
  private static final double NEW_LUGGAGE_WEIGHT = 40.00;

  @Mock
  private FlightService flightService;

  private FlightDto flightDto;

  private FlightsDto flightsDto;

  private FlightInfosToUpdateDto flightInfosToUpdateDto;

  private FlightResource flightResource;

  @Before
  public void setUp() {
    flightDto = new FlightDto();
    flightsDto = new FlightsDto();
    createFlightToUpdateDto();
    flightResource = new FlightResourceApi(flightService);
    flightsDto.listOfFlightDto = Lists.newArrayList(flightDto);
  }

  @Test
  public void validFlight_gettingFlight_delegateToFlightService() {
    flightResource.getFlight(FLIGHT_COMPANY, FLIGHT_DATE, FLIGHT_DESTINATION, FLIGHT_SOURCE);

    verify(flightService).findFlight(FLIGHT_COMPANY, FLIGHT_DATE, FLIGHT_DESTINATION, FLIGHT_SOURCE);
  }

  @Test
  public void validFlight_gettingFlight_returnsCorrespondingFlight() {
    given(flightService.findFlight(FLIGHT_COMPANY, FLIGHT_DATE, FLIGHT_DESTINATION, FLIGHT_SOURCE)).willReturn(flightDto);

    flightResource.getFlight(FLIGHT_COMPANY, FLIGHT_DATE, FLIGHT_DESTINATION, FLIGHT_SOURCE);

    assertEquals(flightDto, flightDto);
  }

  @Test(expected = WebApplicationException.class)
  public void notFoundException_gettingFlight_throwWebApplication() {
    willThrow(new NotFoundException("This flight does not exist.")).given(flightService).findFlight(FLIGHT_COMPANY, FLIGHT_DATE, FLIGHT_DESTINATION,
        FLIGHT_SOURCE);

    flightResource.getFlight(FLIGHT_COMPANY, FLIGHT_DATE, FLIGHT_DESTINATION, FLIGHT_SOURCE);
  }

  @Test
  public void validFlightInfos_gettingFlights_delegateToFlightService() {
    flightResource.getFlights(FLIGHT_DATE, FLIGHT_DESTINATION, FLIGHT_SOURCE, LUGGAGE_WEIGHT, IS_AIR_VIVANT, IS_AIR_CARGO, IS_ECONOMIC, IS_REGULAR,
        IS_BUSINESS);

    verify(flightService).findFlights(any(FlightSearchInfos.class));
  }

  @Test
  public void validFlightInfos_gettingFlights_returnAllFlightsWithMatchingInfos() {

    given(flightService.findFlights(any(FlightSearchInfos.class))).willReturn(flightsDto);

    flightResource.getFlights(FLIGHT_DATE, FLIGHT_DESTINATION, FLIGHT_SOURCE, LUGGAGE_WEIGHT, IS_AIR_VIVANT, IS_AIR_CARGO, IS_ECONOMIC, IS_REGULAR,
        IS_BUSINESS);

    assertThat(flightsDto.listOfFlightDto, org.hamcrest.Matchers.hasItem(flightDto));
  }

  @Test(expected = WebApplicationException.class)
  public void notFoundException_gettingFlights_throwWebApplication() {
    willThrow(new NotFoundException("This flight does not exist.")).given(flightService).findFlights(any(FlightSearchInfos.class));

    flightResource.getFlights(FLIGHT_DATE, FLIGHT_DESTINATION, FLIGHT_SOURCE, LUGGAGE_WEIGHT, IS_AIR_VIVANT, IS_AIR_CARGO, IS_ECONOMIC, IS_REGULAR,
        IS_BUSINESS);
  }

  @Test
  public void requestToGetAllFlights_gettingAllFlights_delegateToFlightService() {
    flightResource.getAllFlights();

    verify(flightService).findAllFlights();
  }

  @Test
  public void requestToGetAllFlights_gettingAllFlights_returnAllFlights() {
    given(flightService.findAllFlights()).willReturn(flightsDto);

    flightResource.getAllFlights();

    assertThat(flightsDto.listOfFlightDto, org.hamcrest.Matchers.hasItem(flightDto));
  }

  @Test(expected = WebApplicationException.class)
  public void notFoundException_gettingAllFlights_throwWebApplication() {
    willThrow(new NotFoundException("This flight does not exist.")).given(flightService).findAllFlights();

    flightResource.getAllFlights();
  }

  @Test
  public void requestAirLourdWeightCategoryFlights_gettingAirLourdWeightCategoryFlights_delegateToFlightService() {
    flightResource.getAirLourdWeightCategoryFlights();

    verify(flightService).findAirLourdWeightCategoryFlights();
  }

  @Test
  public void requestAirLourdWeightCategoryFlights_gettingAirLourdWeightCategoryFlights_returnAllAirLourd() {
    given(flightService.findAirLourdWeightCategoryFlights()).willReturn(flightsDto);

    flightResource.getAirLourdWeightCategoryFlights();

    assertThat(flightsDto.listOfFlightDto, org.hamcrest.Matchers.hasItem(flightDto));
  }

  @Test(expected = WebApplicationException.class)
  public void notFoundException_gettingAirLourdWeightCategoryFlights_throwWebApplication() {
    willThrow(new NotFoundException("This flight does not exist.")).given(flightService).findAirLourdWeightCategoryFlights();

    flightResource.getAirLourdWeightCategoryFlights();
  }

  @Test
  public void flightToUpdateInfos_updatingFlight_delegateToFlightService() {
    flightResource.updateFlight(flightInfosToUpdateDto);

    verify(flightService).updateFlightParameters(flightInfosToUpdateDto);
  }

  @Test(expected = WebApplicationException.class)
  public void notFoundException_updatingFlight_throwWebApplication() {
    willThrow(new NotFoundException("This flight does not exist.")).given(flightService).updateFlightParameters(flightInfosToUpdateDto);

    flightResource.updateFlight(flightInfosToUpdateDto);
  }

  @Test(expected = WebApplicationException.class)
  public void badRequestException_updatingFlight_throwWebApplication() {
    willThrow(new BadRequestException("Can't do these changes")).given(flightService).updateFlightParameters(flightInfosToUpdateDto);

    flightResource.updateFlight(flightInfosToUpdateDto);
  }

  private void createFlightToUpdateDto() {
    flightInfosToUpdateDto = new FlightInfosToUpdateDto();
    flightInfosToUpdateDto.company = FLIGHT_COMPANY;
    flightInfosToUpdateDto.date = FLIGHT_DATE;
    flightInfosToUpdateDto.destination = FLIGHT_DESTINATION;
    flightInfosToUpdateDto.maximumExceedingWeight = NEW_LUGGAGE_WEIGHT;
    flightInfosToUpdateDto.source = FLIGHT_SOURCE;
  }

}