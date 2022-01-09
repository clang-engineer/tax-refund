package com.szs.service.dto;

import com.szs.config.Constants;
import com.szs.domain.User;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserDTO {
    private Long id;

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

    public UserDTO() {
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.userId = user.getUserId();
        this.password = user.getPassword();
        this.name = user.getName();
        this.regNo = user.getRegNo();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", regNo='" + regNo + '\'' +
                '}';
    }
}
