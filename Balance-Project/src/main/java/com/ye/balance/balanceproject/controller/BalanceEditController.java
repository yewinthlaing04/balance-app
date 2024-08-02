package com.ye.balance.balanceproject.controller;


import com.ye.balance.balanceproject.model.BalanceAppException;
import com.ye.balance.balanceproject.model.domain.entity.Balance;
import com.ye.balance.balanceproject.model.domain.entity.BalanceItem;
import com.ye.balance.balanceproject.model.domain.form.BalanceEditForm;
import com.ye.balance.balanceproject.model.domain.form.BalanceItemForm;
import com.ye.balance.balanceproject.model.domain.form.BalanceSummaryForm;
import com.ye.balance.balanceproject.model.service.BalanceService;
import groovy.transform.AutoImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user/balance-edit")
@SessionAttributes("balanceEditForm")
public class BalanceEditController {

    @Autowired
    private BalanceService balanceService;

    @GetMapping()
    public String edit(@ModelAttribute("balanceEditForm") BalanceEditForm balanceEditForm ,
                       @RequestParam(required = false) Integer id  ,
                       @RequestParam(required = false) Balance.Type type){

        if ( null != id && balanceEditForm.getHeader().getId() != id ){
            var result = balanceService.findById(id);
            balanceEditForm.setHeader(result.getHeader());
            balanceEditForm.setItems(result.getItems());
        }

        if ( null != type && balanceEditForm.getHeader().getType() != type) {
               balanceEditForm.setHeader(new BalanceSummaryForm());
                balanceEditForm.getHeader().setType(type);
                balanceEditForm.getItems().clear();
        }

        return "balance-edit";
    }

    @PostMapping("item")
    public String addItem(@ModelAttribute("balanceEditForm") BalanceEditForm form ,
                          @ModelAttribute("balanceItemForm") @Validated BalanceItemForm itemFrom ,
                          BindingResult result){

        if (result.hasErrors()) {
            return "balance-edit";
        }

        form.getItems().add(itemFrom);

        return "redirect:/user/balance-edit?%s".formatted(form.queryParam());
    }

    @GetMapping("item")
    public String deleteItem(@ModelAttribute("balanceEditForm") BalanceEditForm form ,
                             @RequestParam int index){

//        var item = form.getItems().get(index);
//
//        if( item.getId() == 0 ){
//            form.getItems().remove(item);
//        }else {
//            item.setDeleted(true);
//        }

        //validate index to prevent indexoutofboundsexception
        if ( index >= 0 && index < form.getItems().size()){
            var item = form.getItems().get(index);
            if ( item.getId() == 0 ){
                form.getItems().remove(index);
            }else {
                item.setDeleted(true);
            }
        }

        return "redirect:/user/balance-edit?%s".formatted(form.queryParam());
    }


    @GetMapping("/confirm")
    public String confirm (){
        return "balance-edit-confirm";
    }

    @PostMapping
    public String save (@ModelAttribute("balanceEditForm") BalanceEditForm form ,
                        @ModelAttribute("balanceSummaryForm") @Validated BalanceSummaryForm balanceSummaryForm
                        , BindingResult result){

        if ( result.hasErrors() ){
            return "balance-edit-confirm";
        }

        form.getHeader().setCategory(balanceSummaryForm.getCategory());
        form.getHeader().setDate(balanceSummaryForm.getDate());

        var id = balanceService.save(form);

        // delete form in session
        form.clear();

        return "redirect:/user/balance/details/%d".formatted(id);
    }

    @ModelAttribute("balanceItemForm")
    BalanceItemForm balanceItemForm(){
        return new BalanceItemForm();
    }


    @ModelAttribute("balanceEditForm")
    public BalanceEditForm  form (@RequestParam(required = false) Integer id  ,
                                  @RequestParam(required = false) Balance.Type type){

        if ( null != id ) {
            return balanceService.findById(id);
        }

        if ( null == type ) {
            throw new BalanceAppException("Please set type for balance.");
        }

        return new BalanceEditForm().type(type);
    }

    @ModelAttribute("balanceSummaryForm")
     BalanceSummaryForm summaryForm(@ModelAttribute("balanceEditForm") BalanceEditForm balanceEditForm){
        return balanceEditForm.getHeader();
    }
}
