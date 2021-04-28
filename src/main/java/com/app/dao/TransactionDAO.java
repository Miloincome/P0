package com.app.dao;

import com.app.model.BankAccount;
import com.app.model.Transaction;

import java.sql.SQLException;
import java.util.List;

public interface TransactionDAO {
    public Transaction createTransaction(Transaction transaction) throws SQLException;
    public Transaction setTransaction(Transaction transaction) throws SQLException;
    public List<Transaction> viewPostedTransAction(BankAccount bankAccount) throws SQLException;
    public Transaction Post(Transaction transaction, BankAccount bankAccount) throws SQLException;
    public void Accept(Transaction transaction, BankAccount bankAccount) throws SQLException;
    public List<Transaction> viewTransactions(BankAccount bankAccount) throws SQLException;



}
