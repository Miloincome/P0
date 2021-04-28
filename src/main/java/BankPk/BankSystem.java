package BankPk;

import com.app.BusinessException;
import com.app.dao.BankAccountDAO;
import com.app.dao.TransactionDAO;
import com.app.dao.UserDAO;
import com.app.dao.impl.BankAccountDAOImpl;
import com.app.dao.impl.TransactionDAOImpl;
import com.app.dao.impl.UserDAOImpl;
import com.app.model.BankAccount;
import com.app.model.Transaction;
import com.app.model.User;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class BankSystem {

    private static Logger log = Logger.getLogger(System.class);

    public static void run() {
        boolean systemRunning = true;
        Scanner input = new Scanner(System.in);

        User newUser = new User();//singleton
        UserDAO userDAO = new UserDAOImpl();

        BankAccount newAccount = new BankAccount();//singleton
        BankAccountDAO baDAO = new BankAccountDAOImpl();

        Transaction newTrans = new Transaction();//singleton
        TransactionDAO transDAO = new TransactionDAOImpl();

        boolean correctBalance = false;
        boolean validated = false;
        boolean loggedIn = false;
        boolean customerLoggedIn = false;
        boolean employeeLoggedIn = false;

        while (systemRunning == true) {//Display Login Screen
            System.out.println(
                    "Welcome to the Bank App" +
                            "\nWould you like to Login as Employee, Customer or Create an Account?" +
                            "\nType Login to Login," +
                            "Type Create to Create Account");
            String selection = input.nextLine();

            if (selection.equals("Login"))//if user logs in
            {
                newUser = new User();
                System.out.println("\nEnter your Login Credentials" +
                        "\nUser Name: ");
                newUser.setUserName(input.nextLine());
                System.out.println("Now enter your password...");
                newUser.setPassword(input.nextLine());
                try {
                    validated = userDAO.findUser(newUser);
                } catch (SQLException e) {
                    System.out.println("Internal Error...." + e);
                }
                if (validated == true) {
                    loggedIn = true;
                    System.out.println("You logged in");
                    try {
                        newUser = userDAO.setUser(newUser);
                    } catch (SQLException e) {
                        System.out.println("Internal error setting user" + e);
                    }
                    System.out.println("Your User Account Details: " + newUser.toString());
                    while (loggedIn == true) {
                        if (newUser.getUserType().equals("Customer")) {

                                customerLoggedIn = true;
                                System.out.println("Do you wish to view or Apply for a new bank account? or Exit?");
                                System.out.println("Enter V for view accounts, Enter A to apply, or E to Exit");
                                selection = input.nextLine();
                                if (selection.equals("A")) {
                                    do {
                                        System.out.println("Please enter C for Checking or S for savings.");
                                        selection = input.nextLine();
                                    } while (!(selection.equals("C") || selection.equals("S") || selection.equals("E")));
                                    if (selection.equals("C")) {
                                        System.out.println("You have selected Checking.");
                                        newAccount.setAccountType("Checking");
                                    } else if (selection.equals("S")) {
                                        System.out.println("You have selected Savings.");
                                        newAccount.setAccountType("Savings");
                                    } else if (selection.equals("E")) {
                                        System.out.println("Now Exiting");
                                        loggedIn = false;
                                    } else {
                                        System.out.println("Error...");
                                    }
                                    System.out.println("Please enter the name of your Bank Account: ");
                                    newAccount.setName(input.nextLine());
                                    while (correctBalance == false) {
                                        System.out.println("Please enter starting Balance");
                                        newAccount.setBalance(BigDecimal.valueOf(Double.parseDouble(input.nextLine())));
                                        if (newAccount.getBalance().doubleValue() >= 0) {
                                            correctBalance = true;
                                        } else {
                                            System.out.println("incorrect balance.");
                                        }
                                    }
                                    try {
                                        newAccount = baDAO.createBankAccount(newAccount, newUser);
                                    } catch (SQLException e) {
                                        System.out.println("Internal Error Occured" + e);
                                    }
                                    System.out.println("Congrats New Bank Account has been Applied for!\nPlease wait for associate approval!\n ");
                                }//Applying
                                else if (selection.equals("E")) {
                                    customerLoggedIn = false;
                                }//Exit
                                else if (selection.equals("V")) {
                                    System.out.println("Viewing Bank Accounts....");
                                    try {
                                        List<BankAccount> bankAccountList = userDAO.viewAccounts(newUser);
                                        if (bankAccountList != null && bankAccountList.size() > 0) {
                                            System.out.println("We have total " + bankAccountList.size() + " accounts\n");
                                            for (BankAccount b : bankAccountList) {
                                                System.out.println(b);
                                            }
                                        }
                                    } catch (SQLException e) {
                                        System.out.println("Bank account list could not be generated");
                                    }
                                    System.out.println("Reminder: You may only Deposit/Withdraw and Post/Accept Transfer From/To approved accounts ");
                                    System.out.println("Which Account's transactions do you want to view? Select the account number.");
                                    try {
                                        newAccount.setAccountNumber(Integer.parseInt(input.nextLine()));
                                    }catch (Exception e)
                                    {
                                        System.out.println("Invalid Entry");
                                    }
                                    try {
                                        baDAO.setBankAccount(newAccount);
                                    } catch (SQLException e) {
                                        System.out.println("Error setting user account "+e);
                                    }
                                    System.out.println("Selected: "+ newAccount.toString());
                                    System.out.println("Select Depo for Deposit, Wit for Withdraw, or View for View Transactions");
                                    selection = input.nextLine();
                                    if(selection.equals("Depo")){
                                        System.out.println("How much you would like to deposit?");
                                        double deposit= Double.parseDouble(input.nextLine());
                                        try {
                                            baDAO.deposit(newAccount, deposit,newTrans);
                                        } catch (SQLException e) {
                                            System.out.println(e);
                                        }


                                    }else if(selection.equals("Wit")){
                                        System.out.println("How much you would like to withdraw?");
                                        double withdraw= Double.parseDouble(input.nextLine());
                                        try {
                                            baDAO.Withdraw(newAccount,withdraw,newTrans);
                                        } catch (SQLException e) {
                                            System.out.println(e);
                                        }

                                    }else if(selection.equals("View")){
                                        try {
                                            List<Transaction> transactionList = baDAO.viewTransactions(newAccount);
                                            if (transactionList != null && transactionList.size() > 0) {
                                                System.out.println("We have total " + transactionList.size() + " accounts\n");
                                                for (Transaction t : transactionList) {
                                                    System.out.println(t);
                                                }

                                                System.out.println("Would you like to post/accept a money transfer?");
                                                System.out.println("Select Post to Post and Accept to Accept a Money Transfer");
                                                selection = input.nextLine();
                                                if(selection.equals("Post"))
                                                {
                                                    System.out.println("Please enter description");
                                                    newTrans.setDescription(input.nextLine());
                                                    System.out.println("Please enter amount");
                                                    newTrans.setAmount(BigDecimal.valueOf(Double.parseDouble(input.nextLine())));
                                                    System.out.println("Please enter account to transfer money towards.");
                                                    newTrans.setRefaccount(Integer.parseInt(input.nextLine()));
                                                    newTrans.setStatus("Pending");
                                                    transDAO.Post(newTrans,newAccount);
                                                    System.out.println("Money has been posted");

                                                }
                                                if(selection.equals("Accept"))
                                                {
                                                    System.out.println("These are the accounts that Posted to you.");
                                                    List<Transaction> transactionListPosted = transDAO.viewPostedTransAction(newAccount);
                                                    if (transactionListPosted != null && transactionListPosted.size() > 0) {
                                                        System.out.println("We have total " + transactionListPosted.size() + " accounts\n");
                                                        for (Transaction tp : transactionListPosted) {
                                                            System.out.println(tp);
                                                        }
                                                        System.out.println("Select the transaction id to accept Money Transfer");
                                                        newTrans.setTransactionId(Integer.parseInt(input.nextLine()));
                                                        transDAO.setTransaction(newTrans);
                                                        transDAO.Accept(newTrans,newAccount);
                                                        System.out.println("Money Transfer Accepted");
                                                    }
                                                }
                                            }
                                        } catch (SQLException e) {
                                            System.out.println("No Transactions found");
                                        }

                                    }else{
                                        System.out.println("Invalid Selection");

                                    }

                                }//View Accounts
                                else {
                                    System.out.println("Invalid selection");
                                }//if you dont enter the above
                            loggedIn = false;
                        }//When logged in as customer
                        else if (newUser.getUserType().equals("Employee")) {

                                System.out.println("Do you wish to view customer's bank accounts? or Exit?");
                                System.out.println("Enter V for view customers, or E to Exit");
                                selection = input.nextLine();
                                if (selection.equals("V")) {
                                    System.out.println("Viewing Customer Accounts....");
                                    try {
                                        List<User> customerList = userDAO.viewCustomers(newUser);
                                        if (customerList != null && customerList.size() > 0) {
                                            System.out.println("We have total " + customerList.size() + " customer accounts\n");
                                            for (User c : customerList) {
                                                System.out.println(c);
                                            }
                                        }
                                    } catch (SQLException e) {
                                        System.out.println("Customer list could not be generated " + e);
                                        ;
                                    }
                                    System.out.println("Select Customer to view accounts by username");
                                    newUser.setUserName(input.nextLine());
                                    validated = false;
                                    try {
                                        validated = userDAO.findUser(newUser);
                                    } catch (SQLException e) {
                                        System.out.println("Failure Validating Customer.");
                                        ;
                                    }
                                    if (validated == true) {
                                        try {
                                            userDAO.setUser(newUser);
                                        } catch (SQLException e) {
                                            System.out.println("Failure to set User");
                                        }
                                        System.out.println("Looking at : " + newUser.toString());
                                        System.out.println("Printing Bank Accounts....");
                                        try {
                                            List<BankAccount> bankAccountList = userDAO.viewAccounts(newUser);
                                            if (bankAccountList != null && bankAccountList.size() > 0) {
                                                System.out.println("We have total " + bankAccountList.size() + " accounts\n");
                                                for (BankAccount b : bankAccountList) {
                                                    System.out.println(b);
                                                }
                                            }
                                        } catch (SQLException e) {
                                            System.out.println("Bank account list could not be generated");
                                            ;
                                        }
                                        System.out.println("Select Bank Account by account number");
                                        try {
                                            newAccount.setAccountNumber(Integer.parseInt(input.nextLine()));
                                        }catch(Exception e){
                                            System.out.println("Please enter a correct number.");
                                        }
                                        try {
                                            validated = baDAO.findBankAccount(newAccount);
                                        } catch (SQLException e) {
                                            System.out.println("Bank Account Validation failed: " + e);
                                        }
                                        if (validated == true) {
                                            try {
                                                newAccount = baDAO.setBankAccount(newAccount);
                                            } catch (SQLException e) {
                                                System.out.println("Error Setting Bank Account " + e);
                                            }
                                            System.out.println("Enter Approve or Reject for status, View for view transactions regarding:" + newAccount.toString());
                                            selection = input.next();
                                            if (selection.equals("Approve")) {
                                                try {
                                                    System.out.println("Account has been Approved");
                                                    baDAO.approveAccount(newAccount);

                                                } catch (SQLException e) {
                                                }
                                            } else if (selection.equals("Reject")) {
                                                try {
                                                    System.out.println("Account has been rejected");
                                                    baDAO.rejectAccount(newAccount);
                                                } catch (SQLException e) {
                                                }
                                            } else if (selection.equals("View")) {
                                                try {
                                                    List<Transaction> transactionList = baDAO.viewTransactions(newAccount);
                                                    if (transactionList != null && transactionList.size() > 0) {
                                                        System.out.println("We have total " + transactionList.size() + " accounts\n");
                                                        for (Transaction t : transactionList) {
                                                            System.out.println(t);
                                                        }
                                                        System.out.println("Would you like to Post/Accept a money transfer?");

                                                        System.out.println("Select P to Post, A to Accept");
                                                    }
                                                } catch (SQLException e) {
                                                    System.out.println("No Transactions Found.");
                                                }
                                            }else{
                                                System.out.println("Invalid Selection");
                                            }
                                        } else if (validated == false) {
                                            System.out.println("Selected Bank Account doesn't exist");
                                        }

                                    } else if (validated == false) {
                                        System.out.println("Selected username is not a Customer.");
                                    }///if you did not select a user
                                    else {
                                        System.out.println("Error Validating Customer");//if error validating customer
                                    }
                                } else if (selection.equals("E")) {
                                    employeeLoggedIn = false;
                                } else {
                                    System.out.println("Invalid Selection. Please refer to the selection");
                                }

                            loggedIn=false;
                        }//When logged in as employee.

                        else {
                            System.out.println("Failure Setting user");
                            loggedIn = false;
                        }//when user cannot be set
                    }
                } else if (validated == false)//if user does not log in correctly
                {
                    System.out.println("Incorrect Username/Password.");
                }
            } else if (selection.equals("Create"))//this is to create user account
            {
                System.out.println("\nLets create an account!");
                System.out.println("Enter a Username.");
                newUser.setUserName(input.nextLine());
                System.out.println("Enter your Full name.");
                newUser.setFullName(input.nextLine());
                System.out.println("Enter your Password.");
                newUser.setPassword(input.nextLine());
                System.out.println("Creating Customer");
                newUser.setUserType("Customer");
                System.out.println(newUser.toString());
                try {
                    newUser = userDAO.createUser(newUser);
                } catch (SQLException e) {
                    System.out.println("Internal Error Occured");
                }
                System.out.println("Please relog.... Thank You!\n");
            } else//Enters a wrong selection
            {
                System.out.println("incorrect selection");
            }
        }


    }

}

