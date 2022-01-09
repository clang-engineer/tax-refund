package com.szs.service;

import com.szs.domain.Scrap;
import com.szs.domain.User;
import com.szs.repository.ScrapRepository;
import com.szs.repository.UserRepository;
import com.szs.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class ScrapService {
    private final Logger log = LoggerFactory.getLogger(ScrapService.class);

    private RestTemplate restTemplate = new RestTemplateBuilder().build();

    private final UserRepository userRepository;

    private final ScrapRepository scrapRepository;

    public ScrapService(UserRepository userRepository, ScrapRepository scrapRepository) {
        this.userRepository = userRepository;
        this.scrapRepository = scrapRepository;
    }

    public Optional<Scrap> getScrapInfo() throws Exception {
        log.debug("Get current user scrap info");

        User user = SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByUserId).orElseThrow(() -> new Exception());
        Optional<Scrap> scrapInDB = scrapRepository.findOneByUserId(user.getUserId());

        if (scrapInDB.isEmpty()) {
            scrapInDB = saveScrapInfo(user.getName(), user.getRegNo());
        }

        return scrapInDB;
    }


    public Optional<Scrap> saveScrapInfo(String name, String regNo) {
        log.debug("Save current user scrap info from external network");

        Map map = Map.of("name", name, "regNo", regNo);
        HashMap externalScrapInfo = (HashMap) this.restTemplate.postForObject("https://codetest.3o3.co.kr/scrap/", map, Map.class);

        Scrap result = transformJsonToScarp(externalScrapInfo);
        scrapRepository.save(result);
        return Optional.of(result);
    }

    private Scrap transformJsonToScarp(HashMap externalJson) {
        HashMap jsonList = (HashMap) externalJson.get("jsonList");
        Map scrap001 = (HashMap) ((ArrayList) jsonList.get("scrap001")).get(0);
        Map scrap002 = (HashMap) ((ArrayList) jsonList.get("scrap002")).get(0);
        Scrap result = new Scrap()
                .appVer((String) externalJson.get("appVer"))
                .hostNm((String) externalJson.get("hostNm"))
                .workerResDt(LocalDateTime.parse((String) externalJson.get("workerResDt")))
                .workerReqDt(LocalDateTime.parse((String) externalJson.get("workerReqDt")))
                .errMsg((String) jsonList.get("errMsg"))
                .company((String) jsonList.get("company"))
                .svcCd((String) jsonList.get("svcCd"))
                .userId((String) jsonList.get("userId"))
                .scrap001(scrap001)
                .scrap002(scrap002);

        return result;
    }
}