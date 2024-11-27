import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

/**
 * TeacherListing class extends JFrame to provide a user interface for registered teachers.
 * Here teachers can take and view attendance 
 */
@SuppressWarnings({"rawtypes", "unused"})
public class AttendanceListing extends JFrame {
    private TeacherListing teacherListing;
    private AttendanceListing thisAttendance;
    private ArrayList<String> studentsList;

    java.sql.Date today = new Date(System.currentTimeMillis());

    private JPanel pnlCommand;
    private JScrollPane scrollPane;
    private JTable table;
    private DefaultTableModel model;
    private JComboBox attendMornTypes;
    private JComboBox attendNoonTypes;
    private JTextField explanationField;

    //private JButton btnMorning; //Button to take and save morning attendance
    //private JButton btnAfternoon; //Button to take and save afternoon attendance
    private JButton btnMorningSave; // Button to save morning attendance
    private JButton btnAfternoonSave; //Button to save afternoon attendance

    /**
     * Constructor for AttendanceListing class.
     * Initializes the  AttendanceListing instance with a reference to the TeacherListing class.
     * 
     * @param teacherListing the teacherListing instance to which the new attendance will be added 
    */
    @SuppressWarnings("unchecked")
    public AttendanceListing(TeacherListing teacherListing) throws IOException, ParseException {
        this.teacherListing = teacherListing;
        this.thisAttendance = this;

        setLayout(new GridLayout(3, 1));

        pnlCommand = new JPanel();
        studentsList = loadStudents();

        //Filling table with information
        //Headers
        String[] columnNames = { "Full Name", "Morning", "Afternoon", "Note"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        //Columns 2 and 3 - Attendance
        TableColumn morningColumn = table.getColumnModel().getColumn(1);
        attendMornTypes = new JComboBox();
        attendMornTypes.addItem("None");
        attendMornTypes.addItem("Present");
        attendMornTypes.addItem("Absent");
        morningColumn.setCellEditor(new DefaultCellEditor(attendMornTypes));
        TableColumn afternoonColumn = table.getColumnModel().getColumn(2);
        attendNoonTypes = new JComboBox();
        attendNoonTypes.addItem("None");
        attendNoonTypes.addItem("Present");
        attendNoonTypes.addItem("Absent");
        morningColumn.setCellEditor(new DefaultCellEditor(attendMornTypes));
        afternoonColumn.setCellEditor(new DefaultCellEditor(attendNoonTypes));
        //Column 4 - Explanation regarding student attendance
        TableColumn excuseColumn = table.getColumnModel().getColumn(3);
        explanationField = new JTextField();
        excuseColumn.setCellEditor(new DefaultCellEditor(explanationField));
        //Column 1 - Student Names
        showTable(studentsList);

        table.setPreferredScrollableViewportSize(new Dimension(500, studentsList.size() * 15 + 50));
        table.setFillsViewportHeight(true);
        scrollPane = new JScrollPane(table);

        add(new JLabel("Showing Attendance for " + formatDate(today)));
        add(scrollPane);

        //Setting up Buttons
        btnMorningSave = new JButton("Save Morning Attendance");
        btnAfternoonSave = new JButton("Save Afternoon Attendance");
        Color buttonColor = new Color(0, 128, 0);
        btnMorningSave.setBackground(buttonColor);
        btnAfternoonSave.setBackground(buttonColor);
        btnMorningSave.addActionListener(new SaveMorningAttendance());
        btnAfternoonSave.addActionListener(new SaveAfternoonAttendance());
        enableButtons();
        pnlCommand.add(btnMorningSave);
        pnlCommand.add(btnAfternoonSave);

        add(pnlCommand);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

    }

    /**
     * Loads students from the user's file.
     * 
     * @return the list of students loaded
     * @throws IOException    if an I/O error occurs
     * @throws ParseException if the parsing fails
     */
    private ArrayList<String> loadStudents() throws IOException, ParseException {
        ArrayList<String> students = new ArrayList<String>();
        Scanner tscan = null;

        tscan = new Scanner(new File("Data/Student/Records/StudentsList.txt"));
        while (tscan.hasNext()) {
            String name = tscan.nextLine();
            students.add(name);
        }
        tscan.close();
        return students;
    }

    /**
     * Displays the students in the JTable.
     * 
     * @param studentList the list of students to display
     */
    private void showTable(ArrayList<String> studentList) {
        if (studentList == null) {
            System.out.println("Student list is null");
            return;
        }

        for (String name : studentList) {
            addToTable(name);
        }
    }

    /**
     * Adds a name to the JTable and the name list.
     * 
     * @param name the name to add
     */
    public void addToTable(String name) {
        String[] item = { name };

        if (!studentsList.contains(name)) {
            studentsList.add(name);
        }

        model.addRow(item);
    }



    /**
     * Formats a Date object to a string in "dd-MM-yyyy" format.
     * 
     * @param date the Date object to format
     * @return the formatted date string
     */
    private String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(date);
    }

