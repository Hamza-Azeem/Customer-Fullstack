package com.hamza.customer;

import com.hamza.exception.DuplicateObjectException;
import com.hamza.exception.NoUpdatesException;
import com.hamza.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    private CustomerService underTest;
    @Mock
    private CustomerDao customerDao;
    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);
    }

    @Test
    void getAllCustomers() {
        // Act
        underTest.getAllCustomers();
        // Assert
        verify(customerDao).selectAllCustomers();
    }

    @Test
    void getCustomer() {
        // Arrange
        long id = 10;
        Customer customer = new Customer(
                id, "hamza", "hamza@gmail.com", 22
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        // Act
        Customer actual = underTest.getCustomer(10);
        // Assert
        assertThat(actual.getId()).isEqualTo(id);
        assertThat(actual.getName()).isEqualTo("hamza");
        assertThat(actual.getEmail()).isEqualTo("hamza@gmail.com");
        assertThat(actual.getAge()).isEqualTo(22);
    }

    @Test
    void wilThrowExceptionWhenGetCustomerInvokedWithWrongId() {
        // Arrange
        long id = 10;
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());
        // Act
        // Assert
        assertThatThrownBy(() -> underTest.getCustomer(id)).isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("No customer was found.");
    }

    @Test
    void saveCustomer() {
        // Arrange
        String email = "hamza@gmail.com";
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "hamza",
                email,
                22
        );
        when(customerDao.customerExistsByEmail(email)).thenReturn(false);
        ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);
        // Act
        underTest.saveCustomer(request);
        // Assert
        verify(customerDao).addCustomer(argumentCaptor.capture());
        Customer actual = argumentCaptor.getValue();
        assertThat(actual.getId()).isNull();
        assertThat(actual.getEmail()).isEqualTo(request.email());
        assertThat(actual.getName()).isEqualTo(request.name());
        assertThat(actual.getAge()).isEqualTo(request.age());
    }

    @Test
    void willThrowExceptionWhenSaveCustomerGetsDuplicateEmail() {
        // Arrange
        String email = "hamza@gmail.com";
        when(customerDao.customerExistsByEmail(email)).thenReturn(true);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "hamza",
                email,
                22
        );
        // Act
        assertThatThrownBy(() -> underTest.saveCustomer(request)).isInstanceOf(DuplicateObjectException.class)
                .hasMessage("Email is taken.");
        // Assert
        verify(customerDao, never()).addCustomer(any());

    }

    @Test
    void deleteCustomerById() {
        // Arrange
        long id = 10;
        Customer customer = new Customer(
                id,
                "hamza",
                "hamza@gmail.com",
                22
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        // Act
        underTest.deleteCustomerById(id);
        // Assert
        verify(customerDao).removeCustomerById(id);
    }

    @Test
    void willThrowExceptionWhenDeleteCustomerByIdIsInvokedWithWrongId() {
        // Arrange
        long id = 10;
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());
        // Act
        assertThatThrownBy(()->underTest.deleteCustomerById(id)).isInstanceOf(
                ObjectNotFoundException.class
        ).hasMessage("No customer was found.");
        // Assert
        verify(customerDao, never()).removeCustomerById(id);
    }

    @Test
    void updateCustomerName() {
        // Arrange
        String email = "email@gmail.com";
        long id= 10;
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "hamza-updated",
                null,
                null
        );
        Customer customer = new Customer(
                id,
                "hamza",
                email,
                22
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        // Act
        underTest.updateCustomer(id, request);
        // Assert
        ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(argumentCaptor.capture());
        Customer actual = argumentCaptor.getValue();
        assertThat(actual.getId()).isEqualTo(id);
        assertThat(actual.getName()).isEqualTo(request.name());
        assertThat(actual.getEmail()).isEqualTo(customer.getEmail());
        assertThat(actual.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void updateCustomerEmail() {
        // Arrange
        String email = "email@gmail.com";
        long id= 10;
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                null,
                email,
                null
        );
        Customer customer = new Customer(
                id,
                "hamza",
                "old"+email,
                22
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        when(customerDao.customerExistsByEmail(email)).thenReturn(false);
        // Act
        ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);
        underTest.updateCustomer(id, request);
        // Assert
        verify(customerDao).updateCustomer(argumentCaptor.capture());
        Customer actual = argumentCaptor.getValue();
        assertThat(actual.getId()).isEqualTo(id);
        assertThat(actual.getName()).isEqualTo(customer.getName());
        assertThat(actual.getEmail()).isEqualTo(request.email());
        assertThat(actual.getAge()).isEqualTo(customer.getAge());
    }
    @Test
    void updateCustomerAge() {
        // Arrange
        String email = "email@gmail.com";
        long id= 10;
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                null,
                null,
                23
        );
        Customer customer = new Customer(
                id,
                "hamza",
                email,
                22
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        // Act
        ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);
        underTest.updateCustomer(id, request);
        // Assert
        verify(customerDao).updateCustomer(argumentCaptor.capture());
        Customer actual = argumentCaptor.getValue();
        assertThat(actual.getId()).isEqualTo(id);
        assertThat(actual.getName()).isEqualTo(customer.getName());
        assertThat(actual.getEmail()).isEqualTo(customer.getEmail());
        assertThat(actual.getAge()).isEqualTo(request.age());
    }
    @Test
    void updateCustomerAllFields() {
        // Arrange
        String email = "email@gmail.com";
        long id= 10;
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "updated-hamza",
                email,
                23
        );
        Customer customer = new Customer(
                id,
                "hamza",
                "old"+email,
                22
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        when(customerDao.customerExistsByEmail(email)).thenReturn(false);
        // Act
        ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);
        underTest.updateCustomer(id, request);
        // Assert
        verify(customerDao).updateCustomer(argumentCaptor.capture());
        Customer actual = argumentCaptor.getValue();
        assertThat(actual.getId()).isEqualTo(id);
        assertThat(actual.getName()).isEqualTo(request.name());
        assertThat(actual.getEmail()).isEqualTo(request.email());
        assertThat(actual.getAge()).isEqualTo(request.age());
    }
    @Test
    void willThrowExceptionWhenUpdateCustomerInvokedWithDuplicateEmail() {
        // Arrange
        String email = "email@gmail.com";
        long id= 10;
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "hamza-updated",
                email,
                22
        );
        Customer customer = new Customer(
                id,
                "hamza",
                "hamzawy@gmail.com",
                22
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        when(customerDao.customerExistsByEmail(request.email())).thenReturn(true);
        // Act
        assertThatThrownBy(() -> underTest.updateCustomer(id, request)).isInstanceOf(
                DuplicateObjectException.class
        ).hasMessage("Email is taken.");
        // Assert
        verify(customerDao, never()).updateCustomer(any());

    }
    @Test
    void willThrowExceptionWhenUpdateCustomerInvokedWithoutAnyUpdates() {
        // Arrange
        String email = "email@gmail.com";
        long id= 10;
        String name = "hamza";
        int age = 22;
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                name,
                email,
                age
        );
        Customer customer = new Customer(
                id,
                name,
                email,
                age
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        // Act
        assertThatThrownBy(() -> underTest.updateCustomer(id, request)).isInstanceOf(
                NoUpdatesException.class
        ).hasMessage("No updates exist.");
        // Assert
        verify(customerDao, never()).updateCustomer(any());
    }
    @Test
    void willThrowExceptionWhenUpdateCustomerInvokedWithWrongId() {
        // Arrange
        String email = "email@gmail.com";
        long id= 10;
        String name = "hamza";
        int age = 22;
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                name,
                email,
                age
        );
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());
        // Act
        assertThatThrownBy(() -> underTest.updateCustomer(id, request)).isInstanceOf(
                ObjectNotFoundException.class
        ).hasMessage("No customer was found.");
        // Assert
        verify(customerDao, never()).updateCustomer(any());
    }
}