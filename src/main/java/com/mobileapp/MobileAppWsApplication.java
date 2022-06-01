package com.mobileapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.mobileapp.security.AppProperties;

@EnableWebMvc
@SpringBootApplication //(scanBasePackages = {"com.mobileapp.ui.controller"})
public class MobileAppWsApplication extends SpringBootServletInitializer {
	
	@Override
	protected SpringApplicationBuilder configure (SpringApplicationBuilder application) {
		return application.sources(MobileAppWsApplication.class);
		
	}
		
	public static void main(String[] args) {
		SpringApplication.run(MobileAppWsApplication.class, args);
	}
	
	@Bean 
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SpringApplicationContext springApplicationContext(){
		return new SpringApplicationContext();
	}
	
	@Bean(name="AppProperties")
	public AppProperties getAppProperties()
	{
		return new AppProperties();
	}

}
