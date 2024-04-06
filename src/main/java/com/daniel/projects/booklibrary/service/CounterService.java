package com.daniel.projects.booklibrary.service;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CounterService {
	private AtomicInteger counter = new AtomicInteger(0);

	public synchronized void increment() {
		counter.incrementAndGet();
	}

	public synchronized int get() {
		return counter.get();
	}
}
