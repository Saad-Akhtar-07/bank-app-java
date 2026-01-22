package com.bank.model;

import java.util.Date;

/**
 * Represents a single financial transaction within the banking system.
 * Every time money moves in or out of an account, we create one of these
 * to keep a permanent record of what happened and when.
 */
public class Transaction {
    
    // The monetary value of this transaction - positive for deposits, negative for withdrawals
    private float amount;
    
    // When did this transaction actually happen? Important for audit trails
    private Date dateOfTransaction;
    
    /**
     * Creates a new transaction record.
     * We automatically stamp it with the current date and time so there's
     * no way to backdate transactions - important for security!
     * 
     * @param amount The amount of money involved (positive = deposit, negative = withdrawal)
     */
    public Transaction(float amount) {
        this.amount = amount;
        this.dateOfTransaction = new Date(); // Grab the current timestamp right now
    }
    
    /**
     * Sometimes we need to recreate historical transactions from logs,
     * so this constructor lets us specify an exact date.
     * 
     * @param amount The transaction amount
     * @param dateOfTransaction The specific date/time of the transaction
     */
    public Transaction(float amount, Date dateOfTransaction) {
        this.amount = amount;
        this.dateOfTransaction = dateOfTransaction;
    }
    
    // --- Getters ---
    
    public float getAmount() {
        return amount;
    }
    
    public Date getDateOfTransaction() {
        return dateOfTransaction;
    }
    
    /**
     * Nice string representation for debugging and logging purposes.
     * Makes it easy to see what happened at a glance.
     */
    @Override
    public String toString() {
        String type = amount >= 0 ? "DEPOSIT" : "WITHDRAWAL";
        return String.format("[%s] %s: £%.2f on %s", 
                type, 
                type.equals("DEPOSIT") ? "+" : "-",
                Math.abs(amount), 
                dateOfTransaction.toString());
    }
}
