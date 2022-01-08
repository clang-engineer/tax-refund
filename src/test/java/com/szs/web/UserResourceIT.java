package com.szs.web;

import com.szs.IntegrationTest;
import com.szs.domain.User;
import com.szs.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@IntegrationTest
public class UserResourceIT {

    private static final String DEFAULT_USER_ID = "AAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBB";

    private static final String DEFAULT_REG_NO = "AAAAAAAAA";
    private static final String UPDATED_REG_NO = "BBBBBBBBB";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc restUserMockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User user;

    public static User createEntity() {
        User user = new User()
                .useId(DEFAULT_USER_ID)
                .name(DEFAULT_NAME)
                .regNo(DEFAULT_REG_NO);
        return user;
    }

    @BeforeEach
    public void intiTest() {
        user = createEntity();
        user.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
    }

    @Test
    @Transactional
    void createUser() throws Exception {
        int databaseSizeBeforeCreate = userRepository.findAll().size();

        restUserMockMvc
                .perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(user)))
                .andExpect(status().isCreated());
    }

    @Test
    @Transactional
    @WithMockUser(DEFAULT_USER_ID)
    void getUser() throws Exception {
        // Initialize the database
        userRepository.saveAndFlush(user);

        // Get the user
        restUserMockMvc
                .perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.userId").value(user.getUserId()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.regNo").value(user.getRegNo())) ;

    }


}
