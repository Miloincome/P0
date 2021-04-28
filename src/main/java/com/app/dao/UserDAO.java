package com.app.dao;

import com.app.BusinessException;
import com.app.model.BankAccount;
import com.app.model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDAO {
    public User createUser(User user) throws SQLException;

    public boolean findUser(User user) throws SQLException;

    public User setUser(User user) throws SQLException;

    public List<BankAccount> viewAccounts(User user) throws SQLException;

    public List<User> viewCustomers(User user) throws SQLException;
}