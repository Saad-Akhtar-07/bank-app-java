package com.bank.model;

import java.util.Date;

/**
 * Abstract base class for all types of bank accounts.
 * 
 * We made this abstract because you can't just have a generic "account" -
 * it has to be either a CurrentAccount or a SavingsAccount, each with
 * their own specific rules and features.
 * 
 * Common functionality like balance tracking, suspension, and transaction
 * logging is handled here so we don't have to repeat ourselves in subclasses.
 */
public abstract class Account {
    
    // Unique identifier for this account - assigned when account is created
    private int accountNumber;
    
    // How much money is currently in the account
    // For current accounts, this can go negative (up to overdraft limit)
    protected float balance;
    
    // Security flag - if true, all transactions are blocked
    // Only managers can suspend/unsuspend accounts
    private boolean isSuspended;
    
    // When was this account first opened?
    private Date accountOpenedDate;
    
    // When was title account closed? Null if still active
    private Date accountClosedDate;
    
    // Every account keeps its own transaction log for audit purposes
    private TransactionLog transactionLog;
    
    // We use this to generate unique account numbers automatically
    private static int nextAccountNumber = 10001;
    
    /**
     * Creates a new bank account with the specified initial balance.
     * Account number is generated automatically to ensure uniqueness.
     * 
     * @param initialBalance Starting balance for the account
     */
    public Account(float initialBalance) {
        this.accountNumber = nextAccountNumber++;
        this.balance = initialBalance;
        this.isSuspended = false;
        this.accountOpenedDate = new Date();
        this.accountClosedDate = null; // Not closed yet!
        this.transactionLog = new TransactionLog();
        
        // If there's an initial deposit, record it in the transaction log
        if (initialBalance > 0) {
            transactionLog.addTransaction(new Transaction(initialBalance));
        }
    }
    
    /**
     * Deposits money into the account.
     * This will fail if the account is suspended - can't touch frozen accounts!
     * 
     * @param amount The amount to deposit (must be positive)
     * @return true if deposit succeeded, false otherwise
     */
    public boolean deposit(float amount) {
        // Quick sanity checks first
        if (amount <= 0) {
            System.out.println("Error: Deposit amount must be positive!");
            return false;
        }
        
        if (isSuspended) {
            System.out.println("Error: Cannot deposit to a suspended account.");
            return false;
        }
        
        if (accountClosedDate != null) {
            System.out.println("Error: Cannot deposit to a closed account.");
            return false;
        }
        
        // All good - add the money and log it
        balance += amount;
        transactionLog.addTransaction(new Transaction(amount));
        System.out.println("Deposited £" + String.format("%.2f", amount) + 
                " successfully. New balance: £" + String.format("%.2f", balance));
        return true;
    }
    
    /**
     * Withdraws money from the account.
     * Each account type (Current/Savings) has its own rules for withdrawals,
     * so this is abstract and must be implemented by subclasses.
     * 
     * @param amount The amount to withdraw
     * @return true if withdrawal succeeded, false otherwise
     */
    public abstract boolean withdraw(float amount);
    
    /**
     * Helper method for subclasses to record withdrawals in the log.
     * We record withdrawals as negative amounts so the math works out.
     * 
     * @param amount The withdrawal amount (positive number)
     */
    protected void recordWithdrawal(float amount) {
        transactionLog.addTransaction(new Transaction(-amount));
    }
    
    /**
     * Checks if the account is currently suspended.
     * Suspended accounts can't do any transactions until a manager lifts the suspension.
     */
    public boolean isSuspended() {
        return isSuspended;
    }
    
    /**
     * Suspends this account - only managers should call this!
     * Used when there are security concerns or legal issues.
     */
    public void suspend() {
        this.isSuspended = true;
        System.out.println("Account " + accountNumber + " has been SUSPENDED.");
    }
    
    /**
     * Removes the suspension from this account.
     * Account can resume normal operations after this.
     */
    public void unsuspend() {
        this.isSuspended = false;
        System.out.println("Account " + accountNumber + " has been UNSUSPENDED.");
    }
    
    /**
     * Marks this account as closed.
     * Once closed, no more transactions can happen.
     */
    public void closeAccount() {
        this.accountClosedDate = new Date();
        System.out.println("Account " + accountNumber + " has been closed.");
    }
    
    /**
     * Checks if this account has been closed.
     */
    public boolean isClosed() {
        return accountClosedDate != null;
    }
    
    // --- Standard Getters ---
    
    public int getAccountNumber() {
        return accountNumber;
    }
    
    public float getBalance() {
        return balance;
    }
    
    public Date getAccountOpenedDate() {
        return accountOpenedDate;
    }
    
    public Date getAccountClosedDate() {
        return accountClosedDate;
    }
    
    public TransactionLog getTransactionLog() {
        return transactionLog;
    }
    
    /**
     * Returns a string describing what type of account this is.
     * Subclasses should override this to return their specific type.
     */
    public abstract String getAccountType();
    
    @Override
    public String toString() {
        String status = isSuspended ? " [SUSPENDED]" : "";
        status += isClosed() ? " [CLOSED]" : "";
        return String.format("%s #%d - Balance: £%.2f%s", 
                getAccountType(), accountNumber, balance, status);
    }
}