    /**
     * Re-enables buttons daily
     */
    private void enableButtons(){
        String savedDate = "";
        String savedTime = "";
        Scanner txtScanner = null;
        try{
            txtScanner = new Scanner(new File("Data/Student/Attendance/LastSavedDate.txt"));
            while (txtScanner.hasNextLine()){
                savedDate = txtScanner.nextLine();
                savedTime = txtScanner.nextLine();
                savedTime = txtScanner.nextLine();
            }
            txtScanner.close();
        }catch (IOException error) {
            System.out.println("File couldn't be read: " + error.getMessage());
            error.printStackTrace();
        }
        if (!savedDate.equals(formatDate(today))){
            btnMorningSave.setEnabled(true);
            btnAfternoonSave.setEnabled(true);
        } else {
            if (savedTime.equals("Morning")){
                btnMorningSave.setEnabled(false);
                btnAfternoonSave.setEnabled(true);
            } else if (savedTime.equals("Afternoon")){
                btnMorningSave.setEnabled(false);
                btnAfternoonSave.setEnabled(false);
            }
        }
    }

    private class SaveMorningAttendance implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //Confirm if user wishes to save
            int result = JOptionPane.showConfirmDialog(null,"Once you save changes to Morning Attendance can no longer be made. Are you sure?", "Morning Attendance",
               JOptionPane.YES_NO_OPTION,
               JOptionPane.QUESTION_MESSAGE);
            if (result == JOptionPane.NO_OPTION){
               return;
            }
          
            /*
            Check that every student's attendance either says present or absent
                If not met, have a pop up that tells the user so and prevents them from saving
            */
            for (int i = 1; i < table.getRowCount(); i++) {
                String columnData = (String)attendMornTypes.getSelectedItem();
                if (columnData.equals("None") || columnData.equals(null)) {
                    JOptionPane.showMessageDialog(null, "Every student must be marked Present or Absent", "Morning Attendance", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            //Save attendance to this month's file
            Calendar cal = Calendar.getInstance();
            int month = (cal.get(Calendar.MONTH));
            int todayMonth = month++;
            String filename = todayMonth + ".txt";
            File attendanceFile = new File ("Data/Student/Attendance/" + filename);
            String dateToday = formatDate(today);

            try(BufferedWriter writer = new BufferedWriter(new FileWriter(attendanceFile, true))) {
                writer.write(dateToday);
                writer.newLine();
                writer.write("Morning Attendance");
                writer.newLine();
                for (int i = 1; i < table.getRowCount(); i++)
                    writer.write(studentsList.get(i--) + " " + (String)attendMornTypes.getSelectedItem() + " " + explanationField.getText());
                    writer.newLine();
                writer.close();
            } catch (IOException error) {
                System.out.println("Error saving morning attendance to file: " + error.getMessage());
                error.printStackTrace();
            }

            //Store date
            File currentDateFile = new File("Data/Student/Attendance/LastSavedDate.txt");
            try(BufferedWriter writer = new BufferedWriter(new FileWriter(currentDateFile,false))){
                writer.write(dateToday);
                writer.newLine();
                writer.write("Morning");
                writer.close();
            }catch (IOException error) {
                System.out.println("Error saving today's date to file: " + error.getMessage());
                error.printStackTrace();
            }
            
            JOptionPane.showMessageDialog(null, "Morning Attendance Saved!");
            btnMorningSave.setEnabled(false);
            return;
        }   
    }

    private class SaveAfternoonAttendance implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //Confirm if user wishes to save
            int result = JOptionPane.showConfirmDialog(null,"Once you save changes to Afternoon Attendance can no longer be made. Are you sure?", "Afternoon Attendance",
               JOptionPane.YES_NO_OPTION,
               JOptionPane.QUESTION_MESSAGE);
            if (result == JOptionPane.NO_OPTION){
               return;
            }
          
            /*
            Check that morning attendance has been taken
                If not met, have a pop up that tells the user so and prevents them from saving
            */
            if (btnMorningSave.isEnabled()){
                JOptionPane.showMessageDialog(null, "Morning Attendance must be taken first", "Afternoon Attendance", JOptionPane.ERROR_MESSAGE);
                return;
            }

            /*
            Check that every student's attendance either says present or absent
                If not met, have a pop up that tells the user so and prevents them from saving
            */
            for (int i = 1; i < table.getRowCount(); i++) {
                String columnData = (String)attendNoonTypes.getSelectedItem();
                if (columnData.equals("None") || columnData.equals(null)) {
                    JOptionPane.showMessageDialog(null, "Every student must be marked Present or Absent", "Afternoon Attendance", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            //Save attendance to this month's file
            Calendar cal = Calendar.getInstance();
            int month = (cal.get(Calendar.MONTH));
            int todayMonth = month++;
            String filename = todayMonth + ".txt";
            File attendanceFile = new File ("Data/Student/Attendance/" + filename);

            try(BufferedWriter writer = new BufferedWriter(new FileWriter(attendanceFile))) {
                writer.newLine();
                writer.write("Afternoon Attendance");
                writer.newLine();
                for (int i = 1; i < table.getRowCount(); i++)
                    writer.write(studentsList.get(i--) + " " + (String)attendNoonTypes.getSelectedItem() + " " + explanationField.getText());
                    writer.newLine();
                writer.close();
            } catch (IOException error) {
                System.out.println("Error saving afternoon attendance to file: " + error.getMessage());
                error.printStackTrace();
            }

            //Store date
            File currentDateFile = new File("Data/Student/Attendance/LastSavedDate.txt");
            try(BufferedWriter writer = new BufferedWriter(new FileWriter(currentDateFile,true))){
                writer.newLine();
                writer.write("Afternoon");
                writer.close();
            }catch (IOException error) {
                System.out.println("Error saving today's date to file: " + error.getMessage());
                error.printStackTrace();
            }

            JOptionPane.showMessageDialog(null, "Afternoon Attendance Saved!");
            btnMorningSave.setEnabled(false);
            return;
        }   
    }

}
