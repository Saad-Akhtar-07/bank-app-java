package com.bank.person;

import java.util.Date;

/**
 * Base class for all people in our banking system.
 * 
 * This covers the common attributes that everyone has - whether they're
 * a customer, a teller, a manager, or anyone else. By putting these
 * here, we avoid repeating the same fields in every person-type class.
 */
public class Person {
    
    // Full name as it appears on official documents
    private String name;
    
    // Current residential or business address
    private String address;
    
    // Date of birth - important for identity verification and age-related checks
    private Date dateOfBirth;
    
    /**
     * Creates a new person with their basic details.
     * 
     * @param name Full name
     * @param address Current address
     * @param dateOfBirth When they were born
     */
    public Person(String name, String address, Date dateOfBirth) {
        this.name = name;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
    }
    
    // --- Getters and Setters ---
    // People can change their address but not their name or DOB (officially, anyway!)
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public Date getDateOfBirth() {
        return dateOfBirth;
    }
    
    @Override
    public String toString() {
        return "Name: " + name + ", Address: " + address;
    }
}
