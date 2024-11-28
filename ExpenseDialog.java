package com.example.expensemanager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExpenseDialog extends JDialog {
    private JTextField amountField;
    private JTextField descriptionField;
    private JTextField typeField;  // Added a new field for expense type
    private Expense currentExpense;
    private int expenseIndex;

    // Constructor for adding a new expense
    public ExpenseDialog(JFrame parent) {
        super(parent, "Add Expense", true);

        setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));  // Now 4 rows (to include type)

        JLabel amountLabel = new JLabel("Amount:");
        amountField = new JTextField();
        JLabel descriptionLabel = new JLabel("Description:");
        descriptionField = new JTextField();
        JLabel typeLabel = new JLabel("Type:");
        typeField = new JTextField();  // For entering type

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveExpense();
            }
        });

        panel.add(amountLabel);
        panel.add(amountField);
        panel.add(descriptionLabel);
        panel.add(descriptionField);
        panel.add(typeLabel);
        panel.add(typeField);  // Add type field
        panel.add(saveButton);

        add(panel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(parent);
    }

    // Constructor for editing an existing expense
    public ExpenseDialog(JFrame parent, Expense expense, int index) {
        super(parent, "Edit Expense", true);
        this.currentExpense = expense;
        this.expenseIndex = index;

        setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2)); 

        JLabel amountLabel = new JLabel("Amount:");
        amountField = new JTextField(String.valueOf(expense.getAmount()));
        JLabel descriptionLabel = new JLabel("Description:");
        descriptionField = new JTextField(expense.getDescription());
        JLabel typeLabel = new JLabel("Type:");
        typeField = new JTextField(expense.getType());  

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveEditedExpense();
            }
        });

        panel.add(amountLabel);
        panel.add(amountField);
        panel.add(descriptionLabel);
        panel.add(descriptionField);
        panel.add(typeLabel);
        panel.add(typeField);  // Add type field
        panel.add(saveButton);

        add(panel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(parent);
    }

    private void saveExpense() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            String description = descriptionField.getText();
            String type = typeField.getText();  // Get expense type

            Expense newExpense = new Expense(amount, description, type);
            ((ExpenseManagerApp) getOwner()).expenseList.add(newExpense);

            // Save the updated expense list to file
            ((ExpenseManagerApp) getOwner()).saveExpenses();

            JOptionPane.showMessageDialog(this, "Expense saved successfully!");
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount entered.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveEditedExpense() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            String description = descriptionField.getText();
            String type = typeField.getText();  // Get expense type

            currentExpense.setAmount(amount);
            currentExpense.setDescription(description);
            currentExpense.setType(type);

            ((ExpenseManagerApp) getOwner()).expenseList.set(expenseIndex, currentExpense);

            // Save the updated expense list to file
            ((ExpenseManagerApp) getOwner()).saveExpenses();

            JOptionPane.showMessageDialog(this, "Expense updated successfully!");
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount entered.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
