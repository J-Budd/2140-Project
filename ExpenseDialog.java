

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
        this(parent);
        setTitle("Edit Expense");
        currentExpense = expense;
        expenseIndex = index;

        // Set existing values in fields
        amountField.setText(String.valueOf(expense.getAmount()));
        descriptionField.setText(expense.getDescription());
        typeField.setText(expense.getType());
    }

    // Method to save the expense (either new or edited)
    private void saveExpense() {
        try {
            double amount = Double.parseDouble(amountField.getText());  // Get the amount
            String description = descriptionField.getText();  // Get the description
            String type = typeField.getText();  // Get the type

            // If editing, update the current expense
            if (currentExpense != null) {
                currentExpense.setAmount(amount);
                currentExpense.setDescription(description);
                currentExpense.setType(type);
            } else {
                currentExpense = new Expense(amount, description, type);
                ((ExpenseManagerApp) getOwner()).expenseList.add(currentExpense);
            }

            // Save the updated expenses to the file
            ((ExpenseManagerApp) getOwner()).saveExpenses();

            JOptionPane.showMessageDialog(this, "Expense saved successfully!");
            dispose();  // Close the dialog
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount entered.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
