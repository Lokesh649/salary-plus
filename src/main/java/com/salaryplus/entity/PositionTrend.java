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
@Table(name="positiontrend")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PositionTrend {
	
 	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	private String position;
	private String location;
	private String gender;
	private String education;
	private int experience;
	private double salary;

}
