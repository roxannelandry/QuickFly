package ca.ulaval.glo4003.quickflyws.service.account;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ca.ulaval.glo4003.quickflyws.domain.account.Account;
import ca.ulaval.glo4003.quickflyws.domain.account.userType.UserType;
import ca.ulaval.glo4003.quickflyws.dto.AccountDto;

@RunWith(MockitoJUnitRunner.class)
public class AccountAssemblerTest {

  private static final String EMAIL = "roxanne@hotmail.com";
  private static final String PASSWORD = "password";
  
  private static final UserType USER_TYPE = UserType.USER;

  @Mock
  private Account account;

  private AccountDto accountDto;

  private AccountAssembler accountAssembler;

  @Before
  public void setUp() {
    accountAssembler = new AccountAssembler();
    given(account.getEmail()).willReturn(EMAIL);
    given(account.getPassword()).willReturn(PASSWORD);
    given(account.getUserType()).willReturn(USER_TYPE);

    accountDto = new AccountDto();
    accountDto.email = EMAIL;
    accountDto.password = PASSWORD;
    accountDto.userType = USER_TYPE;
  }

  @Test
  public void accountDto_assemblingAccount_accountIsAssemble() {
    Account newAccount = accountAssembler.assembleAccount(accountDto);

    assertEquals(newAccount.getEmail(), EMAIL);
    assertEquals(newAccount.getPassword(), PASSWORD);
    assertEquals(newAccount.getUserType(), USER_TYPE);
  }

  @Test
  public void account_assemblingAccountDto_accountDtoIsAssemble() {
    AccountDto newAccountDto = accountAssembler.assembleAccountDto(account);

    assertEquals(newAccountDto.email, EMAIL);
    assertEquals(newAccountDto.password, PASSWORD);
    assertEquals(newAccountDto.userType, USER_TYPE);
  }
  
}