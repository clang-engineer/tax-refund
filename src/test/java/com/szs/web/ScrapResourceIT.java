package com.szs.web;

import com.szs.IntegrationTest;
import com.szs.domain.Scrap;
import com.szs.domain.User;
import com.szs.repository.ScrapRepository;
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

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@IntegrationTest
class ScrapResourceIT {
    private static final String DEFAULT_APP_VER = "AAAAAAAAA";
    private static final String DEFAULT_HOST_NM = "AAAAAAAAA";
    private static final String DEFAULT_ERR_MSG = "AAAAAAAAA";
    private static final String DEFAULT_COMPANY = "AAAAAAAAA";
    private static final String DEFAULT_SVC_CD = "AAAAAAAAA";
    private static final String DEFAULT_USER_ID = "AAAAAAAAA";
    private static final LocalDateTime DEFAULT_WORKER_RES_DT = LocalDateTime.now();
    private static final LocalDateTime DEFAULT_WORKER_REQ_DT = LocalDateTime.now();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScrapRepository scrapRepository;

    @Autowired
    private MockMvc restScrapMockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Scrap scrap;

    private User user;

    public static Scrap createEntity() {
        Scrap scrap = new Scrap()
                .appVer(DEFAULT_APP_VER)
                .hostNm(DEFAULT_HOST_NM)
                .errMsg(DEFAULT_ERR_MSG)
                .company(DEFAULT_COMPANY)
                .svcCd(DEFAULT_SVC_CD)
                .userId(DEFAULT_USER_ID)
                .workerResDt(DEFAULT_WORKER_RES_DT)
                .workerReqDt(DEFAULT_WORKER_REQ_DT);
        return scrap;
    }

    @BeforeEach
    public void initTest() throws Exception {
        user = UserResourceIT.createEntity().useId(DEFAULT_USER_ID);
        user.setPassword(passwordEncoder.encode("1234"));
        user.setRegNo(AES256Utils.encrypt(user.getRegNo()));

        scrap = createEntity();
    }

    @Test
    @Transactional
    @WithMockUser(DEFAULT_USER_ID)
    void testGetScrapInfo() throws Exception {
        //given
        userRepository.saveAndFlush(user);
        scrapRepository.saveAndFlush(scrap);

        //when,then
        restScrapMockMvc
                .perform(get("/szs/scrap"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.appVer").value(scrap.getAppVer()))
                .andExpect(jsonPath("$.hostNm").value(scrap.getHostNm()))
                .andExpect(jsonPath("$.errMsg").value(scrap.getErrMsg()))
                .andExpect(jsonPath("$.company").value(scrap.getCompany()))
                .andExpect(jsonPath("$.svcCd").value(scrap.getSvcCd()))
                .andExpect(jsonPath("$.userId").value(scrap.getUserId()));
    }

}