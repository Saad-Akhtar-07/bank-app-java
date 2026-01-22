package com.bank.gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;
import java.util.Date;

import com.bank.branch.Branch;
import com.bank.model.*;
import com.bank.person.*;

/**
 * Main GUI application for the Bank Software System.
 * 
 * This provides a simple but functional interface with three tabs:
 * - Teller: Add customers, open accounts, deposit/withdraw
 * - Manager: Suspend/unsuspend accounts, view branch reports
 * - Regional Manager: View total balances across branches
 * 
 * Test data is pre-loaded when the application starts so you can
 * immediately start testing all the features.
 * 
 * @author Bank Development Team
 * @version 1.0
 */
public class BankGUI extends JFrame {
    
    // Colour scheme - keeping it professional but modern
    private static final Color PRIMARY_COLOR = new Color(25, 55, 109);    // Dark blue
    private static final Color SECONDARY_COLOR = new Color(52, 152, 219); // Light blue
    private static final Color ACCENT_COLOR = new Color(46, 204, 113);    // Green
    private static final Color WARNING_COLOR = new Color(231, 76, 60);    // Red
    private static final Color BG_COLOR = new Color(245, 247, 250);       // Light grey
    
    // Core data - our test branch and staff
    private Branch liverpoolBranch;
    private Branch manchesterBranch;
    private Manager manager;
    private Teller teller;
    private RegionalManager regionalManager;
    
    // GUI components we need to access from multiple methods
    private JComboBox<String> customerDropdown;
    private JComboBox<String> accountDropdown;
    private JTextArea outputArea;
    private JTabbedPane tabbedPane;
    
    /**
     * Fires up the GUI with all our test data ready to go.
     */
    public BankGUI() {
        initializeTestData();
        setupFrame();
        createUI();
        setVisible(true);
    }
    
    /**
     * Sets up the main window properties.
     */
    private void setupFrame() {
        setTitle("Bank Software System - Liverpool City Centre Branch");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centre on screen
        getContentPane().setBackground(BG_COLOR);
    }
    
    /**
     * Creates all the UI components and assembles them.
     */
    private void createUI() {
        setLayout(new BorderLayout(10, 10));
        
        // Header panel with bank logo/title
        add(createHeaderPanel(), BorderLayout.NORTH);
        
        // Main tabbed pane for different user roles
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(BG_COLOR);
        
        tabbedPane.addTab("🏧 Teller Operations", createTellerPanel());
        tabbedPane.addTab("👔 Branch Manager", createManagerPanel());
        tabbedPane.addTab("🌍 Regional Manager", createRegionalPanel());
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Output area at the bottom for showing results
        add(createOutputPanel(), BorderLayout.SOUTH);
    }
    
