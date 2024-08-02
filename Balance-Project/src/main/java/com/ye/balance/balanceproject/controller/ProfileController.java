package com.ye.balance.balanceproject.controller;

import com.ye.balance.balanceproject.controller.utils.Pagination;
import com.ye.balance.balanceproject.model.service.UserAccessLogService;
import com.ye.balance.balanceproject.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Security;
import java.util.Optional;

@RequestMapping("/user/profile")
@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserAccessLogService userAccessLogService;

    @GetMapping()
    String index(ModelMap modelMap ,
                 @RequestParam Optional<Integer> page ,
                 @RequestParam Optional<Integer> size){

        var user = SecurityContextHolder.getContext().getAuthentication().getName();

        // for user card
        var uservo = userService.findByLoginId(user);

        modelMap.put("user" , uservo );

        // for access log table
        var accessLogs = userAccessLogService.search( user , page , size );

        modelMap.put("list" , accessLogs.getContent() );

        //for pagination view
//        var pagination = Pagination.builder("/user/profile")
//                .current(accessLogs.getNumber())
//                .total(accessLogs.getTotalPages())
//                .first(accessLogs.isFirst())
//                .last(accessLogs.isLast()).build();
         var pagination = Pagination.builder("/user/profile")
                         .page(accessLogs).build();


        modelMap.put("pagination" , pagination );

        return "profile";
    }

    @PostMapping("/contact")
    String updateContact(@RequestParam(required = false) String email , @RequestParam(required = false) String phone){

        var username = SecurityContextHolder.getContext().getAuthentication().getName();

        userService.updateContact( username , email , phone);

        return "redirect:/user/profile";
    }
}
