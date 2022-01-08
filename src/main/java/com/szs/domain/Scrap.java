package com.szs.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "tbl_scrap")
public class Scrap extends AbstractAuditingEntity implements Serializable {

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
    private Instant workerResDt;

    @Column(name = "worker_req_dt")
    private Instant workerReqDt;

    @ElementCollection
    @CollectionTable( name="tbl_scrap_001", joinColumns = @JoinColumn(name="scrap_id") )
    @MapKeyColumn(name="json_key")
    @Column(name="json_value")
    private Map<String, String> scrap001 = new HashMap<>();

    @ElementCollection
    @CollectionTable( name="tbl_scrap_002", joinColumns = @JoinColumn(name="scrap_id") )
    @MapKeyColumn(name="json_key")
    @Column(name="json_value")
    private Map<String, String> scrap002 = new HashMap<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppVer() {
        return appVer;
    }

    public void setAppVer(String appVer) {
        this.appVer = appVer;
    }

    public String getHostNm() {
        return hostNm;
    }

    public void setHostNm(String hostNm) {
        this.hostNm = hostNm;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getSvcCd() {
        return svcCd;
    }

    public void setSvcCd(String svcCd) {
        this.svcCd = svcCd;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Instant getWorkerResDt() {
        return workerResDt;
    }

    public void setWorkerResDt(Instant workerResDt) {
        this.workerResDt = workerResDt;
    }

    public Instant getWorkerReqDt() {
        return workerReqDt;
    }

    public void setWorkerReqDt(Instant workerReqDt) {
        this.workerReqDt = workerReqDt;
    }

    public Map<String, String> getScrap001() {
        return scrap001;
    }

    public void setScrap001(Map<String, String> scrap001) {
        this.scrap001 = scrap001;
    }

    public Map<String, String> getScrap002() {
        return scrap002;
    }

    public void setScrap002(Map<String, String> scrap002) {
        this.scrap002 = scrap002;
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
