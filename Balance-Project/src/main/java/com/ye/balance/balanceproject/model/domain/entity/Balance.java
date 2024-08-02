package com.ye.balance.balanceproject.model.domain.entity;

import com.ye.balance.balanceproject.model.domain.form.BalanceSummaryForm;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Balance implements Serializable {

    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private int id ;

    @Column(nullable = false)
    private LocalDate date ;

    @Column(nullable = false)
    private String category ;

    private Type type;

    @ManyToOne(optional = false  ,cascade = { CascadeType.PERSIST , CascadeType.MERGE })
    private User user;

    @OneToMany(mappedBy = "balance" ,
            cascade =  { CascadeType.PERSIST , CascadeType.MERGE} ,
            orphanRemoval = true)
    private List<BalanceItem> items;

    public enum Type {
        Income ,
        Expense
    }

    public Balance(){
        items = new ArrayList<>();
    }

    public void addItem(BalanceItem item){
        item.setBalance(this);
        items.add(item);
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<BalanceItem> getItems() {
        return items;
    }

    public void setItems(List<BalanceItem> items) {
        this.items = items;
    }
}
