import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

public class StudentListing extends JPanel {
    private JButton cmdAddStudent;
    private JButton cmdClose;
    private JButton cmdSortLastName;
    private JButton cmdSortFirstName;
    private JButton cmdDelete;
    private JButton cmdUpdate;
    private JButton cmdLoadFile;  
    private JButton cmdSaveFile;  

    private JPanel pnlCommand;
    private JPanel pnlDisplay;
    private DefaultTableModel model;
    private JTable table;
    private JScrollPane scrollPane;
    private TableRowSorter<TableModel> sorter;  

    public StudentListing() {
        super(new BorderLayout());

        pnlCommand = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlDisplay = new JPanel(new BorderLayout());

        String[] columnNames = {"First Name", "Last Name", "Birth Date", "Address", "Guardian", "Regular Contact", "Emergency Contact"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);

        
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);  

        
        addToTable(new String[]{"John", "Doe", "01/01/2000", "123 Main St", "Jane Doe", "123-456-7890", "987-654-3210"});
        addToTable(new String[]{"Alice", "Smith", "02/02/1999", "456 Oak St", "Bob Smith", "234-567-8901", "876-543-2109"});
        addToTable(new String[]{"Charlie", "Brown", "03/03/2001", "789 Pine St", "Sarah Brown", "345-678-9012", "765-432-1098"});

        scrollPane = new JScrollPane(table);
        pnlDisplay.add(scrollPane, BorderLayout.CENTER);

        cmdAddStudent = new JButton("Add Student");
        cmdSortLastName = new JButton("Sort by Last Name");
        cmdSortFirstName = new JButton("Sort by First Name");
        cmdDelete = new JButton("Delete");
        cmdClose = new JButton("Close");
        cmdUpdate = new JButton("Update");
        cmdLoadFile = new JButton("Load from File");
        cmdSaveFile = new JButton("Save to File");  
        cmdAddStudent.addActionListener(e -> addStudentClicked());
        cmdSortLastName.addActionListener(e -> sortListByLastName());
        cmdSortFirstName.addActionListener(e -> sortListByFirstName());
        cmdDelete.addActionListener(e -> deleteSelectedRecord());
        cmdUpdate.addActionListener(e -> updateSelectedRecord());
        cmdClose.addActionListener(e -> System.exit(0));
        cmdLoadFile.addActionListener(e -> loadFileClicked());
        cmdSaveFile.addActionListener(e -> saveFileClicked());  
        pnlCommand.add(cmdSaveFile);  
        pnlCommand.add(cmdAddStudent);
        pnlCommand.add(cmdSortLastName);
        pnlCommand.add(cmdSortFirstName);
        pnlCommand.add(cmdDelete);
        pnlCommand.add(cmdUpdate);
        pnlCommand.add(cmdLoadFile);
        pnlCommand.add(cmdClose);

        add(pnlDisplay, BorderLayout.CENTER);
        add(pnlCommand, BorderLayout.SOUTH);
    }

    private void addToTable(String[] student) {
        model.addRow(student);
    }

    

    private void deleteSelectedRecord() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            model.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a student to delete.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void addStudentClicked(){
        String[] newStudent = {"New", "Student", "01/01/2002", "000 New St", "Parent", "555-555-5555", "444-444-4444"};
    addToTable(newStudent);
    }

    private void updateSelectedRecord(){
        int selectedRow = table.getSelectedRow();
    if (selectedRow != -1) {
        String firstName = JOptionPane.showInputDialog(this, "Enter new first name:");
        String lastName = JOptionPane.showInputDialog(this, "Enter new last name:");
        String birthDate = JOptionPane.showInputDialog(this, "Enter new birth date:");
        String address = JOptionPane.showInputDialog(this, "Enter new address:");
        String guardian = JOptionPane.showInputDialog(this, "Enter new guardian name:");
        String regularContact = JOptionPane.showInputDialog(this, "Enter new regular contact:");
        String emergencyContact = JOptionPane.showInputDialog(this, "Enter new emergency contact:");

        //validation
        if (regularContact != null && !regularContact.isEmpty() && regularContact.length() < 9) {
            JOptionPane.showMessageDialog(this, "Please enter a Valid 9 digit Contact Number", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //validation
        if (emergencyContact != null && !emergencyContact.isEmpty() && emergencyContact.length() < 9) {
            JOptionPane.showMessageDialog(this, "Please enter a Valid 9 digit Contact Number", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        
        if (firstName != null && !firstName.isEmpty()) model.setValueAt(firstName, selectedRow, 0);
        if (lastName != null && !lastName.isEmpty()) model.setValueAt(lastName, selectedRow, 1);
        if (birthDate != null && !birthDate.isEmpty()) model.setValueAt(birthDate, selectedRow, 2);
        if (address != null && !address.isEmpty()) model.setValueAt(address, selectedRow, 3);
        if (guardian != null && !guardian.isEmpty()) model.setValueAt(guardian, selectedRow, 4);
        if (regularContact != null && !regularContact.isEmpty()) model.setValueAt(regularContact, selectedRow, 5);
        if (emergencyContact != null && !emergencyContact.isEmpty()) model.setValueAt(emergencyContact, selectedRow, 6);

        JOptionPane.showMessageDialog(this, "Update Successful", "Success", JOptionPane.INFORMATION_MESSAGE);
    } else {
        JOptionPane.showMessageDialog(this, "Please select a student to update.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    }


    private void sortListByLastName() {
        sorter.setSortKeys(Collections.singletonList(new RowSorter.SortKey(1, SortOrder.ASCENDING)));
    }

    private void sortListByFirstName() {
        sorter.setSortKeys(Collections.singletonList(new RowSorter.SortKey(0, SortOrder.ASCENDING)));
    }

    private void loadFileClicked() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] student = line.split(",");  // Split by commas
                    if (student.length == 7) {
                        addToTable(student);
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid file", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                JOptionPane.showMessageDialog(this, "File Loaded!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error reading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    
    private void saveFileClicked() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (int row = 0; row < model.getRowCount(); row++) {
                    
                    StringBuilder rowData = new StringBuilder();
                    for (int col = 0; col < model.getColumnCount(); col++) {
                        rowData.append(model.getValueAt(row, col).toString());
                        if (col < model.getColumnCount() - 1) {
                            rowData.append(",");  
                        }
                    }
                    writer.write(rowData.toString());
                    writer.newLine(); 
                }
                JOptionPane.showMessageDialog(this, "Data saved ", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Student Listing");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new StudentListing());
            frame.pack();
            frame.setVisible(true);
        });
    }
}

