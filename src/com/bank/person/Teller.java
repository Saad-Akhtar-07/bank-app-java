package com.bank.person;

import java.util.Date;

import com.bank.model.Account;
import com.bank.branch.Branch;

/**
 * A bank teller - the front-line staff who deal with customers day to day.
 * 
 * Tellers can:
 * - Add new customers to the branch
 * - Open and close accounts
 * - Process deposits and withdrawals on behalf of customers
 * 
 * They can't do things like suspend accounts - that's manager-level stuff.
 */
public class Teller extends BankStaff {
    
    // Which branch does this teller work at?
    // Important for knowing where to add customers
    private Branch branch;
    
    /**
     * Creates a new teller for a specific branch.
     * 
     * @param name Teller's name
     * @param address Teller's address
     * @param dateOfBirth Teller's DOB
     * @param loginID Login username
     * @param password Login password
     */
    public Teller(String name, String address, Date dateOfBirth, 
                  String loginID, String password) {
        super(name, address, dateOfBirth, loginID, password);
        this.branch = null; // Will be set when assigned to a branch
    }
    
    /**
     * Assigns this teller to work at a branch.
     * 
     * @param branch The branch where they'll be working
     */
    public void setBranch(Branch branch) {
        this.branch = branch;
    }
    
    public Branch getBranch() {
        return branch;
    }
    
    /**
     * Adds a new customer to the branch.
     * This is the first step when someone wants to bank with us.
     * 
     * @param customer The new customer to register
     */
    public void addNewCustomer(Customer customer) {
        if (branch == null) {
            System.out.println("Error: Teller is not assigned to any branch!");
            return;
        }
        
        branch.addCustomer(customer);
        System.out.println("Teller " + getName() + " added new customer: " + customer.getName());
    }
    
    /**
     * Opens a new account for an existing customer.
     * The customer must already be registered with the branch.
     * 
     * @param account The new account to open
     * @param customer The customer who will own the account
     */
    public void addNewAccount(Account account, Customer customer) {
        if (branch == null) {
            System.out.println("Error: Teller is not assigned to any branch!");
            return;
        }
        
        // Make sure this customer is actually one of ours
        if (!branch.hasCustomer(customer)) {
            System.out.println("Error: Customer " + customer.getName() + 
                    " is not registered at this branch!");
            return;
        }
        
        customer.addAccount(account);
        System.out.println("Teller " + getName() + " opened new " + 
                account.getAccountType() + " for " + customer.getName());
    }
    
    /**
     * Closes an existing account.
     * Any remaining balance should be withdrawn first in practice.
     * 
     * @param account The account to close
     */
    public void closeAccount(Account account) {
        if (account.isSuspended()) {
            System.out.println("Error: Cannot close a suspended account. " +
                    "Please contact a manager first.");
            return;
        }
        
        account.closeAccount();
        System.out.println("Teller " + getName() + " closed account " + 
                account.getAccountNumber());
    }
    
    /**
     * Deposits money into a customer's account.
     * The transaction is logged automatically by the Account class.
     * 
     * @param amount How much to deposit
     * @param account Which account to deposit into
     */
    public void deposit(float amount, Account account) {
        System.out.println("Teller " + getName() + " processing deposit...");
        boolean success = account.deposit(amount);
        if (success) {
            System.out.println("Deposit completed successfully.");
        }
    }
    
    /**
     * Withdraws money from a customer's account.
     * The account's own validation will handle things like overdraft limits
     * and savings withdrawal restrictions.
     * 
     * @param amount How much to withdraw
     * @param account Which account to withdraw from
     */
    public void withdraw(float amount, Account account) {
        System.out.println("Teller " + getName() + " processing withdrawal...");
        boolean success = account.withdraw(amount);
        if (success) {
            System.out.println("Withdrawal completed successfully.");
        }
    }
    
    @Override
    public String toString() {
        String branchInfo = branch != null ? branch.getName() : "Unassigned";
        return "Teller: " + getName() + " | Branch: " + branchInfo;
    }
}
