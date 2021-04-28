package com.app.dao.impl;

import com.app.BusinessException;
import com.app.dbutil.PostgreConnection;
import com.app.dao.UserDAO;
import com.app.model.BankAccount;
import com.app.model.User;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    @Override
    public User createUser(User user) throws SQLException {

        Connection connection = PostgreConnection.getConnection();


        //INSERT INTO bankapp.user(username,fullname ,usertype,password) VALUES("username","fullname","Customer","password");
        String sql = "INSERT INTO bankapplocal.user(" +
                "username,fullname,usertype,password)" +
                "VALUES(?,?,?,?);";


        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, user.getUserName());
        preparedStatement.setString(2, user.getFullName());
        preparedStatement.setString(3, user.getUserType());
        preparedStatement.setString(4, user.getPassword());

        int c = preparedStatement.executeUpdate();
        if (c == 1) {
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                user.setUserId(resultSet.getInt(1));
            }
        }

        return user;
    }

    public boolean findUser(User user) throws SQLException {
        boolean result = false;
        Connection connection = PostgreConnection.getConnection();
        String sql = "SELECT CASE WHEN EXISTS (SELECT *FROM bankapplocal.user where username = ? and password =?)THEN CAST(1 AS BIT)ELSE CAST(0 AS BIT) END";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, user.getUserName());
        preparedStatement.setString(2, user.getPassword());
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
    public User setUser(User user) throws SQLException {

        Connection connection = PostgreConnection.getConnection();
        String sql = "select u.userid,u.username,u.fullname,u.usertype,u.password from bankapplocal.user u where username = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, user.getUserName());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next())
        {
            user.setUserId(resultSet.getInt("userid"));
            user.setUserName(resultSet.getString("username"));
            user.setFullName(resultSet.getString("fullname"));
            user.setUserType(resultSet.getString("usertype"));
            user.setPassword(resultSet.getString("password"));
        }
        return user;
    }

    @Override
    public List<BankAccount> viewAccounts(User user) throws SQLException {
        List<BankAccount> bankAccountList=new ArrayList<>();
        Connection connection = PostgreConnection.getConnection();
        String sql = "select *from bankapplocal.bankaccount where userid = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, user.getUserId());
        ResultSet resultSet=preparedStatement.executeQuery();
        while(resultSet.next()){
            BankAccount bankAccount=new BankAccount();
            bankAccount.setAccountNumber(resultSet.getInt("accountnumber"));
            bankAccount.setAccountType(resultSet.getString("accounttype"));
            bankAccount.setBalance(BigDecimal.valueOf(resultSet.getDouble("balance")));
            bankAccount.setStatus(resultSet.getString("status"));
            bankAccount.setRoutingNumber(resultSet.getInt("routingnumber"));
            bankAccount.setName(resultSet.getString("accountname"));
            bankAccountList.add(bankAccount);
        }
        return bankAccountList;

    }

    @Override
    public List<User> viewCustomers(User user) throws SQLException {
        List<User> customerList=new ArrayList<>();
        Connection connection = PostgreConnection.getConnection();
        String sql = "select *from bankapplocal.user where usertype = 'Customer';";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet=preparedStatement.executeQuery();
        while(resultSet.next()){
            User customer=new User();
            customer.setUserId(resultSet.getInt("userid"));
            customer.setUserName(resultSet.getString("username"));
            customer.setFullName(resultSet.getString("fullname"));
            customer.setUserType(resultSet.getString("usertype"));
            customer.setPassword(resultSet.getString("password"));
            customerList.add(customer);
        }
        return customerList;
    }

}
