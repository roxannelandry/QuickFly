package ca.ulaval.glo4003.quickflyws.api.weightScale;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/quickfly/weightsystem")
public interface WeightScale {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  String autoDetectWeight();
  
}