package ca.ulaval.glo4003.quickflyws.service.account;

import ca.ulaval.glo4003.quickflyws.domain.account.Account;
import ca.ulaval.glo4003.quickflyws.dto.AccountDto;

public class AccountAssembler {

  public Account assembleAccount(AccountDto accountDto) {
    return new Account(accountDto.email, accountDto.password, accountDto.userType);
  }

  public AccountDto assembleAccountDto(Account account) {
    AccountDto accountDto = new AccountDto();
    
    accountDto.email = account.getEmail();
    accountDto.password = account.getPassword();
    accountDto.userType = account.getUserType();
    
    return accountDto;
  }

}