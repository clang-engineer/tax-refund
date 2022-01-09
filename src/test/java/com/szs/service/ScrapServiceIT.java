package com.szs.service;

import com.szs.IntegrationTest;
import com.szs.domain.Scrap;
import com.szs.domain.User;
import com.szs.repository.ScrapRepository;
import com.szs.repository.UserRepository;
import com.szs.web.UserResourceIT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@Transactional
class ScrapServiceIT {
    private static final String DEFAULT_APP_VER = "AAAAAAAAA";
    private static final String DEFAULT_HOST_NM = "AAAAAAAAA";
    private static final String DEFAULT_ERR_MSG = "AAAAAAAAA";
    private static final String DEFAULT_COMPANY = "AAAAAAAAA";
    private static final String DEFAULT_SVC_CD = "AAAAAAAAA";
    private static final String DEFAULT_USER_ID = "AAAAAAAAA";
    private static final LocalDateTime DEFAULT_WORKER_RES_DT = LocalDateTime.now();
    private static final LocalDateTime DEFAULT_WORKER_REQ_DT = LocalDateTime.now();
    private static final Map DEFAULT_SCRAP_001 = Map.of("key1-1", "value1-1", "key1-2", "value1-2");
    private static final Map DEFAULT_SCRAP_002 = Map.of("key2-1", "value2-1", "key2-2", "value2-2");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScrapRepository scrapRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ScrapService scrapService;

    private User user;

    private Scrap scrap;

    public static Scrap createEntity() {
        Scrap scrap = new Scrap()
                .appVer(DEFAULT_APP_VER)
                .hostNm(DEFAULT_HOST_NM)
                .errMsg(DEFAULT_ERR_MSG)
                .company(DEFAULT_COMPANY)
                .svcCd(DEFAULT_SVC_CD)
                .userId(DEFAULT_USER_ID)
                .workerResDt(DEFAULT_WORKER_RES_DT)
                .workerReqDt(DEFAULT_WORKER_REQ_DT)
                .scrap001(DEFAULT_SCRAP_001)
                .scrap002(DEFAULT_SCRAP_002);
        return scrap;
    }

    @BeforeEach
    public void initTest() {
        user = UserResourceIT.createEntity().useId(DEFAULT_USER_ID);
        user.setPassword(passwordEncoder.encode("1234"));

        scrap = createEntity();
    }

    @Test
    @Transactional
    @WithMockUser(DEFAULT_USER_ID)
    void getScrapFromLocalNetwork() throws Exception {

        userRepository.saveAndFlush(user);
        scrapRepository.saveAndFlush(scrap);

        Optional<Scrap> scrap = scrapService.getScrapInfo();

        assertThat(scrap).isPresent();
        assertThat(scrap.orElse(null).getAppVer()).isEqualTo(DEFAULT_APP_VER);

    }

    @Test
    @Transactional
    @WithMockUser(DEFAULT_USER_ID)
    void getScrapFromExternalNetwork() throws Exception {

        userRepository.saveAndFlush(user);

        Optional<Scrap> scrap = scrapService.getScrapInfo();

        assertThat(scrap).isPresent();
        assertThat(scrap.orElse(null).getAppVer()).isEqualTo(DEFAULT_APP_VER);

    }


}