import java.util.Scanner;
import java.io.*;

public class ExpenseTracker {
    public static void main (String[] args)
    {
        System.out.println("Starting the application");
        startApp();
    }

    public static void startApp ()
    {
        System.out.println("Please Enter your name to access the applicaiton");
        Scanner sc = new Scanner(System.in);   
        String name = sc.nextLine();
        
        try
        {
            File myObj = new File (name + ".txt");
            Scanner filereader = new Scanner(myObj);
            System.out.println("Welcome Back "+ name +"!");
            
            
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Hello "+ name +", Welcome to Expense Tracker Application!");
            System.out.println("You haven't added any entries yet. Wanna start tracking your exepenses now? (Yes - Y / NO - N)");
            String choice = sc.nextLine();
            if (choice.equals("Y"))
            {
                addUser(true, name, sc);
            }
        }
    }    

    public static void addUser(Boolean newUser, String name, Scanner sc)
    {
        if (newUser)
        {
            try
            {
                File myObj = new File("filename.txt");
                if (myObj.createNewFile())
                {
                    System.out.println("Successfully Created a account with your name. Please add your 1st Expense now");
                }
            } 
            catch (Exception e)
            {
                System.out.println("exception while creating an account with your name :: "+e);
            }
        }
    }
}