package com.app.dao.impl;

import com.app.dao.TransactionDAO;
import com.app.dbutil.PostgreConnection;
import com.app.model.BankAccount;
import com.app.model.Transaction;
import com.app.model.User;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TransactionDAOImpl implements TransactionDAO {

    @Override
    public Transaction createTransaction(Transaction transaction) throws SQLException {
        Connection connection = PostgreConnection.getConnection();
        String sql = "INSERT INTO bankapp.transaction\n" +
                "\"description\",amount,transtype,status, refaccount\n" +
                "VALUES(?,?,?,?,?);\n";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, transaction.getDescription());
        preparedStatement.setBigDecimal(2, transaction.getAmount());
        preparedStatement.setString(3, transaction.getTransType());
        preparedStatement.setString(4, transaction.getStatus());
        preparedStatement.setInt(5, transaction.getRefaccount());
        int c = preparedStatement.executeUpdate();
        if (c == 1) {
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                transaction.setTransactionId(resultSet.getInt(1));
            }
        }
        return transaction;
    }

    @Override
    public Transaction setTransaction(Transaction transaction) throws SQLException {
        Connection connection = PostgreConnection.getConnection();
        String sql = "select t.transid,t.description,t.amount,t.transtype,t.status,t.refaccount from bankapplocal.transaction t where transid= ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, transaction.getTransactionId());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next())
        {
            transaction.setTransactionId(resultSet.getInt("transid"));
            transaction.setDescription(resultSet.getString("description"));
            transaction.setAmount(BigDecimal.valueOf(resultSet.getDouble("amount")));
            transaction.setTransType(resultSet.getString("transtype"));
            transaction.setStatus(resultSet.getString("status"));
            transaction.setRefaccount(resultSet.getInt("refaccount"));
        }
        return transaction;
    }

    @Override
    public List<Transaction> viewPostedTransAction(BankAccount bankAccount) throws SQLException {
        List<Transaction> postedTransactionList=new ArrayList<>();
        Connection connection = PostgreConnection.getConnection();
        String sql = "select *from bankapplocal.transaction where transtype = '?' and refaccount = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, "Money Transfer");
        preparedStatement.setInt(2, bankAccount.getAccountNumber());
        ResultSet resultSet=preparedStatement.executeQuery();
        while(resultSet.next()){
            Transaction transaction=new Transaction();
            transaction.setTransactionId(resultSet.getInt("transid"));
            transaction.setDescription(resultSet.getString("description"));
            transaction.setAmount(BigDecimal.valueOf(resultSet.getDouble("amount")));
            transaction.setTransType(resultSet.getString("transtype"));
            transaction.setStatus(resultSet.getString("status"));
            transaction.setRefaccount(resultSet.getInt("refaccount"));
            postedTransactionList.add(transaction);
        }
        return postedTransactionList;
    }

    @Override
    public Transaction Post(Transaction transaction,BankAccount bankAccount) throws SQLException {

            Connection connection = PostgreConnection.getConnection();
//t.transid,t.description,t.amount,t.transtype,t.status,t.refaccount from bankapplocal.transaction t where transid= ?;
            String sql = "INSERT INTO bankapplocal.transaction("+
                    "description,amount, transtype,status,refaccount,accountnumber)"+
                    "VALUES(?,?,?,?,?,?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, transaction.getDescription());
            preparedStatement.setBigDecimal(2, transaction.getAmount());
            preparedStatement.setString(3, "Money Transfer");
            preparedStatement.setString(4, "Pending");
            preparedStatement.setInt(5, transaction.getRefaccount());
            preparedStatement.setInt(6, bankAccount.getAccountNumber());

            int c = preparedStatement.executeUpdate();
            if (c == 1) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    transaction.setTransactionId(resultSet.getInt(1));
                }
            }

            return transaction;

    }

    @Override
    public void Accept(Transaction transaction, BankAccount bankAccount) throws SQLException {
        bankAccount.setBalance(bankAccount.getBalance().add(transaction.getAmount()));
        Connection connection = PostgreConnection.getConnection();
        String sql ="update bankapplocal.transaction set status ='Approved' where transid = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, transaction.getTransactionId());
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
        String sql = "select *from bankapplocal.transaction where transtype = '?' and accountnumber = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, "Money Transfer");
        preparedStatement.setInt(2, bankAccount.getAccountNumber());
        ResultSet resultSet=preparedStatement.executeQuery();
        while(resultSet.next()){
            Transaction transaction=new Transaction();
            transaction.setTransactionId(resultSet.getInt("transid"));
            transaction.setDescription(resultSet.getString("description"));
            transaction.setAmount(BigDecimal.valueOf(resultSet.getDouble("amount")));
            transaction.setTransType(resultSet.getString("transtype"));
            transaction.setStatus(resultSet.getString("status"));
            transaction.setRefaccount(resultSet.getInt("refaccount"));
            transactionList.add(transaction);
        }
        return transactionList;
    }
}