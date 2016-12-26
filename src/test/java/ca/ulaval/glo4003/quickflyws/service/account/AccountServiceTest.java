package ca.ulaval.glo4003.quickflyws.service.account;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ca.ulaval.glo4003.quickflyws.domain.account.Account;
import ca.ulaval.glo4003.quickflyws.domain.account.exceptions.AccountAlreadyExistException;
import ca.ulaval.glo4003.quickflyws.domain.account.exceptions.AccountNotFoundException;
import ca.ulaval.glo4003.quickflyws.domain.account.userType.UserType;
import ca.ulaval.glo4003.quickflyws.dto.AccountDto;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

  private static final String EMAIL = "email@email.email";
  private static final String PASSWORD = "password";

  private static final String INVALID_EMAIL = "email";
  private static final String INVALID_PASSWORD = "";
  
  private static final UserType TYPE_USER = UserType.USER;

  @Mock
  private Account account;

  @Mock
  private Account invalidEmailAccount;

  @Mock
  private Account infosEmptyAccount;

  @Mock
  private AccountRepository accountRepository;

  @Mock
  private AccountAssembler accountAssembler;
 
  private AccountDto accountDto;

  private AccountDto invalidAccountDto;

  private AccountService accountService;

  @Before
  public void setUp() {
    accountService = new AccountService(accountRepository, accountAssembler);

    accountDto = new AccountDto();
    accountDto.email = EMAIL;
    accountDto.password = PASSWORD;
    accountDto.userType = TYPE_USER;
  }

  @Test
  public void accountDto_findingAccount_delegateToAccountRepository() {
    accountService.findAccount(EMAIL);

    verify(accountRepository).find(EMAIL);
  }

  @Test
  public void accountDto_findingAccount_delegateToAccountAssembler() {
    given(accountRepository.find(EMAIL)).willReturn(account);

    accountService.findAccount(EMAIL);

    verify(accountAssembler).assembleAccountDto(account);
  }

  @Test
  public void accountDto_addingAccount_delegateToAccountAssembler() {
    accountService.addAccount(accountDto);

    verify(accountAssembler).assembleAccount(accountDto);
  }

  @Test
  public void accountDto_addingAccount_delegateToAccountRepository() {
    given(accountAssembler.assembleAccount(accountDto)).willReturn(account);

    accountService.addAccount(accountDto);

    verify(accountRepository).save(account);
  }

  @Test
  public void accountDto_verifyingLoginInfos_delegateToAccountRepository() {
    given(accountRepository.find(EMAIL)).willReturn(account);

    accountService.verifyLoginInfos(accountDto);

    verify(accountRepository).find(accountDto.email);
  }

  @Test
  public void accountDto_verifyingLoginInfos_delegateToAccount() {
    given(accountRepository.find(EMAIL)).willReturn(account);

    accountService.verifyLoginInfos(accountDto);

    verify(account).verifyPassword(accountDto.password);
  }

  @Test
  public void accountDto_verifyingLoginInfos_delegateToAccountAssembler() {
    given(accountRepository.find(EMAIL)).willReturn(account);

    accountService.verifyLoginInfos(accountDto);

    verify(accountAssembler).assembleAccountDto(account);
  }

  @Test
  public void existingAccount_findingAccount_returnAccountDto() {
    given(accountRepository.find(EMAIL)).willReturn(account);
    given(accountAssembler.assembleAccountDto(account)).willReturn(accountDto);

    AccountDto returnedAccountDto = accountService.findAccount(EMAIL);

    assertEquals(accountDto, returnedAccountDto);
  }

  @Test
  public void existingAccount_verifyingLoginInfos_returnAccountDto() {
    given(accountRepository.find(EMAIL)).willReturn(account);
    given(accountAssembler.assembleAccountDto(account)).willReturn(accountDto);

    AccountDto returnedAccountDto = accountService.verifyLoginInfos(accountDto);

    assertEquals(accountDto, returnedAccountDto);
  }

  @Test(expected = AccountNotFoundException.class)
  public void invalidEmailInAccountDto_findingAccount_throwAccountNotFound() {
    willThrow(AccountNotFoundException.class).given(accountRepository).find(INVALID_EMAIL);

    accountService.findAccount(INVALID_EMAIL);
  }

  @Test(expected = AccountAlreadyExistException.class)
  public void invalidEmailInAccountDto_addingAccount_throwAccountEmailInvalid() {
    willThrow(AccountAlreadyExistException.class).given(accountRepository).save(account);

    given(accountAssembler.assembleAccount(accountDto)).willReturn(account);

    accountService.addAccount(accountDto);
  }

  @Test(expected = AccountNotFoundException.class)
  public void invalidEmailInAccountDto_verifyingLoginInfos_throwAccountNotFound() {
    willThrow(AccountNotFoundException.class).given(accountRepository).find(INVALID_EMAIL);

    invalidAccountDto = new AccountDto();
    invalidAccountDto.email = INVALID_EMAIL;
    invalidAccountDto.password = PASSWORD;
    invalidAccountDto.userType = TYPE_USER;

    accountService.verifyLoginInfos(invalidAccountDto);
  }

  @Test(expected = AccountNotFoundException.class)
  public void invalidPasswordInAccountDto_verifyingLoginInfos_throwAccountNotFound() {
    willThrow(AccountNotFoundException.class).given(account).verifyPassword(INVALID_PASSWORD);
    given(accountRepository.find(EMAIL)).willReturn(account);

    invalidAccountDto = new AccountDto();
    invalidAccountDto.email = EMAIL;
    invalidAccountDto.password = INVALID_PASSWORD;
    invalidAccountDto.userType = TYPE_USER;

    accountService.verifyLoginInfos(invalidAccountDto);
  }

}