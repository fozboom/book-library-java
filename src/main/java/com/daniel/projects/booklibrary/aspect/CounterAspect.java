package com.daniel.projects.booklibrary.aspect;

import com.daniel.projects.booklibrary.service.CounterService;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CounterAspect {
	private final CounterService counterService;
	private static final Logger LOGGER = LoggerFactory.getLogger(CounterAspect.class);


	public CounterAspect(CounterService counterService) {
		this.counterService = counterService;
	}


	@Before("execution(* com.daniel.projects.booklibrary.controller.*.*(..)) ")
	public void incrementCounter() {
		counterService.increment();
		LOGGER.info("Counter incremented to: " + counterService.get());
	}

}
