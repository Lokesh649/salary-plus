package com.salaryplus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.salaryplus.entity.PositionTrend;

@Repository
public interface PositionTrendRepository  extends JpaRepository<PositionTrend, Long>{

}
