package com.ye.balance.balanceproject.model.repo;

import com.ye.balance.balanceproject.model.domain.entity.Balance;
import com.ye.balance.balanceproject.model.domain.entity.BalanceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BalanceItemRepo extends JpaRepository<BalanceItem, Integer> , JpaSpecificationExecutor<BalanceItem> {

    @Query("select sum(bi.unitPrice * bi.quantity) from BalanceItem bi where bi.balance.id < :id and bi.balance.type = :type ")
    Optional<Number> getLastBalance(int id , Balance.Type type);

}
