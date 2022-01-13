package com.szs.web;

import com.szs.IntegrationTest;
import com.szs.domain.User;
import com.szs.repository.UserRepository;
import com.szs.web.vm.LoginVM;
import com.szs.web.vm.ManagedUserVM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@IntegrationTest
class UserJWTControllerIT {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;

    private User user;

    @BeforeEach
    void initTest() {
        user = UserResourceIT.createEntity();
    }

    @Test
    @Transactional
    void testAuthorize() throws Exception {
        //given
        user.setUserId("test_id");
        user.setPassword(passwordEncoder.encode("test1234"));
        userRepository.saveAndFlush(user);

        ManagedUserVM login = new ManagedUserVM();
        login.setUserId("test_id");
        login.setPassword("test1234");

        //when, then
        mockMvc
                .perform(post("/szs/login")
                        .contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_token").isString())
                .andExpect(jsonPath("$.id_token").isNotEmpty())
                .andExpect(header().string("Authorization", not(nullValue())))
                .andExpect(header().string("Authorization", not(is(emptyString()))));
    }

    @Test
    @Transactional
    void testAuthorizeFails() throws Exception {
        //given
        LoginVM login = new LoginVM();
        login.setUserId("wrong-userid");
        login.setPassword("wrong password");

        //when, then
        mockMvc
                .perform(post("/szs/login").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(login)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.id_token").doesNotExist())
                .andExpect(header().doesNotExist("Authorization"));
    }
}