package com.hamza.customer;

import com.hamza.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;



class CustomerJDBCDataAccessServiceTest extends AbstractTestcontainers {
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();
    private CustomerJDBCDataAccessService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(
                getJdbcTemplate(),
                customerRowMapper
        );
    }

    @Test
    void selectAllCustomers() {
        // Arrange
        Customer customer = new Customer(
                faker.name().firstName(),
                faker.internet().emailAddress()+"-"+ UUID.randomUUID(),
                22
        );
        underTest.addCustomer(customer);
        // Act
        List<Customer> actual = underTest.selectAllCustomers();
        // Assert
        assertThat(actual).isNotEmpty();
        assertThat(actual.size()).isEqualTo(1);
    }

    @Test
    void selectCustomerById() {
        // Arrange
        String email = faker.internet().emailAddress() + "-" + UUID.randomUUID();
        String name = faker.name().firstName();
        Customer customer = new Customer(
                name,
                email,
                22
        );
        underTest.addCustomer(customer);
        long id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .findFirst()
                .map(Customer::getId)
                .orElseThrow();
        // Act
        Optional<Customer> actual = underTest.selectCustomerById(id);
        // Assert
        assertThat(actual).isPresent();
        assertThat(actual.get().getId()).isEqualTo(id);
        assertThat(actual.get().getName()).isEqualTo(name);
        assertThat(actual.get().getEmail()).isEqualTo(email);
        assertThat(actual.get().getAge()).isEqualTo(22);

    }

    @Test
    void WillReturnEmptySelectCustomerById() {
        // Arrange
        long id = -1;
        // Act
        var actual = underTest.selectCustomerById(id);
        // Assert
        assertThat(actual).isEmpty();
    }

    @Test
    void addCustomer() {
        // Arrange
        String email = faker.internet().emailAddress() + "-" + UUID.randomUUID();
        String name = faker.name().firstName();
        Customer customer = new Customer(
                name,
                email,
                22
        );
        // Act
        underTest.addCustomer(customer);
        long id = underTest.selectAllCustomers()
                .stream().filter(c -> c.getEmail().equals(email))
                .findFirst()
                .map(Customer::getId)
                .orElseThrow();
        Optional<Customer> actual = underTest.selectCustomerById(id);
        // Assert
        assertThat(actual).isNotEmpty();
        assertThat(actual.get().getId()).isEqualTo(id);
        assertThat(actual.get().getEmail()).isEqualTo(email);
        assertThat(actual.get().getAge()).isEqualTo(22);
        assertThat(actual.get().getName()).isEqualTo(name);
    }

    @Test
    void customerExistsByEmail() {
        // Arrange
        String email = faker.internet().emailAddress() + "-" + UUID.randomUUID();
        String name = faker.name().firstName();
        Customer customer = new Customer(
                name,
                email,
                22
        );
        underTest.addCustomer(customer);
        // Act
        Boolean actual = underTest.customerExistsByEmail(email);
        // Assert
        assertThat(actual).isTrue();
    }

    @Test
    void WillReturnFalseCustomerExistsByEmail() {
        // Arrange
        String email = "rubbish@hotmail.com";
        // Act
        Boolean actual = underTest.customerExistsByEmail(email);
        // Assert
        assertThat(actual).isFalse();

    }

    @Test
    void removeCustomerById() {
        // Arrange
        String email = faker.internet().emailAddress() + "-" + UUID.randomUUID();
        String name = faker.name().firstName();
        Customer customer = new Customer(
                name,
                email,
                22
        );
        underTest.addCustomer(customer);
        long id = underTest.selectAllCustomers()
                .stream().filter(c -> c.getEmail().equals(email))
                .findFirst()
                .map(Customer::getId)
                .orElseThrow();
        // Act
        underTest.removeCustomerById(id);
        // Assert
        assertThat(underTest.selectCustomerById(id)).isEmpty();
    }

    @Test
    void WillReturnExceptionRemoveCustomerById() {
        // Arrange
        long id = -1;
        // Act
//        underTest.removeCustomerById(id);
        // Assert

    }

    @Test
    void updateCustomer() {
        // Arrange
        String email = faker.internet().emailAddress() + "-" + UUID.randomUUID();
        String name = faker.name().firstName();
        Customer customer = new Customer(
                name,
                email,
                22
        );
        underTest.addCustomer(customer);
        long id = underTest.selectAllCustomers()
                .stream().filter(c -> c.getEmail().equals(email))
                .findFirst()
                .map(Customer::getId)
                .orElseThrow();
        // Act
        underTest.updateCustomer(new Customer(
                id,
                "hamza",
                "updated@gmail.com",
                23
        ));
        Customer actual = underTest.selectCustomerById(id).get();
        // Assert
        assertThat(actual.getName()).isEqualTo("hamza");
        assertThat(actual.getEmail()).isEqualTo("updated@gmail.com");
        assertThat(actual.getAge()).isEqualTo(23);
    }
}