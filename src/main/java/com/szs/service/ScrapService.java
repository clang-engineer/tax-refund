package com.szs.service;

import com.szs.domain.Scrap;
import com.szs.domain.ScrapSalary;
import com.szs.domain.ScrapTax;
import com.szs.domain.User;
import com.szs.repository.ScrapRepository;
import com.szs.repository.ScrapSalaryRepository;
import com.szs.repository.ScrapTaxRepository;
import com.szs.repository.UserRepository;
import com.szs.security.SecurityUtils;
import com.szs.service.dto.ScrapDTO;
import com.szs.web.AES256Utils;
import com.szs.web.errors.UserInfoNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ScrapService {
    private final Logger log = LoggerFactory.getLogger(ScrapService.class);

    public static final String SZS_URL = "https://codetest.3o3.co.kr/scrap/";

    private RestTemplate restTemplate = new RestTemplateBuilder().build();

    private final UserRepository userRepository;

    private final ScrapRepository scrapRepository;

    private final ScrapSalaryRepository scrapSalaryRepository;

    private final ScrapTaxRepository scrapTaxRepository;

    public ScrapService(UserRepository userRepository, ScrapRepository scrapRepository,
                        ScrapSalaryRepository scrapSalaryRepository,
                        ScrapTaxRepository scrapTaxRepository
    ) {
        this.userRepository = userRepository;
        this.scrapRepository = scrapRepository;
        this.scrapSalaryRepository = scrapSalaryRepository;
        this.scrapTaxRepository = scrapTaxRepository;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Optional<ScrapDTO> getScrapInfo() {
        log.debug("Get current user scrap info");

        User user = SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByUserIdIgnoreCase).orElseThrow(() -> new UserInfoNotFoundException());

        Optional<ScrapDTO> scrapDTO = scrapRepository.findOneByUserIdIgnoreCase(user.getUserId()).map(ScrapDTO::new);

        if (scrapDTO.isPresent()) {
            return scrapDTO.map(data -> {
                List<ScrapSalary> scrapSalaryList = scrapSalaryRepository.findAllByScrapId(data.getId());
                data.setScrapSalaryList(scrapSalaryList);
                List<ScrapTax> scrapTaxList = scrapTaxRepository.findAllByScrapId(data.getId());
                data.setScrapTaxList(scrapTaxList);
                return data;
            });
        } else {
            return saveScrapInfo(user);
        }

    }

    public Optional<ScrapDTO> saveScrapInfo(User user) {
        log.debug("Save current user scrap info from external network");
        try {
            Map map = Map.of("name", user.getName(), "regNo", AES256Utils.decrypt(user.getRegNo()));

            HashMap externalScrapInfo = (HashMap) this.restTemplate.postForObject("https://codetest.3o3.co.kr/scrap/", map, Map.class);

            if (externalScrapInfo == null || externalScrapInfo.size() <= 1) {
                throw new ScrapNotFoundException();
            }

            Scrap scrap = saveScrap(externalScrapInfo, user.getUserId());

            List salaryList = (List) ((HashMap) externalScrapInfo.get("jsonList")).get("scrap001");
            List taxList = (List) ((HashMap) externalScrapInfo.get("jsonList")).get("scrap002");

            List<ScrapSalary> scrapSalaryList = new ArrayList();
            List<ScrapTax> scrapTaxList = new ArrayList();
            salaryList.forEach(salaryInfo -> {
                ScrapSalary scrapSalary = saveScrappedSalary(scrap, (HashMap<String, Object>) salaryInfo);
                scrapSalaryList.add(scrapSalary);
            });

            taxList.forEach(taxInfo -> {
                ScrapTax scrapTax = saveScrappedTax(scrap, (HashMap<String, Object>) taxInfo);
                scrapTaxList.add(scrapTax);
            });

            ScrapDTO scrapDTO = new ScrapDTO(scrap);
            scrapDTO.setScrapSalaryList(scrapSalaryList);
            scrapDTO.setScrapTaxList(scrapTaxList);
            return Optional.of(scrapDTO);
        } catch (ScrapNotFoundException e) {
            throw new ScrapNotFoundException();
        } catch (Exception e) {
            throw new ScrapSaveFailException();
        }
    }

    private Scrap saveScrap(HashMap externalJson, String userId) {
        HashMap jsonList = (HashMap) externalJson.get("jsonList");

        Scrap result = new Scrap()
                .appVer((String) externalJson.get("appVer"))
                .hostNm((String) externalJson.get("hostNm"))
                .workerResDt(LocalDateTime.parse((String) externalJson.get("workerResDt")))
                .workerReqDt(LocalDateTime.parse((String) externalJson.get("workerReqDt")))
                .errMsg((String) jsonList.get("errMsg"))
                .company((String) jsonList.get("company"))
                .svcCd((String) jsonList.get("svcCd"))
                .userId(userId);
        return scrapRepository.save(result);
    }

    private ScrapSalary saveScrappedSalary(Scrap scrap, HashMap<String, Object> salaryInfo) {
        SimpleDateFormat tranFormat = new SimpleDateFormat("yyyy.mm.dd");
        try {
            ScrapSalary scrappedSalary = new ScrapSalary()
                    .title((String) salaryInfo.get("소득내역"))
                    .total(getCastedMoney(salaryInfo.get("총지급액")))
                    .companyName((String) salaryInfo.get("기업명"))
                    .userName((String) salaryInfo.get("이름"))
                    .regNo(AES256Utils.encrypt((String) salaryInfo.get("주민등록번호")))
                    .category((String) salaryInfo.get("소득구분"))
                    .companyNo((String) salaryInfo.get("사업자등록번호"))
                    .startDate(tranFormat.parse((String) salaryInfo.get("업무시작일")))
                    .endDate(tranFormat.parse((String) salaryInfo.get("업무종료일")))
                    .payDate(tranFormat.parse((String) salaryInfo.get("지급일")))
                    .scrap(scrap);
            return scrapSalaryRepository.save(scrappedSalary);
        } catch (Exception e) {
            throw new ScrapSaveFailException();
        }
    }

    private ScrapTax saveScrappedTax(Scrap scrap, HashMap<String, Object> taxInfo) {
        ScrapTax scrappedTax = new ScrapTax()
                .title((String) taxInfo.get("소득구분"))
                .total(getCastedMoney(taxInfo.get("총사용금액")))
                .scrap(scrap);
        return scrapTaxRepository.save(scrappedTax);
    }

    private Integer getCastedMoney(Object money) {
        if (money instanceof String) {
            return Integer.valueOf((String) money);
        } else {
            return (Integer) money;
        }
    }

    @Scheduled(cron = "0 0 1 * * ?")
    void executeScrapping() {
        log.debug("Execute daily scrap schedule {}", LocalDateTime.now());

        List<String> scrappedUserId = scrapRepository.findAll().stream().map(Scrap::getUserId).collect(Collectors.toList());
        userRepository.findAll().stream()
                .filter(user -> !scrappedUserId.contains(user.getUserId()))
                .forEach(user -> saveScrapInfo(user));
    }
}
