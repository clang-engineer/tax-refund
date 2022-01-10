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

    public Optional<ScrapDTO> getScrapInfo() throws Exception {
        log.debug("Get current user scrap info");

        User user = SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByUserId).orElseThrow(() -> new Exception());
        Optional<ScrapDTO> scrapInDB = scrapRepository.findOneByUserId(user.getUserId()).map(ScrapDTO::new);

        if (scrapInDB.isEmpty()) {
            scrapInDB = saveScrapInfo(user.getName(), AES256Utils.decrypt(user.getRegNo()));
        }

        return scrapInDB;
    }


    public Optional<ScrapDTO> saveScrapInfo(String name, String regNo) {
        log.debug("Save current user scrap info from external network");

        Map map = Map.of("name", name, "regNo", regNo);
        HashMap externalScrapInfo = (HashMap) this.restTemplate.postForObject("https://codetest.3o3.co.kr/scrap/", map, Map.class);

        if (externalScrapInfo.size() <= 1) {
            return null;
        }

        Scrap scrap = saveScrap(externalScrapInfo);

        List salaryList = (List) ((HashMap) externalScrapInfo.get("jsonList")).get("scrap001");
        List taxList = (List) ((HashMap) externalScrapInfo.get("jsonList")).get("scrap002");

        List<ScrapSalary> scrapSalaryList = new ArrayList();
        List<ScrapTax> scrapTaxList = new ArrayList();
        salaryList.forEach(salaryInfo -> {
            try {
                ScrapSalary scrapSalary = saveScrappedSalary(scrap, (HashMap<String, Object>) salaryInfo);
                scrapSalaryList.add(scrapSalary);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        taxList.forEach(taxInfo -> {
            ScrapTax scrapTax = saveScrappedTax(scrap, (HashMap<String, Object>) taxInfo);
            scrapTaxList.add(scrapTax);
        });

        ScrapDTO scrapDTO = new ScrapDTO(scrap);
        scrapDTO.setScrapSalaryList(scrapSalaryList);
        scrapDTO.setScrapTaxList(scrapTaxList);
        return Optional.of(scrapDTO);
    }

    private Scrap saveScrap(HashMap externalJson) {
        HashMap jsonList = (HashMap) externalJson.get("jsonList");

        Scrap result = new Scrap()
                .appVer((String) externalJson.get("appVer"))
                .hostNm((String) externalJson.get("hostNm"))
                .workerResDt(LocalDateTime.parse((String) externalJson.get("workerResDt")))
                .workerReqDt(LocalDateTime.parse((String) externalJson.get("workerReqDt")))
                .errMsg((String) jsonList.get("errMsg"))
                .company((String) jsonList.get("company"))
                .svcCd((String) jsonList.get("svcCd"))
                .userId(jsonList.get("userId").toString());
        return scrapRepository.save(result);
    }

    private ScrapSalary saveScrappedSalary(Scrap scrap, HashMap<String, Object> salaryInfo) throws Exception {
        SimpleDateFormat tranFormat = new SimpleDateFormat("yyyy.mm.dd");
        ScrapSalary scrappedSalary = new ScrapSalary()
                .title((String) salaryInfo.get("소득내역"))
                .total((Integer) salaryInfo.get("총지급액"))
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
    }

    private ScrapTax saveScrappedTax(Scrap scrap, HashMap<String, Object> taxInfo) {
        ScrapTax scrappedTax = new ScrapTax()
                .title((String) taxInfo.get("소득구분"))
                .total((Integer) taxInfo.get("총사용금액"))
                .scrap(scrap);
        return scrapTaxRepository.save(scrappedTax);
    }

    @Scheduled(cron = "0 0 1 * * ?")
    void excuteScrapping() {
        log.debug("Excute daily scrap schedule");

        List<String> scrappedUserId = scrapRepository.findAll().stream().map(Scrap::getUserId).collect(Collectors.toList());
        userRepository.findAll().stream()
                .filter(user -> !scrappedUserId.contains(user.getUserId()))
                .forEach(user -> {
                    try {
                        saveScrapInfo(user.getName(), AES256Utils.decrypt(user.getRegNo()));
                    } catch (Exception e) {
                        log.debug("Error in save scrap info {}", e);
                    }
                });
    }
}
