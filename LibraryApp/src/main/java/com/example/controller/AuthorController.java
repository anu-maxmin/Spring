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

import com.example.entity.Author;
import com.example.repo.AuthorRepository;

@RestController
@RequestMapping("/author")
public class AuthorController {
	@Autowired
	private AuthorRepository authorRepository;

	@GetMapping
	public ResponseEntity<List<Author>> getAllAuthor() {
		return ResponseEntity.ok(authorRepository.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Author> getAuthorById(@PathVariable Long id) {
		return authorRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Author> createAuthor(@RequestBody Author author) {
		Author saveAuthor = authorRepository.save(author);
		return ResponseEntity.status(HttpStatus.CREATED).body(saveAuthor);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Author> updateAuthor(@PathVariable Long id, @RequestBody Author updated) {
		return authorRepository.findById(id).map(existingAuthor -> {
			existingAuthor.setName(updated.getName());
			existingAuthor.setBooks(updated.getBooks());
			return ResponseEntity.ok(authorRepository.save(existingAuthor));
		}).orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable Long id) {
		if (authorRepository.existsById(id)) {
			authorRepository.deleteById(id);
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();

	}
}
