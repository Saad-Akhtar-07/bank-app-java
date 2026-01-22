package com.bank.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Keeps track of all transactions that have happened on an account.
 * Think of this like a bank statement - it records everything that's
 * gone in and out, so we can always look back and see the history.
 * 
 * Each Account has exactly one TransactionLog associated with it.
 */
public class TransactionLog {
    
    // We use an ArrayList here because we need fast appending (adding new transactions)
    // and occasional iteration through the history. Order matters - oldest first.
    private List<Transaction> transactions;
    
    /**
     * Creates a fresh, empty transaction log.
     * Every new account starts with a clean slate.
     */
    public TransactionLog() {
        this.transactions = new ArrayList<>();
    }
    
    /**
     * Adds a new transaction to the log.
     * This is called internally whenever money moves in or out of the account.
     * 
     * @param transaction The transaction to record
     */
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }
    
    /**
     * Calculates the total value of all transactions in the log.
     * This sums up deposits (positive) and withdrawals (negative) to give
     * the net movement of money through this account.
     * 
     * Note: This might not equal the current balance if there were initial deposits
     * or other adjustments, but it's useful for auditing purposes.
     * 
     * @return The sum of all transaction amounts
     */
    public float getTotalValue() {
        float total = 0.0f;
        
        // Loop through every transaction and add them up
        for (Transaction t : transactions) {
            total += t.getAmount();
        }
        
        return total;
    }
    
    /**
     * Gets a copy of all transactions for reporting purposes.
     * We return a new list to prevent external code from messing with our records!
     * 
     * @return A list containing all recorded transactions
     */
    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions); // Defensive copy
    }
    
    /**
     * How many transactions have been recorded so far?
     * Useful for statistics and reporting.
     * 
     * @return The number of transactions in the log
     */
    public int getTransactionCount() {
        return transactions.size();
    }
    
    /**
     * Prints out the complete transaction history.
     * Handy for generating statements or debugging.
     */
    public void printHistory() {
        System.out.println("=== Transaction History ===");
        if (transactions.isEmpty()) {
            System.out.println("No transactions recorded yet.");
        } else {
            for (int i = 0; i < transactions.size(); i++) {
                System.out.println((i + 1) + ". " + transactions.get(i));
            }
        }
        System.out.println("Total: £" + String.format("%.2f", getTotalValue()));
        System.out.println("===========================");
    }
}
