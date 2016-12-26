package ca.ulaval.glo4003.quickflyws.api.account;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

import javax.ws.rs.WebApplicationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ca.ulaval.glo4003.quickflyws.domain.account.exceptions.AccountNotFoundException;
import ca.ulaval.glo4003.quickflyws.domain.generalExceptions.BadRequestException;
import ca.ulaval.glo4003.quickflyws.domain.generalExceptions.ConflictException;
import ca.ulaval.glo4003.quickflyws.dto.AccountDto;
import ca.ulaval.glo4003.quickflyws.service.account.AccountService;

@RunWith(MockitoJUnitRunner.class)
public class AccountResourceApiTest {

  private static final String EXISTING_ACCOUNT_EMAIL = "email";
  private static final String NOT_EXISTING_ACCOUNT_EMAIL = "non_existen_account";

  @Mock
  private AccountService accountService;

  private AccountDto existingAccountDto;

  private AccountDto requestedAccountDto;

  private AccountResource accountResource;

  @Before
  public void setUp() {
    existingAccountDto = new AccountDto();
    requestedAccountDto = new AccountDto();
    accountResource = new AccountResourceApi(accountService);
  }

  @Test
  public void validAccountEmail_gettingAccount_delegateToGetAccountFromAccountService() {
    accountResource.getAccount(EXISTING_ACCOUNT_EMAIL);

    verify(accountService).findAccount(EXISTING_ACCOUNT_EMAIL);
  }

  @Test
  public void validAccountEmail_gettingAccount_returnAccountDto() {
    given(accountService.findAccount(EXISTING_ACCOUNT_EMAIL)).willReturn(existingAccountDto);

    AccountDto returnedAccountDto = accountResource.getAccount(EXISTING_ACCOUNT_EMAIL);

    assertEquals(existingAccountDto, returnedAccountDto);
  }

  @Test(expected = WebApplicationException.class)
  public void notExistingAccount_gettingAccount_throwWebApplication() {
    willThrow(AccountNotFoundException.class).given(accountService).findAccount(NOT_EXISTING_ACCOUNT_EMAIL);

    accountResource.getAccount(NOT_EXISTING_ACCOUNT_EMAIL);
  }

  @Test
  public void newAccount_addingAccount_delegateToService() {
    accountResource.addAccount(existingAccountDto);

    verify(accountService).addAccount(existingAccountDto);
  }

  @Test(expected = WebApplicationException.class)
  public void invalidAccountInfos_addingAccount_throwWebApplication() {
    willThrow(new BadRequestException("Invalid informations")).given(accountService).addAccount(existingAccountDto);

    accountResource.addAccount(existingAccountDto);
  }

  @Test(expected = WebApplicationException.class)
  public void accountAlreadyExists_addingAccount_throwWebApplication() {
    willThrow(new ConflictException("Account already exist")).given(accountService).addAccount(existingAccountDto);

    accountResource.addAccount(existingAccountDto);
  }

  @Test
  public void validAccountDto_verifyLoginInfos_delegateToVerifyLoginInfosFromAccountService() {
    accountResource.verifyLoginInfos(existingAccountDto);

    verify(accountService).verifyLoginInfos(existingAccountDto);
  }

  @Test
  public void validAccountDto_verifyLoginInfos_returnAccountDto() {
    given(accountService.verifyLoginInfos(requestedAccountDto)).willReturn(existingAccountDto);

    AccountDto returnedAccountDto = accountResource.verifyLoginInfos(requestedAccountDto);

    assertEquals(existingAccountDto, returnedAccountDto);
  }

  @Test(expected = WebApplicationException.class)
  public void notExistingAccount_verifyLoginInfos_throwWebApplication() {
    willThrow(AccountNotFoundException.class).given(accountService).verifyLoginInfos(requestedAccountDto);

    accountResource.verifyLoginInfos(requestedAccountDto);
  }

}