package com.ye.balance.balanceproject.model.domain.vo;

import com.ye.balance.balanceproject.model.domain.entity.User;

public class UserVo {
    private int id ;
    private String name ;
    private boolean status;
    private String phone ;
    private String email ;
    private String loginId;

    public UserVo(){}

    public UserVo(User entity){
        this.id = entity.getId();
        this.name = entity.getName();
        this.loginId = entity.getLoginId();
        this.email = entity.getEmail();
        this.phone = entity.getPhone();
        this.status=entity.isActive();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getLoginId(){
        return loginId;
    }
    public void setLoginId(String loginId){
        this.loginId = loginId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
