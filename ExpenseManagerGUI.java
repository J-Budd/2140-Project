package com.example.expensemanager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

@SuppressWarnings("unused")
public class ExpenseManagerGUI {
    private ExpenseManager expenseManager;
    private JFrame frame;
    private JTextField typeField;
    private JTextField amountField;
    private JTextField aboutField;

    public ExpenseManagerGUI() {
        expenseManager = new ExpenseManager();
        frame = new JFrame("Expense Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        Container container = frame.getContentPane();
        container.setLayout(new GridLayout(5, 2));

        JLabel typeLabel = new JLabel("Expense Type:");
        typeField = new JTextField();
        JLabel amountLabel = new JLabel("Amount:");
        amountField = new JTextField();
        JLabel aboutLabel = new JLabel("About:");
        aboutField = new JTextField();

        JButton addButton = new JButton("Add Expense");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String type = typeField.getText();
                double amount = Double.parseDouble(amountField.getText());
                String about = aboutField.getText();
                expenseManager.addExpense(type, amount, about);
                JOptionPane.showMessageDialog(frame, "Expense added successfully!");
            }
        });

        JButton monthlyReportButton = new JButton("Generate Monthly Report");
        monthlyReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                expenseManager.generateMonthlyReport();
                JOptionPane.showMessageDialog(frame, "Monthly report generated successfully!");
            }
        });

        JButton termlyReportButton = new JButton("Generate Termly Report");
        termlyReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                expenseManager.generateTermlyReport();
                JOptionPane.showMessageDialog(frame, "Termly report generated successfully!");
            }
        });

        container.add(typeLabel);
        container.add(typeField);
        container.add(amountLabel);
        container.add(amountField);
        container.add(aboutLabel);
        container.add(aboutField);
        container.add(addButton);
        container.add(monthlyReportButton);
        container.add(termlyReportButton);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ExpenseManagerGUI();
            }
        });
    }
}