package ca.ulaval.glo4003.quickflyws.service.account;

import ca.ulaval.glo4003.quickflyws.domain.account.Account;
import ca.ulaval.glo4003.quickflyws.dto.AccountDto;

public class AccountService {

  private AccountRepository accountRepository;
  private AccountAssembler accountAssembler;

  public AccountService(AccountRepository accountRepository, AccountAssembler accountAssembler) {
    this.accountRepository = accountRepository;
    this.accountAssembler = accountAssembler;
  }

  public AccountDto findAccount(String email) {
    Account account = accountRepository.find(email);

    return accountAssembler.assembleAccountDto(account);
  }

  public void addAccount(AccountDto accountDto) {
    Account account = accountAssembler.assembleAccount(accountDto);

    accountRepository.save(account);
  }

  public AccountDto verifyLoginInfos(AccountDto accountDto) {
    Account account = accountRepository.find(accountDto.email);

    account.verifyPassword(accountDto.password);

    return accountAssembler.assembleAccountDto(account);
  }

}