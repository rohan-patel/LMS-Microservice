package com.rohan.lms.employees.controllers;

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

import com.rohan.lms.employees.model.Employee;
import com.rohan.lms.employees.model.Error;
import com.rohan.lms.employees.model.Gender;
import com.rohan.lms.employees.repository.EmployeeRepository;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

	@Autowired
	private EmployeeRepository empRepo;

	@Autowired
	private RestTemplate restTemplate;

	@GetMapping("all")
	public ResponseEntity<List<Employee>> getAllEMployees() {

		List<Employee> empList = new ArrayList<>();
		empRepo.findAll().forEach(empList::add);

		return new ResponseEntity<>(empList, HttpStatus.OK);
	}

	@GetMapping("/")
	public ResponseEntity<Employee> getOneEmployee(@RequestParam String employeeId) {

		Optional<Employee> optionalEmployee = empRepo.findById(Long.parseLong(employeeId));

		if (!optionalEmployee.isPresent()) {
			String message = "Employee with ID " + employeeId + " not found in the database";
			return buildRestTemplate(message, "entity-not-found");
		}

		Employee employee = optionalEmployee.get();

		return new ResponseEntity<>(employee, HttpStatus.OK);
	}

	@PostMapping("/add")
	public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) {

		if (empRepo.findByEmployeeEmail(employee.getEmployeeEmail()) != null) {
			String meessage = "Employee with Email " + employee.getEmployeeEmail() + " already exists in the database";
			return buildRestTemplate(meessage, "entity-already-exists");
		}

		Employee _emp = empRepo.save(new Employee(employee.getEmployeeName(), employee.getEmployeeStreet(),
				employee.getEmployeeCity(), employee.getEmployeeSalary(), employee.getEmployeeEmail(),
				employee.getEmployeeGender(), new Timestamp(new Date().getTime())));

		return new ResponseEntity<>(_emp, HttpStatus.CREATED);
	}

	@PutMapping("/update")
	public ResponseEntity<Employee> updateEmployee(@RequestBody Map<String, Object> payload) {

		if (!payload.containsKey("employeeId")) {
			String message = "Please provide a valid Employee ID of the Employee present in the database";
			return buildRestTemplate(message, "insufficient-data");
		}

		Optional<Employee> optionalEmployee = empRepo.findById(Long.parseLong((String) payload.get("employeeId")));

		if (!optionalEmployee.isPresent()) {
			String message = "Employee with ID " + payload.get("employeeId") + " not found in the database";
			return buildRestTemplate(message, "entity-not-found");
		}
		
		Employee employee = optionalEmployee.get();
		
		for (Map.Entry<String, Object> entry : payload.entrySet()) {
			if (entry.getKey() != "employeeId") {
				continue;
			}
			switch (entry.getKey()) {
			case "employeeName":
				employee.setEmployeeName((String) entry.getValue());
				break;
			case "employeeStreet":
				employee.setEmployeeStreet((String) entry.getValue());
				break;
			case "employeeCity":
				employee.setEmployeeCity((String) entry.getValue());
				break;
			case "employeeSalary":
				employee.setEmployeeSalary((String) entry.getValue());
				break;
			case "employeeGender":
				employee.setEmployeeGender(Gender.valueOf((String) entry.getValue()));
				break;
			}
		}
		
		employee.setUpdateAt(new Timestamp(new Date().getTime()));
		
		Employee _emp = empRepo.save(employee);

		return new ResponseEntity<>(_emp, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteEmployee (@RequestParam String employeeId) {
		
		if (!empRepo.findById(Long.parseLong(employeeId)).isPresent()) {
			String message = "Employee with ID " + employeeId + " not found in the database";
			return buildRestTemplate(message, "entity-not-found");
		}
		
		empRepo.deleteById(Long.parseLong(employeeId));
		
		return new ResponseEntity<>("The Employee was removced successfully", HttpStatus.OK);
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
