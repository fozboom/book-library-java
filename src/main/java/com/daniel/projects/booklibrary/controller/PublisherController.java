package com.daniel.projects.booklibrary.controller;


import com.daniel.projects.booklibrary.dto.publisher.response.PublisherResponseDTO;
import com.daniel.projects.booklibrary.dto.publisher.save.PublisherSaveDTO;
import com.daniel.projects.booklibrary.model.Publisher;
import com.daniel.projects.booklibrary.service.PublisherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/publishers")
@AllArgsConstructor
@Tag(name = "Publishers", description = "Interaction with publishers")
public class PublisherController {

	private final PublisherService service;

	@Operation(summary = "Get all publishers")
	@GetMapping("get")
	public List<PublisherResponseDTO> findAllPublishers() {

		return service.findAllPublishers();
	}

	@Operation(summary = "Save publisher")
	@PostMapping("save")
	public ResponseEntity<Publisher> addPublisher(@RequestBody final PublisherSaveDTO publisher) {

		Publisher savedPublisher = service.addPublisher(publisher);
		return new ResponseEntity<>(savedPublisher, HttpStatus.CREATED);
	}
	@Operation(summary = "Save collection of publishers")
	@PostMapping("saveCollection")
	public ResponseEntity<List<Publisher>> addAuthors(@Valid @RequestBody final List<PublisherSaveDTO> publisherNames) {
		List<Publisher> savedAuthors = service.addPublishers(publisherNames);
		return new ResponseEntity<>(savedAuthors, HttpStatus.CREATED);
	}

	@Operation(summary = "Get publisher by name")
	@GetMapping("find")
	public ResponseEntity<PublisherResponseDTO> findByName(@RequestParam final String name) {
		PublisherResponseDTO publisher = service.findByName(name);
		return ResponseEntity.ok(publisher);
	}

	@Operation(summary = "Get publisher by id")
	@GetMapping("findById")
	public ResponseEntity<PublisherResponseDTO> findPublisherById(@RequestParam final Long id) {
		PublisherResponseDTO publisher = service.findPublisherById(id);
		return ResponseEntity.ok(publisher);
	}

	@Operation(summary = "Update publisher")
	@PutMapping("update")
	ResponseEntity<String> updateBook(@RequestParam final Long id, @RequestParam final String name) {
		service.updatePublisherName(id, name);
		return new ResponseEntity<>("Publisher updated successfully", HttpStatus.OK);
	}

	@Operation(summary = "Delete publisher by name")
	@DeleteMapping("delete/{publisherName}")
	public ResponseEntity<String> deletePublisher(@PathVariable final String publisherName) {
		service.deletePublisherByName(publisherName);
		return new ResponseEntity<>("Publisher deleted successfully", HttpStatus.OK);
	}
}
