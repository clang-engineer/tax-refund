package com.szs.web;

import com.szs.service.ScrapService;
import com.szs.service.dto.ScrapDTO;
import com.szs.web.errors.ScrapNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/szs")
public class ScrapResource {
    private Logger log = LoggerFactory.getLogger(ScrapResource.class);

    private final ScrapService scrapService;

    public ScrapResource(ScrapService scrapService) {
        this.scrapService = scrapService;
    }


    @GetMapping("/scrap")
    public ResponseEntity<ScrapDTO> getScrap() throws Exception {
        log.debug("REST request to get scrap");
        ScrapDTO scrapDTO = scrapService.getScrapInfo().orElseThrow(() -> new ScrapNotFoundException());

        return ResponseEntity.ok(scrapDTO);
    }

}
