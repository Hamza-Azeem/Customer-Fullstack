package com.hamza.customer;

public record CustomerRegistrationRequest(
        String name,
        String email,
        Integer age
){
}
