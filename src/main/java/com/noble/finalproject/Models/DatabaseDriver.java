package com.noble.finalproject.Models;

import javafx.collections.FXCollections;

import java.sql.*;
import java.time.LocalDate;

public class DatabaseDriver {
    private Connection conn;

    // Constructor for initializing the database connection
    public DatabaseDriver() {
        openConnection();
    }
    public void openConnection(){
        try {
            Class.forName("org.sqlite.JDBC");
            this.conn = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\cypri\\IdeaProjects\\FinalProject\\noblebank.db");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*
     *   CLIENT SECTION
     */
    public ResultSet getClientData(String pAddress, String password) {
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM Clients WHERE PayeeAddress = ? AND Password = ?";
            PreparedStatement preparedStatement = this.conn.prepareStatement(query);
            preparedStatement.setString(1, pAddress);
            preparedStatement.setString(2, password);

            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet getTransactions(String pAddress, int limit){
        Statement statement;
        ResultSet resultSet = null;
        try{
            statement = this.conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Transactions WHERE Sender='"+pAddress+"' OR Receiver='"+pAddress+"' LIMIT "+limit+";");
        } catch (SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }


//    METHOD RETURNS SAVINGS ACCOUNT BALANCE
    public  double getSavingsAccountBalance(String pAddress){
        Statement statement;
        ResultSet resultSet;
        double balance = 0;
        try {
            statement = this.conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM SavingsAccounts WHERE Owner = '"+pAddress+"';");
            balance = resultSet.getDouble("Balance");
        } catch (SQLException e){
            e.printStackTrace();
        }
        return balance;
    }


//  METHOD TO EITHER ADD OR SUBTRACT FROM BALANCE GIVING OPERATION
    public void updateBalance(String pAddress, double amount, String operation){
        Statement statement;
        ResultSet resultSet;
        try {
            statement = this.conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM SavingsAccounts WHERE Owner = '"+pAddress+"';");
            double newBalance;
            if (operation.equals("ADD")){
                newBalance = resultSet.getDouble("Balance") + amount;
                statement.executeUpdate("UPDATE SavingsAccounts SET Balance= "+newBalance+" WHERE Owner= '"+pAddress+"';");
            } else {
                if (resultSet.getDouble("Balance") >= amount){
                    newBalance = resultSet.getDouble("Balance") - amount;
                    statement.executeUpdate("UPDATE SavingsAccounts SET Balance= "+newBalance+" WHERE Owner= '"+pAddress+"';");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void transToChe(String pAddress, double amount, String operation){
        Statement statement;
        ResultSet resultSet;
        try {
            statement = this.conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM SavingsAccounts WHERE Owner = '"+pAddress+"';");
            double newBalance;
            if (operation.equals("addToChecking")){
                newBalance = resultSet.getDouble("Balance") + amount;
                statement.executeUpdate("UPDATE CheckingAccounts SET Balance= "+newBalance+" WHERE Owner= '"+pAddress+"';");
            } else if (operation.equals("subFromSavings")){
                if (resultSet.getDouble("Balance") >= amount){
                    newBalance = resultSet.getDouble("Balance") - amount;
                    statement.executeUpdate("UPDATE SavingsAccounts SET Balance= "+newBalance+" WHERE Owner= '"+pAddress+"';");
                }
            } else if (operation.equals("addToSavings")){
                newBalance = resultSet.getDouble("Balance") + amount;
                statement.executeUpdate("UPDATE SavingsAccounts SET Balance= "+newBalance+" WHERE Owner= '"+pAddress+"';");
            } else {
                if (resultSet.getDouble("Balance") >= amount){
                    newBalance = resultSet.getDouble("Balance") - amount;
                    statement.executeUpdate("UPDATE CheckingAccounts SET Balance= "+newBalance+" WHERE Owner= '"+pAddress+"';");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




//    CREATE AND RECORD NEW TRANSACTION
    public void newTransaction(String sender,String receiver, double amount, String message){
        Statement statement;
        try {
          statement = this.conn.createStatement();
          LocalDate date = LocalDate.now();
          statement.executeUpdate("INSERT INTO "+
                  "Transactions(Sender, Receiver, Amount, Date, Message) " +
                  "VALUES ('"+sender+"', '"+receiver+"', "+amount+", '"+date+"', '"+message+"');"
                  );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    /*
     *    ADMIN SECTION
     */
    public  ResultSet getAdminData(String username, String password){
        ResultSet resultSet = null;
        try{
            String query = "SELECT * FROM Admins WHERE Username = ? AND Password = ?";
            PreparedStatement preparedStatement = this.conn.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            resultSet = preparedStatement.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public void createClient(String fName, String lName, String pAddress, String password, String date) {
        String query = "INSERT INTO Clients (FirstName, LastName, PayeeAddress, Password, Date) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = this.conn.prepareStatement(query)) {
            preparedStatement.setString(1, fName);
            preparedStatement.setString(2, lName);
            preparedStatement.setString(3, pAddress);
            preparedStatement.setString(4, password);
            preparedStatement.setString(5, date);

            preparedStatement.executeUpdate();
            System.out.println("Date to save "+date);
            System.out.println("Client created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createCheckingAccount(String owner, String number, double tLimit, double balance) {
        String query = "INSERT INTO CheckingAccounts (Owner, AccountNumber, TransactionLimit, Balance) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = this.conn.prepareStatement(query)) {
            preparedStatement.setString(1, owner);
            preparedStatement.setString(2, number);
            preparedStatement.setDouble(3, tLimit);
            preparedStatement.setDouble(4, balance);

            preparedStatement.executeUpdate();
            System.out.println("Checking account created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void createSavingsAccount(String owner, String number, double wLimit, double balance) {
        String sql = "INSERT INTO SavingsAccounts (Owner, AccountNumber, WithdrawalLimit, Balance) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            preparedStatement.setString(1, owner);
            preparedStatement.setString(2, number);
            preparedStatement.setDouble(3, wLimit);
            preparedStatement.setDouble(4, balance);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    public  ResultSet getAllClientsData(){
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement = this.conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Clients");
        }catch(SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }



    public void depositSavings(String pAddress, double amount) {
        String sql = "UPDATE SavingsAccounts SET Balance = Balance + ? WHERE Owner = ?";
        try (PreparedStatement statement = this.conn.prepareStatement(sql)) {
            statement.setDouble(1, amount);
            statement.setString(2, pAddress);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /*
     *       UTILITY METHODS
     */

    public ResultSet searchClient(String pAddress){
        Statement statement;
        ResultSet resultSet = null;
        try {
            statement= this.conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Clients WHERE PayeeAddress = '"+pAddress+"';");
        } catch (SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }


    public int getLastClientId() {
        int id = 0;
        String query = "SELECT seq FROM sqlite_sequence WHERE name = ?";

        try (PreparedStatement preparedStatement = this.conn.prepareStatement(query)) {
            preparedStatement.setString(1, "Clients");

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    id = resultSet.getInt("seq");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id;
    }




    public ResultSet getCheckingAccountData(String pAddress){
        Statement statement;
        ResultSet resultSet = null;
        try{
            statement= this.conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM CheckingAccounts WHERE Owner='"+pAddress+"';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }


    public ResultSet getSavingsaccountData(String pAddress){
        Statement statement;
        ResultSet resultSet = null;
        try{
            statement= this.conn.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM SavingsAccounts WHERE Owner='"+pAddress+"';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }


    // Close the database connection
    public void closeConnection() {
        try {
            if (this.conn != null) {
                this.conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


