package com.example.expensemanager;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;



@SuppressWarnings("unused")
public class ExpenseManager{
    private List <Expense> expenses = new ArrayList<>();
    private static final String EXPENSE_FILE = "expenses.txt";


    public ExpenseManager(){
        loadExpenses();

    }

    public void addExpense(String type, double amount, String about) {
        Expense expense = new Expense(type, amount, about);
        expenses.add(expense);
        saveExpenses();
    }

    public Map<String,List<Expense>> sortExpenses(){
        return expenses.stream().collect(Collectors.groupingBy(Expense::getType));
    }

    public void modExpenses(LocalDate date, String type, double newAmount, String newAbout){
         for (Expense expense : expenses){
            if (expense.getDate().equals(date) && expense.getType().equals(type)){
                if (LocalDate.now().isBefore(expense.getDate().plusDays(1))){
                    expense.setAmount(newAmount);
                    expense.setAbout(newAbout);
                    saveExpenses();
                    return;
                } else{
                    System.out.println("The time to edit is expired.");
                }

            }
         }
    }


    public List<Expense> searchExpenses(String type, LocalDate date){
        return expenses.stream()
                .filter(expense -> (type == null || expense.getType().equals(type)) && (date == null || expense.getDate().equals(date)))
                .collect(Collectors.toList());
    }

    public void generateMonthlyReport(){
        LocalDate now = LocalDate.now();
        LocalDate start_of_month = now.withDayOfMonth(1); 
        LocalDate end_of_month = now.withDayOfMonth(now.lengthOfMonth());


        List<Expense> monthlyExpenses = expenses.stream()
            .filter(expense -> !expense.getDate().isBefore(start_of_month) && !expense.getDate().isAfter(end_of_month))
            .collect(Collectors.toList());

        generatePDFReport(monthlyExpenses, "Monthly_Summary_Report.pdf", "Monthly Summary Report");
    }


    public void generateTermlyReport(){
        LocalDate now = LocalDate.now();
        LocalDate start_of_term = now.minusMonths(3).withDayOfMonth(1); 
        LocalDate end_of_term = now.withDayOfMonth(now.lengthOfMonth());


        List<Expense> monthlyExpenses = expenses.stream()
            .filter(expense -> !expense.getDate().isBefore(start_of_term) && !expense.getDate().isAfter(end_of_term))
            .collect(Collectors.toList());

        generatePDFReport(monthlyExpenses, "EndTERM_Summary_Report.pdf", "Term Summary Report");
    }


    private void generatePDFReport(List<Expense> expenses, String filename, String reportTitle) {
        try {
            PdfWriter writer = new PdfWriter(filename);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Add title
            Paragraph title = new Paragraph(reportTitle)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(18)
                    .setBold();
            document.add(title);

            // Create table with column headers
            Table table = new Table(UnitValue.createPercentArray(new float[]{3, 3, 2, 4}))
                    .useAllAvailableWidth();
            table.addHeaderCell(new Cell().add(new Paragraph("Date")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
            table.addHeaderCell(new Cell().add(new Paragraph("Type")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
            table.addHeaderCell(new Cell().add(new Paragraph("Amount")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
            table.addHeaderCell(new Cell().add(new Paragraph("About")).setBackgroundColor(ColorConstants.LIGHT_GRAY));

            // Add expense data to the table
            for (Expense expense : expenses) {
                table.addCell(new Cell().add(new Paragraph(expense.getDate().toString())));
                table.addCell(new Cell().add(new Paragraph(expense.getType())));
                table.addCell(new Cell().add(new Paragraph(String.format("%.2f", expense.getamount()))));
                table.addCell(new Cell().add(new Paragraph(expense.getAbout())));
            }

            document.add(table);
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadExpenses() {
        try (BufferedReader reader = new BufferedReader(new FileReader(EXPENSE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                LocalDate date = LocalDate.parse(parts[0]);
                String type = parts[1];
                double amount = Double.parseDouble(parts[2]);
                String about = parts[3];
                expenses.add(new Expense(date, type, amount, about));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Save expenses to file
    private void saveExpenses() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(EXPENSE_FILE))) {
            for (Expense expense : expenses) {
                writer.printf("%s,%s,%.2f,%s%n", expense.getDate(), expense.getType(), expense.getamount(), expense.getAbout());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    
}
