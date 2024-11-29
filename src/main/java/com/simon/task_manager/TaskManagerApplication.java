package com.simon.task_manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaskManagerApplication {

	private static final Logger log = LoggerFactory.getLogger(TaskManagerApplication.class);
	public static void main(String[] args) {		
		SpringApplication.run(TaskManagerApplication.class, args);
		log.info("Welcome, I'm alive!");
	}

}
