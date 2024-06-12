package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class BankAccount {
  private final String name;
  private BigDecimal balance;

  public static BankAccount create(String name, BigDecimal balance) {
    return new BankAccount(name, balance);
  }

  public static BankAccount proxy(BankAccount account) {
    return new BankAccount(account.name, account.balance);
  }

  public static BankAccount deserialize(String accountString) {
    var parts = accountString.split(",");
    return new BankAccount(parts[0], new BigDecimal(parts[1]));
  }

  public void addAmount(BigDecimal amount) {
    balance = balance.add(amount);
  }

  public void subtractAmount(BigDecimal amount) {
    balance = balance.subtract(amount);
  }

  public String toCsv() {
    return name + "," + balance.toString();
  }

  @Override
  public String toString() {
    return name + ": $" + balance.toString();
  }
}