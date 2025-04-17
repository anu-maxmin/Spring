package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.College;

@Repository
public interface CollegeRepository extends JpaRepository<College, Long> {

}
