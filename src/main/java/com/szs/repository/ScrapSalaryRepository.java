package com.szs.repository;

import com.szs.domain.ScrapSalary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScrapSalaryRepository extends JpaRepository<ScrapSalary, Long> {
    List<ScrapSalary> findAllByScrapId(Long scrapId);
}
