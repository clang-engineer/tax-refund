package com.szs.web;

import com.szs.config.Constants;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RefundUtilsUnitTest {

    @Test
    void testGetLimitedMoneyLower() {
        assertThat(RefundUtils.getLimitedMoney(Constants.REFUND_LOWER_LIMIT)).isEqualTo(740000);
        assertThat(RefundUtils.getLimitedMoney(Constants.REFUND_LOWER_LIMIT - 1)).isEqualTo(740000);
        assertThat(RefundUtils.getLimitedMoney(Constants.REFUND_LOWER_LIMIT + 1)).isNotEqualTo(740000);
    }

    @Test
    void testGetLimitedMoneyMiddle() {
        int totalSalary1 = Constants.REFUND_LOWER_LIMIT + 1;
        int result = (int) (740000 - (totalSalary1 - 33000000) * 0.008);
        result = Math.max(result, 660000);
        assertThat(RefundUtils.getLimitedMoney(totalSalary1)).isEqualTo(result);

        int totalSalary2 = Constants.REFUND_UPPER_LIMIT;
        int result2 = (int) (740000 - (totalSalary2 - 33000000) * 0.008);
        result2 = Math.max(result2, 660000);
        assertThat(RefundUtils.getLimitedMoney(totalSalary2)).isEqualTo(result2);
    }

    @Test
    void testGetLimitedMoneyUpper() {
        int totalSalary1 = Constants.REFUND_UPPER_LIMIT + 1;
        int result = (int) (660000 - (totalSalary1 - 70000000) * 0.5);
        assertThat(RefundUtils.getLimitedMoney(totalSalary1)).isEqualTo(result);
    }

    @Test
    void testFormattedMoney() {
        assertThat(RefundUtils.getFormattedMoney(91230000)).isEqualTo("9123만원");
        assertThat(RefundUtils.getFormattedMoney(1230000)).isEqualTo("123만원");

        assertThat(RefundUtils.getFormattedMoney(1231000)).isEqualTo("123만 1천원");
        assertThat(RefundUtils.getFormattedMoney(1231900)).isEqualTo("123만 1천원");

        assertThat(RefundUtils.getFormattedMoney(1000)).isEqualTo("1천원");
        assertThat(RefundUtils.getFormattedMoney(1100)).isEqualTo("1천원");
        assertThat(RefundUtils.getFormattedMoney(1123)).isEqualTo("1천원");

        assertThat(RefundUtils.getFormattedMoney(100)).isEqualTo("0원");

    }
}
