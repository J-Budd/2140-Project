import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import java.awt.*;
import java.awt.event.*;
//import java.io.BufferedtxtScanner;
import java.io.BufferedWriter;
import java.io.File;
//import java.io.FiletxtScanner;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * TeacherListing class extends JFrame to provide a user interface for registered teachers.
 * Here teachers can take and view attendance 
 */
@SuppressWarnings({"rawtypes", "unused"})
public class AttendanceListing extends JFrame {
    private TeacherListing teacherListing;
    private AttendanceListing thisAttendance;
    private ArrayList<String> studentsList = new ArrayList<>();

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
    private JButton cmdBack; //Button to close this screen
    Color onColor;
    Color offColor;

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
        //studentsList = loadStudents(); Remove if works

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
        //showTable(studentsList); Remove if works
        loadAttendance(); //TODO ensure this works

        table.setPreferredScrollableViewportSize(new Dimension(700, studentsList.size() * 25 + 50));
        table.setFillsViewportHeight(true);
        scrollPane = new JScrollPane(table);

        add(new JLabel("Showing Attendance for " + formatDate(today)));
        add(scrollPane);

        //Setting up Buttons
        btnMorningSave = new JButton("Save Morning Attendance");
        btnAfternoonSave = new JButton("Save Afternoon Attendance");
        cmdBack = new JButton("Back");
        onColor = new Color(135, 206, 235);
        offColor = new Color(255, 127, 127);
        btnMorningSave.addActionListener(new SaveMorningAttendance());
        btnAfternoonSave.addActionListener(new SaveAfternoonAttendance());
        cmdBack.addActionListener(new cmdBackButtonListener());
        enableTable(); 
        pnlCommand.add(btnMorningSave);
        pnlCommand.add(btnAfternoonSave);
        pnlCommand.add(cmdBack);

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
        Collections.sort(students);
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
            Collections.sort(studentsList);
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
    private void enableTable(){
        String savedDate = "";
        String savedTime = "";
        Scanner txtScanner = null;
        try{
            txtScanner = new Scanner(new File("Data/Student/Attendance/LastSavedDate.txt"));
            while (txtScanner.hasNextLine()){
                savedDate = txtScanner.nextLine();
                savedTime = txtScanner.nextLine();
                if (txtScanner.hasNextLine()){
                    savedTime = txtScanner.nextLine();
                }
            }
            txtScanner.close();
        }catch (IOException error) {
            System.out.println("File couldn't be read: " + error.getMessage());
            error.printStackTrace();
        }
        if (!savedDate.equals(formatDate(today))){
            btnMorningSave.setEnabled(true);
            btnMorningSave.setBackground(onColor);
            attendMornTypes.setEnabled(true);

            btnAfternoonSave.setEnabled(true);            
            btnAfternoonSave.setBackground(onColor);
            attendNoonTypes.setEnabled(true);

            explanationField.setEnabled(true);
        } else {
            if (savedTime.equals("Morning")){
                btnMorningSave.setEnabled(false);
                btnMorningSave.setBackground(offColor);
                attendMornTypes.setEnabled(false);

                btnAfternoonSave.setEnabled(true);            
                btnAfternoonSave.setBackground(onColor);
                attendNoonTypes.setEnabled(true);

                explanationField.setEnabled(true);
            } else if (savedTime.equals("Afternoon")){
                btnMorningSave.setEnabled(false);
                btnMorningSave.setBackground(offColor);
                attendMornTypes.setEnabled(false);

                btnAfternoonSave.setEnabled(false);            
                btnAfternoonSave.setBackground(offColor);
                attendNoonTypes.setEnabled(false);

                explanationField.setEnabled(false);
            }
        }
    }

