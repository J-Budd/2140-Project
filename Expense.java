package com.example.expensemanager;

import java.io.Serializable;
import java.util.Date;

public class Expense implements Serializable {
    private double amount;
    private String description;
    private String type;  // Added type field for expense type
    private Date date;

    public Expense(double amount, String description, String type) {
        this.amount = amount;
        this.description = description;
        this.type = type;
        this.date = new Date();
    }

    // Getters and Setters
    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public Date getDate() {
        return date;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(String type) {
        this.type = type;
    }
}
