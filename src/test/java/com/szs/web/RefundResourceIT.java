package com.szs.web;

import com.szs.IntegrationTest;
import com.szs.config.Constants;
import com.szs.domain.Scrap;
import com.szs.domain.ScrapSalary;
import com.szs.domain.ScrapTax;
import com.szs.domain.User;
import com.szs.repository.ScrapRepository;
import com.szs.repository.ScrapSalaryRepository;
import com.szs.repository.ScrapTaxRepository;
import com.szs.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@IntegrationTest
class RefundResourceIT {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScrapRepository scrapRepository;

    @Autowired
    private ScrapSalaryRepository scrapSalaryRepository;

    @Autowired
    private ScrapTaxRepository scrapTaxRepository;

    @Autowired
    private MockMvc restRefundMock;

    private User user;

    @BeforeEach
    void initTest() throws Exception {
        user = UserResourceIT.createEntity();
        user.setRegNo(AES256Utils.encrypt(user.getRegNo()));
        user.setPassword("test");
    }

    @Test
    @Transactional
    @WithMockUser(username = "NOT_IN_DB_USER_ID")
    public void testGetRefundUserNotFoundException() throws Exception {
        restRefundMock.perform(get("/szs/refund"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Not Authorized"));
    }

    @Test
    @Transactional
    @WithMockUser("test")
    public void testRefundLowerBoundary() throws Exception {
        //given
        user.setUserId("test");
        userRepository.saveAndFlush(user);
        Scrap scrap = new Scrap().userId(user.getUserId());
        scrapRepository.saveAndFlush(scrap);

        int totalSalary = Constants.SALARY_LOWER_BOUNDARY - 1;
        ScrapSalary scrapSalary = new ScrapSalary().scrap(scrap).total(totalSalary);
        scrapSalaryRepository.saveAndFlush(scrapSalary);

        int totalTax = Constants.TAX_BOUNDARY - 1;
        ScrapTax scrapTax = new ScrapTax().scrap(scrap).total(totalTax);
        scrapTaxRepository.saveAndFlush(scrapTax);

        //when, then
        restRefundMock.perform(get("/szs/refund"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.한도").value("74만원"))
                .andExpect(jsonPath("$.공제액").value("71만 4천원"))
                .andExpect(jsonPath("$.환급액").value("71만 4천원"))
        ;

    }
}