package ca.ulaval.glo4003.quickflyws.infrastructure.account;

import java.util.HashMap;
import java.util.Map;

import ca.ulaval.glo4003.quickflyws.domain.account.Account;
import ca.ulaval.glo4003.quickflyws.domain.account.exceptions.AccountAlreadyExistException;
import ca.ulaval.glo4003.quickflyws.domain.account.exceptions.AccountNotFoundException;
import ca.ulaval.glo4003.quickflyws.service.account.AccountRepository;

public class AccountRepositoryInMemory implements AccountRepository {

  private Map<String, Account> accounts = new HashMap<>();

  @Override
  public Account find(String email) {
    if (accounts.containsKey(email)) {
      return accounts.get(email);
    }
    throw new AccountNotFoundException("This account doesn't exist.");
  }

  @Override
  public void save(Account account) {
    checkIfAccountAlreadyExist(account);

    accounts.put(account.getEmail(), account);
  }

  private void checkIfAccountAlreadyExist(Account account) {
    if (accounts.containsKey(account.getEmail())) {
      throw new AccountAlreadyExistException("This account already exist.");
    }
  }

}