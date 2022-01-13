package com.szs.web;

import com.szs.IntegrationTest;
import com.szs.domain.User;
import com.szs.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@IntegrationTest
public class UserResourceIT {

    private static final String DEFAULT_USER_ID = "AAAAAAAAA";
    private static final String DEFAULT_PASSWORD = "AAAAAAAAA";
    private static final String DEFAULT_NAME = "AAAAAAAAA";
    private static final String DEFAULT_REG_NO = "900101-1234567";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc restUserMockMvc;

    private User user;

    public static User createEntity() {
        User user = new User()
                .useId(DEFAULT_USER_ID)
                .password(DEFAULT_PASSWORD)
                .name(DEFAULT_NAME)
                .regNo(DEFAULT_REG_NO);
        return user;
    }

    @BeforeEach
    public void intiTest() {
        user = createEntity();
    }

    @Test
    @Transactional
    void createUser() throws Exception {
        int databaseSizeBeforeCreate = userRepository.findAll().size();

        //when
        restUserMockMvc
                .perform(post("/szs/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(user)))
                .andExpect(status().isCreated());

        //then
        List<User> userList = userRepository.findAll();
        assertThat(userList).hasSize(databaseSizeBeforeCreate + 1);

        User testUser = userList.get(userList.size() - 1);
        assertThat(testUser.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testUser.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUser.getRegNo()).isEqualTo(AES256Utils.encrypt(DEFAULT_REG_NO));
    }

    @Test
    @Transactional
    @WithMockUser(DEFAULT_USER_ID)
    void getUser() throws Exception {
        // given
        user.setRegNo(AES256Utils.encrypt(user.getRegNo()));
        userRepository.saveAndFlush(user);

        // when,then
        restUserMockMvc
                .perform(get("/szs/me"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.userId").value(user.getUserId()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.regNo").value(DEFAULT_REG_NO));
    }


}
