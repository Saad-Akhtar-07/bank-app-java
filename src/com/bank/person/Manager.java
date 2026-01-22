package com.bank.person;

import java.util.Date;

import com.bank.model.Account;
import com.bank.branch.Branch;

/**
 * A branch manager - responsible for overseeing a single branch.
 * 
 * Managers have elevated privileges compared to tellers:
 * - They can suspend accounts when there are security or legal concerns
 * - They can unsuspend accounts once matters are resolved
 * - They report branch statistics to Head Office on demand
 * 
 * Each branch has exactly one manager.
 */
public class Manager extends BankStaff {
    
    // The branch this manager is responsible for
    private Branch branch;
    
    /**
     * Creates a new branch manager.
     * 
     * @param name Manager's name
     * @param address Manager's address
     * @param dateOfBirth Manager's DOB
     * @param loginID Login username
     * @param password Login password
     */
    public Manager(String name, String address, Date dateOfBirth, 
                   String loginID, String password) {
        super(name, address, dateOfBirth, loginID, password);
        this.branch = null;
    }
    
    /**
     * Assigns this manager to run a branch.
     * 
     * @param branch The branch they'll be managing
     */
    public void setBranch(Branch branch) {
        this.branch = branch;
    }
    
    public Branch getBranch() {
        return branch;
    }
    
    /**
     * Suspends an account due to security or legal concerns.
     * 
     * When an account is suspended, absolutely no transactions can happen -
     * not deposits, not withdrawals, nothing. This is a serious action
     * typically used when:
     * - Fraud is suspected
     * - There's a court order
     * - The customer has reported their card stolen
     * - There's unusual activity that needs investigation
     * 
     * @param account The account to freeze
     */
    public void suspendAccount(Account account) {
        if (account.isSuspended()) {
            System.out.println("Account " + account.getAccountNumber() + 
                    " is already suspended.");
            return;
        }
        
        account.suspend();
        System.out.println("Manager " + getName() + " suspended account " + 
                account.getAccountNumber());
    }
    
    /**
     * Removes the suspension from an account.
     * 
     * This should only be done once any investigation is complete and
     * we're satisfied there's no ongoing issue. The account can then
     * resume normal operations.
     * 
     * @param account The account to unfreeze
     */
    public void unsuspendAccount(Account account) {
        if (!account.isSuspended()) {
            System.out.println("Account " + account.getAccountNumber() + 
                    " is not currently suspended.");
            return;
        }
        
        account.unsuspend();
        System.out.println("Manager " + getName() + " unsuspended account " + 
                account.getAccountNumber());
    }
    
    /**
     * Generates a report for Head Office showing branch statistics.
     * This is typically requested periodically for overall bank reporting.
     */
    public void generateHeadOfficeReport() {
        if (branch == null) {
            System.out.println("Error: Manager is not assigned to any branch!");
            return;
        }
        
        System.out.println("\n========== HEAD OFFICE REPORT ==========");
        System.out.println("Branch: " + branch.getName());
        System.out.println("Sort Code: " + branch.getSortCode());
        System.out.println("Manager: " + getName());
        System.out.println("-----------------------------------------");
        System.out.println("Total Customers: " + branch.getNumberOfCustomers());
        System.out.println("Total Accounts: " + branch.getNumberOfAccounts());
        System.out.println("=========================================\n");
    }
    
    @Override
    public String toString() {
        String branchInfo = branch != null ? branch.getName() : "Unassigned";
        return "Manager: " + getName() + " | Managing: " + branchInfo;
    }
}
