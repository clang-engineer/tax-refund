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
import com.szs.web.UserResourceIT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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

    public static final String HONG_GIL_DONG_USER_ID = "1";
    public static final String HONG_GIL_DONG_NAME = "홍길동";
    public static final String HONG_GIL_DONG_REG_NO = "860824-1655068";

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

    public void saveHongGilDongLocally() throws Exception {
        user.setUserId(HONG_GIL_DONG_USER_ID);
        user.setName(HONG_GIL_DONG_NAME);
        user.setRegNo(AES256Utils.encrypt(HONG_GIL_DONG_REG_NO));

        userRepository.saveAndFlush(user);
    }

    @BeforeEach
    public void initTest() {
        user = UserResourceIT.createEntity().useId(DEFAULT_USER_ID);
        user.setPassword(passwordEncoder.encode("1234"));

        scrap = createEntity();
        scrapSalary = createScrapSalary();
        scrapTax = createScrapTax();
    }

    @Test
    @Transactional
    @WithMockUser(DEFAULT_USER_ID)
    void getScrapFromLocalNetwork() throws Exception {

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
    @WithMockUser(HONG_GIL_DONG_USER_ID)
    void getScrapFromExternalNetwork() throws Exception {

        saveHongGilDongLocally();
        Optional<ScrapDTO> scrap = scrapService.getScrapInfo();

        assertThat(scrap).isPresent();
        assertThat(scrap.orElse(null).getUserId()).isEqualTo("1");
    }

    @Test
    @Transactional
    void assertThatScrappingScheduledIn24Hours() throws Exception {

        saveHongGilDongLocally();
        scrapService.executeScrapping();

        Optional<Scrap> scrap = scrapRepository.findOneByUserId("1");

        assertThat(scrap).isPresent();
    }

}