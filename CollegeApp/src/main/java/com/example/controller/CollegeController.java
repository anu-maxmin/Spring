package com.example.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.College;
import com.example.repository.CollegeRepository;

@RestController
@RequestMapping("/colleges")
public class CollegeController {
	
	@Autowired
	private CollegeRepository collegeRepository;
	
	@GetMapping
	public ResponseEntity<List<College>> getAllColeges(){
		return ResponseEntity.ok(collegeRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<College> getCollegeById(@PathVariable Long id){
		return collegeRepository.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
	
	@PostMapping
	public ResponseEntity<College> createCollge(@RequestBody College college){
		College savesColleges = collegeRepository.save(college);
		return ResponseEntity.status(HttpStatus.CREATED).body(savesColleges);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<College> updateCollege(@PathVariable Long id, @RequestBody College updated){
		return collegeRepository.findById(id)
				.map(existingCollege ->{
				existingCollege.setCollegeName(updated.getCollegeName());
				existingCollege.setAddress(updated.getAddress());
				existingCollege.setStudent(updated.getStudent());
				return ResponseEntity.ok(collegeRepository.save(existingCollege));
				}).orElse(ResponseEntity.notFound().build());
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable Long id){
		if(collegeRepository.existsById(id)) {
			collegeRepository.deleteById(id);
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}
}
