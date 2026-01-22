package com.bank.model;

/**
 * A savings account designed for customers who want to grow their money.
 * 
 * The main difference from a current account is that savings accounts:
 * 1. Pay interest on the balance (calculated daily)
 * 2. Have limited withdrawals - only 4 per year (Jan 1 to Dec 31)
 * 3. Cannot go overdrawn - you can only take out what's there
 * 
 * The withdrawal limit encourages saving behaviour and typically means
 * we can offer better interest rates than current accounts.
 */
public class SavingsAccount extends Account {
    
    // Annual interest rate as a decimal (e.g., 0.05 for 5%)
    private float interestRate;
    
    // How many times has the customer withdrawn money this year?
    // Resets to 0 at midnight on Dec 31st each year
    private int numberOfWithdrawalsThisYear;
    
    // The magic number - maximum 4 withdrawals allowed per calendar year
    private static final int MAX_WITHDRAWALS_PER_YEAR = 4;
    
    /**
     * Creates a new savings account with the specified balance and interest rate.
     * 
     * @param initialBalance Starting balance (should be positive for a savings account)
     * @param interestRate Annual interest rate as a decimal (e.g., 0.035 for 3.5%)
     */
    public SavingsAccount(float initialBalance, float interestRate) {
        super(initialBalance);
        this.interestRate = interestRate;
        this.numberOfWithdrawalsThisYear = 0; // Fresh start
    }
    
    /**
     * Withdraws money from the savings account.
     * 
     * There are several extra restrictions compared to current accounts:
     * - Can't withdraw more than the balance (no overdraft)
     * - Limited to 4 withdrawals per calendar year
     * 
     * @param amount How much to withdraw
     * @return true if successful, false if blocked for any reason
     */
    @Override
    public boolean withdraw(float amount) {
        // Standard validation checks
        if (amount <= 0) {
            System.out.println("Error: Withdrawal amount must be positive!");
            return false;
        }
        
        if (isSuspended()) {
            System.out.println("Error: Cannot withdraw from a suspended account.");
            return false;
        }
        
        if (isClosed()) {
            System.out.println("Error: Cannot withdraw from a closed account.");
            return false;
        }
        
        // Check the withdrawal limit - this is strict!
        if (numberOfWithdrawalsThisYear >= MAX_WITHDRAWALS_PER_YEAR) {
            System.out.println("Error: Withdrawal limit reached! Savings accounts are limited to " +
                    MAX_WITHDRAWALS_PER_YEAR + " withdrawals per year.");
            System.out.println("You have already made " + numberOfWithdrawalsThisYear + 
                    " withdrawals this year. Please wait until January 1st.");
            return false;
        }
        
        // Unlike current accounts, we can't go negative
        if (amount > balance) {
            System.out.println("Error: Insufficient funds. Balance: £" + 
                    String.format("%.2f", balance));
            return false;
        }
        
        // All good - process the withdrawal
        balance -= amount;
        numberOfWithdrawalsThisYear++;
        recordWithdrawal(amount);
        
        int remainingWithdrawals = MAX_WITHDRAWALS_PER_YEAR - numberOfWithdrawalsThisYear;
        System.out.println("Withdrew £" + String.format("%.2f", amount) + 
                " successfully. New balance: £" + String.format("%.2f", balance));
        System.out.println("Withdrawals remaining this year: " + remainingWithdrawals);
        
        return true;
    }
    
    /**
     * Calculates interest earned on the current balance.
     * 
     * This gives us the daily interest amount based on the annual rate.
     * In a real system, we'd run this at the end of each day and add
     * the interest to the balance.
     * 
     * Formula: (balance * annualRate) / 365
     * 
     * @return The amount of interest earned per day
     */
    public float calculateInterestOnBalance() {
        // Simple interest calculation - daily rate from annual rate
        float dailyInterest = (balance * interestRate) / 365.0f;
        return dailyInterest;
    }
    
    /**
     * Applies the daily interest to the account balance.
     * This would typically be called by an automated system each night.
     */
    public void applyDailyInterest() {
        if (balance > 0) {
            float interest = calculateInterestOnBalance();
            balance += interest;
            System.out.println("Daily interest of £" + String.format("%.4f", interest) + 
                    " applied. New balance: £" + String.format("%.2f", balance));
        }
    }
    
    /**
     * Resets the withdrawal counter back to zero.
     * 
     * This is called at midnight on December 31st by the branch's
     * year-end process. Customers get a fresh set of 4 withdrawals
     * for the new year.
     */
    public void resetWithdrawalCount() {
        this.numberOfWithdrawalsThisYear = 0;
        System.out.println("Account " + getAccountNumber() + 
                ": Withdrawal count reset for new year.");
    }
    
    // --- Getters ---
    
    public float getInterestRate() {
        return interestRate;
    }
    
    public int getNumberOfWithdrawalsThisYear() {
        return numberOfWithdrawalsThisYear;
    }
    
    public int getRemainingWithdrawals() {
        return MAX_WITHDRAWALS_PER_YEAR - numberOfWithdrawalsThisYear;
    }
    
    @Override
    public String getAccountType() {
        return "Savings Account";
    }
    
    @Override
    public String toString() {
        return super.toString() + 
               " | Interest Rate: " + String.format("%.2f%%", interestRate * 100) +
               " | Withdrawals Used: " + numberOfWithdrawalsThisYear + "/" + MAX_WITHDRAWALS_PER_YEAR;
    }
}
