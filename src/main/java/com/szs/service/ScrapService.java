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
        Map map = Map.of("name", "홍길동", "regNo", "860824-1655068");
        this.restTemplate.postForObject("https://codetest.3o3.co.kr/scrap/", map, Map.class);
//        {name: "홍길동" , regNo: "860824-1655068"}
        return Optional.of(new Scrap());
    }
}
