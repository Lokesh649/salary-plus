package com.salaryplus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.salaryplus.entity.JobTrend;

@Repository
public interface JobTrendRepository extends JpaRepository<JobTrend, Long> {
}
