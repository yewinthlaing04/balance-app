package com.ye.balance.balanceproject.model.repo;

import com.ye.balance.balanceproject.model.domain.entity.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BalanceRepo extends JpaRepository<Balance, Integer>, JpaSpecificationExecutor<Balance> {
}
