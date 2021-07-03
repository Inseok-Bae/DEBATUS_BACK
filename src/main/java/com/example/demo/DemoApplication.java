package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.demo.jwt.JwtInterceptor;

@SpringBootApplication
public class DemoApplication implements WebMvcConfigurer{

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	

	@Autowired
	private JwtInterceptor jwtInterceptor;
	
	@Override
	public void addInterceptors (InterceptorRegistry registry) {
		registry.addInterceptor(jwtInterceptor).addPathPatterns("/api/required/**");
//												.excludePathPatterns("/api/login/**")
												
	}
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("*")
				.allowedMethods("*")
				.allowedHeaders("*")
				.exposedHeaders("jwt-auth-token");
	}
	
}
