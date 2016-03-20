package com.xyl.mmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * server start
 * 
 */
@EnableAutoConfiguration
@ComponentScan
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
