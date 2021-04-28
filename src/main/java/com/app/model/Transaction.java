package com.app.model;

import java.math.BigDecimal;

public class Transaction {
    private int transactionId;
    private String description;
    private BigDecimal amount;
    private String transType;
    private String status;
    private int refaccount;

    public Transaction(int transactionId) {
        this.transactionId = transactionId;
    }

    public Transaction()
    {

    }

    public Transaction( String description, BigDecimal amount, String transType, int refaccount, String status) {
        this.description = description;
        this.amount = amount;
        this.transType = transType;
        this.refaccount = refaccount;
        this.status = status;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public int getRefaccount() {
        return refaccount;
    }

    public void setRefaccount(int refaccount) {
        this.refaccount = refaccount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", transType='" + transType + '\'' +
                ", status='" + status + '\'' +
                ", refaccount=" + refaccount +
                '}';
    }
}
