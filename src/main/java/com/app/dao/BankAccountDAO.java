package com.app.dao;

import com.app.BusinessException;
import com.app.model.BankAccount;
import com.app.model.Transaction;
import com.app.model.User;

import java.sql.SQLException;
import java.util.List;

public interface BankAccountDAO {
    public BankAccount createBankAccount(BankAccount bankAccount, User user)throws SQLException;
    public boolean findBankAccount(BankAccount bankAccount) throws SQLException;
    public BankAccount setBankAccount(BankAccount bankAccount) throws SQLException;
    public void approveAccount(BankAccount bankAccount) throws SQLException;
    public void rejectAccount(BankAccount bankAccount) throws SQLException;
    public  List<Transaction> viewTransactions(BankAccount bankAccount) throws SQLException;
    public Transaction deposit(BankAccount bankAccount, double deposit,Transaction transaction)throws SQLException;
    public Transaction Withdraw(BankAccount bankAccount, double deposit,Transaction transaction)throws SQLException;
}
