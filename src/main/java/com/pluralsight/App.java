package com.pluralsight;
import java.util.*;
import java.time.*;
import java.io.*;

public class App {
    static Scanner scanner = new Scanner(System.in);
    static ArrayList<Transaction> transactions = new ArrayList<>();

    public static void main(String[] args) {
        runHomeScreen();
    }

    public static void runHomeScreen() {
        String command;
        do {
            System.out.println();
            System.out.println("=== Home ===");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment(Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");
            System.out.print("Enter a command: ");
            command = scanner.nextLine().trim().toUpperCase();


            switch (command) {
                case "D" -> addDeposit();
                case "P" -> makePayment();
                case "L" -> runLedgerScreen();
                case "X" -> System.out.println("Good Bye!");
                default -> System.out.println("Unknown Command! Try again.");
            }
        } while (!command.equals("X"));
    }
    public static void runLedgerScreen() {
        String command;
        do {
            System.out.println();
            System.out.println("=== Home ===");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");
            System.out.print("Enter a command: ");
            command = scanner.nextLine().trim().toUpperCase();
        switch (command) {
            case "A" -> displayAll();
            case "D" -> displayDeposits();
            case "P" -> displayPayments();
            case "R" -> runReportScreen();
            case "H" -> System.out.println("Back to Home");
            default -> System.out.println("Unknown Command! Try again.");
        }
        } while (!command.equals("H"));
    }
    public static void runReportScreen() {
        String command;
        do {
            System.out.println();
            System.out.println("=== Reports ===");
            System.out.println("1) Month to Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year to Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");
            System.out.print("Enter a command: ");
            command = scanner.nextLine().trim().toUpperCase();


            switch (command) {
                case "1" -> displayMonthToDate();
                case "2" -> displayPreviousMonth();
                case "3" -> displayYearToDate();
                case "4" -> displayPreviousYear();
                case "5" -> searchVendor();
                case "0" -> System.out.println("Returning...");
                default -> System.out.println("Unknown Command! Try again.");
            }
        } while (!command.equals("0"));
    }

    public static ArrayList<Transaction> getTransactions() {
        ArrayList<Transaction> transactions = new ArrayList<Transaction>();
        return transactions;
    }

    private static ArrayList<Transaction> loadTransactions() {
        ArrayList<Transaction> transactions = getTransactions();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("transactions.csv"));
            String line;
            line = bufferedReader.readLine();

            while ((line = bufferedReader.readLine()) != null) {
                String[] tokens = line.split("\\|");
                LocalDate date = LocalDate.parse(tokens[0]);
                LocalTime time = LocalTime.parse(tokens[1]);
                String description = tokens[2];
                String vendor = tokens[3];
                Double amount = Double.parseDouble(tokens[4]);
                Transaction transaction = new Transaction(date, time, description, vendor, amount);
                transactions.add(transaction);

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return transactions;
    }

    public static void addDeposit() {
        System.out.println("TODO: Add Deposit");
        pressEnterToContinue();
    }
    public static void makePayment() {
        System.out.println("TODO: Make Payment");
        pressEnterToContinue();
    }
    public static void displayAll() {
        System.out.println("TODO: Display All");
        pressEnterToContinue();
    }
    public static void displayDeposits() {
        System.out.println("TODO: Display Deposits");
        pressEnterToContinue();
    }
    public static void displayPayments() {
        System.out.println("TODO: Display Payments");
        pressEnterToContinue();
    }
    public static void  displayMonthToDate() {
        System.out.println("TODO: Display Month to Date");
        pressEnterToContinue();
    }
    public static void  displayPreviousMonth() {
        System.out.println("TODO: Display Previous Month");
        pressEnterToContinue();
    }
    public static void  displayYearToDate() {
        System.out.println("TODO: Display Year to Date");
        pressEnterToContinue();
    }
    public static void  displayPreviousYear() {
        System.out.println("TODO: Display Previous Year");
        pressEnterToContinue();
    }
    public static void  searchVendor() {
        System.out.println("TODO: Search Vendor");
        pressEnterToContinue();
    }

    public static void pressEnterToContinue() {
        System.out.println();
        System.out.print("Press Enter to go back to the menu...");
        scanner.nextLine();
    }
}
