package com.noble.finalproject.Models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.time.LocalDate;

public class CheckingAccount extends Account{
//    NUMBER OF TRANSACTIONS A CLIENT IS ALLOWED TO DO PER DAY
    private final IntegerProperty transactionLimit;

    public CheckingAccount (String owner, String accountNumber, double balance, int tLmit){
        super(owner, accountNumber, balance);
        this.transactionLimit = new SimpleIntegerProperty(this, "Transaction Limit", tLmit);
    }

    public IntegerProperty transactionLimitProp(){return transactionLimit;}

    @Override
    public String toString(){
        return accountNumberProperty().get();
    }
}