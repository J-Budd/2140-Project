package com.example.expensemanager;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ExpenseManagerApp extends JFrame {
    public ArrayList<Expense> expenseList = new ArrayList<>();  // List to store expenses
    private JTextArea textArea;

    public ExpenseManagerApp() {
        // Set up the frame properties
        setTitle("Expense Manager");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // Center the window

        // Set up the text area to display the expenses
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        // Create buttons for adding, editing, viewing expenses, and generating reports
        JButton addButton = new JButton("Add Expense");
        addButton.addActionListener(e -> addExpense());

        JButton editButton = new JButton("Edit Expense");
        editButton.addActionListener(e -> editExpense());

        JButton viewButton = new JButton("View Expenses");
        viewButton.addActionListener(e -> viewExpenses());

        JButton monthlyReportButton = new JButton("Generate Monthly Report");
        monthlyReportButton.addActionListener(e -> generateMonthlyReport());

        JButton termlyReportButton = new JButton("Generate Termly Report");
        termlyReportButton.addActionListener(e -> generateTermlyReport());

        // Layout setup
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(addButton);
        panel.add(editButton);
        panel.add(viewButton);
        panel.add(monthlyReportButton);  // Add the button for monthly report
        panel.add(termlyReportButton);   // Add the button for termly report

        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Load the saved expenses when the app starts
        loadExpenses();
    }

    // Method to load expenses from file
    public void loadExpenses() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("expenses.dat"))) {
            expenseList = (ArrayList<Expense>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // If file not found or cannot be read, initialize an empty list
            expenseList = new ArrayList<>();
        }
    }

    // Method to save expenses to file
    public void saveExpenses() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("expenses.dat"))) {
            oos.writeObject(expenseList);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving expenses.");
        }
    }

    // Method to add a new expense
    private void addExpense() {
        ExpenseDialog dialog = new ExpenseDialog(this);
        dialog.setVisible(true);
    }

    // Method to edit an existing expense
    private void editExpense() {
        // Get selected expense
        int selectedIndex = getSelectedExpenseIndex();
        if (selectedIndex >= 0) {
            Expense expense = expenseList.get(selectedIndex);
            ExpenseDialog dialog = new ExpenseDialog(this, expense, selectedIndex);
            dialog.setVisible(true);
        }
    }

    // Method to get the index of the selected expense in the JTextArea
    private int getSelectedExpenseIndex() {
        String selectedText = textArea.getSelectedText();
        if (selectedText != null) {
            String[] lines = textArea.getText().split("\n");
            for (int i = 1; i < lines.length; i++) {  // Start from 1 to skip the header
                if (lines[i].contains(selectedText)) {
                    return i - 1;  // Subtract 1 to get the correct index in the list
                }
            }
        }
        return -1;
    }

    // Method to view all expenses
    private void viewExpenses() {
        textArea.setText("");  // Clear previous output
        textArea.append(String.format("%-10s %-20s %-20s %-15s\n", "Amount", "Description", "Type", "Date"));
        textArea.append("----------------------------------------------------------------------------------\n");
        for (Expense expense : expenseList) {
            textArea.append(String.format("%-10.2f %-20s %-20s %-15s\n", 
                    expense.getAmount(), 
                    expense.getDescription(), 
                    expense.getType(), 
                    new SimpleDateFormat("yyyy-MM-dd").format(expense.getDate())));
        }
    }

    // Method to generate a monthly report
    private void generateMonthlyReport() {
        double totalAmount = 0;
        Calendar cal = Calendar.getInstance();
        int currentMonth = cal.get(Calendar.MONTH);
        int currentYear = cal.get(Calendar.YEAR);

        StringBuilder report = new StringBuilder();
        report.append("Monthly Report\n");
        report.append(String.format("%-10s %-20s %-20s %-15s\n", "Amount", "Description", "Type", "Date"));
        report.append("----------------------------------------------------------------------------------\n");

        for (Expense expense : expenseList) {
            cal.setTime(expense.getDate());
            int expenseMonth = cal.get(Calendar.MONTH);
            int expenseYear = cal.get(Calendar.YEAR);

            if (expenseMonth == currentMonth && expenseYear == currentYear) {
                totalAmount += expense.getAmount();
                report.append(String.format("%-10.2f %-20s %-20s %-15s\n",
                        expense.getAmount(), 
                        expense.getDescription(), 
                        expense.getType(), 
                        new SimpleDateFormat("yyyy-MM-dd").format(expense.getDate())));
            }
        }

        report.append("----------------------------------------------------------------------------------\n");
        report.append(String.format("Total Amount: %.2f\n", totalAmount));
        textArea.setText(report.toString());
    }

    // Method to generate a termly report (last 3 months)
    private void generateTermlyReport() {
        double totalAmount = 0;
        Calendar cal = Calendar.getInstance();
        int currentMonth = cal.get(Calendar.MONTH);
        int currentYear = cal.get(Calendar.YEAR);

        StringBuilder report = new StringBuilder();
        report.append("Termly Report (Last 3 Months)\n");
        report.append(String.format("%-10s %-20s %-20s %-15s\n", "Amount", "Description", "Type", "Date"));
        report.append("----------------------------------------------------------------------------------\n");

        for (Expense expense : expenseList) {
            cal.setTime(expense.getDate());
            int expenseMonth = cal.get(Calendar.MONTH);
            int expenseYear = cal.get(Calendar.YEAR);

            if ((currentYear == expenseYear && expenseMonth >= currentMonth - 2) || 
                (currentYear - 1 == expenseYear && expenseMonth >= 9)) {
                totalAmount += expense.getAmount();
                report.append(String.format("%-10.2f %-20s %-20s %-15s\n",
                        expense.getAmount(), 
                        expense.getDescription(), 
                        expense.getType(), 
                        new SimpleDateFormat("yyyy-MM-dd").format(expense.getDate())));
            }
        }

        report.append("----------------------------------------------------------------------------------\n");
        report.append(String.format("Total Amount: %.2f\n", totalAmount));
        textArea.setText(report.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ExpenseManagerApp app = new ExpenseManagerApp();
            app.setVisible(true);
        });
    }
}
