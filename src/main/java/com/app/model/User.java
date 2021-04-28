package com.app.model;



public class User{
    private int UserId;
    private String userName;
    private String fullName;
    private String userType;
    private String password;

    public User()
    {

    }

    public User(String userName, String fullName, String userType,String password) {
        this.userName = userName;
        this.fullName = fullName;
        this.userType = userType;
        this.password = password;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                " userId='"+ UserId+ '\''+
                " userName='" + userName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", userType='" + userType + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
