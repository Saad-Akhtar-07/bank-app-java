package com.bank.branch;

import java.util.ArrayList;
import java.util.List;

import com.bank.model.Account;
import com.bank.model.SavingsAccount;
import com.bank.person.BankStaff;
import com.bank.person.Customer;
import com.bank.person.Manager;

/**
 * Represents a physical bank branch location.
 * 
 * A branch is the central hub where customers are registered, accounts
 * are managed, and staff work. Each branch:
 * - Has a unique sort code for identification
 * - Is managed by exactly one Manager
 * - Employs multiple BankStaff members (tellers, etc.)
 * - Services multiple Customers
 * 
 * The branch is also responsible for year-end operations like resetting
 * savings account withdrawal limits.
 * 
 * @author Bank Development Team
 * @version 1.0
 */
public class Branch {
    
    // Human-readable name for the branch (e.g., "Liverpool City Centre")
    private String name;
    
    // The sort code identifies which branch holds an account
    // Format typically like "12-34-56"
    private String sortCode;
    
    // Physical address of the branch
    private String address;
    
    // Every branch has exactly one manager in charge
    private Manager manager;
    
    // All the customers who bank at this branch
    private List<Customer> customers;
    
    // All staff members working at this branch (including the manager)
    private List<BankStaff> staff;
    
    /**
     * Creates a new branch with the given details.
     * Note: Manager should be assigned separately after creation.
     * 
     * @param name Branch name
     * @param sortCode Unique sort code
     * @param address Physical address
     */
    public Branch(String name, String sortCode, String address) {
        this.name = name;
        this.sortCode = sortCode;
        this.address = address;
        this.manager = null;
        this.customers = new ArrayList<>();
        this.staff = new ArrayList<>();
    }
    
    /**
     * Sets the manager for this branch.
     * Also adds them to the staff list and updates their branch reference.
     * 
     * @param manager The manager to assign
     */
    public void setManager(Manager manager) {
        this.manager = manager;
        manager.setBranch(this);
        
        // Add to staff if not already there
        if (!staff.contains(manager)) {
            staff.add(manager);
        }
        
        System.out.println(manager.getName() + " is now managing " + name);
    }
    
    /**
     * Adds a staff member to this branch.
     * Could be a teller or other staff type.
     * 
     * @param staffMember The staff to add
     */
    public void addStaff(BankStaff staffMember) {
        if (!staff.contains(staffMember)) {
            staff.add(staffMember);
            System.out.println(staffMember.getName() + " added to " + name + " staff.");
        }
    }
    
    /**
     * Removes a staff member from this branch.
     * 
     * @param staffMember The staff to remove
     */
    public void removeStaff(BankStaff staffMember) {
        if (staff.remove(staffMember)) {
            System.out.println(staffMember.getName() + " removed from " + name + " staff.");
        }
    }
    
    /**
     * Registers a new customer with this branch.
     * Called by tellers when someone opens their first account.
     * 
     * @param customer The new customer
     */
    public void addCustomer(Customer customer) {
        if (!customers.contains(customer)) {
            customers.add(customer);
            System.out.println("Customer " + customer.getName() + 
                    " registered at " + name);
        }
    }
    
    /**
     * Removes a customer from the branch.
     * Only happens when they close all accounts and leave.
     * 
     * @param customer The customer to remove
     */
    public void removeCustomer(Customer customer) {
        if (customers.remove(customer)) {
            System.out.println("Customer " + customer.getName() + 
                    " removed from " + name);
        }
    }
    
    /**
     * Checks if a customer is registered at this branch.
     * 
     * @param customer Customer to check
     * @return true if they're our customer
     */
    public boolean hasCustomer(Customer customer) {
        return customers.contains(customer);
    }
    
    /**
     * Resets the withdrawal count for ALL savings accounts at this branch.
     * 
     * This is the Dec 31st midnight operation mentioned in the requirements.
     * Every savings account gets a fresh set of 4 withdrawals for the new year.
     */
    public void resetSavingsWDcount() {
        System.out.println("\n=== YEAR-END RESET: " + name + " ===");
        System.out.println("Resetting savings account withdrawal counts...");
        
        int resetCount = 0;
        
        // Go through every customer and every account
        for (Customer customer : customers) {
            for (Account account : customer.getAccounts()) {
                // Only reset savings accounts
                if (account instanceof SavingsAccount) {
                    SavingsAccount savings = (SavingsAccount) account;
                    savings.resetWithdrawalCount();
                    resetCount++;
                }
            }
        }
        
        System.out.println("Reset complete. " + resetCount + " savings accounts processed.");
        System.out.println("===============================\n");
    }
    
    /**
     * Returns the total number of customers at this branch.
     * Required for Head Office reporting.
     * 
     * @return Number of registered customers
     */
    public int getNumberOfCustomers() {
        return customers.size();
    }
    
    /**
     * Returns the total number of accounts across all customers.
     * A customer might have multiple accounts, so this could be
     * higher than the customer count.
     * 
     * @return Total number of accounts at this branch
     */
    public int getNumberOfAccounts() {
        int total = 0;
        for (Customer customer : customers) {
            total += customer.getNumberOfAccounts();
        }
        return total;
    }
    
    // --- Getters ---
    
    public String getName() {
        return name;
    }
    
    public String getSortCode() {
        return sortCode;
    }
    
    public String getAddress() {
        return address;
    }
    
    public Manager getManager() {
        return manager;
    }
    
    public List<Customer> getCustomers() {
        return new ArrayList<>(customers); // Return copy for safety
    }
    
    public List<BankStaff> getStaff() {
        return new ArrayList<>(staff);
    }
    
    @Override
    public String toString() {
        return "Branch: " + name + " (" + sortCode + ") | " +
               "Customers: " + customers.size() + " | " +
               "Accounts: " + getNumberOfAccounts() + " | " +
               "Manager: " + (manager != null ? manager.getName() : "None");
    }
}
