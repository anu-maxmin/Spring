package com.example.entity;

import java.util.ArrayList;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class College {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String collegeName;
	private String address;
	
	@OneToMany(mappedBy = "college", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<Student> student = new ArrayList<>();
	
	public College() {
		// TODO Auto-generated constructor stub
	}
	public College(Long id, String collegeName, String address, List<Student> student) {
		super();
		this.id = id;
		this.collegeName = collegeName;
		this.address = address;
		this.student = student;
	}

	public Long getId() {
		return id;
	}
	
	public String getCollegeName() {
		return collegeName;
	}
	public void setCollegeName(String collegeName) {
		this.collegeName = collegeName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public List<Student> getStudent() {
		return student;
	}
	public void setStudent(List<Student> student) {
		this.student = student;
	}
	@Override
	public String toString() {
		return "College [id=" + id + ", collegeName=" + collegeName + ", address=" + address + ", student=" + student
				+ "]";
	}
	
	
	
	
}
