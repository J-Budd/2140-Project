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
 * TaskListing class extends JFrame to provide a user interface for registered teachers.
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

    
    }


}
