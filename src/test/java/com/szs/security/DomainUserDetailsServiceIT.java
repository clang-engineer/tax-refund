package com.szs.security;

import com.szs.IntegrationTest;
import com.szs.domain.User;
import com.szs.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@IntegrationTest
class DomainUserDetailsServiceIT {

    private static final String USER_ONE_USER_ID = "test-user-one-id";
    private static final String USER_ONE_NAME = "test-user-one-name";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsService domainUserDetailsService;

    @BeforeEach
    public void init() {
        User userOne = new User();
        userOne.setUserId(USER_ONE_USER_ID);
        userOne.setName(USER_ONE_NAME);
        userOne.setPassword(RandomStringUtils.random(60));
        userRepository.save(userOne);
    }

    @Test
    void assertThatUserCanBeFoundByLogin() {
        UserDetails userDetails = domainUserDetailsService.loadUserByUsername(USER_ONE_USER_ID);
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(USER_ONE_USER_ID);
    }

    @Test
    void assertThatUserCanBeFoundByLoginIgnoreCase() {
        UserDetails userDetails = domainUserDetailsService.loadUserByUsername(USER_ONE_USER_ID.toUpperCase(Locale.ENGLISH));
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(USER_ONE_USER_ID);
    }

}
