package com.rohan.lms.employees.model;

import java.sql.Timestamp;

//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name = "emp_seq", initialValue = 1, allocationSize = 1)
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "emp_seq")
	private Long employeeId;

	private String employeeName;

	private String employeeStreet;

	private String employeeCity;

	private String employeeSalary;

	@Column(unique = true)
	private String employeeEmail;

	private Gender employeeGender;
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
	private Timestamp joinedAt;
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
	private Timestamp updateAt;

	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
	private Timestamp leftAt;

	public Employee() {
	}

	public Employee(String employeeName, String employeeStreet, String employeeCity, String employeeSalary,
			String employeeEmail, Gender employeeGender, Timestamp joinedAt) {
		super();
		this.employeeName = employeeName;
		this.employeeStreet = employeeStreet;
		this.employeeCity = employeeCity;
		this.employeeSalary = employeeSalary;
		this.employeeEmail = employeeEmail;
		this.employeeGender = employeeGender;
		this.joinedAt = joinedAt;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmployeeStreet() {
		return employeeStreet;
	}

	public void setEmployeeStreet(String employeeStreet) {
		this.employeeStreet = employeeStreet;
	}

	public String getEmployeeCity() {
		return employeeCity;
	}

	public void setEmployeeCity(String employeeCity) {
		this.employeeCity = employeeCity;
	}

	public String getEmployeeSalary() {
		return employeeSalary;
	}

	public void setEmployeeSalary(String employeeSalary) {
		this.employeeSalary = employeeSalary;
	}

	public String getEmployeeEmail() {
		return employeeEmail;
	}

	public void setEmployeeEmail(String employeeEmail) {
		this.employeeEmail = employeeEmail;
	}

	public Gender getEmployeeGender() {
		return employeeGender;
	}

	public void setEmployeeGender(Gender employeeGender) {
		this.employeeGender = employeeGender;
	}

	public Timestamp getJoinedAt() {
		return joinedAt;
	}

	public void setJoinedAt(Timestamp joinedAt) {
		this.joinedAt = joinedAt;
	}

	public Timestamp getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Timestamp updateAt) {
		this.updateAt = updateAt;
	}

	public Timestamp getLeftAt() {
		return leftAt;
	}

	public void setLeftAt(Timestamp leftAt) {
		this.leftAt = leftAt;
	}
	
	

}
