package com.eryka.cadoc3044;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Cadoc3044Application {

	public static void main(String[] args) {
		// Inicia o contexto do Spring Boot
		ConfigurableApplicationContext context = SpringApplication.run(Cadoc3044Application.class, args);
		context.close();
	}

}
