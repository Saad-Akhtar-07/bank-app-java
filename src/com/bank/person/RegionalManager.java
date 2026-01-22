package com.bank.person;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bank.branch.Branch;
import com.bank.person.Customer;
import com.bank.model.Account;

/**
 * A regional manager who oversees multiple branches in a geographic area.
 * 
 * Regional managers sit above branch managers in the hierarchy. They don't
 * deal with individual customer accounts, but instead focus on the bigger
 * picture - how are the branches in their region performing overall?
 * 
 * Their main capability is viewing aggregate financial data across branches.
 */
public class RegionalManager extends BankStaff {
    
    // All the branches this regional manager is responsible for
    private List<Branch> branches;
    
    /**
     * Creates a new regional manager.
     * 
     * @param name Regional manager's name
     * @param address Their address
     * @param dateOfBirth Their DOB
     * @param loginID Login username
     * @param password Login password
     */
    public RegionalManager(String name, String address, Date dateOfBirth, 
                           String loginID, String password) {
        super(name, address, dateOfBirth, loginID, password);
        this.branches = new ArrayList<>();
    }
    
    /**
     * Adds a branch to this regional manager's oversight.
     * 
     * @param branch The branch to add to the region
     */
    public void addBranch(Branch branch) {
        if (!branches.contains(branch)) {
            branches.add(branch);
            System.out.println("Branch " + branch.getName() + 
                    " added to regional manager " + getName() + "'s oversight.");
        }
    }
    
    /**
     * Removes a branch from oversight (e.g., if regions are restructured).
     * 
     * @param branch The branch to remove
     */
    public void removeBranch(Branch branch) {
        if (branches.remove(branch)) {
            System.out.println("Branch " + branch.getName() + 
                    " removed from regional manager " + getName() + "'s oversight.");
        }
    }
    
    /**
     * Gets the total balance of ALL accounts in a specific branch.
     * 
     * This is a key metric for regional managers - it shows how much
     * customer money is held at each branch, which indicates branch size
     * and importance.
     * 
     * Important: Only works for branches this regional manager oversees!
     * 
     * @param branch The branch to calculate total balance for
     * @return Total of all account balances, or -1 if branch not in region
     */
    public float getTotalBranchBalance(Branch branch) {
        // First check - do we actually manage this branch?
        if (!branches.contains(branch)) {
            System.out.println("Error: Branch " + branch.getName() + 
                    " is not under this regional manager's oversight.");
            return -1;
        }
        
        float totalBalance = 0.0f;
        
        // Go through every customer at the branch
        for (Customer customer : branch.getCustomers()) {
            // And add up all their account balances
            for (Account account : customer.getAccounts()) {
                totalBalance += account.getBalance();
            }
        }
        
        return totalBalance;
    }
    
    /**
     * Generates a summary report for all branches in the region.
     * Useful for getting a quick overview of regional performance.
     */
    public void generateRegionalReport() {
        System.out.println("\n============ REGIONAL REPORT ============");
        System.out.println("Regional Manager: " + getName());
        System.out.println("Number of Branches: " + branches.size());
        System.out.println("-----------------------------------------");
        
        float totalRegionalBalance = 0.0f;
        int totalCustomers = 0;
        int totalAccounts = 0;
        
        for (Branch branch : branches) {
            float branchBalance = getTotalBranchBalance(branch);
            int customers = branch.getNumberOfCustomers();
            int accounts = branch.getNumberOfAccounts();
            
            System.out.println("\nBranch: " + branch.getName());
            System.out.println("  Customers: " + customers);
            System.out.println("  Accounts: " + accounts);
            System.out.println("  Total Balance: £" + String.format("%.2f", branchBalance));
            
            totalRegionalBalance += branchBalance;
            totalCustomers += customers;
            totalAccounts += accounts;
        }
        
        System.out.println("\n-----------------------------------------");
        System.out.println("REGIONAL TOTALS:");
        System.out.println("  Total Customers: " + totalCustomers);
        System.out.println("  Total Accounts: " + totalAccounts);
        System.out.println("  Total Balance: £" + String.format("%.2f", totalRegionalBalance));
        System.out.println("=========================================\n");
    }
    
    // --- Getters ---
    
    public List<Branch> getBranches() {
        return new ArrayList<>(branches); // Return copy for safety
    }
    
    public int getNumberOfBranches() {
        return branches.size();
    }
    
    @Override
    public String toString() {
        return "Regional Manager: " + getName() + " | Overseeing " + 
                branches.size() + " branches";
    }
}
