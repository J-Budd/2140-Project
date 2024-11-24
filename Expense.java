//Todo 
//1.1: The system shall prompt the user with a data entry screen.
//1.2: The system shall sort the expense entered by type, e.g. Salary,Groceries, etc.
//1.3: The system shall provide a means for an authorized user to make changes within a day of the initial entry. 
//1.4: The system shall provide a means for an authorized user to search for specific expenses by date or by category. 
//1.5: The system shall generate a summary expense report at the end of the month for an authorized user 
//1.6: The system shall provide a detailed expense report at the end of a three month period for an authorized user. 



import java.time.LocalDate;

public class Expense{
    private LocalDate date;
    private String type;
    private double amount;
    private String about;

    public Expense(String type, double amount,String about){
        this.date= LocalDate.now();
        this.type = type;
        this.amount = amount;
        this.about = about; 
    }

    public Expense(LocalDate date, String type,double amount,String about){
        this.date =  date;
        this.type = type;
        this.amount = amount;
        this.about = about; 
    }

    public LocalDate getDate(){
        return date;
    }


    public String getType(){
        return type;
    }

    public double getamountb(){
        return date;
    }
    
    
}
