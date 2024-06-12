package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class AccountRepository {
  private final String dataFile;
  private final Map<String, BankAccount> accounts = new TreeMap<>();

  public AccountRepository(String dataFile) {
    this.dataFile = dataFile;
    loadData();
  }

  public BankAccount findAccount(String name) {
    var account = accounts.get(name);
    return account != null ? BankAccount.proxy(account) : null;
  }

  public Set<BankAccount> findAccounts() {
    return accounts.values().stream().map(BankAccount::proxy).collect(Collectors.toSet());
  }

  public void createAccount(BankAccount account) {
    accounts.put(account.getName(), account);
    commit();
  }

  public void saveAccount(BankAccount account) {
    accounts.put(account.getName(), account);
    commit();
  }

  public void saveAccounts(BankAccount... accounts) {
    Arrays.stream(accounts).forEach(this::saveAccount);
  }

  public void loadData() {
    var path = Paths.get(dataFile);
    if (!Files.exists(path)) {
      return;
    }
    try (var lines = Files.lines(path)) {
      lines.forEach(line -> {
        try {
          var account = BankAccount.deserialize(line);
          accounts.put(account.getName(), account);
        } catch (Exception e) {
          System.err.println("Error occurred while deserializing account from line: " + line + ". Error: " + e.getMessage());
        }
      });
    } catch (IOException e) {
      throw new RuntimeException("Error occurred while loading accounts data.", e);
    }
  }

  public void commit() {
    try (var writer = new PrintWriter(new FileWriter(dataFile))) {
      accounts.values().stream()
        .map(BankAccount::toCsv)
        .forEach(writer::println);
    } catch (IOException e) {
      throw new RuntimeException("Error occurred while saving accounts data.", e);
    }
  }
}
