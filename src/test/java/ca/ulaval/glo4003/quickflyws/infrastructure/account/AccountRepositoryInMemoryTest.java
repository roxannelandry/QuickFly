package ca.ulaval.glo4003.quickflyws.infrastructure.account;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ca.ulaval.glo4003.quickflyws.domain.account.Account;
import ca.ulaval.glo4003.quickflyws.domain.account.exceptions.AccountAlreadyExistException;
import ca.ulaval.glo4003.quickflyws.domain.account.exceptions.AccountNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class AccountRepositoryInMemoryTest {

  private static final String ACCOUNT_IN_REPO_EMAIL = "something@hotmail.com";
  private static final String ACCOUNT_PASSWORD = "password";

  private static final String NEW_ACCOUNT_EMAIL = "otherthing@hotmail.com";

  private static final String WRONG_INFO = "wrong";

  @Mock
  private Account account;

  private AccountRepositoryInMemory accountRepositoryInMemory;

  @Before
  public void setUp() {
    accountRepositoryInMemory = new AccountRepositoryInMemory();
  }

  @Test
  public void persistedAccount_findingAccount_returnAccount() {
    addAccountToRepositoryForTest();
    Account accountFound = accountRepositoryInMemory.find(ACCOUNT_IN_REPO_EMAIL);

    assertEquals(account, accountFound);
  }

  @Test
  public void newValidAccount_savingAccount_accountSaved() {
    given(account.getEmail()).willReturn(NEW_ACCOUNT_EMAIL);

    accountRepositoryInMemory.save(account);

    Account returnedAccount = accountRepositoryInMemory.find(NEW_ACCOUNT_EMAIL);

    assertEquals(account, returnedAccount);
  }

  @Test(expected = AccountNotFoundException.class)
  public void existingAccount_findingAccount_throwNotFound() {
    addAccountToRepositoryForTest();

    accountRepositoryInMemory.find(WRONG_INFO);
  }

  @Test(expected = AccountAlreadyExistException.class)
  public void alreadyExistingAccount_savingAccount_throwAccountAlreadyExist() {
    addAccountToRepositoryForTest();

    accountRepositoryInMemory.save(account);
  }

  private void addAccountToRepositoryForTest() {
    given(account.getEmail()).willReturn(ACCOUNT_IN_REPO_EMAIL);
    given(account.getPassword()).willReturn(ACCOUNT_PASSWORD);
    accountRepositoryInMemory.save(account);
  }

}