package com.hamza.customer;

import java.util.List;
import java.util.Optional;

interface CustomerDao {
    List<Customer> selectAllCustomers();
    Optional<Customer> selectCustomerById(long id);
    void addCustomer(Customer customer);
    boolean customerExistsByEmail(String email);
    void removeCustomerById(long id);
    void updateCustomer(Customer customer);
}
