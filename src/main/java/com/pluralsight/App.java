package com.pluralsight;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.*;
import java.io.*;

public class App {
    static Scanner scanner = new Scanner(System.in);
    static ArrayList<Transaction> transactions = loadTransactions();

    public static void main(String[] args) {
        displayBanner();
        runHomeScreen();
    }

    // all user screens
    public static void runHomeScreen() {
        String command;
        do {
            System.out.println();
            System.out.println("=== Home ===");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment");
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
            System.out.println("=== Ledger ===");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");
            System.out.print("Enter a command: \n");
            command = scanner.nextLine().trim().toUpperCase();
            switch (command) {
                case "A" -> displayAll(transactions);
                case "D" -> displayDeposits(transactions);
                case "P" -> displayPayments(transactions);
                case "R" -> runReportsScreen();
                case "H" -> System.out.println("Back to Home...");
                default -> System.out.println("Unknown Command! Try again.");
            }
        } while (!command.equals("H"));
    }

    public static void runReportsScreen() {
        String command;
        do {
            System.out.println("=== Reports ===");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search By Vendor");
            System.out.println("0) Back");
            System.out.print("Enter a command: ");
            command = scanner.nextLine().trim().toUpperCase();


            switch (command) {
                case "1" -> displayMonthToDate();
                case "2" -> displayPreviousMonth();
                case "3" -> displayYearToDate();
                case "4" -> displayPreviousYear();
                case "5" -> searchByVendor();
                case "0" -> System.out.println("Returning...");
                default -> System.out.println("Unknown Command! Try again.\n");
            }
        } while (!command.equals("0"));
    }

