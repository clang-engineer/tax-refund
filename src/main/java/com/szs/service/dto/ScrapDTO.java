package com.szs.service.dto;

import com.szs.domain.Scrap;
import com.szs.domain.ScrapSalary;
import com.szs.domain.ScrapTax;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScrapDTO  {
    private Long id;

    private String appVer;

    private String hostNm;

    private String errMsg;

    private String company;

    private String svcCd;

    private String userId;

    private LocalDateTime workerResDt;

    private LocalDateTime workerReqDt;

    List<ScrapSalary> scrapSalaryList;

    List<ScrapTax> scrapTaxList;

    public ScrapDTO(Scrap scrap) {
        this.id = scrap.getId();
        this.appVer = scrap.getAppVer();
        this.hostNm = scrap.getHostNm();
        this.errMsg = scrap.getErrMsg();
        this.company = scrap.getCompany();
        this.svcCd = scrap.getSvcCd();
        this.userId = scrap.getUserId();
        this.workerReqDt = scrap.getWorkerReqDt();
        this.workerResDt = scrap.getWorkerResDt();
    }
}
