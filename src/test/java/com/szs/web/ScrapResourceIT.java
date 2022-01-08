package com.szs.web;

import com.szs.IntegrationTest;
import com.szs.domain.Scrap;
import com.szs.repository.ScrapRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasValue;
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
    private static final Instant DEFAULT_WORKER_RES_DT = Instant.now();
    private static final Instant DEFAULT_WORKER_REQ_DT = Instant.now();
    private static final Map DEFAULT_SCRAP_001 = Map.of("key1-1", "value1-1", "key1-2", "value1-2");
    private static final Map DEFAULT_SCRAP_002 = Map.of("key2-1", "value2-1", "key2-2", "value2-2");

    @Autowired
    private ScrapRepository scrapRepository;

    @Autowired
    private MockMvc restScrapMockMvc;

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
        scrap = createEntity();
    }

    @Test
    @Transactional
    @WithMockUser(DEFAULT_USER_ID)
    void getScrap() throws Exception {
        scrapRepository.saveAndFlush(scrap);

        restScrapMockMvc
                .perform(get("/szs/scrap"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.appVer").value(scrap.getAppVer()))
                .andExpect(jsonPath("$.hostNm").value(scrap.getHostNm()))
                .andExpect(jsonPath("$.errMsg").value(scrap.getErrMsg()))
                .andExpect(jsonPath("$.company").value(scrap.getCompany()))
                .andExpect(jsonPath("$.svcCd").value(scrap.getSvcCd()))
                .andExpect(jsonPath("$.userId").value(scrap.getUserId()))
                .andExpect(jsonPath("$.scrap001").value(hasKey("key1-1")))
                .andExpect(jsonPath("$.scrap001").value(hasKey("key1-2")))
                .andExpect(jsonPath("$.scrap001").value(hasValue("value1-1")))
                .andExpect(jsonPath("$.scrap001").value(hasValue("value1-2")))
        ;

    }

}