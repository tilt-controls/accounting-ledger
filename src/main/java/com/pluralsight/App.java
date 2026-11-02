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
        }
    }
}
