package com.szs.repository;

import com.szs.domain.ScrapTax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScrapTaxRepository extends JpaRepository<ScrapTax, Long> {
    List<ScrapTax> findAllByScrapId(Long scrapId);
}
