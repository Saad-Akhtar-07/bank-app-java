package com.bank.main;

import java.util.Calendar;
import java.util.Date;

import com.bank.branch.Branch;
import com.bank.model.Account;
import com.bank.model.CurrentAccount;
import com.bank.model.SavingsAccount;
import com.bank.person.Customer;
import com.bank.person.Manager;
import com.bank.person.RegionalManager;
import com.bank.person.Teller;

/**
 * Main application class for testing the banking system.
 * 
 * This class creates sample test data and demonstrates all the key
 * functionality required by the specification:
 * - Branch setup with manager and teller
 * - Customer registration and account creation
 * - Deposits and withdrawals (current and savings)
 * - Account suspension and unsuspension
 * - Regional manager oversight
 * - Year-end savings withdrawal reset
 * - Transaction logging
 * 
 * Run this class to see all features in action!
 * 
 * @author Bank Development Team
 * @version 1.0
 */
public class BankApplication {
    
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║          BANK SOFTWARE SYSTEM - TEST RUN         ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");
        
        // ---------- SETUP PHASE ----------
        System.out.println("=== PHASE 1: SETTING UP BRANCHES AND STAFF ===\n");
        
        // Create a couple of branches
        Branch liverpoolBranch = new Branch("Liverpool City Centre", "12-34-56", 
                "45 Castle Street, Liverpool, L2 9SH");
        Branch manchesterBranch = new Branch("Manchester Piccadilly", "12-34-78",
                "12 Piccadilly Gardens, Manchester, M1 1LU");
        
        System.out.println("Created branches:");
        System.out.println("  - " + liverpoolBranch.getName());
        System.out.println("  - " + manchesterBranch.getName());
        
        // Create managers and assign them to branches
        Date managerDOB = createDate(1975, 5, 15);
        Manager sarahManager = new Manager("Sarah Wilson", "22 Oak Avenue, Liverpool", 
                managerDOB, "swilson", "manager123");
        Manager johnManager = new Manager("John Roberts", "15 Pine Road, Manchester",
                createDate(1980, 3, 22), "jroberts", "secure456");
        
        liverpoolBranch.setManager(sarahManager);
        manchesterBranch.setManager(johnManager);
        
        // Create tellers for the Liverpool branch
        Teller teller1 = new Teller("Emma Thompson", "8 Birch Lane, Liverpool",
                createDate(1992, 8, 10), "ethompson", "teller001");
        Teller teller2 = new Teller("James Brown", "33 Elm Street, Liverpool",
                createDate(1995, 1, 28), "jbrown", "teller002");
        
        // Assign tellers to the branch
        teller1.setBranch(liverpoolBranch);
        teller2.setBranch(liverpoolBranch);
        liverpoolBranch.addStaff(teller1);
        liverpoolBranch.addStaff(teller2);
        
        // Create a regional manager overseeing both branches
        RegionalManager regionalManager = new RegionalManager("David Chen",
                "50 Victoria Street, London", createDate(1968, 11, 5),
                "dchen", "regional789");
        regionalManager.addBranch(liverpoolBranch);
        regionalManager.addBranch(manchesterBranch);
        
        System.out.println("\nStaff setup complete.");
        
        // ---------- CUSTOMER AND ACCOUNTS ----------
        System.out.println("\n=== PHASE 2: REGISTERING CUSTOMERS AND OPENING ACCOUNTS ===\n");
        
        // Create some customers
        Customer customer1 = new Customer("Alice Smith", "123 High Street, Liverpool",
                createDate(1988, 7, 20));
        Customer customer2 = new Customer("Bob Johnson", "456 Low Road, Liverpool",
                createDate(1975, 12, 3));
        Customer customer3 = new Customer("Carol Williams", "789 Middle Way, Liverpool",
                createDate(1990, 4, 15));
        
        // Teller adds customers to the branch
        teller1.addNewCustomer(customer1);
        teller1.addNewCustomer(customer2);
        teller2.addNewCustomer(customer3);
        
        // Open various types of accounts
        System.out.println("\n--- Opening Accounts ---");
        
        // Alice gets a current account with £500 overdraft
        CurrentAccount aliceCurrentAcc = new CurrentAccount(1000.0f, 500.0f);
        teller1.addNewAccount(aliceCurrentAcc, customer1);
        
        // Alice also wants a savings account - 3.5% interest rate
        SavingsAccount aliceSavingsAcc = new SavingsAccount(5000.0f, 0.035f);
        teller1.addNewAccount(aliceSavingsAcc, customer1);
        
        // Bob just wants a basic current account
        CurrentAccount bobCurrentAcc = new CurrentAccount(250.0f, 200.0f);
        teller2.addNewAccount(bobCurrentAcc, customer2);
        
        // Carol opens a savings account with higher interest
        SavingsAccount carolSavingsAcc = new SavingsAccount(10000.0f, 0.04f);
        teller2.addNewAccount(carolSavingsAcc, customer3);
        
        System.out.println("\nCustomers and accounts setup complete.");
        System.out.println(customer1);
        System.out.println(customer2);
        System.out.println(customer3);
        
        // ---------- TRANSACTIONS ----------
        System.out.println("\n=== PHASE 3: PERFORMING TRANSACTIONS ===\n");
        