    private void loadAttendance() throws ParseException, IOException {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        int todayMonth = month + 1;
        String filename = todayMonth + ".txt";
        File attendanceFile = new File("Data/Student/Attendance/" + filename);
        String savedDate = "";
        Scanner txtScanner = null;
    
        // Ensure studentsList is initialized before use
        if (studentsList == null) {
            studentsList = new ArrayList<>();
        }
    
        // Step 1: Read the last saved date
        try {
            txtScanner = new Scanner(new File("Data/Student/Attendance/LastSavedDate.txt"));
            if (txtScanner.hasNextLine()) {
                savedDate = txtScanner.nextLine();
            }
            txtScanner.close();
        } catch (IOException error) {
            System.out.println("File couldn't be read: " + error.getMessage());
            error.printStackTrace();
        }
    
        // Step 2: Check if it's a new day (i.e., no saved attendance for today)
        if (!savedDate.equals(formatDate(today))) {
            studentsList = loadStudents();
            showTable(studentsList);
            return;
        }
    
        // Step 3: Proceed if attendance data exists for the day
        try {
            txtScanner = new Scanner(attendanceFile);
    
            // Step 4: Skip lines until we find the saved date
            String line = "";
            while (txtScanner.hasNextLine()) {
                line = txtScanner.nextLine();
                if (line.equals(savedDate)) {
                    break;
                }
            }
    
            // Step 5: Read attendance data for both morning and afternoon
            String lineTime = "";
            boolean isMorning = true;  // Track whether we're loading morning or afternoon data
    
            // Initialize a map to hold morning and afternoon attendance for each student
            Map<String, String[]> studentAttendanceMap = new HashMap<>();
    
            // Step 6: Read the next line for the time (Morning or Afternoon)
            if (txtScanner.hasNextLine()) {
                lineTime = txtScanner.nextLine();
            }
    
            while (txtScanner.hasNextLine()) {
                line = txtScanner.nextLine();
    
                // If we encounter "Afternoon" or another section heading, stop loading or switch section
                if (line.contains("Afternoon")) {
                    isMorning = false;
                    continue;
                }
    
                // Split line data by commas
                String[] attendData = line.split(",", -1);
    
                // Ensure the data is well-formed with 3 elements (Name, Attendance status, Note)
                if (attendData.length >= 3) {
                    String name = attendData[0].trim();
                    String attendance = attendData[1].trim();
                    String note = attendData[2].trim();
    
                    // If student data doesn't already exist, initialize the map entry
                    if (!studentAttendanceMap.containsKey(name)) {
                        studentAttendanceMap.put(name, new String[] {"None", "None", note});
                    }
    
                    // Based on whether we're loading morning or afternoon, update the attendance data
                    if (isMorning) {
                        studentAttendanceMap.get(name)[0] = attendance;  // Morning attendance
                    } else {
                        studentAttendanceMap.get(name)[1] = attendance;  // Afternoon attendance
                    }
                }
            }
    
            // Step 7: Add names to the table only once, and populate attendance data
            for (Map.Entry<String, String[]> entry : studentAttendanceMap.entrySet()) {
                String studentName = entry.getKey();
                String[] attendanceData = entry.getValue();
                String morningAttendance = attendanceData[0];
                String afternoonAttendance = attendanceData[1];
                String note = attendanceData[2];
    
                // Ensure the student is already in studentsList to avoid duplicates
                if (!studentsList.contains(studentName)) {
                    studentsList.add(studentName);
                    addToTable(studentName);
                }
    
                // Set attendance data for morning and afternoon
                int rowIndex = studentsList.indexOf(studentName);
                model.setValueAt(morningAttendance, rowIndex, 1);  // Morning attendance
                model.setValueAt(afternoonAttendance, rowIndex, 2);  // Afternoon attendance
                model.setValueAt(note, rowIndex, 3);  // Note
            }
    
            txtScanner.close();
        } catch (IOException error) {
            System.out.println("Error reading attendance file: " + error.getMessage());
            error.printStackTrace();
        }
    }
    
            

    private class SaveMorningAttendance implements ActionListener {
        public void actionPerformed(ActionEvent e){
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
            int todayMonth = month + 1;
            String filename = todayMonth + ".txt";
            File attendanceFile = new File ("Data/Student/Attendance/" + filename);
            String dateToday = formatDate(today);
            BufferedWriter writer;
            int i = 0;

            try {
                if (attendanceFile.createNewFile()){
                    writer = new BufferedWriter(new FileWriter(attendanceFile, false));
                } else {
                    writer = new BufferedWriter(new FileWriter(attendanceFile, true));
                }
                writer.write(dateToday);
                writer.newLine();
                writer.write("Morning Attendance");
                writer.newLine(); 
                for (i = 0; i < studentsList.size(); i++){
                    //k = i++;
                    writer.write(studentsList.get(i) + " , ");
                    writer.write(table.getValueAt(i, 1) + " , ");
                    writer.write(table.getValueAt(i, 3) + " ");
                    writer.newLine();
                    writer.flush();
                }
                writer.close();
            } catch (IOException error) {
                System.out.println("Error saving morning attendance to file: " + error.getMessage());
                error.printStackTrace();
            }

            //Store date
            File currentDateFile = new File("Data/Student/Attendance/LastSavedDate.txt");
            try(BufferedWriter recorder = new BufferedWriter(new FileWriter(currentDateFile,false))){
                recorder.write(dateToday);
                recorder.newLine();
                recorder.write("Morning");
                recorder.close();
            } catch (IOException error) {
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
            int todayMonth = month + 1;
            String filename = todayMonth + ".txt";
            File attendanceFile = new File ("Data/Student/Attendance/" + filename);
            int i = 0;

            try(BufferedWriter writer = new BufferedWriter(new FileWriter(attendanceFile, true))) {
                writer.newLine();
                writer.write("Afternoon Attendance");
                writer.newLine();
                for (i = 0; i < studentsList.size(); i++){
                    //k = i++;
                    writer.write(studentsList.get(i) + " , ");
                    writer.write(table.getValueAt(i, 2) + " , ");
                    writer.write(table.getValueAt(i, 3) + " ");
                    writer.newLine();
                    writer.flush();
                }
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

    private class cmdBackButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            dispose();
        }
    }

}
