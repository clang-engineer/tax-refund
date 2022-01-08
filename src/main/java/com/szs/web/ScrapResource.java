package com.szs.web;

import com.szs.domain.Scrap;
import com.szs.repository.ScrapRepository;
import com.szs.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/szs")
public class ScrapResource {
    private Logger log = LoggerFactory.getLogger(ScrapResource.class);

    private final ScrapRepository scrapRepository;

    public ScrapResource(ScrapRepository scrapRepository) {
        this.scrapRepository = scrapRepository;
    }


    @GetMapping("/scrap")
    public ResponseEntity<Scrap> getScrap() {
        log.debug("REST request to get scrap");

        return SecurityUtils.getCurrentUserLogin().flatMap(scrapRepository::findOneByUserId)
                .map((response) -> ResponseEntity.ok(response))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

}
