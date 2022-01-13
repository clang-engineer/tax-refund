package com.szs.config;

public final class Constants {

    public static final String LOGIN_REGEX = "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)|(?>[_.@A-Za-z0-9-]+)$";
    public static final String REG_NO_REGEX =  "^(?:[0-9]{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[1,2][0-9]|3[0,1]))-[1-4][0-9]{6}$";
    public static final String SYSTEM = "system";

    public static final Integer SALARY_UPPER_BOUNDARY = 70000000;
    public static final Integer SALARY_LOWER_BOUNDARY = 33000000;

    public static final Integer TAX_BOUNDARY = 1300000;

    private Constants() {}
}
