package com.szs.web.vm;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class LoginVM {
    @NotNull
    @Size(min = 1, max = 50)
    private String userId;

    @NotNull
    @Size(min = 4, max = 100)
    private String password;

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

    @Override
    public String toString() {
        return "LoginVM{" +
                "userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
