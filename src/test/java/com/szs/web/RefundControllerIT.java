package com.szs.web;

import com.szs.IntegrationTest;
import com.szs.domain.User;
import com.szs.repository.UserRepository;
import com.szs.service.RefundService;
import com.szs.service.ScrapService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@IntegrationTest
class RefundControllerIT {

    @Autowired
    private UserRepository userRepository;

    private ScrapService scrapService;

    private RefundService refundService;

    @Autowired
    private MockMvc restRefundMock;

    private User user;

    @BeforeEach
    void initTest() {
        scrapService = mock(ScrapService.class);
        refundService = mock(RefundService.class);

        user = UserResourceIT.createEntity();
    }

    @Test
    @Transactional
    @WithMockUser(username = "NOT_IN_DB_USER_ID")
    public void testGetRefundUserNotFoundException() throws Exception {
        restRefundMock.perform(get("/szs/refund"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Not Authorized")) ;
    }


}