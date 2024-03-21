package com.daniel.projects.booklibrary.controller;

import com.daniel.projects.booklibrary.dto.publisher.response.PublisherResponseDTO;
import com.daniel.projects.booklibrary.dto.publisher.save.PublisherSaveDTO;
import com.daniel.projects.booklibrary.model.Fields;
import com.daniel.projects.booklibrary.model.Publisher;
import com.daniel.projects.booklibrary.service.PublisherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
	public ResponseEntity<String> addPublisher(
			@RequestBody final PublisherSaveDTO publisher) {

		Optional<Publisher> savedPublisher =
				service.addPublisher(publisher);
		if (savedPublisher.isPresent()) {
			return new ResponseEntity<>(
					Fields.SUCCESS_MESSAGE,
					HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(
					"Publisher with this title "
							+ "already exists",
					HttpStatus.BAD_REQUEST);
		}
	}

	@Operation(summary = "Get publisher by name")
	@GetMapping("find")
	public ResponseEntity<PublisherResponseDTO> findByName(
			@RequestParam final String name) {
		PublisherResponseDTO publisher = service.findByName(name);
		if (publisher == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(publisher);
	}

	@Operation(summary = "Get publisher by id")
	@GetMapping("findById")
	public ResponseEntity<PublisherResponseDTO> findPublisherById(
			@RequestParam final Long id) {
		PublisherResponseDTO publisher = service.findPublisherById(id);
		if (publisher == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(publisher);
	}

	@Operation(summary = "Update publisher")
	@PutMapping("update")
	ResponseEntity<String> updateBook(
			@RequestParam final Long id,
			@RequestParam final String name) {
		boolean updated = service.updatePublisherName(id, name);
		if (updated) {
			return new ResponseEntity<>(
					Fields.SUCCESS_MESSAGE, HttpStatus.OK);
		}
		return new ResponseEntity<>(
				"Publisher to update not found",
				HttpStatus.NOT_FOUND);
	}

	@Operation(summary = "Delete publisher by name")
	@DeleteMapping("delete/{publisherName}")
	public ResponseEntity<String> deletePublisher(
			@PathVariable final String publisherName) {
		if (service.deletePublisherByName(publisherName)) {
			return new ResponseEntity<>(
					Fields.SUCCESS_MESSAGE, HttpStatus.OK);
		}
		return new ResponseEntity<>(
				"Publisher not found", HttpStatus.NOT_FOUND);
	}
}
