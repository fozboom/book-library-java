package com.daniel.projects.booklibrary.controller;

import com.daniel.projects.booklibrary.model.Publisher;
import com.daniel.projects.booklibrary.service.PublisherService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/publisher")
@AllArgsConstructor
public class PublisherController {

	private final PublisherService service;

	@GetMapping("get")
	public List<Publisher> findAllPublishers() {
		return service.findAllPublishers();
	}

	@PostMapping("save")
	public Publisher addPublisher(@RequestBody Publisher publisher) {
		return service.addPublisher(publisher);
	}

	@GetMapping("find")
	public Publisher findByName(@RequestParam String publisherName) {
		return service.findByName(publisherName);
	}

	@DeleteMapping("delete/{publisherName}")
	public String deletePublisher(@PathVariable String publisherName) {
		return service.deletePublisher(publisherName);
	}
}