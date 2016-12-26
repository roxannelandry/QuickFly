package ca.ulaval.glo4003.quickflyws.service.account;

import ca.ulaval.glo4003.quickflyws.domain.account.Account;

public interface AccountRepository {

  Account find(String email);

  void save(Account account);

}