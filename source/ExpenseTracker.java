package source;

import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;


@Command(name = "ExpenseTracker", mixinStandardHelpOptions = true, version = "1.0", description = "Tracks user expenses.")

public class ExpenseTracker implements Runnable
{

    @Option(names = {"-n", "--name"}, description = "User's name", required = true)
    private String name;

    @Option(names = {"-d", "--description"}, description = "Expense description")
    private String description;

    @Option(names = {"-add"}, description = "Add a new expense", defaultValue = "true")
    private boolean addExpense;

    @Option(names = {"-del"}, description = "Delete an expense", defaultValue = "true")
    private boolean deleteExpense;

    @Option(names = {"-summary"}, description = "Shows the summary of your expenses", defaultValue = "true")
    private boolean summary;

    @Option(names = {"--amount"}, description = "Amount", defaultValue = "0")
    private float amount;

    @Option(names = {"-id"}, description = "ID of the entry", defaultValue = "1")
    private int id;

    public static void main(String[] args)
    {
        int exitCode = new CommandLine(new ExpenseTracker()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run()
    {
        startApp();
    }

    public void startApp ()
    {
        Scanner sc = new Scanner(System.in);  
        try
        {
            String fileName = name +"'s_Expense.csv";
            File myObj = new File (fileName);
            if (myObj.exists())
            {
                if (addExpense)
                {
                    addEntry(fileName);
                }
                else if (deleteExpense)
                {
                    deleteEntry(fileName);
                }
                else if (summary)
                {
                    showSummary(fileName);
                }
            }
            else 
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
        catch (Exception e)
        {
            System.out.println("Error accessing file: " + e.getMessage());
        }
    }    

    public void addUser(Boolean newUser, String name, Scanner sc)
    {
        if (newUser)
        {
            try
            {
                File fileObj = new File(name + ".csv");
                if (fileObj.createNewFile())
                {
                    System.out.println("Successfully Created a account with your name. Please add your 1st Expense now");
                    startApp();
                }
            } 
            catch (Exception e)
            {
                System.out.println("exception while creating an account with your name :: "+e);
            }
        }
    }

    public void addEntry(String fileName)
    {
        if (description != null && amount!=0)
        {
            int nextlLine = getTotalLines(fileName) + 1;
            try
            {
                FileWriter fileWriter = new FileWriter(fileName, true);
                fileWriter.write(nextlLine + "," + description + "," + amount + "\n");
                fileWriter.close();
            }
            catch (Exception e)
            {
                System.out.println("Exception while dding entry");
            }
            

        }
    }

    public void deleteEntry(String fileName)
    {

    }

    public void showSummary(String fileName)
    {

    }

    public int getTotalLines(String fileName)
    {
        int line = 0;
        try 
        {
            File myObj = new File(fileName);
            Scanner fileReder = new Scanner(myObj);
            while (fileReder.hasNextLine())
            {
                line++;
                fileReder.nextLine();
            }
        }
        catch (Exception e)
        {
            System.out.println("Error while reading the file");
        }
        return line;
    }
}