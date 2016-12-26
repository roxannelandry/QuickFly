package ca.ulaval.glo4003.quickflyws.api.flight;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import ca.ulaval.glo4003.quickflyws.domain.generalExceptions.BadRequestException;
import ca.ulaval.glo4003.quickflyws.domain.generalExceptions.NotFoundException;
import ca.ulaval.glo4003.quickflyws.dto.FlightDto;
import ca.ulaval.glo4003.quickflyws.dto.FlightInfosToUpdateDto;
import ca.ulaval.glo4003.quickflyws.dto.FlightSearchInfos;
import ca.ulaval.glo4003.quickflyws.dto.FlightsDto;
import ca.ulaval.glo4003.quickflyws.service.flight.FlightService;

public class FlightResourceApi implements FlightResource {

  private FlightService flightService;

  public FlightResourceApi(FlightService flightService) {
    this.flightService = flightService;
  }

  @Override
  public FlightDto getFlight(String company, String date, String destination, String source) {
    try {
      return flightService.findFlight(company, date, destination, source);

    } catch (NotFoundException e) {
      throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build());
    }
  }

  @Override
  public FlightsDto getFlights(String date, String destination, String source, double luggageWeight, boolean wantAirVivant, boolean airCargoAllowed,
      boolean wantEconomic, boolean wantRegular, boolean wantBusiness) {
    FlightSearchInfos flightSearchInfos = createFlightSearchInfos(date, destination, source, luggageWeight, wantAirVivant, airCargoAllowed,
        wantEconomic, wantRegular, wantBusiness);

    try {
      return flightService.findFlights(flightSearchInfos);

    } catch (NotFoundException e) {
      throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build());
    }
  }

  private FlightSearchInfos createFlightSearchInfos(String date, String destination, String source, double luggageWeight, boolean wantAirVivant,
      boolean airCargoAllowed, boolean wantEconomic, boolean wantRegular, boolean wantBusiness) {
    return new FlightSearchInfos(date, destination, source, luggageWeight, wantAirVivant, airCargoAllowed, wantEconomic, wantRegular, wantBusiness);

  }

  @Override
  public FlightsDto getAllFlights() {
    try {
      return flightService.findAllFlights();

    } catch (NotFoundException e) {
      throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build());
    }
  }

  @Override
  public FlightsDto getAirLourdWeightCategoryFlights() {
    try {
      return flightService.findAirLourdWeightCategoryFlights();

    } catch (NotFoundException e) {
      throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build());
    }
  }

  @Override
  public void updateFlight(FlightInfosToUpdateDto flightInfosToUpdateDto) {
    try {
      flightService.updateFlightParameters(flightInfosToUpdateDto);

    } catch (NotFoundException e) {
      throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build());

    } catch (BadRequestException e) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build());
    }
  }

}