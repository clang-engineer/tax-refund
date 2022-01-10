package com.szs.repository;

import com.szs.domain.ScrapTax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScrapTaxRepository extends JpaRepository<ScrapTax, Long> {
}
