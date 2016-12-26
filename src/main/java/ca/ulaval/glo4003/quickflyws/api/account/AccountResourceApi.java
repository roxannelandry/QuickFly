package ca.ulaval.glo4003.quickflyws.api.account;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import ca.ulaval.glo4003.quickflyws.domain.generalExceptions.BadRequestException;
import ca.ulaval.glo4003.quickflyws.domain.generalExceptions.ConflictException;
import ca.ulaval.glo4003.quickflyws.domain.generalExceptions.NotFoundException;
import ca.ulaval.glo4003.quickflyws.dto.AccountDto;
import ca.ulaval.glo4003.quickflyws.service.account.AccountService;

public class AccountResourceApi implements AccountResource {

  private AccountService accountService;

  public AccountResourceApi(AccountService accountService) {
    this.accountService = accountService;
  }

  @Override
  public AccountDto getAccount(String email) {
    try {
      return accountService.findAccount(email);

    } catch (NotFoundException e) {
      throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build());
    }
  }

  @Override
  public void addAccount(AccountDto accountDto) {
    try {
      accountService.addAccount(accountDto);

    } catch (BadRequestException e) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build());
    
    } catch (ConflictException e) {
      throw new WebApplicationException(Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build());
    }
  }

  @Override
  public AccountDto verifyLoginInfos(AccountDto accountDto) {
    try {
      return accountService.verifyLoginInfos(accountDto);

    } catch (NotFoundException e) {
      throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build());
    }
  }

}