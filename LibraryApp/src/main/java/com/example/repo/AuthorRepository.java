package com.example.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long>{

}
