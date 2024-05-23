package com.hamza;

import com.github.javafaker.Faker;
import com.hamza.customer.Customer;
import com.hamza.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
    @Bean
    public CommandLineRunner commandLineRunner(CustomerRepository customerRepository){
        return args -> {
            Faker faker = new Faker();
            String firstName = faker.name().firstName().toLowerCase();
            String lastName = faker.name().lastName().toLowerCase();
            String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@gmail.com";
            firstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1);
            lastName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1);
            int age = faker.number().randomDigit();
            Customer customer = new Customer(
                    firstName+" "+lastName,
                    email,
                    age
            );
            customerRepository.save(customer);
        };
    }
}
