package com.bank.person;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bank.model.Account;

/**
 * Represents a bank customer - someone who holds accounts with us.
 * 
 * Customers can have multiple accounts (current, savings, or both) and
 * we track when they first joined so we know our long-standing customers.
 * 
 * Note: Customers don't interact with the system directly - they go through
 * a Teller who performs operations on their behalf.
 */
public class Customer extends Person {
    
    // When did this person become our customer?
    private Date joinedDate;
    
    // A customer can have multiple accounts - typically a current and savings
    private List<Account> accounts;
    
    /**
     * Creates a new customer record.
     * The joined date is automatically set to today.
     * 
     * @param name Customer's full name
     * @param address Customer's address
     * @param dateOfBirth Customer's date of birth
     */
    public Customer(String name, String address, Date dateOfBirth) {
        super(name, address, dateOfBirth);
        this.joinedDate = new Date(); // They're joining right now
        this.accounts = new ArrayList<>();
    }
    
    /**
     * Adds an account to this customer's portfolio.
     * Called by Tellers when opening new accounts.
     * 
     * @param account The account to add
     */
    public void addAccount(Account account) {
        accounts.add(account);
        System.out.println("Account " + account.getAccountNumber() + 
                " added to customer " + getName());
    }
    
    /**
     * Removes an account from the customer's portfolio.
     * Used when closing accounts.
     * 
     * @param account The account to remove
     * @return true if removed, false if it wasn't found
     */
    public boolean removeAccount(Account account) {
        return accounts.remove(account);
    }
    
    /**
     * Calculates the total balance across ALL this customer's accounts.
     * 
     * This is useful for getting a quick snapshot of how much the customer
     * has with us overall, though it doesn't account for overdrafts being used.
     * 
     * @return Sum of all account balances
     */
    public float getTotalBalance() {
        float total = 0.0f;
        for (Account acc : accounts) {
            total += acc.getBalance();
        }
        return total;
    }
    
    /**
     * Finds an account by its number.
     * Handy when a customer comes in with their account number.
     * 
     * @param accountNumber The account number to search for
     * @return The account if found, null otherwise
     */
    public Account getAccountByNumber(int accountNumber) {
        for (Account acc : accounts) {
            if (acc.getAccountNumber() == accountNumber) {
                return acc;
            }
        }
        return null;
    }
    
    // --- Getters ---
    
    public Date getJoinedDate() {
        return joinedDate;
    }
    
    public List<Account> getAccounts() {
        return new ArrayList<>(accounts); // Return a copy for safety
    }
    
    public int getNumberOfAccounts() {
        return accounts.size();
    }
    
    @Override
    public String toString() {
        return "Customer: " + getName() + 
               " | Accounts: " + accounts.size() + 
               " | Total Balance: £" + String.format("%.2f", getTotalBalance()) +
               " | Member since: " + joinedDate;
    }
}
