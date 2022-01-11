package com.szs.web;

import com.szs.domain.ScrapSalary;
import com.szs.domain.ScrapTax;
import com.szs.domain.User;
import com.szs.repository.UserRepository;
import com.szs.security.SecurityUtils;
import com.szs.service.ScrapService;
import com.szs.service.dto.ScrapDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;

@RestController
@RequestMapping("/szs")
public class RefundController {
    private Logger log = LoggerFactory.getLogger(ScrapResource.class);

    private UserRepository userRepository;

    private ScrapService scrapService;

    public RefundController(
            UserRepository userRepository,
            ScrapService scrapService) {
        this.userRepository = userRepository;
        this.scrapService = scrapService;
    }

    @GetMapping("/refund")
    public ResponseEntity<HashMap<String, String>> getRefund() throws Exception {

        HashMap<String, String> result = new HashMap<>();

        String username = SecurityUtils.getCurrentUserLogin()
                .flatMap(userRepository::findOneByUserId).map(User::getName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        result.put("이름", username);

        ScrapDTO scrap = scrapService.getScrapInfo().orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        int limitedMoney = RefundUtils.getLimitedMoney(scrap.getScrapSalaryList().stream().map(ScrapSalary::getTotal).reduce(0, Integer::sum));
        int deductedMoney = RefundUtils.getDeductedMoney(scrap.getScrapTaxList().stream().map(ScrapTax::getTotal).reduce(0, Integer::sum));
        int refundMoney = Math.min(limitedMoney, deductedMoney);

        result.put("한도", RefundUtils.getFormattedMoney(limitedMoney));
        result.put("공제액", RefundUtils.getFormattedMoney(deductedMoney));
        result.put("환급액", RefundUtils.getFormattedMoney(refundMoney));

        return ResponseEntity.ok(result);
    }

}
