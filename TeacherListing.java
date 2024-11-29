import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.io.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

/**
 * TeacherListing class extends JFrame to provide a user interface for registered teachers.
 * Teachers can manage student records, check student attendance, and manage assignments 
 */
@SuppressWarnings({ "unused", "serial" })
public class TeacherListing extends JFrame {
    User currentUser; // Reference to the current user
    TeacherListing thisForm; // Reference to the TaskListing instance

    // Buttons for user actions
    private JButton cmdDisplayRecords;
    private JButton cmdDisplayAttendance;
    private JButton cmdDisplayAssignments;
    private JButton cmdClose;

    private JPanel pnlGreeting; //Welcomes the current user
    private JPanel pnlSummary; //Panel that displays current information
    private JPanel pnlCommand; //Panel that holds buttons

    //Labels
    private JLabel name;
    private JLabel reminder;
    private JLabel assignmentDayReminder;
    private JLabel assignmentMorrowReminder;

    /**
     * Constructor for TaskListing class.
     * Initializes the TaskListing instance with the current user and loads tasks from file.
     * 
     * @param currentUser the current user
     * @throws IOException    if an I/O error occurs
     * @throws ParseException if the parsing fails
     */
    public TeacherListing(User currentUser) throws IOException, ParseException {
        this.currentUser = currentUser;
        thisForm = this;

        setLayout(new BorderLayout());

        pnlGreeting = new JPanel();
        name = new JLabel("Welcome back, " + this.currentUser.getUsername());
        pnlGreeting.add(name);
        add(pnlGreeting,BorderLayout.NORTH);

        pnlSummary = new JPanel();
        //Display reminder to take attendance
        reminder = new JLabel("Remember to take attendance and hand out assignments today!");
        pnlSummary.add(reminder);
        add(pnlSummary,BorderLayout.CENTER);

        pnlCommand = new JPanel();
        //Formatting buttons
        cmdDisplayRecords = new JButton("View Student Records");
        cmdDisplayAttendance = new JButton("View Student Attendance");
        cmdDisplayAssignments = new JButton("View Assignments");
        cmdClose = new JButton("Close");

        Color buttonColor = new Color(135, 206, 235);
        cmdDisplayRecords.setBackground(buttonColor);
        cmdDisplayAttendance.setBackground(buttonColor);
        cmdDisplayAssignments.setBackground(buttonColor);
        cmdClose.setBackground(buttonColor);

        cmdDisplayRecords.addActionListener(new DisplayRecordsButtonListener());
        cmdDisplayAttendance.addActionListener(new DisplayAttendanceButtonListener());
        cmdDisplayAssignments.addActionListener(new DisplayAssignmentsButtonListener());
        cmdClose.addActionListener(new CloseButtonListener());

        pnlCommand.add(cmdDisplayRecords);
        pnlCommand.add(cmdDisplayAttendance);
        pnlCommand.add(cmdDisplayAssignments);
        pnlCommand.add(cmdClose);

        add(pnlCommand,BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);    
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
     * ActionListener implementation to save tasks to file and close the application.
     */
    private class CloseButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            saveUsers();
            System.exit(0);
        }
    }


    /**
     * Saves user details to the users file.
     */
    private void saveUsers() {
        File usersFile = new File("Data/UserData/Users.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(usersFile))) {
            for (User user : User.userlist) {
                writer.write(user.getUsername() + " " + user.getPassword() + " " + user.getRole());
                writer.newLine();
            }
            System.out.println("Users saved to file: " + usersFile);
        } catch (IOException e) {
            System.out.println("Error saving users to file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private class DisplayRecordsButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            StudentListing allStudents = new StudentListing();
        	   allStudents.setVisible(true);   
        }

    }

    private class DisplayAttendanceButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            try{
                AttendanceListing attendance = new AttendanceListing(thisForm);
                attendance.setVisible(true);
            }  catch (IOException | ParseException ex) {
                ex.printStackTrace();
            }
        }
        
    }

    private class DisplayAssignmentsButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
                GradeManagementSystem system = new GradeManagementSystem();
                system.setVisible(true);
        }
    }

    
}
