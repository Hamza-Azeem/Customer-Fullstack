package com.hamza.customer;

import com.github.javafaker.Faker;
import com.hamza.AbstractTestcontainers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;


class CustomerJPADataAccessServiceTest{
    private CustomerJPADataAccessService underTest;
    @Mock
    private CustomerRepository customerRepository;
    private AutoCloseable autoCloseable;
    private Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJPADataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {
        // Act
        underTest.selectAllCustomers();
        // Assert
        verify(customerRepository).findAll();
    }

    @Test
    void selectCustomerById() {
        // Arrange
        long id = 1;
        // Act
        underTest.selectCustomerById(id);
        // Assert
        verify(customerRepository).findById(id);
    }

    @Test
    void addCustomer() {
        // Arrange
        Customer customer = new Customer(
                faker.name().firstName(),
                faker.internet().emailAddress(),
                22
        );
        // Act
        underTest.addCustomer(customer);
        // Assert
        verify(customerRepository).save(customer);
    }

    @Test
    void customerExistsByEmail() {
        // Arrange
        String email = faker.internet().emailAddress();
        // Act
        underTest.customerExistsByEmail(email);
        // Assert
        verify(customerRepository).existsByEmail(email);
    }

    @Test
    void removeCustomerById() {
        // Arrange
        long id = faker.random().nextLong();
        // Act
        underTest.removeCustomerById(id);
        // Assert
        verify(customerRepository).deleteById(id);
    }

    @Test
    void updateCustomer() {
        // Arrange
        Customer customer = new Customer(
                faker.random().nextLong(),
                faker.name().firstName(),
                faker.internet().emailAddress(),
                22
        );
        // Act
        underTest.updateCustomer(customer);
        // Assert
        verify(customerRepository).save(customer);
    }
}