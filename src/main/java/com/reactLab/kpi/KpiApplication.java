package com.reactLab.kpi;

import com.reactLab.kpi.hello.GreetingClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class KpiApplication {

	public static void main(String[] args) {
		SpringApplication.run(KpiApplication.class, args);
	}

}
