package com.rohan.lms.api.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rohan.lms.api.member.model.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	Member findByMemberEmail(String email);
}
