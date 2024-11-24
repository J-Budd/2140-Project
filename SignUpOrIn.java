import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

/**
 * SignUpOrIn class represents the sign-up or sign-in screen of the application.
 * It allows users to sign up with a new account or sign in with an existing account.
 */
public class SignUpOrIn extends JFrame {
    private boolean signIn; // Stores the state of whether a user is signing in or not
    private MainEntry mainScreen; // Reference to the main entry screen
    //TODO: Add individual screens for Teacher, Principal and Cook
    private PrincipalListing principalScreen; // Reference to the principal's main screen
    private TeacherListing teacherScreen; // Reference to the teachers' main screen
    private CookListing cookScreen; // Reference to the cooks' main screen

    private JLabel screenDescription; // Label displaying the screen description

    // Text fields for user input
    private JTextField usernameField;
    private JTextField passwordField;
    private JTextField secondPasswordField;
    private JTextField roleField;

    // Buttons for user actions
    private JButton cmdBack;
    private JButton cmdEnter;
    private JRadioButton principalButton;
    private JRadioButton teacherButton;
    private JRadioButton cookButton;

    // Panels for UI components
    private JPanel mainPanel;
    private JPanel secondPanel;

    /**
     * Constructor for SignUpOrIn class.
     * Initializes UI components and sets up the sign-up or sign-in screen.
     * 
     * @param signIn true if signing in, false if signing up
     * @param mainScreen reference to the main entry screen
     */
    public SignUpOrIn(boolean signIn, MainEntry mainScreen) {
        this.signIn = signIn;
        this.mainScreen = mainScreen;

        setLayout(new BorderLayout());

        mainPanel = new JPanel(new GridLayout(0, 1));
        secondPanel = new JPanel(new GridLayout(5, 2, 40, 10));

        screenDescription = new JLabel(signIn ? "Sign-In" : "Sign-Up");
        JLabel spacer = new JLabel("");
        usernameField = new JTextField();
        passwordField = new JTextField();
        cmdBack = new JButton("Back");
        cmdEnter = new JButton("Enter");

        screenDescription.setFont(new Font("Arial", Font.BOLD, 24));

        cmdBack.setBackground(Color.DARK_GRAY);
        cmdBack.setForeground(Color.WHITE);
        cmdEnter.setBackground(Color.DARK_GRAY);
        cmdEnter.setForeground(Color.WHITE);

        cmdEnter.addActionListener(new cmdEnterButtonListener());
        cmdBack.addActionListener(new cmdBackButtonListener());

        secondPanel.add(screenDescription);
        secondPanel.add(spacer);
        secondPanel.add(new JLabel("Username:"));
        secondPanel.add(usernameField);
        secondPanel.add(new JLabel("Password:"));
        secondPanel.add(passwordField);

        if (!signIn) {
            addSecondaryComponents();
        }
        secondPanel.add(cmdEnter);
        secondPanel.add(cmdBack);

        mainPanel.setPreferredSize(new Dimension(450, 300));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        mainPanel.add(secondPanel);
        add(mainPanel);
        pack();

        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Adds a second password field to the UI for sign-up state.
     */
    public void addSecondaryComponents() {
        //Add new password field for confirmation
        secondPasswordField = new JTextField();
        secondPanel.add(new JLabel("Re-enter Password: "));
        secondPanel.add(secondPasswordField);

        //adding a textfield that displays the role that was selected; TODO: Find a method that doesn't require it
        roleField = new JTextField();
        roleField.setEditable(false);
        secondPanel.add(new JLabel("User's Role: "));
        secondPanel.add(roleField);

        //Add radio button groups for roles
        teacherButton = new JRadioButton("Teacher");
        cookButton = new JRadioButton("Cook");
        principalButton = new JRadioButton("Principal");
        JRadioButton[] buttons = {teacherButton, cookButton, principalButton};

        //ensuring only one button can be selected at a time
        ButtonGroup group = new ButtonGroup();
        group.add(principalButton);
        group.add(teacherButton);
        group.add(cookButton);

        for (int i = 0; i < buttons.length; i++) {
            buttons[i].addActionListener(new Selected());
        }

    }

    /**
     * Displays an error pop-up with the given error message.
     * 
     * @param errorMessage the error message to display
     */
    private void showPopUp(String errorMessage) {
        JOptionPane.showMessageDialog(rootPane, errorMessage);
    }

    /**
     * ActionListener implementation for the back button.
     * Redirects the user to the main screen.
     */
    private class cmdBackButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            mainScreen.setVisible(true);
            dispose();
        }
    }

    /**
     * ActionListener implementation for the enter button.
     * Handles user input validation and navigation based on the sign-in or sign-up state.
     */
    private class cmdEnterButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password1 = passwordField.getText();
            ArrayList<User> users = User.userlist;
            User userLogin = null;
            boolean usernameAvail = true;

            if (!signIn) {
                for (User i : users) {
                    if (i.getUsername().equals(username)) {
                        usernameAvail = false;
                        showPopUp("Username Not Available");
                        return;
                    }
                }
            }

            if (usernameAvail && username.length() >= 3) {
                if (!signIn) {
                    String password2 = secondPasswordField.getText();
                    if (password1.length() < 3 || !password1.equals(password2)) {
                        showPopUp("Password Invalid");
                        return;
                    } else {
                        String role = roleField.getText();
                        if (role.equals("Principal")) {
                            showPopUp("Role Invalid");
                            return;
                        } else {
                            userLogin = new User(username, password1, role);
                        }
                    }
                } else {
                    userLogin = User.findUser(username, password1);
                    if (userLogin == null) {
                        showPopUp("Invalid login");
                        return;
                    }
                }
            } else {
                showPopUp("Username Invalid");
                return;
            }

            if (userLogin != null) {
                String role = User.getRole();
                try {
                    //TODO: Create new screens based on role selected
                    if (role.equals("Principal")) {
                        principalScreen = new PrincipalListing(userLogin);
                        principalScreen.setVisible(true);
                    } else if (role.equals("Principal")) {
                        teacherScreen = new TeacherListing(userLogin);
                        teacherScreen.setVisible(true);
                    } else if (role.equals("Principal")) {
                        cookScreen = new CookListing(userLogin);
                        cookScreen.setVisible(true);
                    } else {
                        showPopUp("Invalid Role");
                        return;
                    }
                } catch (IOException | ParseException ex) {
                    ex.printStackTrace();
                }
                setVisible(false);
            }
        }
    }

    private class Selected implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();//gets which button was pressed
            String name = e.getActionCommand();
            roleField.setText(name);
        }
    }

}