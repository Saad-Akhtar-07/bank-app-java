# Bank Software System

## Project Purpose
This project is a robust Java-based banking management system designed to simulate core banking operations. It facilitates the management of customer accounts, transaction processing, and administrative oversight through both a Graphical User Interface (GUI) and a console interface. The system supports multiple user roles—including Tellers, Managers, and Regional Managers—and handles various account types such as Savings and Current accounts. Key features include transaction logging, limit management, account suspension, and comprehensive financial reporting.


## How to Run

### Option 1: Run the GUI Application (Recommended)
```bash
cd bin
java com.bank.gui.BankGUI
```

### Option 2: Run Console Demo
```bash
cd bin
java com.bank.main.BankApplication
```


## Project Structure

```
├── src/                    # Java source files
│   └── com/bank/
│       ├── branch/         # Branch class
│       ├── gui/            # Swing GUI
│       ├── main/           # Console application
│       ├── model/          # Account, Transaction classes
│       └── person/         # Customer, Staff classes
├── bin/                    # Compiled .class files
└── Bank_Software_Development_Report.pdf
```

---

## Features

- **Teller:** Add customers, open/close accounts, deposit/withdraw
- **Manager:** Suspend/unsuspend accounts, generate reports
- **Regional Manager:** View branch balances and regional statistics

Test data is pre-loaded when the GUI starts.
