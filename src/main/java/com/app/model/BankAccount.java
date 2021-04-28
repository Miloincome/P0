package com.app.model;
import java.math.BigDecimal;

public class BankAccount {
    private String accountType;
    private int accountNumber;
    private int routingNumber;
    private BigDecimal Balance;
    private String name;
    private String status;


    public BankAccount(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BankAccount()
    {

    }

    public BankAccount(String accountType, int accountNumber, int routingNumber, BigDecimal balance, String name,String status) {
        this.accountType = accountType;
        this.routingNumber = routingNumber;
        Balance = balance;
        this.name = name;
        this.status = status;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getRoutingNumber() {
        return routingNumber;
    }

    public void setRoutingNumber(int routingNumber) {
        this.routingNumber = routingNumber;
    }

    public BigDecimal getBalance() {
        return Balance;
    }

    public void setBalance(BigDecimal balance) {
        Balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getStatus() {
        return name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "accountType='" + accountType + '\'' +
                ", accountNumber=" + accountNumber +
                ", routingNumber=" + routingNumber +
                ", Balance=" + Balance +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
