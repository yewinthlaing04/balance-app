package com.ye.balance.balanceproject.model.domain.form;

import com.ye.balance.balanceproject.model.domain.entity.Balance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BalanceEditForm implements Serializable {

    private static final long serialVersionUID = 1L;

    private BalanceSummaryForm header ;
    private List<BalanceItemForm> items;

    public BalanceEditForm(){
        header = new BalanceSummaryForm();
        items = new ArrayList<>();
    }

    public BalanceEditForm(Balance entity ){
        header = new BalanceSummaryForm();
        header.setId(entity.getId());
        header.setCategory(entity.getCategory());
        header.setDate(entity.getDate());
        header.setType(entity.getType());

        items = entity.getItems().stream().map(a -> {
            var item = new BalanceItemForm();

            item.setId(a.getId());
            item.setItem(a.getItem());
            item.setQuantity(a.getQuantity());
            item.setUniPrice(a.getUnitPrice());

            return item;
        }).toList();
    }

    public BalanceEditForm type ( Balance.Type type ) {
        header.setType(type);
        return this;
    }

    public int getTotal(){
        return items
                .stream()
                .filter(a ->a.getDeleted() == null || !a.getDeleted())
                .mapToInt(a -> a.getQuantity() * a.getUniPrice())
                .sum();
    }

    public int getTotalCount (){
        return items
                .stream()
                .filter(a ->a.getDeleted() == null || !a.getDeleted())
                .mapToInt(a -> a.getQuantity())
                .sum();
    }

    public BalanceSummaryForm getHeader() {
        return header;
    }

    public void setHeader(BalanceSummaryForm header) {
        this.header = header;
    }

    public List<BalanceItemForm> getItems() {
        return items;
    }

    public void setItems(List<BalanceItemForm> items) {
        this.items = items;
    }

    public boolean isShowSaveButton (){
        return !items.isEmpty();
    }

    public String queryParam() {
        return header.getId() == 0 ? "type=%s".formatted(header.getType()) :
                "id=%s".formatted(header.getId());
    }

    public List<BalanceItemForm> getValidItems() {
        return items.stream()
                .filter(item -> item.getDeleted() == null || !item.getDeleted())
                .collect(Collectors.toList());
    }

    public void clear() {
        items.clear();
        header = new BalanceSummaryForm();
    }
}
