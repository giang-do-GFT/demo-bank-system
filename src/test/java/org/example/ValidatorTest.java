package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class ValidatorTest {
  private Validator validator;
  private AccountRepository accountRepository;

  @BeforeEach
  public void setUp() {
    accountRepository = new AccountRepository("test.csv");
    validator = new Validator(accountRepository);
  }

  @Test
  public void testEnsureValidName() {
    assertDoesNotThrow(() -> validator.ensureValidName("ValidName123"));
    var exception = assertThrows(IllegalArgumentException.class, () -> validator.ensureValidName(""));
    assertEquals("Account name should contains only alphanumeric characters.", exception.getMessage());
  }

  @Test
  public void testEnsureNoAccount() {
    assertDoesNotThrow(() -> validator.ensureNoAccount("NonExistentAccount"));

    accountRepository.createAccount(BankAccount.create("ExistingAccount", BigDecimal.valueOf(1000)));
    var exception = assertThrows(IllegalArgumentException.class, () -> validator.ensureNoAccount("ExistingAccount"));
    assertEquals("Account with name 'ExistingAccount' already exists.", exception.getMessage());
  }

  @Test
  public void testEnsureValidBalance() {
    assertDoesNotThrow(() -> validator.ensureValidBalance(BigDecimal.valueOf(1000)));
    var exception = assertThrows(IllegalArgumentException.class, () -> validator.ensureValidBalance(BigDecimal.valueOf(-1000)));
    assertEquals("Invalid balance: -1000. balance cannot negative.", exception.getMessage());
  }

  @Test
  public void testEnsureValidAmount() {
    assertDoesNotThrow(() -> validator.ensureValidAmount(BigDecimal.valueOf(100)));
    var exception = assertThrows(IllegalArgumentException.class, () -> validator.ensureValidAmount(BigDecimal.ZERO));
    assertEquals("Invalid amount: 0. Amount must be greater than zero.", exception.getMessage());
  }

  @Test
  public void testEnsureSufficientFunds() {
    var account = BankAccount.create("TestAccount", BigDecimal.valueOf(500));
    assertDoesNotThrow(() -> validator.ensureSufficientFunds(account, BigDecimal.valueOf(100)));
    var exception = assertThrows(IllegalStateException.class, () -> validator.ensureSufficientFunds(account, BigDecimal.valueOf(600)));
    assertEquals("Insufficient funds. Balance: 500, Amount: 600", exception.getMessage());
  }
}
