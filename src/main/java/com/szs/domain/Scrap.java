package com.szs.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "tbl_scrap")
public class Scrap implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "app_ver", nullable = false)
    private String appVer;

    @Column(name = "host_nm", nullable = false)
    private String hostNm;

    @Column(name = "err_msg", nullable = false)
    private String errMsg;

    @Column(name = "company", nullable = false)
    private String company;

    @Column(name = "svc_cd", nullable = false)
    private String svcCd;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "worker_res_dt")
    private LocalDateTime workerResDt;

    @Column(name = "worker_req_dt")
    private LocalDateTime workerReqDt;

    @ElementCollection
    @CollectionTable(name = "tbl_scrap_001", joinColumns = @JoinColumn(name = "scrap_id"))
    @MapKeyColumn(name = "json_key")
    @Column(name = "json_value")
    private Map<String, String> scrap001 = new HashMap<>();

    @ElementCollection
    @CollectionTable(name = "tbl_scrap_002", joinColumns = @JoinColumn(name = "scrap_id"))
    @MapKeyColumn(name = "json_key")
    @Column(name = "json_value")
    private Map<String, String> scrap002 = new HashMap<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Scrap id(Long id) {
        this.id = id;
        return this;
    }

    public String getAppVer() {
        return appVer;
    }

    public void setAppVer(String appVer) {
        this.appVer = appVer;
    }

    public Scrap appVer(String appVer) {
        this.appVer = appVer;
        return this;
    }

    public String getHostNm() {
        return hostNm;
    }

    public void setHostNm(String hostNm) {
        this.hostNm = hostNm;
    }

    public Scrap hostNm(String hostNm) {
        this.hostNm = hostNm;
        return this;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public Scrap errMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Scrap company(String company) {
        this.company = company;
        return this;
    }

    public String getSvcCd() {
        return svcCd;
    }

    public void setSvcCd(String svcCd) {
        this.svcCd = svcCd;
    }

    public Scrap svcCd(String svcCd) {
        this.svcCd = svcCd;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Scrap userId(String userId) {
        this.userId = userId;
        return this;
    }

    public LocalDateTime getWorkerResDt() {
        return workerResDt;
    }

    public void setWorkerResDt(LocalDateTime workerResDt) {
        this.workerResDt = workerResDt;
    }

    public Scrap workerResDt(LocalDateTime workerResDt) {
        this.workerResDt = workerResDt;
        return this;
    }

    public LocalDateTime getWorkerReqDt() {
        return workerReqDt;
    }

    public void setWorkerReqDt(LocalDateTime workerReqDt) {
        this.workerReqDt = workerReqDt;
    }

    public Scrap workerReqDt(LocalDateTime workerReqDt) {
        this.workerReqDt = workerReqDt;
        return this;
    }

    public Map<String, String> getScrap001() {
        return scrap001;
    }

    public void setScrap001(Map<String, String> scrap001) {
        this.scrap001 = scrap001;
    }

    public Scrap scrap001(Map<String, String> scrap001) {
        this.scrap001 = scrap001;
        return this;
    }

    public Map<String, String> getScrap002() {
        return scrap002;
    }

    public void setScrap002(Map<String, String> scrap002) {
        this.scrap002 = scrap002;
    }

    public Scrap scrap002(Map<String, String> scrap002) {
        this.scrap002 = scrap002;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        return id != null && id.equals(((Scrap) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Scrap{" +
                "id=" + id +
                ", appVer='" + appVer + '\'' +
                ", hostNm='" + hostNm + '\'' +
                ", errMsg='" + errMsg + '\'' +
                ", company='" + company + '\'' +
                ", svcCd='" + svcCd + '\'' +
                ", userId='" + userId + '\'' +
                ", workerResDt=" + workerResDt +
                ", workerReqDt=" + workerReqDt +
                '}';
    }
}
