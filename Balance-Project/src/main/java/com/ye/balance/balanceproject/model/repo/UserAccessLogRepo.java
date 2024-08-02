package com.ye.balance.balanceproject.model.repo;

import com.ye.balance.balanceproject.model.domain.entity.UserAccessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserAccessLogRepo extends JpaRepository<UserAccessLog , Integer> , JpaSpecificationExecutor<UserAccessLog > {

}
