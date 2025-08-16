package com.example.bankapps;

import org.springframework.boot.SpringApplication;

public class TestBankAppsApplication {

	public static void main(String[] args) {
		SpringApplication.from(BankAppsApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
