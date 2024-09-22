package source;

import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

@Command(name = "ExpenseTracker", mixinStandardHelpOptions = true, version = "1.0", description = "Tracks user expenses.",
subcommands = {ExpenseTracker.AddCommand.class, ExpenseTracker.DeleteCommand.class, ExpenseTracker.SummaryCommand.class})

public class ExpenseTracker implements Runnable {

    public static void main(String[] args) {
        int exitCode = new CommandLine(new ExpenseTracker()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() { }
    
    // Subcommand to add an expense
    @Command(name = "add", description = "Add a new expense")
    static class AddCommand implements Runnable {

        @Option(names = {"-d", "--description"}, description = "Expense description", required = true)
        private String description;

        @Option(names = {"--amount"}, description = "Amount", required = true)
        private float amount;

        @Option(names = {"-n", "--name"}, description = "User's name", required = true)
        private String name;

        @Override
        public void run()
        {
            startApp();
        }

        public void startApp() 
        {
            String fileName = name + "'s_Expenses.csv";
            try 
            {
                File file = new File(fileName);
                if (!file.exists())
                {
                    Scanner sc = new Scanner(System.in);
                    System.out.println("Hello "+ name +", Welcome to Expense Tracker Application!");
                    System.out.println("You haven't added any entries yet. Wanna start tracking your exepenses now? (Yes - Y / NO - N)");
                    String choice = sc.nextLine();
                    if (choice.equals("Y"))
                    {
                        addUser(true, name, sc);
                    }
                    else 
                    {
                        System.exit(0);
                    }
                }
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                LocalDateTime currentLocalTime = LocalDateTime.now();
                String date = dateFormatter.format(currentLocalTime);
                int nextLine = getTotalLines(fileName) + 1;

                try (FileWriter fileWriter = new FileWriter(fileName, true))
                {
                    fileWriter.write(nextLine + "," + date + "," + description + "," + amount + "\n");
                    System.out.println("Expense added successfully.");
                }
            }
            catch (Exception e)
            {
                System.out.println("Error while adding entry: " + e.getMessage());
            }
        }
        public void addUser(Boolean newUser, String name, Scanner sc)
        {
            if (newUser)
            {
                try
                {
                    File fileObj = new File(name + "'s_Expenses.csv");
                    if (fileObj.createNewFile())
                    {
                        System.out.println("Successfully Created a account with your name. Your first expense will added now...");
                    }
                } 
                catch (Exception e)
                {
                    System.out.println("exception while creating an account with your name :: "+e);
                }
            }
        }

        private int getTotalLines(String fileName)
        {
            int line = 0;
            try (Scanner fileReader = new Scanner(new File(fileName)))
            {
                while (fileReader.hasNextLine())
                {
                    line++;
                    fileReader.nextLine();
                }
            }
            catch (Exception e)
            {
                System.out.println("Error while reading the file.");
            }
            return line;
        }
    }

    // Subcommand to delete an expense
    @Command(name = "delete", description = "Delete an expense")
    static class DeleteCommand implements Runnable {

        @Option(names = {"-id"}, description = "ID of the expense to delete", required = true)
        private int id;

        @Option(names = {"-n", "--name"}, description = "User's name", required = true)
        private String name;

        @Override
        public void run()
        {
            String fileName = name + "'s_Expenses.csv";
            File originalFile = new File(fileName);
            File tempFile = new File("temp_" + fileName);

            try (Scanner fileReader = new Scanner(originalFile); FileWriter fileWriter = new FileWriter(tempFile))
            {
                boolean found = false;
                while (fileReader.hasNextLine())
                {
                    String line = fileReader.nextLine();
                    String[] entry = line.split(",");
                    if (Integer.parseInt(entry[0]) == id) {
                        found = true;
                        System.out.println("Entry with ID " + id + " deleted successfully.");
                        continue;
                    }
                    fileWriter.write(line + "\n");
                }

                if (!found)
                {
                    System.out.println("Entry with ID " + id + " not found.");
                }
            }
            catch (Exception e)
            {
                System.out.println("Error while deleting entry: " + e.getMessage());
            }

            // Replace the original file with the modified file
            if (originalFile.delete() && tempFile.renameTo(originalFile))
            {
                System.out.println("File updated successfully.");
            }
            else
            {
                System.out.println("Error updating file.");
            }
        }
    }

    // Subcommand to show the summary of expenses
    @Command(name = "list", description = "Shows the list of your expenses")
    static class SummaryCommand implements Runnable
    {

        @Option(names = {"-n", "--name"}, description = "User's name", required = true)
        private String name;

        @Override
        public void run()
        {
            String fileName = name + "'s_Expenses.csv";
            try (Scanner fileReader = new Scanner(new File(fileName)))
            {
                System.out.println("Summary of Expenses:");
                while (fileReader.hasNextLine())
                {
                    String line = fileReader.nextLine();
                    System.out.println(line);
                }
            }
            catch (Exception e)
            {
                System.out.println("Error reading file: " + e.getMessage());
            }
        }
    }
}