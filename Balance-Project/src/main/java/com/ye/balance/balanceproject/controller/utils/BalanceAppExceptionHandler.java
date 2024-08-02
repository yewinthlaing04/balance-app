package com.ye.balance.balanceproject.controller.utils;

import com.ye.balance.balanceproject.model.BalanceAppException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.servlet.support.RequestContextUtils;

@ControllerAdvice
public class BalanceAppExceptionHandler {

    @ExceptionHandler(BalanceAppException.class)
    String handle(BalanceAppException exception , HttpServletRequest request){

        RequestContextUtils.getOutputFlashMap( request ).put("message" , exception.getMessage());

        return "redirect:/";
    }

//    @ExceptionHandler(BalanceAppException.class)
//    public String handleBalanceAppException(BalanceAppException ex, RedirectAttributes redirectAttributes) {
//        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
//        return "redirect:/changePassword"; // Ensure this redirects to the correct URL
//    }

    }
