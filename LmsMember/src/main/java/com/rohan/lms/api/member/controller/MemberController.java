package com.rohan.lms.api.member.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.rohan.lms.api.member.model.Gender;
import com.rohan.lms.api.member.model.Member;
import com.rohan.lms.api.member.repository.MemberRepository;
import com.rohan.lms.api.member.model.Error;

@RestController
@RequestMapping("/member")
public class MemberController {

	@Autowired
	private MemberRepository memberRepo;

	@Autowired
	private RestTemplate restTemplate;

	@GetMapping("/all")
	public ResponseEntity<List<Member>> getAllMembers() {

		List<Member> memberList = new ArrayList<>();
		memberRepo.findAll().forEach(memberList::add);

		return new ResponseEntity<>(memberList, HttpStatus.OK);
	}

	@GetMapping("/")
	public ResponseEntity<Member> getOneMember(@RequestParam String memberId) {

		Optional<Member> optionalMember = memberRepo.findById(Long.parseLong(memberId));

		if (!optionalMember.isPresent()) {
			String message = "Member with ID " + memberId + " not found in the database";
			return buildRestTemplate(message, "entity-not-found");
		}

		Member member = optionalMember.get();

		return new ResponseEntity<>(member, HttpStatus.OK);
	}

	@PostMapping("/add")
	public ResponseEntity<Member> addMember(@RequestBody Member member) {

		if (memberRepo.findByMemberEmail(member.getMemberEmail()) != null) {
			String meessage = "Member with Email " + member.getMemberEmail() + " already exists in the database";
			return buildRestTemplate(meessage, "entity-already-exists");
		}

		Member _member = memberRepo.save(new Member(member.getMemberName(), member.getMemberPhone(), member.getMemberEmail(),
				member.getMemberStreet(), member.getMemberCity(), member.getMemberGender(),
				new Timestamp(new Date().getTime())));

		return new ResponseEntity<>(_member, HttpStatus.CREATED);
	}
	
	@PutMapping("/update")
	public ResponseEntity<Member> updateMember(@RequestBody Map<String , Object> payload) {
		
		if (!payload.containsKey("memberId")) {
			String message = "Please provide a valid Member ID of the Member present in the database";
			return buildRestTemplate(message, "insufficient-data");
		}
		
		Optional<Member> optionalMember = memberRepo.findById(Long.parseLong((String) payload.get("memberId")));
		
		if (!optionalMember.isPresent()) {
			String message = "Member with ID " + payload.get("memberId") + " not found in the database";
			return buildRestTemplate(message, "entity-not-found");
		}
		
		Member member = optionalMember.get();
		
		for (Map.Entry<String, Object> entry : payload.entrySet()) {
			if(entry.getKey() == "memberId") {
				continue;
			}
			switch(entry.getKey()) {
			case "memberName":
				member.setMemberName((String) entry.getValue());
				break;
			case "memberPhone":
				member.setMemberPhone((String) entry.getValue());
				break;
			case "memberEmail":
				member.setMemberEmail((String) entry.getValue());
				break;
			case "memberStreet":
				member.setMemberStreet((String) entry.getValue());
				break;
			case "memberCity":
				member.setMemberCity((String) entry.getValue());
				break;
			case "memberGender":
				member.setMemberGender(Gender.valueOf((String) entry.getValue()));
				break;
			}
		}
		
		member.setUpdatedAt(new Timestamp(new Date().getTime()));
		
		Member _member = memberRepo.save(member);
		
		return new ResponseEntity<>(_member, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteMember(@RequestParam String memberId) {
		
		if (!memberRepo.findById(Long.parseLong(memberId)).isPresent()) {
			String message = "Member with ID " + memberId + " not found in the database";
			return buildRestTemplate(message, "entity-not-found");
		}
		
		memberRepo.deleteById(Long.parseLong(memberId));
		
		return new ResponseEntity<>("The Member was deleted successfully", HttpStatus.OK);
	}

//	Utility Functions
	public ResponseEntity buildRestTemplate(String message, String exception) {

		String url = "http://localhost:8082/exception-ws/" + exception + "?message=" + message;

		HttpHeaders httpHeaders = new HttpHeaders();
		HttpEntity<String> httpEntity = new HttpEntity<String>(httpHeaders);

		try {
			return restTemplate.exchange(url, HttpMethod.GET, httpEntity, ResponseEntity.class).getBody();
		} catch (HttpStatusCodeException e) {
			return ResponseEntity.status(e.getRawStatusCode()).body(e.getResponseBodyAs(Error.class));
		}

	}
}
