package com.ye.balance.balanceproject.model.domain.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class BalanceItemForm {

    private int id ;
    @NotBlank(message = "Enter Item name")
    private String items;
    @Min(value=1, message = "Enter price")
    private int uniPrice;
    @Min(value=1 , message = "Enter quantity")
    private int quantity;

    private Boolean isdeleted ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItem() {
        return items;
    }

    public void setItem(String item) {
        this.items = item;
    }

    public int getUniPrice() {
        return uniPrice;
    }

    public void setUniPrice(int uniPrice) {
        this.uniPrice = uniPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Boolean getDeleted() {
        return isdeleted ;
    }

    public void setDeleted(Boolean deleted) {
        this.isdeleted = deleted;
    }



}
