package com.hamza.customer;

import com.hamza.exception.DuplicateObjectException;
import com.hamza.exception.NoUpdatesException;
import com.hamza.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class CustomerService {

    private final CustomerDao customerDao;

    CustomerService(@Qualifier("jdbc") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    List<Customer> getAllCustomers(){
        return customerDao.selectAllCustomers();
    }
    Customer getCustomer(long id){
        return customerDao.selectCustomerById(id)
                .orElseThrow(() -> new ObjectNotFoundException("No customer was found."));
    }
    void saveCustomer(CustomerRegistrationRequest customerRegistrationRequest){
        if(customerDao.customerExistsByEmail(customerRegistrationRequest.email())){
            throw new DuplicateObjectException("Email is taken.");
        }
        Customer customer = new Customer(
                customerRegistrationRequest.name(),
                customerRegistrationRequest.email(),
                customerRegistrationRequest.age()
        );
        customerDao.addCustomer(customer);
    }
    void deleteCustomerById(long id){
        Customer customer = getCustomer(id);
        customerDao.removeCustomerById(id);
    }
    void updateCustomer(long id, CustomerRegistrationRequest customerRegistrationRequest){
        Customer customer = getCustomer(id);
        boolean change=false;
        if(customerRegistrationRequest.name() != null && !customerRegistrationRequest.name().equals(customer.getName())){
            customer.setName(customerRegistrationRequest.name());
            change = true;
        }
        if(customerRegistrationRequest.email() != null && !customerRegistrationRequest.email().equals(customer.getEmail())){
            if(customerDao.customerExistsByEmail(customerRegistrationRequest.email())){
                throw new DuplicateObjectException("Email is taken.");
            }
            customer.setEmail(customerRegistrationRequest.email());
            change = true;
        }
        if(customerRegistrationRequest.age() != null && customerRegistrationRequest.age() != customer.getAge()){
            customer.setAge(customerRegistrationRequest.age());
            change = true;
        }

        if(!change){
            throw new NoUpdatesException("No updates exist.");
        }
        customerDao.updateCustomer(customer);
    }
}
