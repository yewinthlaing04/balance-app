package com.ye.balance.balanceproject.controller;

import com.ye.balance.balanceproject.model.BalanceAppException;
import com.ye.balance.balanceproject.model.domain.entity.User;
import com.ye.balance.balanceproject.model.domain.form.ChangePasswordForm;
import com.ye.balance.balanceproject.model.domain.form.SignUpForm;
import com.ye.balance.balanceproject.model.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SecurityController {



    @Autowired
    private UserService userService;


    @GetMapping("/")
    public String index(){

        var auth = SecurityContextHolder.getContext().getAuthentication();

        if ( auth != null &&
        auth.getAuthorities().stream().anyMatch( a -> a.getAuthority().equals(User.Role.Admin.name())
        || a.getAuthority().equals(User.Role.Member.name()))){
            return "redirect:/user/home";
        }

        return "signin";
    }

    @GetMapping("signup")
    public void loadSignUp(){

    }

    @PostMapping("signup")
    public String signUp(@ModelAttribute(name="form") @Validated SignUpForm form ,
                         BindingResult bindingResult){

        if ( bindingResult.hasErrors() ){

           // System.out.println(bindingResult.getAllErrors());  //print all error messages

            return "signup"; //return signup view
        }

        // we need to save form data to db here

        userService.signUp(form);

        System.out.println(form);

        return "redirect:/";
    }

    @PostMapping("/user/changepass")
    public String changePass(@ModelAttribute(name="changePasswordForm") ChangePasswordForm changePasswordForm
                            , RedirectAttributes redirectAttributes){

        System.out.println(changePasswordForm.getLoginId());
        System.out.println(changePasswordForm.getOldPassword());
        System.out.println(changePasswordForm.getNewPassword());

       userService.changePassword(changePasswordForm);
       redirectAttributes.addFlashAttribute("message",
               "Your password has been successfully changed");

        return "redirect:/";
    }

    // sign up data binding form
    @ModelAttribute(name="form")
    SignUpForm signUpForm(){
        return new SignUpForm();
    }

    @ModelAttribute(name="changePasswordForm")
    ChangePasswordForm changePasswordForm(){
        return new ChangePasswordForm();
    }

}
