package com.xyl.mmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;


/**
 * @author Yang,Nan
 *
 */
@EnableAutoConfiguration(exclude=RabbitAutoConfiguration.class)
@ComponentScan
public class App 
{
 
    public static void main(String[] args) {
    	int i = 0;
    	System.out.println(i++);
    	
        SpringApplication.run(App.class, args);
    }
}
