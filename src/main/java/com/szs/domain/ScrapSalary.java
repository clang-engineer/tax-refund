package com.szs.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "tbl_scrap_salary")
public class ScrapSalary implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "total", nullable = false)
    private Integer total;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "reg_no")
    private String regNo;

    @Column(name = "category")
    private String category;

    @Column(name = "company_no")
    private String companyNo;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "pay_date")
    private Date payDate;

    @ManyToOne(optional = false)
    @NotNull
    private Scrap scrap;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ScrapSalary id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ScrapSalary title(String title) {
        this.title = title;
        return this;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public ScrapSalary total(Integer total) {
        this.total = total;
        return this;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public ScrapSalary companyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ScrapSalary userName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public ScrapSalary regNo(String regNo) {
        this.regNo = regNo;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ScrapSalary category(String category) {
        this.category = category;
        return this;
    }

    public String getCompanyNo() {
        return companyNo;
    }

    public void setCompanyNo(String companyNo) {
        this.companyNo = companyNo;
    }

    public ScrapSalary companyNo(String companyNo) {
        this.companyNo = companyNo;
        return this;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public ScrapSalary startDate(Date startDate) {
        this.startDate = startDate;
        return this;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public ScrapSalary endDate(Date endDate) {
        this.endDate = endDate;
        return this;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public ScrapSalary payDate(Date payDate) {
        this.payDate = payDate;
        return this;
    }

    public Scrap getScrap() {
        return scrap;
    }

    public void setScrap(Scrap scrap) {
        this.scrap = scrap;
    }

    public ScrapSalary scrap(Scrap scrap) {
        this.scrap = scrap;
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
        return id != null && id.equals(((ScrapSalary) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "ScrapSalary{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", total=" + total +
                ", companyName='" + companyName + '\'' +
                ", userName='" + userName + '\'' +
                ", regNo='" + regNo + '\'' +
                ", category='" + category + '\'' +
                ", companyNo='" + companyNo + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", payDate=" + payDate +
                ", scrap=" + scrap +
                '}';
    }
}
