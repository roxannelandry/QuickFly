package ca.ulaval.glo4003.quickflyws.api.flight;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import ca.ulaval.glo4003.quickflyws.dto.FlightDto;
import ca.ulaval.glo4003.quickflyws.dto.FlightInfosToUpdateDto;
import ca.ulaval.glo4003.quickflyws.dto.FlightsDto;

@Path("/quickfly/flights")
public interface FlightResource {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/flight")
  FlightDto getFlight(@QueryParam("company") String company, @QueryParam("date") String date, @QueryParam("destination") String destination,
      @QueryParam("source") String source);

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/search")
  FlightsDto getFlights(@QueryParam("date") String date, @QueryParam("destination") String destination, @QueryParam("source") String source,
      @QueryParam("luggageWeight") double luggageWeight, @QueryParam("wantAirVivant") boolean wantAirVivant,
      @QueryParam("airCargoAllowed") boolean airCargoAllowed, @QueryParam("wantEconomic") boolean wantEconomic,
      @QueryParam("wantRegular") boolean wantRegular, @QueryParam("wantBusiness") boolean wantBusiness);

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  FlightsDto getAllFlights();

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/AirLourd")
  FlightsDto getAirLourdWeightCategoryFlights();

  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("/updateFlight")
  void updateFlight(FlightInfosToUpdateDto flightToUpdateDto);

}