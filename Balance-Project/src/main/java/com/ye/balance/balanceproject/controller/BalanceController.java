package com.ye.balance.balanceproject.controller;



import com.ye.balance.balanceproject.controller.utils.Pagination;
import com.ye.balance.balanceproject.model.domain.entity.Balance;
import com.ye.balance.balanceproject.model.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("user/balance")
public class BalanceController {

    @Autowired
    private BalanceService balanceService;

    @Autowired
    DateTimeFormatter dateTimeFormatter;

    @GetMapping
    String report (ModelMap model,
                   @RequestParam(required = false) Balance.Type type,
                   @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateFrom,
                   @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateTo ,
                   @RequestParam(required = false) Optional<Integer> page ,
                    @RequestParam(required = false) Optional<Integer> size){

        var result = balanceService.searchReports ( type , dateFrom , dateTo , page , size);

        var pagination = Pagination.builder("/user/balance")
                .params(Map.of(
                        "type" , type == null ? "" : type.name(),
                        "dateFrom" , dateFrom == null ? "" : dateFrom.format(dateTimeFormatter),
                        "dateTo" , dateTo == null ? "" : dateTo.format(dateTimeFormatter)
                ))
                .page(result)
                .build();

        model.put("pagination", pagination);
        model.put("list" , result.getContent());

        return "balance-report";
    }

    @GetMapping("{type}")
    public String searchBalanceItems(ModelMap modelMap ,
                                     @PathVariable Balance.Type type ,
                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom ,
                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo ,
                                     @RequestParam(required = false) String keyword ,
                                     @RequestParam(required = false) Optional<Integer> page ,
                                     @RequestParam(required = false) Optional<Integer> size ) {



        modelMap.put("title", "%s Management".formatted(type));

        modelMap.put("type", type);

        var result = balanceService.searchItems( type , dateFrom , dateTo , keyword , page ,size );

        modelMap.put( "list" , result.getContent());

        var params = new HashMap<String , String >();

        params.put("type" , type.name());
        params.put("dateFrom" , null == dateFrom ? "" : dateFrom.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        params.put("dateTo" , null == dateTo ? "" : dateTo.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        params.put("keyword" , null == keyword ? "" : keyword);


        var pagination = Pagination.builder("/user/balance/%s".formatted(type))
                .page(result)
                .params(params)
                .build();

        modelMap.put("pagination", pagination);


        return "balance-list";
    }



    @GetMapping("details/{id:\\d+}")
    public String findById(@PathVariable("id") int id , ModelMap modelMap){
        modelMap.put("vo" , balanceService.findById(id));
        return "balance-details";
    }

    @GetMapping("delete/{id:\\d+}")
    public String delete(@PathVariable int id){
        balanceService.deleteById(id);
        return "redirect:/";
    }

    @ModelAttribute("balanceType")
    Balance.Type[] types(){
        return Balance.Type.values();
    }
}
