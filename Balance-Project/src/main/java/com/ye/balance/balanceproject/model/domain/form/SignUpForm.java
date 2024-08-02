package com.ye.balance.balanceproject.model.domain.form;

import jakarta.validation.constraints.NotBlank;

public class SignUpForm {

    @NotBlank( message = "Enter Member name.")
    private String name;
    @NotBlank( message = "Enter Password. ")
    private String loginId;
    @NotBlank(message = "Enter Password.")
    private String password ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "SignUpForm{" +
                "name='" + name + '\'' +
                ", loginId='" + loginId + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
