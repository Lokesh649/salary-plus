package com.salaryplus.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="jobtrend")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobTrend {

	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    private String role;
	    private String company;
	    private String location;
	    private String experience;
	    private String skills;
	    // Scale of demand (e.g., 1-10)
	
}
