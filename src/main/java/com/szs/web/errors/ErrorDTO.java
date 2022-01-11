package com.szs.web.errors;

import org.springframework.http.HttpStatus;

public class ErrorDTO {
    private String title;
    private String description;
    private Integer status;

    public ErrorDTO(String title, String description, Integer status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public ErrorDTO(HttpStatus httpStatus) {
        this.title = httpStatus.getReasonPhrase();
        this.description = "";
        this.status = httpStatus.value();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
