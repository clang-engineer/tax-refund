package com.szs.service;

import com.szs.config.Constants;

public class RefundService {
    public Integer getLimitedMoney(Integer totalSalary) {
        if (totalSalary <= Constants.REFUND_LOWER_LIMIT) {
            return 740000;
        } else if (totalSalary <= Constants.REFUND_UPPER_LIMIT) {
            Integer temp = (int) (740000 - (totalSalary - Constants.REFUND_LOWER_LIMIT) * 0.008);
            return Math.max(660000, temp);
        } else {
            Integer temp = (int) (660000 - (totalSalary - Constants.REFUND_UPPER_LIMIT) * 0.5);
            return Math.max(500000, temp);
        }
    }

    public Integer getDeductedMoney(Integer calculateTax) {
        Integer standardMoney = 1300000;
        if (calculateTax <= standardMoney) {
            return (int) (calculateTax * 0.55);
        } else {
            return (int) (715000 + (calculateTax - standardMoney) * 0.3);
        }
    }

    public String getFormattedMoney(Integer money) {
        if (money < 1000) {
            return "0원";
        }
        int first = money / 10000;
        int second = money % 10000 / 1000;

        String result = "";
        if (first != 0) {
            result += first + "만";
        }

        if (second != 0) {
            if (first != 0) {
                result += " ";
            }
            result += second + "천";
        }

        result += "원";

        return result;
    }

}
