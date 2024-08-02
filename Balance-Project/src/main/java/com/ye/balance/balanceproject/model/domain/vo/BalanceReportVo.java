package com.ye.balance.balanceproject.model.domain.vo;

import com.ye.balance.balanceproject.model.domain.entity.Balance;

import java.time.LocalDate;

public class BalanceReportVo {

    private int id ;
    private LocalDate date ;
    private Balance.Type type;
    private String category;
    private int amount ;
    private int balance;

    public BalanceReportVo() {

    }

    public BalanceReportVo (Balance entity ){
        this.id = entity.getId();
        this.date = entity.getDate();
        this.category = entity.getCategory();
        this.amount = entity.getItems().stream().mapToInt( a -> a.getQuantity() * a.getUnitPrice()).sum();
        this.type= entity.getType();

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Balance.Type getType() {
        return type;
    }

    public void setType(Balance.Type type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getIncome(){
        return type == Balance.Type.Income ? amount : 0;
    }

    public int getExpense(){
        return type == Balance.Type.Expense ? amount : 0;
    }
}
