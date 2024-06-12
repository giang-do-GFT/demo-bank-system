package org.example;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class BankAccountTest {
  @Test
  public void testCreateAccount() {
    var account = BankAccount.create("Frank", BigDecimal.valueOf(500));
    assertEquals("Frank", account.getName());
    assertEquals(BigDecimal.valueOf(500), account.getBalance());
  }

  @Test
  public void testAddAmount() {
    var account = BankAccount.create("Grace", BigDecimal.valueOf(1000));
    account.addAmount(BigDecimal.valueOf(500));
    assertEquals(BigDecimal.valueOf(1500), account.getBalance());
  }

  @Test
  public void testSubtractAmount() {
    var account = BankAccount.create("Hank", BigDecimal.valueOf(2000));
    account.subtractAmount(BigDecimal.valueOf(500));
    assertEquals(BigDecimal.valueOf(1500), account.getBalance());
  }

  @Test
  public void testToCsv() {
    var account = BankAccount.create("Ivy", BigDecimal.valueOf(750));
    assertEquals("Ivy,750", account.toCsv());
  }

  @Test
  public void testDeserialize() {
    var csvString = "Jack,1250";
    var account = BankAccount.deserialize(csvString);
    assertEquals("Jack", account.getName());
    assertEquals(BigDecimal.valueOf(1250), account.getBalance());
  }
}
