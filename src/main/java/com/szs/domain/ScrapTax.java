package com.szs.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "tbl_scrap_tax")
public class ScrapTax implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "total", nullable = false)
    private Integer total;

    @ManyToOne(optional = false)
    @NotNull
    private Scrap scrap;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ScrapTax id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ScrapTax title(String title) {
        this.title = title;
        return this;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public ScrapTax total(Integer total) {
        this.total = total;
        return this;
    }

    public Scrap getScrap() {
        return scrap;
    }

    public void setScrap(Scrap scrap) {
        this.scrap = scrap;
    }

    public ScrapTax scrap(Scrap scrap) {
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
        return id != null && id.equals(((ScrapTax) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "ScrapTax{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", total=" + total +
                '}';
    }
}