        // Deposit to Alice's current account
        System.out.println("--- Deposit Test ---");
        teller1.deposit(500.0f, aliceCurrentAcc);
        
        // Withdrawal from Alice's current account
        System.out.println("\n--- Withdrawal Test (Current Account) ---");
        teller1.withdraw(200.0f, aliceCurrentAcc);
        
        // Test overdraft on Bob's account
        System.out.println("\n--- Overdraft Test ---");
        System.out.println("Bob's balance before: £" + bobCurrentAcc.getBalance());
        System.out.println("Attempting to withdraw £400 (balance is £250, overdraft £200)...");
        teller2.withdraw(400.0f, bobCurrentAcc);
        System.out.println("Bob is overdrawn: " + bobCurrentAcc.isOverdrawn());
        
        // Savings account withdrawal test
        System.out.println("\n--- Savings Withdrawal Test ---");
        System.out.println("Carol's savings: " + carolSavingsAcc);
        teller2.withdraw(1000.0f, carolSavingsAcc);
        teller2.withdraw(500.0f, carolSavingsAcc);
        teller2.withdraw(500.0f, carolSavingsAcc);
        teller2.withdraw(250.0f, carolSavingsAcc);
        
        // This 5th withdrawal should fail - exceeds yearly limit
        System.out.println("\n--- Attempting 5th withdrawal (should fail) ---");
        teller2.withdraw(100.0f, carolSavingsAcc);
        
        // ---------- SUSPENSION TEST ----------
        System.out.println("\n=== PHASE 4: ACCOUNT SUSPENSION ===\n");
        
        // Manager suspends Bob's account due to suspicious activity
        System.out.println("Manager investigates suspicious activity...");
        sarahManager.suspendAccount(bobCurrentAcc);
        
        // Try to deposit to suspended account - should fail
        System.out.println("\n--- Attempting transaction on suspended account ---");
        teller1.deposit(100.0f, bobCurrentAcc);
        
        // Now unsuspend it
        System.out.println("\n--- Unsuspending account ---");
        sarahManager.unsuspendAccount(bobCurrentAcc);
        
        // This should work now
        teller1.deposit(100.0f, bobCurrentAcc);
        
        // ---------- REPORTING ----------
        System.out.println("\n=== PHASE 5: REPORTING ===\n");
        
        // Manager generates Head Office report
        sarahManager.generateHeadOfficeReport();
        
        // Regional manager views branch balances
        System.out.println("--- Regional Manager Views ---");
        float liverpoolBalance = regionalManager.getTotalBranchBalance(liverpoolBranch);
        System.out.println("Liverpool branch total balance: £" + 
                String.format("%.2f", liverpoolBalance));
        
        // Full regional report
        regionalManager.generateRegionalReport();
        
        // ---------- YEAR-END RESET ----------
        System.out.println("\n=== PHASE 6: YEAR-END SAVINGS RESET ===\n");
        
        System.out.println("Before reset - Carol's remaining withdrawals: " + 
                carolSavingsAcc.getRemainingWithdrawals());
        
        // Simulate Dec 31 midnight operation
        liverpoolBranch.resetSavingsWDcount();
        
        System.out.println("After reset - Carol's remaining withdrawals: " + 
                carolSavingsAcc.getRemainingWithdrawals());
        
        // Now Carol can withdraw again
        System.out.println("\n--- Post-reset withdrawal test ---");
        teller2.withdraw(100.0f, carolSavingsAcc);
        
        // ---------- TRANSACTION HISTORY ----------
        System.out.println("\n=== PHASE 7: TRANSACTION HISTORY ===\n");
        
        System.out.println("Alice's Current Account Transaction History:");
        aliceCurrentAcc.getTransactionLog().printHistory();
        
        System.out.println("\nCarol's Savings Account Transaction History:");
        carolSavingsAcc.getTransactionLog().printHistory();
        
        // ---------- INTEREST CALCULATION ----------
        System.out.println("\n=== PHASE 8: INTEREST CALCULATION ===\n");
        
        System.out.println("Carol's savings balance: £" + carolSavingsAcc.getBalance());
        System.out.println("Interest rate: " + (carolSavingsAcc.getInterestRate() * 100) + "%");
        System.out.println("Daily interest earned: £" + 
                String.format("%.4f", carolSavingsAcc.calculateInterestOnBalance()));
        
        // Apply interest to see the effect
        carolSavingsAcc.applyDailyInterest();
        
        // ---------- FINAL SUMMARY ----------
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║              TEST RUN COMPLETE                   ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        System.out.println("\nFinal Account States:");
        System.out.println("  " + aliceCurrentAcc);
        System.out.println("  " + aliceSavingsAcc);
        System.out.println("  " + bobCurrentAcc);
        System.out.println("  " + carolSavingsAcc);
        
        System.out.println("\nBranch Summary:");
        System.out.println("  " + liverpoolBranch);
        System.out.println("  " + manchesterBranch);
        
        System.out.println("\n=== ALL TESTS COMPLETED SUCCESSFULLY ===");
    }
    
    /**
     * Helper method to create Date objects more easily.
     * Java's Date constructors are deprecated, so this wraps Calendar.
     * 
     * @param year The year
     * @param month The month (1-12)
     * @param day The day of month
     * @return A Date object for the specified date
     */
    private static Date createDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day); // Calendar months are 0-indexed
        return cal.getTime();
    }
}
