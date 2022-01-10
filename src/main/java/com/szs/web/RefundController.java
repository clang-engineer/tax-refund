package com.szs.web;

import com.szs.config.Constants;
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

        int limitedMoney = getLimitedMoney(scrap.getScrapSalaryList().stream().map(ScrapSalary::getTotal).reduce(0, Integer::sum));
        int deductedMoney = getDeductedMoney(scrap.getScrapTaxList().stream().map(ScrapTax::getTotal).reduce(0, Integer::sum));
        int refundMoney = Math.min(limitedMoney, deductedMoney);

        result.put("한도", getFormattedMoney(limitedMoney));
        result.put("공제액", getFormattedMoney(deductedMoney));
        result.put("환급액", getFormattedMoney(refundMoney));

        return ResponseEntity.ok(result);
    }

    public Integer getLimitedMoney(Integer totalSalary) {
        if (totalSalary <= Constants.REFUND_LOWER_LIMIT) {
            return 740000;
        } else if (totalSalary <= Constants.REFUND_UPPER_LIMIT) {
            Integer temp = (int) (740000 - (totalSalary - Constants.REFUND_LOWER_LIMIT) * 0.008);
            return Math.max(660000, temp);
        } else {
            Integer temp = (int) (660000 - (totalSalary - Constants.REFUND_UPPER_LIMIT) * 0.5);
            return Math.max(500000, temp);
        }
    }

    public Integer getDeductedMoney(Integer calculateTax) {
        Integer standardMoney = 1300000;
        if (calculateTax <= standardMoney) {
            return (int) (calculateTax * 0.55);
        } else {
            return (int) (715000 + (calculateTax - standardMoney) * 0.3);
        }
    }

    public String getFormattedMoney(Integer money) {
        int first = money / 10000;
        int second = money % 10000 / 1000;

        String result = "";
        if (first != 0) {
            result += first + "만";
        }

        if (second != 0) {
            if (first != 0) {
                result += " ";
            }
            result += second + "천";
        }

        result += "원";

        return result;

    }

}
