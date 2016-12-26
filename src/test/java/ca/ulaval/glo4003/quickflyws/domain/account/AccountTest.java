package ca.ulaval.glo4003.quickflyws.domain.account;

import org.junit.Test;

import ca.ulaval.glo4003.quickflyws.domain.account.exceptions.AccountEmailInvalidException;
import ca.ulaval.glo4003.quickflyws.domain.account.exceptions.AccountInfosMissingException;
import ca.ulaval.glo4003.quickflyws.domain.account.exceptions.AccountNotFoundException;
import ca.ulaval.glo4003.quickflyws.domain.account.userType.UserType;

public class AccountTest {

  private static final String VALID_EMAIL = "something@hotmail.com";
  private static final String INVALID_EMAIL = "somethinghotmail.com";
  private static final String EMPTY_EMAIL = "";
  private static final String NULL_EMAIL = null;

  private static final String PASSWORD = "password";
  private static final String INVALID_PASSWORD = "";
  private static final String NULL_PASSWORD = null;

  private static final UserType TYPE_USER = UserType.USER;

  private Account userAccount;

  @Test(expected = AccountEmailInvalidException.class)
  public void accountWithInvalidEmail_verifyInfos_throwAccountEmailInvalid() {
    userAccount = new Account(INVALID_EMAIL, PASSWORD, TYPE_USER);
  }

  @Test(expected = AccountInfosMissingException.class)
  public void accountWithEmptyEmail_verifyInfos_throwAccountInfosMissing() {
    userAccount = new Account(EMPTY_EMAIL, PASSWORD, TYPE_USER);
  }

  @Test(expected = AccountInfosMissingException.class)
  public void accountWithNullEmail_verifyInfos_throwAccountInfosMissing() {
    userAccount = new Account(NULL_EMAIL, PASSWORD, TYPE_USER);
  }

  @Test(expected = AccountInfosMissingException.class)
  public void accountWithInvalidPassword_verifyInfos_throwAccountInfosMissing() {
    userAccount = new Account(VALID_EMAIL, INVALID_PASSWORD, TYPE_USER);
  }

  @Test(expected = AccountInfosMissingException.class)
  public void accountWithNullPassword_verifyInfos_throwAccountInfosMissing() {
    userAccount = new Account(VALID_EMAIL, NULL_PASSWORD, TYPE_USER);
  }

  @Test(expected = AccountInfosMissingException.class)
  public void accountWithEmptyInfos_verifyInfos_throwAccountInfosMissing() {
    userAccount = new Account(EMPTY_EMAIL, INVALID_PASSWORD, TYPE_USER);
  }

  @Test
  public void accountWithValidInfos_verifyPassword_doNotThrowException() {
    userAccount = new Account(VALID_EMAIL, PASSWORD, TYPE_USER);

    userAccount.verifyPassword(PASSWORD);
  }
 
  @Test(expected = AccountNotFoundException.class)
  public void invalidPassword_verifyPassword_throwAccountNotFound() {
    userAccount = new Account(VALID_EMAIL, PASSWORD, TYPE_USER);

    userAccount.verifyPassword(INVALID_PASSWORD);
  }

}