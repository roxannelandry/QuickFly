package ca.ulaval.glo4003.quickflyws.infrastructure.account;

import java.util.List;

import ca.ulaval.glo4003.quickflyws.domain.account.Account;
import ca.ulaval.glo4003.quickflyws.domain.account.userType.UserType;
import jersey.repackaged.com.google.common.collect.Lists;

public class AccountDevDataFactory {

  public List<Account> createMockData() {
    List<Account> accounts = Lists.newArrayList();
    
    accounts.add(new Account("roxanne@hotmail.com", "1234", UserType.USER));

    accounts.add(new Account("maxime@hotmail.com", "1234", UserType.USER));

    accounts.add(new Account("admin@hotmail.com", "admin", UserType.ADMIN));

    return accounts;
  }

}