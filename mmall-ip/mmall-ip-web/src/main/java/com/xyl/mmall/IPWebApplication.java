package com.xyl.mmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@EnableAutoConfiguration
public class IPWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(IPWebApplication.class, args);
	}

}
