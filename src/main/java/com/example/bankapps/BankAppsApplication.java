package com.example.bankapps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class BankAppsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankAppsApplication.class, args);
	}

}