    // creating array list from transactions file
    private static ArrayList<Transaction> loadTransactions() {
        ArrayList<Transaction> transactions = new ArrayList<>();
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
            System.out.println("Error reading file!");
        }
        return transactions;
    }

    private static void saveTransaction(String description, String vendor, double amount) {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.csv", true));
            writer.write(currentDate.format(dateFormat) + "|" + currentTime.format(timeFormat) + "|" + description + "|" + vendor + "|" + amount + "\n");
            writer.close();
            transactions.add(new Transaction(currentDate, currentTime, description, vendor, amount));
        } catch (IOException e) {
            System.out.println("Error! Transaction could not be saved.");
        }
    }

    // home screen options
    public static void addDeposit() {
        // gather info and return error if invalid
        String description = promptForString("Enter description: ");
        String vendor = promptForString("Enter vendor: ");
        double amount = promptForDouble("Enter deposit amount: ");
        while (amount <= 0) {
            amount = promptForDouble("""
                    Error! Amount can't be 0 or negative.
                    Try again: """);
        }

        saveTransaction(description, vendor, amount);

        System.out.println("Your deposit has been added to the database!");
        pressEnterToContinue();
    }

    public static void makePayment() {
        // gather info and return error if invalid
        String description = promptForString("Enter description: ");
        String vendor = promptForString("Enter vendor: ");
        double amount = promptForDouble("Enter payment amount: ");
        while (amount == 0) {
            amount = promptForDouble("""
                    Error! Cannot be 0.
                    Try again:  """);
        }
        if (amount > 0) {
            amount *= -1;
        }

        saveTransaction(description, vendor, amount);

        System.out.println("Your payment has been added to the database!");
        pressEnterToContinue();
    }

    public static void displayTransactions(ArrayList<Transaction> matchedTransactions) {
        for (Transaction matchedTransaction : matchedTransactions) {
            System.out.println(matchedTransaction.toString());
        }
        if (matchedTransactions.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            System.out.println(matchedTransactions.size() + " transactions found!");
        }
        pressEnterToContinue();
    }

    // ledger screen options
    public static void displayAll(ArrayList<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            System.out.println(transaction.toString());
        }
        System.out.println("Total transactions: " + transactions.size());
        pressEnterToContinue();
    }

    public static void displayDeposits(ArrayList<Transaction> transactions) {
        ArrayList<Transaction> matchedTransactions = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > 0) {
                matchedTransactions.add(transaction);
            }
        }
        displayTransactions(matchedTransactions);
    }

    public static void displayPayments(ArrayList<Transaction> transactions) {
        ArrayList<Transaction> matchedTransactions = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                matchedTransactions.add(transaction);
            }
        }
        displayTransactions(matchedTransactions);
    }

    // reports screen options
    public static void displayMonthToDate() {
        ArrayList<Transaction> matchedTransactions = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();

        for (Transaction transaction : transactions) {
            if (transaction.getDate().getMonthValue() == currentDate.getMonthValue() &&
                    transaction.getDate().getYear() == currentDate.getYear()) {
                    matchedTransactions.add(transaction);
            }
        }
        displayTransactions(matchedTransactions);
    }

    public static void displayPreviousMonth() {
        ArrayList<Transaction> matchedTransactions = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        LocalDate previousDate = currentDate.minusMonths(1);

        for (Transaction transaction : transactions) {
            if (transaction.getDate().getMonthValue() == previousDate.getMonthValue() &&
                    transaction.getDate().getYear() == previousDate.getYear()) {
                matchedTransactions.add(transaction);
            }
        }
        displayTransactions(matchedTransactions);
    }

    public static void displayYearToDate() {
        LocalDate currentDate = LocalDate.now();
        ArrayList<Transaction> matchedTransactions = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (transaction.getDate().getYear() == currentDate.getYear()) {
                matchedTransactions.add(transaction);
            }
        }
        displayTransactions(matchedTransactions);
    }

    public static void displayPreviousYear() {
        ArrayList<Transaction> matchedTransactions = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        LocalDate previousDate = currentDate.minusYears(1);

        for (Transaction transaction : transactions) {
            if (transaction.getDate().getYear() == previousDate.getYear()) {
                matchedTransactions.add(transaction);
            }
        }
        displayTransactions(matchedTransactions);
    }

    public static void searchByVendor() {
        ArrayList<Transaction> matchedTransactions = new ArrayList<>();
        String vendor = promptForString("Enter vendor: ");

        for (Transaction transaction : transactions) {
            if (transaction.getVendor().equalsIgnoreCase(vendor)) {
                matchedTransactions.add(transaction);
            }
        }
        displayTransactions(matchedTransactions);
    }

    public static double promptForDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public static String promptForString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("This can't be blank.");
            } else if (input.contains("|")) {
                System.out.println("Please don't use the | character.");
            } else {
                return input;
            }
        }
    }

    public static void displayBanner() {
        System.out.println("""
                [0;35m▄[0;91m▄▄▄▄▄[0;37m▄▄[0;91m▄▄[0;35m▄[0;37m [0;35m█[0;91;45m▄[0;35m▄[0;37m [0;35m█[0;91;45m▄[0;35m█[0;37m  [0;35m▄[0;91m▄▄[0;37m▄▄[0;91m▄▄▄[0;35m▄[0;37m      [0;35m▐[0;91;45m▄[0;91;47m▀▐[0;91;45m▄[0;91m▄[0;35m▄[0;37m   [0;35m▄[0;91m▄▄[0;37m▄▄[0;91m▄▄▄[0;35m▄[0;37m   [0;35m▄[0;91m▄▄[0;37m▄[0;91m▄[0;35m▄[0;37m    [0;35m▄[0;37m▄[0;91m▄▄[0;35m▄[0;37m   [0;35m▐[0;91;45m▄[0;91;47m▀▐[0;91;45m▄[0;91m▄[0;35m▄[0;37m  [0;35m▄▄[0;91m▄[0;37m▄[0;91m▄▄[0;35m▄[0;37m         [0;35m▄[0;91;45m▄[0;91;47m▀[0;91;45m█▓░[0;37m  [0;35m▄[0;91m▄▄▄▄▄[0;37m▄▄[0;91m▄▄[0;35m▄[0;37m   [0;35m▄[0;37m▄[0;91m▄▄[0;35m▄[0;37m   [0;35m▐[0;91;45m▄[0;91;47m▀▐[0;91;45m▄[0;91m▄[0;35m▄[0;37m   [0;35m▄[0;91m▄▄[0;37m▄▄[0;91m▄▄▄[0;35m▄[0m
                [0;35m▀▀▀[0;91;45m██▀[0;91m▀▀▀[0;35m▀[0;37m  [0;91;45m▌[0;91;47m██[0;37m [0;91;47m██[0;35m█[0;37m [0;35m▐[0;91;45m██▀[0;91m▀▀▀▀[0;35m▀[0;37m       [0;35m█[0;91;45m▓▓░[0;35m▀[0;91;45m▀█[0;35m█[0;37m [0;35m▐[0;91;45m██▀[0;91m▀▀▀▀[0;35m▀[0;37m  [0;35m▄[0;91;45m▄[0;91;47m██[0;35m▀[0;91;45m▀[0;91;47m▓█[0;91;45m▌[0;37m [0;35m▄[0;91;45m▄[0;91;47m▓[0;91;45m▀[0;35m▀[0;91;47m██[0;91;45m▄[0;35m▄[0;37m [0;35m█[0;91;45m▓▓░[0;35m▀[0;91;45m▀█[0;35m█[0;37m [0;91;45m▐[0;91;47m█▓[0;91;45m▀[0;35m▀[0;91;47m██[0;91;45m▄[0;35m▄[0;37m      [0;35m█[0;91;45m█[0;91;47m▓[0;91m▀[0;35m▀▀▀[0;37m  [0;35m▀▀▀[0;91;45m██▀[0;91m▀▀▀[0;35m▀[0;37m  [0;35m▄[0;91;45m▄[0;91;47m▓[0;91;45m▀[0;35m▀[0;91;47m██[0;91;45m▄[0;35m▄[0;37m [0;35m█[0;91;45m▓▓░[0;35m▀[0;91;45m▀█[0;35m█[0;37m [0;35m▐[0;91;45m██▀[0;91m▀▀▀▀[0;35m▀[0;37m [0m
                [0;37m  [0;35m█[0;91;45m▓▓[0;35m█[0;37m      [0;35m█[0;91;45m▀▌[0;35m▀[0;91;45m▓[0;95;41m▒[0;35m█[0;37m [0;35m█[0;91;45m▓▓[0;35m█▀▀▀[0;37m         [0;35m█[0;91;45m░░[0;95;45m░[0;35m▀[0;95;45m▀[0;35m█▀[0;37m [0;35m█[0;91;45m▓▓[0;35m█▀▀▀[0;37m    [0;35m█[0;91;45m▀▓▓[0;37m [0;35m▄[0;95m▄▄[0;35m▄[0;37m [0;35m█[0;91m█[0;91;45m▀[0;35m█[0;37m [0;95m▓▓[0;91;45m▀[0;35m█[0;37m [0;35m█[0;91;45m░░[0;95;45m░[0;35m▀[0;95;45m▀[0;35m█▀[0;37m [0;35m█[0;91m█[0;91;45m▀[0;35m█[0;37m [0;95m▓▓[0;91;45m▀[0;35m█[0;37m      [0;35m▀▀▀▀[0;95;45m░░▄[0;35m▄[0;37m   [0;35m█[0;91;45m▓▓[0;35m█[0;37m      [0;35m█[0;91m█[0;91;45m▀[0;35m█[0;37m [0;95m▓▓[0;91;45m▀[0;35m█[0;37m [0;35m█[0;91;45m░░[0;95;45m░[0;35m▀[0;95;45m▀[0;35m█▀[0;37m [0;35m█[0;91;45m▓▓[0;35m█▀▀▀[0;37m   [0m
                [0;37m  [0;35m▐[0;95;45m▄[0;95m█[0;35m█[0;37m      [0;95;45m [0;95m▒▒[0;37m [0;95;41m▒█▒[0;37m [0;35m▐[0;91;45m░░[0;95;45m░▓[0;95;47m▀[0;95;45m▄▄[0;37m        [0;35m█[0;95;45m▄▄▓[0;35m▌▐[0;95;45m▓▄[0;37m [0;35m▐[0;91;45m░░[0;95;45m░▓[0;95;47m▀[0;95;45m▄▄[0;37m    [0;35m█[0;91;45m░░[0;95;45m▄[0;95;47m▓▄[0;95;45m▀ [0;37m [0;35m▀[0;91;45m▀[0;95;45m▒▓▄[0;95m█[0;95;45m▀[0;35m▀[0;37m  [0;35m█[0;95;45m▄▄▓[0;35m▌▐[0;95;45m▓▄[0;37m [0;95;45m [0;91;45m▀[0;95;45m▒▓▄[0;95m█[0;95;45m▀[0;35m▀[0;37m       [0;35m▀[0;95;45m▀▓▓[0;95;47m▌▀[0;95;45m▀[0;35m▀[0;37m   [0;35m▐[0;95;45m▄[0;95m█[0;35m█[0;37m      [0;35m▀[0;91;45m▀[0;95;45m▒▓▄[0;95m█[0;95;45m▀[0;35m▀[0;37m  [0;35m█[0;95;45m▄▄▓[0;35m▌▐[0;95;45m▓▄[0;37m [0;35m▐[0;91;45m░░[0;95;45m░▓[0;95;47m▀[0;95;45m▄▄[0;37m  [0m
                [0;37m   [0;35m▀▀▀[0;37m      [0;35m▀▀▀[0;37m [0;35m▀▀▀[0;37m  [0;35m▀▀▀▀▀▀▀▀[0;37m       [0;35m▀▀[0;95m▀[0;35m▀[0;37m  [0;35m▀▀[0;37m  [0;35m▀▀▀▀▀▀▀▀[0;37m    [0;35m▀▀▀▀▀▀[0;37m    [0;35m▀▀▀▀[0;37m    [0;35m▀▀[0;95m▀[0;35m▀[0;37m  [0;35m▀▀[0;37m [0;35m▀▀▀▀▀▀[0;37m           [0;35m▀▀▀▀[0;37m      [0;35m▀▀▀[0;37m        [0;35m▀▀▀▀[0;37m    [0;35m▀▀[0;95m▀[0;35m▀[0;37m  [0;35m▀▀[0;37m  [0;35m▀▀▀▀▀▀▀▀[0;37m [0m""");
    }

    public static void pressEnterToContinue() {
        System.out.println();
        System.out.print("Press Enter to go back to the menu...");
        scanner.nextLine();
    }
}