    /**
     * Creates the header with bank branding.
     */
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY_COLOR);
        header.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("🏦 Bank Software System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel branchLabel = new JLabel("Liverpool City Centre Branch | Sort Code: 12-34-56");
        branchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        branchLabel.setForeground(new Color(200, 200, 200));
        
        header.add(titleLabel, BorderLayout.WEST);
        header.add(branchLabel, BorderLayout.EAST);
        
        return header;
    }
    
    /**
     * Creates the Teller operations panel.
     * Tellers can: add customers, open/close accounts, deposit/withdraw
     */
    private JPanel createTellerPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_COLOR);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Left side - Customer and Account selection
        JPanel selectionPanel = new JPanel(new GridBagLayout());
        selectionPanel.setBackground(Color.WHITE);
        selectionPanel.setBorder(createTitledBorder("Customer & Account Selection"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Customer dropdown
        gbc.gridx = 0; gbc.gridy = 0;
        selectionPanel.add(new JLabel("Select Customer:"), gbc);
        
        customerDropdown = new JComboBox<>();
        refreshCustomerDropdown();
        customerDropdown.addActionListener(e -> refreshAccountDropdown());
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        selectionPanel.add(customerDropdown, gbc);
        
        // Account dropdown
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        selectionPanel.add(new JLabel("Select Account:"), gbc);
        
        accountDropdown = new JComboBox<>();
        refreshAccountDropdown();
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        selectionPanel.add(accountDropdown, gbc);
        
        // Refresh button
        JButton refreshBtn = createStyledButton("🔄 Refresh", SECONDARY_COLOR);
        refreshBtn.addActionListener(e -> {
            refreshCustomerDropdown();
            refreshAccountDropdown();
            appendOutput("Customer and account lists refreshed.");
        });
        gbc.gridx = 1; gbc.gridy = 2;
        selectionPanel.add(refreshBtn, gbc);
        
        // Right side - Operations panels
        JPanel operationsPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        operationsPanel.setBackground(BG_COLOR);
        
        // Transaction panel
        JPanel transactionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        transactionPanel.setBackground(Color.WHITE);
        transactionPanel.setBorder(createTitledBorder("Transactions"));
        
        JTextField amountField = new JTextField(10);
        transactionPanel.add(new JLabel("Amount (£):"));
        transactionPanel.add(amountField);
        
        JButton depositBtn = createStyledButton("💰 Deposit", ACCENT_COLOR);
        depositBtn.addActionListener(e -> performDeposit(amountField));
        transactionPanel.add(depositBtn);
        
        JButton withdrawBtn = createStyledButton("💸 Withdraw", WARNING_COLOR);
        withdrawBtn.addActionListener(e -> performWithdrawal(amountField));
        transactionPanel.add(withdrawBtn);
        
        JButton historyBtn = createStyledButton("📜 History", SECONDARY_COLOR);
        historyBtn.addActionListener(e -> showTransactionHistory());
        transactionPanel.add(historyBtn);
        
        operationsPanel.add(transactionPanel);
        
        // Customer/Account management panel
        JPanel managementPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        managementPanel.setBackground(Color.WHITE);
        managementPanel.setBorder(createTitledBorder("Customer & Account Management"));
        
        JButton addCustomerBtn = createStyledButton("➕ Add Customer", ACCENT_COLOR);
        addCustomerBtn.addActionListener(e -> showAddCustomerDialog());
        managementPanel.add(addCustomerBtn);
        
        JButton openCurrentBtn = createStyledButton("📂 Open Current A/C", SECONDARY_COLOR);
        openCurrentBtn.addActionListener(e -> openCurrentAccount());
        managementPanel.add(openCurrentBtn);
        
        JButton openSavingsBtn = createStyledButton("📂 Open Savings A/C", SECONDARY_COLOR);
        openSavingsBtn.addActionListener(e -> openSavingsAccount());
        managementPanel.add(openSavingsBtn);
        
        JButton closeAccountBtn = createStyledButton("❌ Close Account", WARNING_COLOR);
        closeAccountBtn.addActionListener(e -> closeSelectedAccount());
        managementPanel.add(closeAccountBtn);
        
        operationsPanel.add(managementPanel);
        
        // Combine left and right
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, 
                selectionPanel, operationsPanel);
        splitPane.setDividerLocation(300);
        splitPane.setBackground(BG_COLOR);
        
        panel.add(splitPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Creates the Manager operations panel.
     * Managers can: suspend/unsuspend accounts, generate reports
     */
    private JPanel createManagerPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_COLOR);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Manager info panel
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(createTitledBorder("Logged in as Branch Manager"));
        infoPanel.add(new JLabel("👔 " + manager.getName() + " | Branch: " + liverpoolBranch.getName()));
        
        panel.add(infoPanel, BorderLayout.NORTH);
        
        // Main operations
        JPanel opsPanel = new JPanel(new GridLayout(1, 2, 15, 10));
        opsPanel.setBackground(BG_COLOR);
        
        // Account suspension panel
        JPanel suspendPanel = new JPanel(new GridBagLayout());
        suspendPanel.setBackground(Color.WHITE);
        suspendPanel.setBorder(createTitledBorder("Account Suspension"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JTextField accountNumField = new JTextField(15);
        suspendPanel.add(new JLabel("Enter Account Number:"), gbc);
        
        gbc.gridy = 1;
        suspendPanel.add(accountNumField, gbc);
        
        gbc.gridy = 2; gbc.gridwidth = 1;
        JButton suspendBtn = createStyledButton("🔒 Suspend", WARNING_COLOR);
        suspendBtn.addActionListener(e -> suspendAccount(accountNumField.getText()));
        suspendPanel.add(suspendBtn, gbc);
        
        gbc.gridx = 1;
        JButton unsuspendBtn = createStyledButton("🔓 Unsuspend", ACCENT_COLOR);
        unsuspendBtn.addActionListener(e -> unsuspendAccount(accountNumField.getText()));
        suspendPanel.add(unsuspendBtn, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JButton checkStatusBtn = createStyledButton("🔍 Check Account Status", SECONDARY_COLOR);
        checkStatusBtn.addActionListener(e -> checkAccountStatus(accountNumField.getText()));
        suspendPanel.add(checkStatusBtn, gbc);
        
        opsPanel.add(suspendPanel);
        
        // Reporting panel
        JPanel reportPanel = new JPanel(new GridBagLayout());
        reportPanel.setBackground(Color.WHITE);
        reportPanel.setBorder(createTitledBorder("Reports for Head Office"));
        
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        
        gbc.gridx = 0; gbc.gridy = 0;
        JButton branchReportBtn = createStyledButton("📊 Generate Branch Report", PRIMARY_COLOR);
        branchReportBtn.addActionListener(e -> generateBranchReport());
        reportPanel.add(branchReportBtn, gbc);
        
        gbc.gridy = 1;
        JButton yearEndBtn = createStyledButton("🗓️ Year-End Reset (Dec 31)", WARNING_COLOR);
        yearEndBtn.addActionListener(e -> performYearEndReset());
        reportPanel.add(yearEndBtn, gbc);
        
        gbc.gridy = 2;
        JButton listAccountsBtn = createStyledButton("📋 List All Accounts", SECONDARY_COLOR);
        listAccountsBtn.addActionListener(e -> listAllAccounts());
        reportPanel.add(listAccountsBtn, gbc);
        
        opsPanel.add(reportPanel);
        
        panel.add(opsPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Creates the Regional Manager panel.
     * Regional managers can: view total balances across branches
     */
    private JPanel createRegionalPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_COLOR);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Regional manager info
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(createTitledBorder("Logged in as Regional Manager"));
        infoPanel.add(new JLabel("🌍 " + regionalManager.getName() + 
                " | Overseeing " + regionalManager.getNumberOfBranches() + " branches"));
        
        panel.add(infoPanel, BorderLayout.NORTH);
        
        // Branch overview
        JPanel branchPanel = new JPanel(new GridLayout(1, 2, 15, 10));
        branchPanel.setBackground(BG_COLOR);
        
        // Liverpool branch card
        branchPanel.add(createBranchCard(liverpoolBranch));
        
        // Manchester branch card
        branchPanel.add(createBranchCard(manchesterBranch));
        
        panel.add(branchPanel, BorderLayout.CENTER);
        
        // Actions panel
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        actionsPanel.setBackground(Color.WHITE);
        actionsPanel.setBorder(createTitledBorder("Regional Actions"));
        
        JButton refreshBalancesBtn = createStyledButton("🔄 Refresh All Balances", SECONDARY_COLOR);
        refreshBalancesBtn.addActionListener(e -> refreshRegionalBalances());
        actionsPanel.add(refreshBalancesBtn);
        
        JButton fullReportBtn = createStyledButton("📊 Full Regional Report", PRIMARY_COLOR);
        fullReportBtn.addActionListener(e -> generateRegionalReport());
        actionsPanel.add(fullReportBtn);
        
        panel.add(actionsPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Creates a nice card-style panel showing branch summary.
     */
    private JPanel createBranchCard(Branch branch) {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SECONDARY_COLOR, 2),
                new EmptyBorder(15, 15, 15, 15)));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel nameLabel = new JLabel("🏦 " + branch.getName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLabel.setForeground(PRIMARY_COLOR);
        card.add(nameLabel, gbc);
        
        gbc.gridy = 1;
        card.add(new JLabel("Sort Code: " + branch.getSortCode()), gbc);
        
        gbc.gridy = 2;
        card.add(new JLabel("Manager: " + (branch.getManager() != null ? 
                branch.getManager().getName() : "None")), gbc);
        
        gbc.gridy = 3;
        card.add(new JLabel("Customers: " + branch.getNumberOfCustomers()), gbc);
        
        gbc.gridy = 4;
        card.add(new JLabel("Accounts: " + branch.getNumberOfAccounts()), gbc);
        
        gbc.gridy = 5;
        float balance = regionalManager.getTotalBranchBalance(branch);
        JLabel balanceLabel = new JLabel("Total Balance: £" + String.format("%.2f", balance));
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        balanceLabel.setForeground(ACCENT_COLOR);
        card.add(balanceLabel, gbc);
        
        gbc.gridy = 6;
        JButton viewBtn = createStyledButton("View Details", SECONDARY_COLOR);
        viewBtn.addActionListener(e -> showBranchDetails(branch));
        card.add(viewBtn, gbc);
        
        return card;
    }
    
    /**
     * Creates the output panel at the bottom of the window.
     */
    private JPanel createOutputPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(createTitledBorder("System Output"));
        panel.setBackground(Color.WHITE);
        
        outputArea = new JTextArea(8, 50);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        outputArea.setBackground(new Color(250, 250, 250));
        
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Clear button
        JButton clearBtn = createStyledButton("Clear Output", new Color(150, 150, 150));
        clearBtn.addActionListener(e -> outputArea.setText(""));
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(clearBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // ==================== HELPER METHODS ====================
    
    /**
     * Creates a styled button with consistent look.
     */
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(8, 15, 8, 15));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    /**
     * Creates a nice titled border.
     */
    private TitledBorder createTitledBorder(String title) {
        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                title);
        border.setTitleFont(new Font("Segoe UI", Font.BOLD, 12));
        border.setTitleColor(PRIMARY_COLOR);
        return border;
    }
    
    /**
     * Appends text to the output area with timestamp.
     */
    private void appendOutput(String text) {
        String timestamp = String.format("[%tT] ", new Date());
        outputArea.append(timestamp + text + "\n");
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }
    
    /**
     * Shows an error dialog.
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
        appendOutput("ERROR: " + message);
    }
    
    /**
     * Shows a success dialog.
     */
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
        appendOutput("SUCCESS: " + message);
    }
    
    /**
     * Refreshes the customer dropdown with current data.
     */
    private void refreshCustomerDropdown() {
        customerDropdown.removeAllItems();
        for (Customer c : liverpoolBranch.getCustomers()) {
            customerDropdown.addItem(c.getName());
        }
    }
    
    /**
     * Refreshes the account dropdown based on selected customer.
     */
    private void refreshAccountDropdown() {
        accountDropdown.removeAllItems();
        Customer customer = getSelectedCustomer();
        if (customer != null) {
            for (Account acc : customer.getAccounts()) {
                String accInfo = String.format("#%d - %s (£%.2f)", 
                        acc.getAccountNumber(), acc.getAccountType(), acc.getBalance());
                accountDropdown.addItem(accInfo);
            }
        }
    }
    
    /**
     * Gets the currently selected customer.
     */
    private Customer getSelectedCustomer() {
        String name = (String) customerDropdown.getSelectedItem();
        if (name == null) return null;
        
        for (Customer c : liverpoolBranch.getCustomers()) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }
    
    /**
     * Gets the currently selected account.
     */
    private Account getSelectedAccount() {
        Customer customer = getSelectedCustomer();
        if (customer == null) return null;
        
        String accInfo = (String) accountDropdown.getSelectedItem();
        if (accInfo == null) return null;
        
        // Extract account number from the dropdown text
        try {
            int accNum = Integer.parseInt(accInfo.split(" ")[0].substring(1));
            return customer.getAccountByNumber(accNum);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Finds an account by number across all customers.
     */
    private Account findAccountByNumber(int accountNumber) {
        for (Customer c : liverpoolBranch.getCustomers()) {
            Account acc = c.getAccountByNumber(accountNumber);
            if (acc != null) return acc;
        }
        return null;
    }
    
    // ==================== TELLER OPERATIONS ====================
    
    private void performDeposit(JTextField amountField) {
        Account account = getSelectedAccount();
        if (account == null) {
            showError("Please select an account first.");
            return;
        }
        
        try {
            float amount = Float.parseFloat(amountField.getText().trim());
            if (amount <= 0) {
                showError("Amount must be positive.");
                return;
            }
            
            if (account.deposit(amount)) {
                showSuccess(String.format("Deposited £%.2f to account #%d. New balance: £%.2f",
                        amount, account.getAccountNumber(), account.getBalance()));
                refreshAccountDropdown();
                amountField.setText("");
            } else {
                showError("Deposit failed. Account may be suspended or closed.");
            }
        } catch (NumberFormatException e) {
            showError("Please enter a valid amount.");
        }
    }
    
    private void performWithdrawal(JTextField amountField) {
        Account account = getSelectedAccount();
        if (account == null) {
            showError("Please select an account first.");
            return;
        }
        
        try {
            float amount = Float.parseFloat(amountField.getText().trim());
            if (amount <= 0) {
                showError("Amount must be positive.");
                return;
            }
            
            if (account.withdraw(amount)) {
                showSuccess(String.format("Withdrew £%.2f from account #%d. New balance: £%.2f",
                        amount, account.getAccountNumber(), account.getBalance()));
                refreshAccountDropdown();
                amountField.setText("");
            } else {
                if (account.isSuspended()) {
                    showError("Cannot withdraw - account is suspended.");
                } else if (account instanceof SavingsAccount) {
                    SavingsAccount sa = (SavingsAccount) account;
                    if (sa.getRemainingWithdrawals() <= 0) {
                        showError("Savings account withdrawal limit reached (4 per year).");
                    } else {
                        showError("Insufficient funds.");
                    }
                } else {
                    showError("Insufficient funds (including overdraft).");
                }
            }
        } catch (NumberFormatException e) {
            showError("Please enter a valid amount.");
        }
    }
    
    private void showTransactionHistory() {
        Account account = getSelectedAccount();
        if (account == null) {
            showError("Please select an account first.");
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("=== Transaction History for Account #").append(account.getAccountNumber()).append(" ===\n");
        
        for (Transaction t : account.getTransactionLog().getAllTransactions()) {
            sb.append(t.toString()).append("\n");
        }
        
        sb.append("Total: £").append(String.format("%.2f", account.getTransactionLog().getTotalValue()));
        
        appendOutput(sb.toString());
        
        // Also show in a dialog for easy viewing
        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        JOptionPane.showMessageDialog(this, scrollPane, "Transaction History", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showAddCustomerDialog() {
        JTextField nameField = new JTextField(20);
        JTextField addressField = new JTextField(20);
        
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Customer Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Address:"));
        panel.add(addressField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Customer",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String address = addressField.getText().trim();
            
            if (name.isEmpty()) {
                showError("Customer name cannot be empty.");
                return;
            }
            
            Customer newCustomer = new Customer(name, address, new Date());
            teller.addNewCustomer(newCustomer);
            refreshCustomerDropdown();
            showSuccess("Customer '" + name + "' added successfully.");
        }
    }
    
    private void openCurrentAccount() {
        Customer customer = getSelectedCustomer();
        if (customer == null) {
            showError("Please select a customer first.");
            return;
        }
        
        JTextField balanceField = new JTextField("0", 10);
        JTextField overdraftField = new JTextField("500", 10);
        
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Initial Balance (£):"));
        panel.add(balanceField);
        panel.add(new JLabel("Overdraft Limit (£):"));
        panel.add(overdraftField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Open Current Account",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                float balance = Float.parseFloat(balanceField.getText().trim());
                float overdraft = Float.parseFloat(overdraftField.getText().trim());
                
                CurrentAccount newAcc = new CurrentAccount(balance, overdraft);
                teller.addNewAccount(newAcc, customer);
                refreshAccountDropdown();
                showSuccess("Current Account #" + newAcc.getAccountNumber() + " opened for " + customer.getName());
            } catch (NumberFormatException e) {
                showError("Please enter valid numbers.");
            }
        }
    }
    
    private void openSavingsAccount() {
        Customer customer = getSelectedCustomer();
        if (customer == null) {
            showError("Please select a customer first.");
            return;
        }
        
        JTextField balanceField = new JTextField("0", 10);
        JTextField rateField = new JTextField("3.5", 10);
        
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Initial Balance (£):"));
        panel.add(balanceField);
        panel.add(new JLabel("Interest Rate (%):"));
        panel.add(rateField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Open Savings Account",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                float balance = Float.parseFloat(balanceField.getText().trim());
                float rate = Float.parseFloat(rateField.getText().trim()) / 100.0f; // Convert to decimal
                
                SavingsAccount newAcc = new SavingsAccount(balance, rate);
                teller.addNewAccount(newAcc, customer);
                refreshAccountDropdown();
                showSuccess("Savings Account #" + newAcc.getAccountNumber() + " opened for " + customer.getName());
            } catch (NumberFormatException e) {
                showError("Please enter valid numbers.");
            }
        }
    }
    
    private void closeSelectedAccount() {
        Account account = getSelectedAccount();
        if (account == null) {
            showError("Please select an account first.");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to close account #" + account.getAccountNumber() + "?\n" +
                "Current balance: £" + String.format("%.2f", account.getBalance()),
                "Confirm Account Closure",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            teller.closeAccount(account);
            refreshAccountDropdown();
            showSuccess("Account #" + account.getAccountNumber() + " has been closed.");
        }
    }
    
    // ==================== MANAGER OPERATIONS ====================
    
    private void suspendAccount(String accNumStr) {
        try {
            int accNum = Integer.parseInt(accNumStr.trim());
            Account account = findAccountByNumber(accNum);
            
            if (account == null) {
                showError("Account #" + accNum + " not found.");
                return;
            }
            
            manager.suspendAccount(account);
            showSuccess("Account #" + accNum + " has been SUSPENDED.");
            refreshAccountDropdown();
        } catch (NumberFormatException e) {
            showError("Please enter a valid account number.");
        }
    }
    
    private void unsuspendAccount(String accNumStr) {
        try {
            int accNum = Integer.parseInt(accNumStr.trim());
            Account account = findAccountByNumber(accNum);
            
            if (account == null) {
                showError("Account #" + accNum + " not found.");
                return;
            }
            
            manager.unsuspendAccount(account);
            showSuccess("Account #" + accNum + " has been UNSUSPENDED.");
            refreshAccountDropdown();
        } catch (NumberFormatException e) {
            showError("Please enter a valid account number.");
        }
    }
    
    private void checkAccountStatus(String accNumStr) {
        try {
            int accNum = Integer.parseInt(accNumStr.trim());
            Account account = findAccountByNumber(accNum);
            
            if (account == null) {
                showError("Account #" + accNum + " not found.");
                return;
            }
            
            StringBuilder sb = new StringBuilder();
            sb.append("=== Account Status ===\n");
            sb.append("Account: #").append(account.getAccountNumber()).append("\n");
            sb.append("Type: ").append(account.getAccountType()).append("\n");
            sb.append("Balance: £").append(String.format("%.2f", account.getBalance())).append("\n");
            sb.append("Status: ");
            
            if (account.isClosed()) {
                sb.append("CLOSED");
            } else if (account.isSuspended()) {
                sb.append("SUSPENDED ⚠️");
            } else {
                sb.append("ACTIVE ✓");
            }
            
            if (account instanceof SavingsAccount) {
                SavingsAccount sa = (SavingsAccount) account;
                sb.append("\nWithdrawals this year: ").append(4 - sa.getRemainingWithdrawals()).append("/4");
            }
            
            appendOutput(sb.toString());
            JOptionPane.showMessageDialog(this, sb.toString(), "Account Status", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            showError("Please enter a valid account number.");
        }
    }
    
    private void generateBranchReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("========== HEAD OFFICE REPORT ==========\n");
        sb.append("Branch: ").append(liverpoolBranch.getName()).append("\n");
        sb.append("Sort Code: ").append(liverpoolBranch.getSortCode()).append("\n");
        sb.append("Manager: ").append(manager.getName()).append("\n");
        sb.append("-----------------------------------------\n");
        sb.append("Total Customers: ").append(liverpoolBranch.getNumberOfCustomers()).append("\n");
        sb.append("Total Accounts: ").append(liverpoolBranch.getNumberOfAccounts()).append("\n");
        sb.append("Total Balance: £").append(String.format("%.2f", 
                regionalManager.getTotalBranchBalance(liverpoolBranch))).append("\n");
        sb.append("=========================================\n");
        
        appendOutput(sb.toString());
        showSuccess("Branch report generated - see output below.");
    }
    
    private void performYearEndReset() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "This will reset ALL savings account withdrawal counts.\n" +
                "This operation is normally performed at midnight on December 31st.\n\n" +
                "Are you sure you want to proceed?",
                "Year-End Reset Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            liverpoolBranch.resetSavingsWDcount();
            showSuccess("Year-end reset complete! All savings account withdrawal counts have been reset.");
        }
    }
    
    private void listAllAccounts() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== All Accounts in Branch ===\n\n");
        
        for (Customer c : liverpoolBranch.getCustomers()) {
            sb.append("Customer: ").append(c.getName()).append("\n");
            for (Account acc : c.getAccounts()) {
                sb.append("  ").append(acc.toString()).append("\n");
            }
            sb.append("\n");
        }
        
        appendOutput(sb.toString());
    }
    
    // ==================== REGIONAL MANAGER OPERATIONS ====================
    
    private void refreshRegionalBalances() {
        // Refresh the regional manager tab by recreating it
        tabbedPane.setComponentAt(2, createRegionalPanel());
        appendOutput("Regional balances refreshed.");
    }
    
    private void generateRegionalReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("============ REGIONAL REPORT ============\n");
        sb.append("Regional Manager: ").append(regionalManager.getName()).append("\n");
        sb.append("Number of Branches: ").append(regionalManager.getNumberOfBranches()).append("\n");
        sb.append("-----------------------------------------\n");
        
        float totalBalance = 0;
        int totalCustomers = 0;
        int totalAccounts = 0;
        
        for (Branch branch : regionalManager.getBranches()) {
            float branchBalance = regionalManager.getTotalBranchBalance(branch);
            sb.append("\nBranch: ").append(branch.getName()).append("\n");
            sb.append("  Customers: ").append(branch.getNumberOfCustomers()).append("\n");
            sb.append("  Accounts: ").append(branch.getNumberOfAccounts()).append("\n");
            sb.append("  Total Balance: £").append(String.format("%.2f", branchBalance)).append("\n");
            
            totalBalance += branchBalance;
            totalCustomers += branch.getNumberOfCustomers();
            totalAccounts += branch.getNumberOfAccounts();
        }
        
        sb.append("\n-----------------------------------------\n");
        sb.append("REGIONAL TOTALS:\n");
        sb.append("  Total Customers: ").append(totalCustomers).append("\n");
        sb.append("  Total Accounts: ").append(totalAccounts).append("\n");
        sb.append("  Total Balance: £").append(String.format("%.2f", totalBalance)).append("\n");
        sb.append("=========================================\n");
        
        appendOutput(sb.toString());
        showSuccess("Regional report generated - see output below.");
    }
    
    private void showBranchDetails(Branch branch) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(branch.getName()).append(" ===\n\n");
        sb.append("Sort Code: ").append(branch.getSortCode()).append("\n");
        sb.append("Address: ").append(branch.getAddress()).append("\n");
        sb.append("Manager: ").append(branch.getManager() != null ? branch.getManager().getName() : "None").append("\n");
        sb.append("\nCustomers: ").append(branch.getNumberOfCustomers()).append("\n");
        sb.append("Accounts: ").append(branch.getNumberOfAccounts()).append("\n");
        sb.append("Total Balance: £").append(String.format("%.2f", 
                regionalManager.getTotalBranchBalance(branch))).append("\n");
        
        appendOutput(sb.toString());
    }
    
    // ==================== TEST DATA INITIALIZATION ====================
    
    /**
     * Sets up all the test data so we have something to work with
     * immediately when the application starts.
     */
    private void initializeTestData() {
        // Create branches
        liverpoolBranch = new Branch("Liverpool City Centre", "12-34-56",
                "45 Castle Street, Liverpool, L2 9SH");
        manchesterBranch = new Branch("Manchester Piccadilly", "12-34-78",
                "12 Piccadilly Gardens, Manchester, M1 1LU");
        
        // Create and assign manager
        manager = new Manager("Sarah Wilson", "22 Oak Avenue, Liverpool",
                createDate(1975, 5, 15), "swilson", "manager123");
        liverpoolBranch.setManager(manager);
        
        Manager manchesterManager = new Manager("John Roberts", "15 Pine Road, Manchester",
                createDate(1980, 3, 22), "jroberts", "secure456");
        manchesterBranch.setManager(manchesterManager);
        
        // Create teller
        teller = new Teller("Emma Thompson", "8 Birch Lane, Liverpool",
                createDate(1992, 8, 10), "ethompson", "teller001");
        teller.setBranch(liverpoolBranch);
        liverpoolBranch.addStaff(teller);
        
        // Create regional manager
        regionalManager = new RegionalManager("David Chen", "50 Victoria Street, London",
                createDate(1968, 11, 5), "dchen", "regional789");
        regionalManager.addBranch(liverpoolBranch);
        regionalManager.addBranch(manchesterBranch);
        
        // Create test customers and accounts
        Customer alice = new Customer("Alice Smith", "123 High Street, Liverpool",
                createDate(1988, 7, 20));
        teller.addNewCustomer(alice);
        
        CurrentAccount aliceCurrent = new CurrentAccount(1500.0f, 500.0f);
        teller.addNewAccount(aliceCurrent, alice);
        
        SavingsAccount aliceSavings = new SavingsAccount(5000.0f, 0.035f);
        teller.addNewAccount(aliceSavings, alice);
        
        Customer bob = new Customer("Bob Johnson", "456 Low Road, Liverpool",
                createDate(1975, 12, 3));
        teller.addNewCustomer(bob);
        
        CurrentAccount bobCurrent = new CurrentAccount(250.0f, 200.0f);
        teller.addNewAccount(bobCurrent, bob);
        
        Customer carol = new Customer("Carol Williams", "789 Middle Way, Liverpool",
                createDate(1990, 4, 15));
        teller.addNewCustomer(carol);
        
        SavingsAccount carolSavings = new SavingsAccount(10000.0f, 0.04f);
        teller.addNewAccount(carolSavings, carol);
    }
    
    /**
     * Helper to create dates without all the deprecated constructor warnings.
     */
    private Date createDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day);
        return cal.getTime();
    }
    
    // ==================== MAIN METHOD ====================
    
    /**
     * Launch the application!
     */
    public static void main(String[] args) {
        // Use system look and feel for a more native appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Fall back to default if system L&F isn't available
        }
        
        // Run on the Event Dispatch Thread as Swing requires
        SwingUtilities.invokeLater(() -> new BankGUI());
    }
}
