package com.hamza.journey;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.hamza.customer.Customer;
import com.hamza.customer.CustomerRegistrationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CustomerIntegrationTest {
    @Autowired
    private WebTestClient webTestClient;
    private final static String CUSTOMERS_URI = "/customers";
    @Test
    void canAddCustomer() {
        // create request for adding customer
        Faker faker = new Faker();
        Name fullName = faker.name();
        String name = fullName.firstName() + " " + fullName.lastName();
        String email = fullName.firstName() + "-" + fullName.lastName() + "@HOTFUCKINGMAIL.com";
        int age = 22;
        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(
                name,
                email,
                age
        );

        // sending post request to correct uri
        webTestClient.post()
                .uri("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(customerRegistrationRequest)
                .exchange()
                .expectStatus()
                .isOk();

        // getting all customers and making sure the customer was added
        List<Customer> customers = webTestClient.get()
                .uri("/customers")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult().getResponseBody();
        Customer expected =  customers.stream().filter(
                c -> c.getEmail().equals(email)
        ).findFirst().orElseThrow();
        assertThat(name).isEqualTo(expected.getName());
        assertThat(email).isEqualTo(expected.getEmail());
        assertThat(age).isEqualTo(expected.getAge());

        // retrieve the added customer by id
        webTestClient.get()
                .uri("/customers/{id}", expected.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {})
                .returnResult().equals(expected);
    }

    @Test
    void canDeleteCustomer() {
        // create request for adding customer
        Faker faker = new Faker();
        Name fullName = faker.name();
        String name = fullName.firstName() + " " + fullName.lastName();
        String email = fullName.firstName() + "-" + fullName.lastName() + "@HOTFUCKINGMAIL.com";
        int age = 22;
        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(
                name,
                email,
                age
        );

        // sending post request to correct uri
        webTestClient.post()
                .uri(CUSTOMERS_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(customerRegistrationRequest)
                .exchange()
                .expectStatus()
                .isOk();

        // getting all customers and making sure the customer was added
        List<Customer> customers = webTestClient.get()
                .uri(CUSTOMERS_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult().getResponseBody();
        Customer expected =  customers.stream().filter(
                c -> c.getEmail().equals(email)
        ).findFirst().orElseThrow();
        // delete saved customer
        webTestClient.delete()
                        .uri(CUSTOMERS_URI+"/{id}", expected.getId())
                                .accept(MediaType.APPLICATION_JSON)
                                        .exchange()
                                                .expectStatus()
                                                        .isOk();

        // retrieve the deleted customer by id
        webTestClient.get()
                .uri(CUSTOMERS_URI+"/{id}", expected.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void canUpdateCustomer() {
        // create request for adding customer
        Faker faker = new Faker();
        Name fullName = faker.name();
        String name = fullName.firstName() + " " + fullName.lastName();
        String email = fullName.firstName() + "-" + fullName.lastName() + "@HOTFUCKINGMAIL.com";
        int age = 22;
        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(
                name,
                email,
                age
        );
        // sending post request to correct uri
        webTestClient.post()
                .uri(CUSTOMERS_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(customerRegistrationRequest)
                .exchange()
                .expectStatus()
                .isOk();
        // retrieving all customers
        List<Customer> customers = webTestClient.get()
                .uri(CUSTOMERS_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult().getResponseBody();
        // filtering customers to get saved customer id
        Long id = customers.stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst().orElseThrow();
        // creating update request
        CustomerRegistrationRequest updateRequest = new CustomerRegistrationRequest(
                null,
                UUID.randomUUID() + "-" + faker.internet().emailAddress() ,
                null
        );
        // sending put request

        webTestClient.put()
                .uri(CUSTOMERS_URI+"/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();
        // retrieve updated customer
        Customer actual = webTestClient.get()
                .uri(CUSTOMERS_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult().getResponseBody();
        // assert
        assertThat(actual.getId()).isEqualTo(id);
        assertThat(actual.getName()).isEqualTo(name);
        assertThat(actual.getEmail()).isEqualTo(updateRequest.email());
        assertThat(actual.getAge()).isEqualTo(age);

    }
}
