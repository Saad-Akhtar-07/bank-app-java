package com.bank.person;

import java.util.Date;

/**
 * Base class for all bank employees who need system access.
 * 
 * This adds login credentials on top of the basic Person info.
 * Different types of staff (Teller, Manager, RegionalManager) extend
 * this to add their specific capabilities.
 */
public class BankStaff extends Person {
    
    // Unique identifier for logging into bank systems
    private String loginID;
    
    // Should be stored hashed in a real system, but keeping it simple here
    private String password;
    
    /**
     * Creates a new bank staff member.
     * 
     * @param name Staff member's full name
     * @param address Staff member's address
     * @param dateOfBirth Staff member's DOB
     * @param loginID Their unique login username
     * @param password Their password (in practice, this would be hashed)
     */
    public BankStaff(String name, String address, Date dateOfBirth, 
                     String loginID, String password) {
        super(name, address, dateOfBirth);
        this.loginID = loginID;
        this.password = password;
    }
    
    /**
     * Verifies login credentials.
     * In a real system, we'd hash the attempted password and compare hashes.
     * 
     * @param attemptedLoginID The login ID being tried
     * @param attemptedPassword The password being tried
     * @return true if credentials match
     */
    public boolean authenticate(String attemptedLoginID, String attemptedPassword) {
        return this.loginID.equals(attemptedLoginID) && 
               this.password.equals(attemptedPassword);
    }
    
    // --- Getters (no setter for loginID - that shouldn't change) ---
    
    public String getLoginID() {
        return loginID;
    }
    
    /**
     * Allows password changes - important for security.
     * In a real system, we'd have more validation here.
     */
    public void setPassword(String newPassword) {
        this.password = newPassword;
        System.out.println("Password updated for " + getLoginID());
    }
    
    @Override
    public String toString() {
        return "Staff: " + getName() + " (Login: " + loginID + ")";
    }
}
