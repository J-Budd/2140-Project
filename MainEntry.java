import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * MainEntry class represents the main entry screen of the application.
 * It contains buttons to sign in, sign up, and close the application.
 */
public class MainEntry extends JFrame {
    private SignUpOrIn signUpOrIn; // Reference to the SignUpOrInClass instance

    private JLabel screenDescription; // Label displaying the screen description
    
    private JButton cmdSignIn; // Button for signing in
    private JButton cmdSignUp; // Button for signing up
    private JButton cmdClose; // Button for closing the application

    private JPanel panel; // Panel containing UI elements

    private MainEntry thisForm; // Reference to the current MainEntry instance

    /**
     * Constructor for MainEntry class.
     * Initializes UI components and sets up the main entry screen.
     */
    public MainEntry() {
        thisForm = this;

        // Creating panel with a grid layout and setting border
        panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(new EmptyBorder(50, 50, 50, 50));

        // Screen elements
        screenDescription = new JLabel("Lakespen Basic School Database Manager", SwingConstants.CENTER);
        cmdSignIn = new JButton("Sign In");
        cmdSignUp = new JButton("Sign Up");
        cmdClose = new JButton("Close");

        // Element Properties
        screenDescription.setFont(new Font("Arial", Font.BOLD, 18));

        cmdClose.setBackground(Color.DARK_GRAY);
        cmdClose.setForeground(Color.WHITE);
        cmdClose.setPreferredSize(new Dimension(150, 50));

        cmdSignIn.setBackground(Color.DARK_GRAY);
        cmdSignIn.setForeground(Color.WHITE);
        cmdSignIn.setPreferredSize(new Dimension(150, 50));

        cmdSignUp.setBackground(Color.DARK_GRAY);
        cmdSignUp.setForeground(Color.WHITE);
        cmdSignUp.setPreferredSize(new Dimension(150, 50));

        // Action listeners
        cmdClose.addActionListener(new CloseButtonListener());
        cmdSignIn.addActionListener(new cmdSignInButtonListener());
        cmdSignUp.addActionListener(new cmdSignUpButtonListener());

        // Adding elements to the panel
        panel.add(screenDescription);
        panel.add(cmdSignIn);
        panel.add(cmdSignUp);
        panel.add(cmdClose);
        panel.setPreferredSize(new Dimension(500, 400));

        add(panel);
        pack();
        setContentPane(panel);
    }

    /**
     * Creates and displays the MainEntry GUI.
     */
    private static void createAndShowGUI() {
        MainEntry mainEntry = new MainEntry();
        mainEntry.setPreferredSize(new Dimension(500, 400));
        mainEntry.setResizable(false);
        mainEntry.setLocationRelativeTo(null);
        mainEntry.setVisible(true);
    }

    /**
     * Main method to start the application.
     * Loads existing users and invokes createAndShowGUI method.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                User.loadExistingUsers();
                createAndShowGUI();
            }
        });
    }

    /**
     * ActionListener implementation for handling sign-in button click.
     * Displays sign-in screen and hides the main entry screen.
     */
    private class cmdSignInButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            signUpOrIn = new SignUpOrIn(true, thisForm);
            signUpOrIn.setVisible(true); // Making screen visible
            setVisible(false);
        }
    }

    /**
     * ActionListener implementation for handling sign-up button click.
     * Displays sign-up screen and hides the main entry screen.
     */
    private class cmdSignUpButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            signUpOrIn = new SignUpOrIn(false, thisForm);
            signUpOrIn.setVisible(true); // Making screen visible
            setVisible(false);
        }
    }

    /**
     * ActionListener implementation for the close button to exit the application.
     * Exits the application when the close button is clicked.
     */
    private class CloseButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }
}
