package com.szs.web;

import com.szs.config.Constants;
import com.szs.domain.Scrap;
import com.szs.domain.User;
import com.szs.repository.UserRepository;
import com.szs.security.SecurityUtils;
import com.szs.service.ScrapService;
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

        Scrap scrap = scrapService.getScrapInfo().orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        Double limitedMoney = getLimitedMoney(Integer.parseInt(scrap.getScrap001().get("총지급액")));
        Double deductedMoney = getDeductedMoney(Integer.parseInt(scrap.getScrap002().get("총사용금액")));
        Double refundMoney = Math.min(limitedMoney, deductedMoney);

        result.put("한도", limitedMoney.toString());
        result.put("공제액", deductedMoney.toString());
        result.put("환급액", refundMoney.toString());

        return ResponseEntity.ok(result);
    }

    public double getLimitedMoney(int totalSalary) {
        if (totalSalary <= Constants.REFUND_LOWER_LIMIT) {
            return 740000;
        } else if (totalSalary <= Constants.REFUND_UPPER_LIMIT) {
            double temp = 740000 - (totalSalary - Constants.REFUND_LOWER_LIMIT) * 0.008;
            return Math.max(660000, temp);
        } else {
            double temp = 660000 - (totalSalary - Constants.REFUND_UPPER_LIMIT) * 0.5;
            return Math.max(500000, temp);
        }
    }

    public double getDeductedMoney(int calculateTax) {
        int standardMoney = 1300000;
        if (calculateTax <= standardMoney) {
            return calculateTax * 0.55;
        } else {
            return 715000 + (calculateTax - standardMoney) * 0.3;
        }
    }

}
