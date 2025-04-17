package com.example.controller;

import java.util.List;
import java.util.Optional;

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
import com.example.entity.Student;
import com.example.repository.CollegeRepository;
import com.example.repository.StudentRepository;

@RestController
@RequestMapping("/students")
public class StudentController {
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private CollegeRepository collegeRepository;
	
	@GetMapping
	public ResponseEntity<List<Student>> getAllColeges(){
		return ResponseEntity.ok(studentRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Student> getStudentById(@PathVariable Long id){
		return studentRepository.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
	
	@PostMapping("/college/{collegeId}")
	public ResponseEntity<Student> createCollge(@PathVariable Long collegeId, @RequestBody Student student){
Optional<College> collegeOptional = collegeRepository.findById(collegeId);
        
        if (collegeOptional.isPresent()) {
            College college = collegeOptional.get();
            student.setCollege(college);
            Student savedStudent = studentRepository.save(student);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedStudent);
        } else {
            return ResponseEntity.notFound().build();
        }
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student updated){
		return studentRepository.findById(id)
				.map(existingStudent ->{
				existingStudent.setStudentName(updated.getStudentName());
				existingStudent.setAge(updated.getAge());
				return ResponseEntity.ok(studentRepository.save(existingStudent));
				}).orElse(ResponseEntity.notFound().build());
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable Long id){
		if(studentRepository.existsById(id)) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}
}
