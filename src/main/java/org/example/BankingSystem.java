package org.example;

import java.math.BigDecimal;
import java.util.Set;

public class BankingSystem {
  private final AccountRepository accountRepository;
  private final Validator validator;

  public BankingSystem(String dataFile) {
    accountRepository = new AccountRepository(dataFile);
    validator = new Validator(accountRepository);
  }

  public Set<BankAccount> listAccounts() {
    return accountRepository.findAccounts();
  }

  public void createAccount(String name, BigDecimal initialBalance) {
    validator.ensureValidName(name);
    validator.ensureNoAccount(name);
    validator.ensureValidBalance(initialBalance);
    var account = BankAccount.create(name, initialBalance);
    accountRepository.createAccount(account);
  }

  public BankAccount getAccount(String name) {
    return accountRepository.findAccount(name);
  }

  public void deposit(String name, BigDecimal amount) {
    BankAccount account = getAccount(name);
    validator.ensureValidAmount(amount);
    account.addAmount(amount);
    accountRepository.saveAccount(account);
  }

  public void withdraw(String name, BigDecimal amount) {
    BankAccount account = getAccount(name);
    validator.ensureValidAmount(amount);
    validator.ensureSufficientFunds(account, amount);
    account.subtractAmount(amount);
    accountRepository.saveAccount(account);
  }

  public void transfer(String srcName, String dstname, BigDecimal amount) {
    BankAccount srcAccount = getAccount(srcName);
    BankAccount dstAccount = getAccount(dstname);

    validator.ensureValidAmount(amount);
    validator.ensureSufficientFunds(srcAccount, amount);

    srcAccount.subtractAmount(amount);
    dstAccount.addAmount(amount);

    accountRepository.saveAccounts(srcAccount, dstAccount);
  }
}
