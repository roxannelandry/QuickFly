package ca.ulaval.glo4003.quickflyws.api.localization;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import ca.ulaval.glo4003.quickflyws.dto.LocalizationInfoDto;

@Path("/quickfly/localization")
public interface CityLocator {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  LocalizationInfoDto cityLocator(@QueryParam("localizationInfo") String localizationInfo);

}