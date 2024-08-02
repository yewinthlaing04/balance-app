package com.ye.balance.balanceproject.model.domain.entity;

import com.ye.balance.balanceproject.model.domain.form.SignUpForm;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
//@Table( name = "user_table")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;

    @Column(nullable = false )
    private String name ;

    @Column(nullable = false )
    private Role role ;

    @Column(nullable = false , unique = true)
    private String loginId;

    @Column(nullable = false )
    private String password ;

    private String phone ;

    private String email ;

    private boolean active ;

    public enum Role {
        Admin , Member
    }

    public User(){

    }
    //for signup method in user service
    public User(SignUpForm signUp){
        this.name = signUp.getName();
        this.loginId = signUp.getLoginId();
        this.password = signUp.getPassword();
        this.active = true ;
        this.role = Role.Member;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
