package com.hamza.customer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }
    @GetMapping
    private List<Customer> findAllCustomers(){
        return customerService.getAllCustomers();
    }
    @GetMapping("/{id}")
    private Customer findById(@PathVariable int id){
        return customerService.getCustomer(id);
    }
    @PostMapping
    private void saveCustomer(@RequestBody CustomerRegistrationRequest registrationRequest){
        customerService.saveCustomer(registrationRequest);
    }
    @DeleteMapping("/{id}")
    private void deleteCustomerById(@PathVariable int id){
        customerService.deleteCustomerById(id);
    }
    @PutMapping("/{id}")
    private void updateCustomerById(@PathVariable int id,
                                    @RequestBody CustomerRegistrationRequest customerRegistrationRequest){
        customerService.updateCustomer(id, customerRegistrationRequest);
    }
}
