package com.xyl.mmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration(exclude=RabbitAutoConfiguration.class)
@ComponentScan
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
