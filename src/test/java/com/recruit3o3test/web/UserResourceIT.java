package com.recruit3o3test.web;

import com.recruit3o3test.YoungJunApplication;
import com.recruit3o3test.domain.User;
import com.recruit3o3test.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = YoungJunApplication.class)
@AutoConfigureMockMvc
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

        restUserMockMvc
                .perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(user)))
                .andExpect(status().isCreated());
    }


}
