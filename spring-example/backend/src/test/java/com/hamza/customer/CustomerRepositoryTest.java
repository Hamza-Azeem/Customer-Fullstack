package com.hamza.customer;

import com.hamza.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestcontainers {
    @Autowired
    private CustomerRepository customerRepository;
    
    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
    }

    @Test
    void existsByEmail() {
        // Arrange
        String name = faker.name().fullName();
        String email = faker.internet().emailAddress();
        Customer customer = new Customer(name, email, 22);
        customerRepository.save(customer);
        // Act
        boolean actual = customerRepository.existsByEmail(email);
        // Assert
        assertThat(actual).isTrue();
    }

    @Test
    void existsByEmailWillReturnFalseWithInvalidEmail() {
        // Arrange
        String email = faker.internet().emailAddress();
        // Act
        boolean actual = customerRepository.existsByEmail(email);
        // Assert
        assertThat(actual).isFalse();
    }
}