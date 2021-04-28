package com.app.dao.impl;

import com.app.BusinessException;
import com.app.dao.BankAccountDAO;
import com.app.dbutil.PostgreConnection;
import com.app.dao.BankAccountDAO;
import com.app.model.BankAccount;
import com.app.model.Transaction;
import com.app.model.User;


import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankAccountDAOImpl implements BankAccountDAO {

    @Override
    public BankAccount createBankAccount(BankAccount bankAccount, User user) throws SQLException {

        Connection connection = PostgreConnection.getConnection();

        String sql = "INSERT INTO bankapplocal.bankaccount("+
                "accountname,balance,status,routingnumber,userid,accounttype)"+
                "VALUES(?,?,?,?,?,?);";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, bankAccount.getName());
        preparedStatement.setBigDecimal(2, bankAccount.getBalance());
        preparedStatement.setString(3, "Pending");
        preparedStatement.setInt(4, 4003300);
        preparedStatement.setInt(5, user.getUserId());
        preparedStatement.setString(6, bankAccount.getAccountType());

        int c = preparedStatement.executeUpdate();
        if (c == 1) {
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                bankAccount.setAccountNumber(resultSet.getInt(1));
            }
        }

        return bankAccount;
    }


    @Override
    public boolean findBankAccount(BankAccount bankAccount) throws SQLException {
        boolean result = false;
        Connection connection = PostgreConnection.getConnection();
        String sql = "SELECT CASE WHEN EXISTS (SELECT *FROM bankapplocal.bankaccount where accountnumber = ?)THEN CAST(1 AS BIT)ELSE CAST(0 AS BIT) END";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, bankAccount.getAccountNumber());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            if (resultSet.getInt("case")==1) {
                result = true;
            }
            else{ result = false;}
        }
        return result;
    }

    @Override
    public BankAccount setBankAccount(BankAccount bankAccount) throws SQLException {
        Connection connection = PostgreConnection.getConnection();
        String sql = "select b.accountnumber,b.balance,b.accounttype,b.status,b.routingnumber,b.userid,b.accountname from bankapplocal.bankaccount b where accountnumber = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, bankAccount.getAccountNumber());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next())
        {
            bankAccount.setAccountNumber(resultSet.getInt("accountnumber"));
            bankAccount.setBalance(BigDecimal.valueOf(resultSet.getDouble("balance")));
            bankAccount.setAccountType(resultSet.getString("accounttype"));
            bankAccount.setStatus(resultSet.getString("status"));
            bankAccount.setRoutingNumber(resultSet.getInt("routingnumber"));
            bankAccount.setName(resultSet.getString("accountname"));
        }
        return bankAccount;
    }

    @Override
    public void approveAccount(BankAccount bankAccount) throws SQLException {
        Connection connection = PostgreConnection.getConnection();
        String sql ="update bankapplocal.bankaccount set status ='Approved' where accountnumber = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, bankAccount.getAccountNumber());
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next())
        {

        }
        return;

    }
    public void rejectAccount(BankAccount bankAccount) throws SQLException {
        Connection connection = PostgreConnection.getConnection();
        String sql ="update bankapplocal.bankaccount set status ='Rejected' where accountnumber = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, bankAccount.getAccountNumber());
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next())
        {

        }
        return;
    }

    @Override
    public List<Transaction> viewTransactions(BankAccount bankAccount) throws SQLException {
        List<Transaction> transactionList=new ArrayList<>();
        Connection connection = PostgreConnection.getConnection();
        String sql = "select *from bankapplocal.transaction where accountnumber =?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, bankAccount.getAccountNumber());
        ResultSet resultSet=preparedStatement.executeQuery();
        while(resultSet.next()){
            Transaction transaction =new Transaction();
            transaction.setTransactionId(resultSet.getInt("transid"));
            transaction.setDescription(resultSet.getString("description"));
            transaction.setAmount(BigDecimal.valueOf(resultSet.getDouble("amount")));
            transaction.setTransType(resultSet.getString("transType"));
            transaction.setRefaccount(resultSet.getInt("refaccount"));
            transactionList.add(transaction);
        }
        return transactionList;
    }

    @Override
    public Transaction deposit(BankAccount bankAccount, double deposit,Transaction transaction) throws SQLException {

        bankAccount.setBalance(bankAccount.getBalance().add(BigDecimal.valueOf(deposit)));

        Connection connection = PostgreConnection.getConnection();
        String sql = "update bankapplocal.bankaccount  set balance = ? where accountnumber = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setBigDecimal(1,bankAccount.getBalance());
        preparedStatement.setInt(2, bankAccount.getAccountNumber());
        preparedStatement.executeUpdate();
        sql =" Insert INTO bankapplocal.transaction (description,amount,accountnumber,transtype,status)VALUES(?,?,?,?,?);";
        PreparedStatement preparedStatement2 = connection.prepareStatement(sql);
        preparedStatement2.setString(1,"Deposited: "+deposit+" to account.");
        preparedStatement2.setDouble(2,deposit);
        preparedStatement2.setInt(3,bankAccount.getAccountNumber());
        preparedStatement2.setString(4,"Deposit");
        preparedStatement2.setString(5,"Approved");
        int c = preparedStatement2.executeUpdate();
        if (c == 1) {
            ResultSet resultSet = preparedStatement2.getGeneratedKeys();
            if (resultSet.next()) {
                transaction.setTransactionId(resultSet.getInt(1));
            }
        }
        return transaction;

    }

    @Override
    public Transaction Withdraw(BankAccount bankAccount, double withdraw, Transaction transaction) throws SQLException {

        bankAccount.setBalance(bankAccount.getBalance().subtract(BigDecimal.valueOf(withdraw)));

        Connection connection = PostgreConnection.getConnection();
        String sql = "update bankapplocal.bankaccount  set balance = ? where accountnumber = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setBigDecimal(1,bankAccount.getBalance());
        preparedStatement.setInt(2, bankAccount.getAccountNumber());
        preparedStatement.executeUpdate();
        sql =" Insert INTO bankapplocal.transaction (description,amount,accountnumber,transtype,status)VALUES(?,?,?,?,?);";
        PreparedStatement preparedStatement2 = connection.prepareStatement(sql);
        preparedStatement2.setString(1,"Withdrew: "+withdraw+" from account.");
        preparedStatement2.setDouble(2,withdraw);
        preparedStatement2.setInt(3,bankAccount.getAccountNumber());
        preparedStatement2.setString(4,"Withdraw");
        preparedStatement2.setString(5,"Approved");
        int c = preparedStatement2.executeUpdate();
        if (c == 1) {
            ResultSet resultSet = preparedStatement2.getGeneratedKeys();
            if (resultSet.next()) {
                transaction.setTransactionId(resultSet.getInt(1));
            }
        }
        return transaction;
    }

}
