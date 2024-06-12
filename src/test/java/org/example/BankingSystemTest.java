package org.example;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class BankingSystemTest {
  public static final String TEST_CSV = "test.csv";
  private BankingSystem bankingSystem;

  @BeforeEach
  public void setUp() throws Exception {
    Files.deleteIfExists(Paths.get(TEST_CSV));
    bankingSystem = new BankingSystem(TEST_CSV);
  }

  @AfterAll
  public static void cleanup() throws Exception {
    Files.deleteIfExists(Paths.get(TEST_CSV));
  }

  @Test
  public void testListAccounts() {
    bankingSystem.createAccount("Kevin", BigDecimal.valueOf(1000));
    bankingSystem.createAccount("Laura", BigDecimal.valueOf(2000));

    var accounts = bankingSystem.listAccounts();
    assertEquals(2, accounts.size());
  }

  @Test
  public void testCreateAccount() {
    bankingSystem.createAccount("Mike", BigDecimal.valueOf(1500));
    var account = bankingSystem.getAccount("Mike");
    assertNotNull(account);
    assertEquals("Mike", account.getName());
    assertEquals(BigDecimal.valueOf(1500), account.getBalance());
  }

  @Test
  public void testCreateAccountWithExistingName() {
    bankingSystem.createAccount("Nancy", BigDecimal.valueOf(1000));
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      bankingSystem.createAccount("Nancy", BigDecimal.valueOf(500));
    });
    assertEquals("Account with name 'Nancy' already exists.", exception.getMessage());
  }

  @Test
  public void testCreateAccountWithInvalidName() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      bankingSystem.createAccount("!!!", BigDecimal.valueOf(500));
    });
    assertEquals("Account name should contains only alphanumeric characters.", exception.getMessage());
  }

  @Test
  public void testCreateAccountWithNegativeBalance() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      bankingSystem.createAccount("Oscar", BigDecimal.valueOf(-100));
    });
    assertEquals("Invalid balance: -100. balance cannot negative.", exception.getMessage());
  }

  @Test
  public void testDeposit() {
    bankingSystem.createAccount("Nancy", BigDecimal.valueOf(1000));
    bankingSystem.deposit("Nancy", BigDecimal.valueOf(500));
    var account = bankingSystem.getAccount("Nancy");
    assertEquals(BigDecimal.valueOf(1500), account.getBalance());
  }

  @Test
  public void testDepositNegativeAmount() {
    bankingSystem.createAccount("Paul", BigDecimal.valueOf(1000));
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      bankingSystem.deposit("Paul", BigDecimal.valueOf(-500));
    });
    assertEquals("Invalid amount: -500. Amount must be greater than zero.", exception.getMessage());
  }

  @Test
  public void testWithdraw() {
    bankingSystem.createAccount("Oscar", BigDecimal.valueOf(2000));
    bankingSystem.withdraw("Oscar", BigDecimal.valueOf(500));
    var account = bankingSystem.getAccount("Oscar");
    assertEquals(BigDecimal.valueOf(1500), account.getBalance());
  }

  @Test
  public void testWithdrawMoreThanBalance() {
    bankingSystem.createAccount("Quinn", BigDecimal.valueOf(1000));
    Exception exception = assertThrows(IllegalStateException.class, () -> {
      bankingSystem.withdraw("Quinn", BigDecimal.valueOf(1500));
    });
    assertEquals("Insufficient funds. Balance: 1000, Amount: 1500", exception.getMessage());
  }

  @Test
  public void testTransfer() {
    bankingSystem.createAccount("Paul", BigDecimal.valueOf(3000));
    bankingSystem.createAccount("Quinn", BigDecimal.valueOf(1000));

    bankingSystem.transfer("Paul", "Quinn", BigDecimal.valueOf(500));
    var paulAccount = bankingSystem.getAccount("Paul");
    var quinnAccount = bankingSystem.getAccount("Quinn");

    assertEquals(BigDecimal.valueOf(2500), paulAccount.getBalance());
    assertEquals(BigDecimal.valueOf(1500), quinnAccount.getBalance());
  }

  @Test
  public void testTransferMoreThanBalance() {
    bankingSystem.createAccount("Rachel", BigDecimal.valueOf(1000));
    bankingSystem.createAccount("Steve", BigDecimal.valueOf(500));
    Exception exception = assertThrows(IllegalStateException.class, () -> {
      bankingSystem.transfer("Rachel", "Steve", BigDecimal.valueOf(1500));
    });
    assertEquals("Insufficient funds. Balance: 1000, Amount: 1500", exception.getMessage());
  }

  @Test
  public void testTransferToNonExistentAccount() {
    bankingSystem.createAccount("Thomas", BigDecimal.valueOf(1000));
    Exception exception = assertThrows(NullPointerException.class, () -> {
      bankingSystem.transfer("Thomas", "Unknown", BigDecimal.valueOf(500));
    });
    assertEquals("Cannot invoke \"org.example.BankAccount.addAmount(java.math.BigDecimal)\" because \"dstAccount\" is null", exception.getMessage());
  }
}
