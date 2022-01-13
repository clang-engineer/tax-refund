package com.szs.service;

import com.szs.config.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RefundServiceUnitTest {

    RefundService refundService;

    @BeforeEach
    void init() {
        this.refundService = new RefundService();
    }

    @Test
    void testSalaryLowerBoundary() {
        assertThat(refundService.getLimitedMoney(Constants.SALARY_LOWER_BOUNDARY)).isEqualTo(740000);
        assertThat(refundService.getLimitedMoney(Constants.SALARY_LOWER_BOUNDARY - 100000)).isEqualTo(740000);
    }

    @Test
    void testSalaryMiddleBoundary() {
        assertThat(refundService.getLimitedMoney(Constants.SALARY_LOWER_BOUNDARY + 100000)).isEqualTo(739200);
        assertThat(refundService.getLimitedMoney(Constants.SALARY_UPPER_BOUNDARY)).isEqualTo(660000);
    }

    @Test
    void testSalaryUpperBoundary() {
        assertThat(refundService.getLimitedMoney(Constants.SALARY_UPPER_BOUNDARY + 100000)).isEqualTo(610000);
    }

    @Test
    void testTaxBoundary() {
        assertThat(refundService.getDeductedMoney(1300000)).isEqualTo(715000);
        assertThat(refundService.getDeductedMoney(1290000)).isEqualTo(709500);
        assertThat(refundService.getDeductedMoney(1310000)).isEqualTo(718000);
    }

    @Test
    void testFormattedMoney() {
        assertThat(refundService.getFormattedMoney(91230000)).isEqualTo("9123만원");
        assertThat(refundService.getFormattedMoney(1230000)).isEqualTo("123만원");

        assertThat(refundService.getFormattedMoney(1231000)).isEqualTo("123만 1천원");
        assertThat(refundService.getFormattedMoney(1231900)).isEqualTo("123만 1천원");

        assertThat(refundService.getFormattedMoney(1000)).isEqualTo("1천원");
        assertThat(refundService.getFormattedMoney(1100)).isEqualTo("1천원");
        assertThat(refundService.getFormattedMoney(1123)).isEqualTo("1천원");

        assertThat(refundService.getFormattedMoney(100)).isEqualTo("0원");
    }
}
