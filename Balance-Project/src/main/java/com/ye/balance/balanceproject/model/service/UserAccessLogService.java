package com.ye.balance.balanceproject.model.service;


import com.ye.balance.balanceproject.model.domain.entity.UserAccessLog;
import com.ye.balance.balanceproject.model.repo.UserAccessLogRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UserAccessLogService {

    @Autowired
    private UserAccessLogRepo userAccessLogRepo;

    public Page<UserAccessLog> search(String user, Optional<Integer> page, Optional<Integer> size) {

        // to customize the page and number of logs in page
        var pageInfo = PageRequest.of(page.orElse(0) ,
                size.orElse(5)).withSort(Sort.by("accessTime").descending());

        // create dynamic query for username
        Specification<UserAccessLog> spec = (root , query , builder ) ->
                builder.equal(root.get("username"),user);

            //return spec (name) and for pagination
        return userAccessLogRepo.findAll(spec , pageInfo);
    }

    @PreAuthorize("hasAuthority('Admin')")
    public Page<UserAccessLog> serachByAdmin(UserAccessLog.AccessType type, String username, LocalDate date, Optional<Integer> page, Optional<Integer> size) {

        // to customize the page and number of logs in page
        var pageInfo = PageRequest.of(page.orElse(0) ,
                size.orElse(5)).withSort(Sort.by("accessTime").descending());

        Specification<UserAccessLog> spec = Specification.where(null);

        if ( null != type ) {
            spec = spec.and( (root , query , builder ) -> builder.equal(root.get("accessType") , type ));
        }

        if (StringUtils.hasLength(username)){
            spec = spec.and (  (root , query , builder ) -> builder.like( builder.lower(root.get("username")),
                    username.toLowerCase().concat("%") ));
        }

        if ( null != date ){
            spec = spec.and( (root , query , builder ) ->
                builder.greaterThanOrEqualTo(root.get("accessTime")
                        , date.atStartOfDay()));
        }

        Page<UserAccessLog> result = userAccessLogRepo.findAll(spec , pageInfo);

        // Debug log
//        System.out.println("Filtered Logs: " + result.getContent().size());
//        for (UserAccessLog log : result.getContent()) {
//            System.out.println(log);
//        }


        return result;
    }
}
