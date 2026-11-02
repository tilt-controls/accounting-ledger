package com.pluralsight;
import java.util.*;
import java.time.*;
import java.io.*;

public class App {
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {

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

    public static ArrayList<Transaction> getTransactions() {
        ArrayList<Transaction> transactions = new ArrayList<Transaction>();
        return transactions;
    }

    private static ArrayList<Transaction> loadTransactions() {
        ArrayList<Transaction> transactions = getTransactions();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("transactions.csv"));
            String line;

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

    public static void pressEnterToContinue() {
        System.out.println();
        System.out.print("Press Enter to go back to the menu...");
        scanner.nextLine();
    }
}
