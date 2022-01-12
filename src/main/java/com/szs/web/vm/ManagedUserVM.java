package com.szs.web.vm;

import com.szs.config.Constants;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class ManagedUserVM {
    @NotNull
    @Pattern(regexp = Constants.LOGIN_REGEX)
    private String userId;

    @NotNull
    @Size(min = 4, max = 60)
    private String password;

    @NotNull
    private String name;

    @Pattern(regexp = Constants.REG_NO_REGEX)
    private String regNo;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ManagedUserVM userId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ManagedUserVM password(String password) {
        this.password = password;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ManagedUserVM name(String name) {
        this.name = name;
        return this;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public ManagedUserVM regNo(String regNo) {
        this.regNo = regNo;
        return this;
    }
}
