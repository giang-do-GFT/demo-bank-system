package org.example;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AccountRepositoryTest {
  public static final String TEST_CSV = "test.csv";
  private AccountRepository accountRepository;

  @BeforeEach
  public void setUp() throws Exception {
    Files.deleteIfExists(Paths.get(TEST_CSV));
    accountRepository = new AccountRepository(TEST_CSV);
  }

  @AfterAll
  public static void cleanup() throws Exception {
    Files.deleteIfExists(Paths.get(TEST_CSV));
  }

  @Test
  public void testCreateAccount() {
    var account = BankAccount.create("Alice", BigDecimal.valueOf(1000));
    accountRepository.createAccount(account);

    var retrievedAccount = accountRepository.findAccount("Alice");
    assertNotNull(retrievedAccount);
    assertEquals("Alice", retrievedAccount.getName());
    assertEquals(BigDecimal.valueOf(1000), retrievedAccount.getBalance());
  }

  @Test
  public void testSaveAccount() {
    var account = BankAccount.create("Bob", BigDecimal.valueOf(2000));
    accountRepository.saveAccount(account);

    var retrievedAccount = accountRepository.findAccount("Bob");
    assertNotNull(retrievedAccount);
    assertEquals("Bob", retrievedAccount.getName());
    assertEquals(BigDecimal.valueOf(2000), retrievedAccount.getBalance());
  }

  @Test
  public void testFindAccounts() {
    var account1 = BankAccount.create("Charlie", BigDecimal.valueOf(1500));
    var account2 = BankAccount.create("Dave", BigDecimal.valueOf(2500));
    accountRepository.saveAccounts(account1, account2);

    var accounts = accountRepository.findAccounts();
    assertEquals(2, accounts.size());
  }

  @Test
  public void testCommitAndLoadData() {
    var account = BankAccount.create("Eve", BigDecimal.valueOf(3000));
    accountRepository.createAccount(account);

    accountRepository = new AccountRepository(TEST_CSV);  // Reload data
    var retrievedAccount = accountRepository.findAccount("Eve");

    assertNotNull(retrievedAccount);
    assertEquals("Eve", retrievedAccount.getName());
    assertEquals(BigDecimal.valueOf(3000), retrievedAccount.getBalance());
  }

  @Test
  public void testFindAndModifyWithoutSave() {
    var account = BankAccount.create("Frank", BigDecimal.valueOf(1000));
    accountRepository.createAccount(account);

    // Find account and modify its balance without saving
    var retrievedAccount = accountRepository.findAccount("Frank");
    retrievedAccount.addAmount(BigDecimal.valueOf(500));

    // Reload the account repository to simulate a fresh read from the database
    accountRepository = new AccountRepository(TEST_CSV);
    var unchangedAccount = accountRepository.findAccount("Frank");

    // Ensure the balance has not changed in the database
    assertNotNull(unchangedAccount);
    assertEquals("Frank", unchangedAccount.getName());
    assertEquals(BigDecimal.valueOf(1000), unchangedAccount.getBalance());
  }
}
