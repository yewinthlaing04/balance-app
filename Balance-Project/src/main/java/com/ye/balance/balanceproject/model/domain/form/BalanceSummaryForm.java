package com.ye.balance.balanceproject.model.domain.form;

import com.ye.balance.balanceproject.model.domain.entity.Balance;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

public class BalanceSummaryForm implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id ;

    @NotNull(message = "Enter Date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date ;

    @NotBlank(message = "Enter Category")
    private String category ;

    private Balance.Type type;


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

    public Balance.Type getType() {
        return type;
    }

    public void setType(Balance.Type type) {
        this.type = type;
    }
}
