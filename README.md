# Bank Software System

**Module:** 7129COMP - Software Development with Java  

---

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

---

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
