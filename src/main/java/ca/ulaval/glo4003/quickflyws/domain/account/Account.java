package ca.ulaval.glo4003.quickflyws.domain.account;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.ulaval.glo4003.quickflyws.domain.account.exceptions.AccountEmailInvalidException;
import ca.ulaval.glo4003.quickflyws.domain.account.exceptions.AccountInfosMissingException;
import ca.ulaval.glo4003.quickflyws.domain.account.exceptions.AccountNotFoundException;
import ca.ulaval.glo4003.quickflyws.domain.account.userType.UserType;

public class Account {

  private String email;
  private String password;
  private UserType userType = UserType.USER;

  public Account(String email, String password, UserType userType) {
    this.email = email;
    this.password = password;
    this.userType = userType;
    
    verifyInfos();
  }

  public void verifyPassword(String password) {
    if (!this.password.equals(password)) {
      throw new AccountNotFoundException("This account doesn't exist.");
    }
  }

  private void verifyInfos() {
    if (infosIsEmpty()) {
      throw new AccountInfosMissingException("You need to fill all the informations to register.");
    }

    if (!emailIsValid()) {
      throw new AccountEmailInvalidException("The email enter is invalid.");
    }
  }

  private boolean emailIsValid() {
    Pattern pattern = Pattern.compile(".+@.+\\.[a-z]+");
    Matcher matcher = pattern.matcher(email);
    
    return matcher.matches();
  }

  private boolean infosIsEmpty() {
    if (email == null || password == null) {
      return true;
    }

    if (email.isEmpty() || password.isEmpty()) {
      return true;
    }

    return false;
  }

  public UserType getUserType() {
    return userType;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

}