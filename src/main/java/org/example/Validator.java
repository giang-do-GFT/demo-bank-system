package org.example;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class Validator {
  private final AccountRepository accountRepository;

  public void ensureValidName(String name) {
    if (name.isEmpty() || name.matches("^[^a-zA-Z0-9]*$")) {
      throw new IllegalArgumentException("Account name should contains only alphanumeric characters.");
    }
  }

  public void ensureNoAccount(String name) {
    if (accountRepository.findAccount(name) != null) {
      throw new IllegalArgumentException("Account with name '" + name + "' already exists.");
    }
  }

  public void ensureValidBalance(BigDecimal balance) {
    if (balance.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Invalid balance: " + balance + ". balance cannot negative.");
    }
  }

  public void ensureValidAmount(BigDecimal amount) {
    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Invalid amount: " + amount + ". Amount must be greater than zero.");
    }
  }

  public void ensureSufficientFunds(BankAccount account, BigDecimal amount) {
    if (amount.compareTo(account.getBalance()) > 0) {
      throw new IllegalStateException("Insufficient funds. Balance: " + account.getBalance() + ", Amount: " + amount);
    }
  }
}
