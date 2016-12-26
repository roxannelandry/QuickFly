package ca.ulaval.glo4003.quickflyws.api.account;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ca.ulaval.glo4003.quickflyws.dto.AccountDto;

@Path("/quickfly/accounts")
public interface AccountResource {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("{email}")
  AccountDto getAccount(@PathParam("email") String email);

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  void addAccount(AccountDto accountDto);

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("/login")
  AccountDto verifyLoginInfos(AccountDto accountDto);

}