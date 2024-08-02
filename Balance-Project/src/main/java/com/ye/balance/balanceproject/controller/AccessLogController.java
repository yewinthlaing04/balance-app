package com.ye.balance.balanceproject.controller;

import com.ye.balance.balanceproject.controller.utils.Pagination;
import com.ye.balance.balanceproject.model.domain.entity.UserAccessLog;
import com.ye.balance.balanceproject.model.service.UserAccessLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Optional;


@Controller
@RequestMapping("/admin/accesses")
public class AccessLogController {

    @Autowired
    private UserAccessLogService userAccessLogService;

    @GetMapping
    public String search(
            @RequestParam(required = false) UserAccessLog.AccessType type,
            @RequestParam(required = false) String username ,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date ,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size,
            ModelMap modelmap){

        var result = userAccessLogService.serachByAdmin( type , username , date , page , size);

        modelmap.put ( "list" , result.getContent() );

        //for pagination
        modelmap.put( "pager" , Pagination.builder("/admin/accesses")
                .page(result).build());

        // Populate param attributes for form repopulation
        modelmap.put("paramType", type != null ? type.name() : "");
        modelmap.put("paramUsername", username);
        modelmap.put("paramDate", date != null ? date.toString() : "");

        // Debug log
        //  System.out.println("Search Params - Type: " + type +
        //  ", Username: " + username + ", Date: " + date);


        return "access-log";
    }

    @ModelAttribute(name = "types")
    UserAccessLog.AccessType[] types(){
        return UserAccessLog.AccessType.values();
    }
}
