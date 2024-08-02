package com.ye.balance.balanceproject.model.domain.entity;



import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.*;

@Entity
public class UserAccessLog implements Serializable {

    @Id
    @GeneratedValue ( strategy = GenerationType.IDENTITY)
    private int id ;
    private String username ;

    private AccessType accessType;

    private LocalDateTime accessTime;

    private String errorMessage ;

    public enum AccessType{
        Signin , Signout , Error
    }

    public enum Type{
        Signin , Signout , Error
    }

    public UserAccessLog() {}

    // success message constructor
    public UserAccessLog(String username , AccessType type , LocalDateTime accessTime ){
          super();
          this.username = username;
          this.accessType = type;
          this.accessTime = accessTime;
    }

    // failure message constructor
    public UserAccessLog( String username , AccessType type , LocalDateTime accessTime , String errorMessage ){
        super();
        this.username = username ;
        this.accessType = type ;
        this.accessTime = accessTime ;
        this.errorMessage = errorMessage ;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public AccessType getAccessType() {
        return accessType;
    }

    public void setAccessType(AccessType accessType) {
        this.accessType = accessType;
    }

    public LocalDateTime getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(LocalDateTime accessTime) {
        this.accessTime = accessTime;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


}
