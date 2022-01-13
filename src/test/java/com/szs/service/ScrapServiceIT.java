package com.szs.service;

import com.szs.IntegrationTest;
import com.szs.domain.Scrap;
import com.szs.domain.ScrapSalary;
import com.szs.domain.ScrapTax;
import com.szs.domain.User;
import com.szs.repository.ScrapRepository;
import com.szs.repository.ScrapSalaryRepository;
import com.szs.repository.ScrapTaxRepository;
import com.szs.repository.UserRepository;
import com.szs.service.dto.ScrapDTO;
import com.szs.web.AES256Utils;
import com.szs.web.TestUtil;
import com.szs.web.UserResourceIT;
import com.szs.web.errors.UserInfoNotFoundException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@IntegrationTest
@Transactional
public class ScrapServiceIT {
    private static final String DEFAULT_APP_VER = "AAAAAAAAA";
    private static final String DEFAULT_HOST_NM = "AAAAAAAAA";
    private static final String DEFAULT_ERR_MSG = "AAAAAAAAA";
    private static final String DEFAULT_COMPANY = "AAAAAAAAA";
    private static final String DEFAULT_SVC_CD = "AAAAAAAAA";
    private static final String DEFAULT_USER_ID = "AAAAAAAAA";
    private static final LocalDateTime DEFAULT_WORKER_RES_DT = LocalDateTime.now();
    private static final LocalDateTime DEFAULT_WORKER_REQ_DT = LocalDateTime.now();

    public static final String DEFAULT_SCRAP_SALARY_TITLE = "AAAAAAAA";
    public static final Integer DEFAULT_SCRAP_SALARY_TOTAL = 3234000;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScrapRepository scrapRepository;

    @Autowired
    private ScrapSalaryRepository scrapSalaryRepository;

    @Autowired
    private ScrapTaxRepository scrapTaxRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ScrapService scrapService;

    private User user;

    private Scrap scrap;

    private ScrapSalary scrapSalary;

    private ScrapTax scrapTax;

    private JSONObject jsonObject;

    RestTemplate restTemplate;
    private ArgumentCaptor<String> postUrlCapture;
    private ArgumentCaptor<Map> postParamCapture;

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

    public ScrapSalary createScrapSalary() {
        ScrapSalary scrapSalary = new ScrapSalary()
                .title(DEFAULT_SCRAP_SALARY_TITLE)
                .total(DEFAULT_SCRAP_SALARY_TOTAL);
        return scrapSalary;
    }

    public ScrapTax createScrapTax() {
        ScrapTax scrapTax = new ScrapTax()
                .title(DEFAULT_SCRAP_SALARY_TITLE)
                .total(DEFAULT_SCRAP_SALARY_TOTAL);
        return scrapTax;
    }

    @BeforeEach
    public void initTest() throws Exception{
        user = UserResourceIT.createEntity().useId(DEFAULT_USER_ID);
        user.setPassword(passwordEncoder.encode("1234"));
        user.setRegNo(AES256Utils.encrypt(user.getRegNo()));

        scrap = createEntity();
        scrapSalary = createScrapSalary();
        scrapTax = createScrapTax();

        restTemplate = mock(RestTemplate.class);
        postUrlCapture = ArgumentCaptor.forClass(String.class);
        postParamCapture = ArgumentCaptor.forClass(Map.class);
    }

    @Test
    @Transactional
    @WithMockUser(DEFAULT_USER_ID)
    void getScrapInfoInLocalNetwork() throws Exception {

        userRepository.saveAndFlush(user);
        scrapRepository.saveAndFlush(scrap);
        scrapSalaryRepository.saveAndFlush(scrapSalary.scrap(scrap));
        scrapTaxRepository.saveAndFlush(scrapTax.scrap(scrap));

        Optional<ScrapDTO> scrapDTO = scrapService.getScrapInfo();

        assertThat(scrapDTO).isPresent();
        assertThat(scrapDTO.orElse(null).getAppVer()).isEqualTo(DEFAULT_APP_VER);
        assertThat(scrapDTO.orElse(null).getScrapSalaryList().get(0)).isEqualTo(scrapSalary);
        assertThat(scrapDTO.orElse(null).getScrapTaxList().get(0)).isEqualTo(scrapTax);
    }

    @Test
    @Transactional
    void getScrapInfoUnAuthorizedException() throws Exception {
        assertThrows(UserInfoNotFoundException.class, () -> scrapService.getScrapInfo());
    }

    @Test
    @Transactional
    void testSaveScarpInfoScrapSaveFailException() throws Exception {
        when(restTemplate.postForObject(anyString(), anyString(), eq(Map.class))).thenReturn(null);
        scrapService.setRestTemplate(restTemplate);

        assertThrows(ScrapSaveFailException.class, () -> {
            scrapService.saveScrapInfo(user);
        });
    }

    @Test
    @Transactional
    void testSaveScrapInfo() throws Exception {
        userRepository.saveAndFlush(user);

        JSONObject jsonObject = TestUtil.createScrapJSONObject();
        jsonObject.getJSONObject("jsonList").put("userId", DEFAULT_USER_ID);

        //given
        when(restTemplate.postForObject(postUrlCapture.capture(), postParamCapture.capture(), eq(Map.class))).thenReturn(TestUtil.getMapFromJsonObject(jsonObject));
        scrapService.setRestTemplate(restTemplate);

        //when
        scrapService.saveScrapInfo(user);

        //then
        Scrap scrap = scrapRepository.findOneByUserId(DEFAULT_USER_ID).orElseThrow(() -> new Exception());
        assertThat(scrap).isNotNull();
        assertThat(scrapTaxRepository.findAllByScrapId(scrap.getId())).isNotEmpty();
        assertThat(scrapSalaryRepository.findAllByScrapId(scrap.getId())).isNotEmpty();

        assertThat(postUrlCapture.getValue()).isEqualTo(ScrapService.SZS_URL);
        assertThat(postParamCapture.getValue().get("name")).isEqualTo(user.getName());
    }

    @Test
    @Transactional
    @WithMockUser(DEFAULT_USER_ID)
    void assertThatScrappingScheduledIn24Hours() throws Exception {
        userRepository = mock(UserRepository.class);
        scrapRepository = mock(ScrapRepository.class);

        scrapService = new ScrapService(userRepository, scrapRepository, scrapSalaryRepository, scrapTaxRepository);

        scrapService.executeScrapping();

        verify(scrapRepository, times(1)).findAll();
        verify(userRepository, times(1)).findAll();
    }

}