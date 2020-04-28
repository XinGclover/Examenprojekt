package com.newscrawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.boot.builder.SpringApplicationBuilder;


@SpringBootApplication
public class NewscrawlerApplication extends SpringBootServletInitializer {
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(NewscrawlerApplication.class);
	}


	public static void main(String[] args) {
		SpringApplication.run(NewscrawlerApplication.class, args);
	}
}
