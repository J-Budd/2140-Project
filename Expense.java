

import java.io.Serializable;
import java.util.Date;

public class Expense implements Serializable {
    private double amount;
    private String description;
    private String type;
    private Date date;  // Add a Date field to store the date of the expense

    // Constructor for the expense
    public Expense(double amount, String description, String type) {
        this.amount = amount;
        this.description = description;
        this.type = type;
        this.date = new Date();  // Set the date to the current date when the expense is created
    }

    // Getters and Setters for the fields
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;  // This is the method that was missing
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
