package com.daniel.projects.booklibrary.controller;

import com.daniel.projects.booklibrary.dto.publisher.response.PublisherResponseDTO;
import com.daniel.projects.booklibrary.dto.publisher.save.PublisherSaveDTO;
import com.daniel.projects.booklibrary.model.Fields;
import com.daniel.projects.booklibrary.model.Publisher;
import com.daniel.projects.booklibrary.service.PublisherService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/publishers")
@AllArgsConstructor
public class PublisherController {

	private final PublisherService service;


	@GetMapping("get")
	public List<PublisherResponseDTO> findAllPublishers() {

		return service.findAllPublishers();
	}

	@PostMapping("save")
	public ResponseEntity<String> addPublisher(@RequestBody PublisherSaveDTO publisher) {

		Optional<Publisher> savedPublisher = service.addPublisher(publisher);
		if (savedPublisher.isPresent()) {
			return new ResponseEntity<>(Fields.SUCCESS_MESSAGE, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>("Publisher with this title already exists", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("find")
	public ResponseEntity<PublisherResponseDTO> findByName(@RequestParam String name) {
		PublisherResponseDTO publisher = service.findByName(name);
		if (publisher == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(publisher);
	}

	@GetMapping("findById")
	public ResponseEntity<PublisherResponseDTO> findPublisherById(@RequestParam Long id) {
		PublisherResponseDTO publisher = service.findPublisherById(id);
		if (publisher == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(publisher);
	}

	@PutMapping("update")
	ResponseEntity<String> updateBook(@RequestParam Long id, @RequestParam String name) {
		boolean updated = service.updatePublisherName(id, name);
		if (updated) {
			return new ResponseEntity<>(Fields.SUCCESS_MESSAGE, HttpStatus.OK);
		}
		return new ResponseEntity<>("Publisher to update not found", HttpStatus.NOT_FOUND);
	}

	@DeleteMapping("delete/{publisherName}")
	public ResponseEntity<String> deletePublisher(@PathVariable String publisherName) {
		if (service.deletePublisherByName(publisherName)) {
			return new ResponseEntity<>(Fields.SUCCESS_MESSAGE, HttpStatus.OK);
		}
		return new ResponseEntity<>("Publisher not found", HttpStatus.NOT_FOUND);
	}
}