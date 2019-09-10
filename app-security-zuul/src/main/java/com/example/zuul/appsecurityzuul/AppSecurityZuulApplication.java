package com.example.zuul.appsecurityzuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableZuulProxy
@EnableFeignClients(basePackages = {"com.example.zuul"})
@SpringBootApplication(scanBasePackages="com.example.zuul")
public class AppSecurityZuulApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppSecurityZuulApplication.class, args);
	}

}
