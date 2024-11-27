
import java.io.*;
import java.util.*;

/**
 * The User class represents a user in the school management system.
 * Each user has a username and a password to authenticate.
 * It also manages user data files and provides methods to manage user information.
 */
public class User {
    
    /** The username of the user. */
    protected String username;
    
    /** The password of the user. */
    protected String password;

    /** The role of the user. */
    protected String role;
    
        
    /** List of existing user objects. */
    public ArrayList<User> existingUsers;
    
    /** List of all user objects. */
    public static ArrayList<User> userlist = new ArrayList<User>();
    
    /** The file associated with the user to store tasks. */
    protected File ufile;

    /**
     * Constructs a new User object with the given username and password.
     * Initializes the user's file to store tasks.
     *
     * @param uname the username of the user
     * @param upass the password of the user
     */
    public User(String uname, String upass, String urole) {
        username = uname;
        password = upass;
        role = urole;
        String fileName = username + ".txt";
        new File("Data/UserData/").mkdirs();
        ufile = new File("Data/UserData/" + fileName);
        
        try {
            if (ufile.createNewFile()) {
                System.out.println("File created");
            } else {
                System.out.println("File already exists");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        userlist.add(this);
    }

    /**
     * Gets the username of the user.
     *
     * @return the username of the user
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Gets the password of the user.
     *
     * @return the password of the user
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Gets the password of the user.
     *
     * @return the password of the user
     */
    public String getRole() {
        return role;
    }

    /**
     * Gets the file associated with the user.
     *
     * @return the file associated with the user
     */
    public File getUFile() {
        return this.ufile;
    }

    /**
     * Finds and returns a user object by username and password.
     *
     * @param name the username to search for
     * @param pass the password to search for
     * @return the user object if found, otherwise null
     */
    public static User findUser(String name, String pass) {
        User retval = null;
        for (User u : userlist)
            if ((u.getUsername().equals(name)) && (u.getPassword().equals(pass))) {
                retval = u;
                break;
            }
        return retval;
    }

    /**
     * Loads existing users from a file and creates User objects for each user.
     */
    public static void loadExistingUsers() {
        Scanner uScan = null;
        try {
            uScan = new Scanner(new File("Data/UserData/Users.txt"));
            while (uScan.hasNext()) {
                String[] nextLine = uScan.nextLine().split(" ");
                new User(nextLine[0], nextLine[1], nextLine[2]);
            }

            uScan.close();
        } catch (IOException e) {
            // Handle exception
        }
    }
}
