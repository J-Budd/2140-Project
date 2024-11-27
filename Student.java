
import java.io.*;
import java.util.*;
/**
 * The Student class represents a studnet in the school management system.
 * Each student has a name, birthday, address and relevant contact information
 * It also manages studnet data files and provides methods to manage student information.
 */
public class Student {
    /**Student Information */
    private String firstname;
    private String lastname;
    private String birthdate;
    private String address;
    private String guardian;
    private String regularContact;
    private String emergencyContact;

    /** List of all student objects */
    public static ArrayList<Student> studentList = new ArrayList<Student>();
    
    /** The file associated with existing students */
    protected File sfile;

    /**
     * Constructs a new Student object
     * Adds new student to the list
     */
    public Student(String sfname, String slname, String sbirthdate, String saddress, String sguardian, String sregularContact, String semergencyContact){
        this.firstname = sfname;
        this.lastname = slname;
        this.birthdate = sbirthdate;
        this.address = saddress;
        this.guardian = sguardian;
        this.regularContact = sregularContact;
        this.emergencyContact = semergencyContact;

        String fileName = slname + sfname + ".txt";
            new File("Data/Student/Records/").mkdirs();
            sfile = new File("Data/Student/Records/" + fileName);
            
            try {
                if (sfile.createNewFile()) {
                    System.out.println("File created");
                } else {
                    System.out.println("File already exists");
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

        studentList.add(this);
        UpdateFile(this);
    }

    /**
     * Accessor Functions
     */
    public String getFirstName(){
        return this.firstname;
    }

    public String getLastName(){
        return this.lastname;
    }

    public String getBirthdate(){
        return this.birthdate;
    }

    public String getAddress(){
        return this.address;
    }

    public String getGuardian(){
        return this.guardian;
    }

    public String getRegContact(){
        return this.regularContact;
    }

    public String getEContact(){
        return this.emergencyContact;
    }

    public File getSFile(){
        return sfile;
    }

    /**
     * Finds and returns a student based on first and last name
     */
    public static Student findStudent(String name1, String name2){
        Student retval = null;
        for (Student s: studentList)
            if (s.getFirstName().equals(name1) && s.getLastName().equals(name2)){
                retval = s;
                break;
            }
        return retval;
    }

    /**
     * Loads existing students from a file and creates Student objects for each user.
     */
    public static void loadExistingStudnets() {
        Scanner uScan = null;
        try {
            uScan = new Scanner(new File("Data/Student/Records/StudentsList.txt"));
            while (uScan.hasNext()) {
                String[] nextLine = uScan.nextLine().split(" ");
                new Student(nextLine[0], nextLine[1], nextLine[2], nextLine[3], nextLine[4], nextLine[5], nextLine[6]);
            }
            uScan.close();
        } catch (IOException e) {
            // Handle exception
        }
    }

    public static void UpdateFile(Student student) {
        File studentFile = new File("Data/Student/Records/StudentsList.txt");
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(studentFile, true))) {
                writer.write(student.getLastName() + " " + student.getFirstName());
                writer.newLine();
                writer.close();
            
                System.out.println("Student saved to file: " + studentFile);
        } catch (IOException e) {
            System.out.println("Error saving tasks to file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

