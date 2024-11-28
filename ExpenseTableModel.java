package com.example.expensemanager;

import javax.swing.table.AbstractTableModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ExpenseTableModel extends AbstractTableModel {
    private final String[] columnNames = {"Amount", "Description", "Type", "Date"};
    private ArrayList<Expense> expenseList;

    public ExpenseTableModel(ArrayList<Expense> expenseList) {
        this.expenseList = expenseList;
    }

    @Override
    public int getRowCount() {
        return expenseList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Expense expense = expenseList.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return expense.getAmount();
            case 1:
                return expense.getDescription();
            case 2:
                return expense.getType();
            case 3:
                return new SimpleDateFormat("yyyy-MM-dd").format(expense.getDate());
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    // Method to update the table data
    public void updateData(ArrayList<Expense> expenseList) {
        this.expenseList = expenseList;
        fireTableDataChanged();  // Notify the table that the data has changed
    }
}
