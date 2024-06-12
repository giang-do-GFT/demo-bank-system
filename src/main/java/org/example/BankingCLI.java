package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

public class BankingCLI {
  private record FunctionEntry(String text, Runnable function) {
  }

  private final BankingSystem bankingSystem = new BankingSystem("data.csv");
  private final List<FunctionEntry> functionList = new ArrayList<>();
  private final Scanner scanner = new Scanner(System.in);

  public static void main(String[] args) {
    var bankingCLI = new BankingCLI();
    bankingCLI.run();
  }

  private void addFunction(String text, Runnable function) {
    functionList.add(new FunctionEntry(text, function));
  }

  private void run() {
    addFunction("Exit", this::exit);
    addFunction("List all accounts", this::listAccounts);
    addFunction("Create new account", this::createAccount);
    addFunction("Deposit", this::deposit);
    addFunction("Withdraw", this::withdraw);
    addFunction("Transfer", this::transfer);


    var scanner = new Scanner(System.in);
    while (true) {
      System.out.println("==========Banking CLI==========");
      IntStream.range(0, functionList.size()).forEach(i -> System.out.printf("%d. %s%n", i, functionList.get(i).text()));
      System.out.print("Enter your choice (0-" + (functionList.size() - 1) + "): ");
      var choice = scanner.nextInt();
      scanner.nextLine();

      if (choice >= 0 && choice < functionList.size()) {
        try {
          System.out.println();
          var functionEntry = functionList.get(choice);
          System.out.println("----------" + functionEntry.text() + "----------");
          functionEntry.function().run();
        } catch (Exception e) {
          System.out.println(e.getMessage());
        }
      } else {
        System.out.println("Invalid choice. Please try again.");
      }
      System.out.println();
    }
  }

  private void listAccounts() {
    bankingSystem.listAccounts().forEach(System.out::println);
  }

  private void createAccount() {
    System.out.print("Enter account name: ");
    var name = scanner.nextLine().trim();

    System.out.print("Enter initial balance: ");
    var balance = scanner.nextBigDecimal();
    scanner.nextLine();

    bankingSystem.createAccount(name, balance);
    System.out.println("Account created successfully.");
    listAccounts();
  }

  private void deposit() {
    System.out.print("Enter account name: ");
    var name = scanner.nextLine().trim();

    System.out.print("Enter amount to deposit: ");
    var amount = scanner.nextBigDecimal();
    scanner.nextLine();

    bankingSystem.deposit(name, amount);
    System.out.println("Deposit successful.");
    listAccounts();
  }

  private void withdraw() {
    System.out.print("Enter account name: ");
    var name = scanner.nextLine().trim();

    System.out.print("Enter amount to withdraw: ");
    var amount = scanner.nextBigDecimal();
    scanner.nextLine();

    bankingSystem.withdraw(name, amount);
    System.out.println("Withdrawal successful.");
    listAccounts();
  }

  private void transfer() {
    System.out.print("Enter source account name: ");
    var sourceName = scanner.nextLine();

    System.out.print("Enter destination account name: ");
    var dstName = scanner.nextLine();

    System.out.print("Enter amount to transfer: ");
    var amount = scanner.nextBigDecimal();
    scanner.nextLine();

    bankingSystem.transfer(sourceName, dstName, amount);
    System.out.println("Transfer successful.");
    listAccounts();
  }

  private void exit() {
    System.out.println("Exiting...");
    System.exit(0);
  }
}