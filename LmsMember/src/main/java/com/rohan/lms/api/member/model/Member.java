package com.rohan.lms.api.member.model;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name = "mem_seq", initialValue = 1, allocationSize = 1)
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mem_seq")
	private Long memberId;

	private String memberName;

	private String memberPhone;

	@Column(unique = true)
	private String memberEmail;

	private String memberStreet;

	private String memberCity;

	private Gender memberGender;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Timestamp joinedAt;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Timestamp updatedAt;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Timestamp leftAt;

	public Member() {
		super();
	}

	public Member(String memberName, String memberPhone, String memberEmail, String memberStreet, String memberCity,
			Gender memberGender, Timestamp joinedAt) {
		super();
		this.memberName = memberName;
		this.memberPhone = memberPhone;
		this.memberEmail = memberEmail;
		this.memberStreet = memberStreet;
		this.memberCity = memberCity;
		this.memberGender = memberGender;
		this.joinedAt = joinedAt;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getMemberPhone() {
		return memberPhone;
	}

	public void setMemberPhone(String memberPhone) {
		this.memberPhone = memberPhone;
	}

	public String getMemberEmail() {
		return memberEmail;
	}

	public void setMemberEmail(String memberEmail) {
		this.memberEmail = memberEmail;
	}

	public String getMemberStreet() {
		return memberStreet;
	}

	public void setMemberStreet(String memberStreet) {
		this.memberStreet = memberStreet;
	}

	public String getMemberCity() {
		return memberCity;
	}

	public void setMemberCity(String memberCity) {
		this.memberCity = memberCity;
	}

	public Gender getMemberGender() {
		return memberGender;
	}

	public void setMemberGender(Gender memberGender) {
		this.memberGender = memberGender;
	}

	public Timestamp getJoinedAt() {
		return joinedAt;
	}

	public void setJoinedAt(Timestamp joinedAt) {
		this.joinedAt = joinedAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Timestamp getLeftAt() {
		return leftAt;
	}

	public void setLeftAt(Timestamp leftAt) {
		this.leftAt = leftAt;
	}

}
