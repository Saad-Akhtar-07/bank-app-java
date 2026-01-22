package com.bank.model;

/**
 * A current account for everyday banking transactions.
 * 
 * The main feature that sets this apart from a savings account is the overdraft.
 * Customers can spend more than they have (up to a limit), which is useful for
 * unexpected expenses, but they should pay it back as soon as possible.
 * 
 * Unlike savings accounts, there's no limit on how many times you can withdraw.
 */
public class CurrentAccount extends Account {
    
    // How far into the red can this customer go?
    // This is agreed with the bank and varies by customer creditworthiness
    private float overdraftLimit;
    
    /**
     * Creates a new current account with the specified balance and overdraft limit.
     * 
     * @param initialBalance Starting balance (can be 0 for new accounts)
     * @param overdraftLimit How much the customer can borrow (must be positive or 0)
     */
    public CurrentAccount(float initialBalance, float overdraftLimit) {
        super(initialBalance);
        
        // Overdraft limit should never be negative - that makes no sense
        this.overdraftLimit = Math.max(0, overdraftLimit);
    }
    
    /**
     * Creates a current account with no overdraft facility.
     * Some customers prefer to avoid the temptation of going overdrawn!
     * 
     * @param initialBalance Starting balance
     */
    public CurrentAccount(float initialBalance) {
        this(initialBalance, 0.0f);
    }
    
    /**
     * Withdraws money from the current account.
     * 
     * The clever bit here is that we allow the balance to go negative,
     * but only up to the agreed overdraft limit. So if you have £100
     * and a £500 overdraft, you can actually withdraw up to £600.
     * 
     * @param amount How much to withdraw
     * @return true if the withdrawal went through, false if blocked
     */
    @Override
    public boolean withdraw(float amount) {
        // Basic validation first
        if (amount <= 0) {
            System.out.println("Error: Withdrawal amount must be positive!");
            return false;
        }
        
        // Can't touch suspended accounts - might be fraud investigation etc.
        if (isSuspended()) {
            System.out.println("Error: Cannot withdraw from a suspended account.");
            return false;
        }
        
        // Obviously can't use closed accounts
        if (isClosed()) {
            System.out.println("Error: Cannot withdraw from a closed account.");
            return false;
        }
        
        // Here's the key calculation - would this withdrawal take us past the overdraft limit?
        float availableFunds = getAvailableFunds();
        if (amount > availableFunds) {
            System.out.println("Error: Insufficient funds. Available: £" + 
                    String.format("%.2f", availableFunds) + 
                    " (including £" + String.format("%.2f", overdraftLimit) + " overdraft)");
            return false;
        }
        
        // All checks passed - make the withdrawal
        balance -= amount;
        recordWithdrawal(amount);
        
        System.out.println("Withdrew £" + String.format("%.2f", amount) + 
                " successfully. New balance: £" + String.format("%.2f", balance));
        
        // Friendly warning if they've gone into the overdraft
        if (isOverdrawn()) {
            System.out.println("Warning: You are now overdrawn by £" + 
                    String.format("%.2f", Math.abs(balance)));
        }
        
        return true;
    }
    
    /**
     * Checks if the account is currently in the red.
     * Being overdrawn isn't necessarily bad, but customers should be aware
     * and aim to get back into positive territory.
     * 
     * @return true if balance is negative
     */
    public boolean isOverdrawn() {
        return balance < 0;
    }
    
    /**
     * Calculates how much money the customer can actually spend.
     * This is their current balance PLUS whatever overdraft they have available.
     * 
     * For example: balance of £200 with £500 overdraft = £700 available
     * Or: balance of -£100 with £500 overdraft = £400 available
     * 
     * @return The total amount that can be withdrawn right now
     */
    public float getAvailableFunds() {
        return balance + overdraftLimit;
    }
    
    // --- Getters and Setters ---
    
    public float getOverdraftLimit() {
        return overdraftLimit;
    }
    
    /**
     * Updates the overdraft limit - typically done after a credit review.
     * Can only be done by bank staff, not by the customer.
     */
    public void setOverdraftLimit(float newLimit) {
        if (newLimit >= 0) {
            this.overdraftLimit = newLimit;
            System.out.println("Overdraft limit updated to £" + String.format("%.2f", newLimit));
        }
    }
    
    @Override
    public String getAccountType() {
        return "Current Account";
    }
    
    @Override
    public String toString() {
        return super.toString() + " | Overdraft Limit: £" + String.format("%.2f", overdraftLimit) +
               " | Available: £" + String.format("%.2f", getAvailableFunds());
    }
}
