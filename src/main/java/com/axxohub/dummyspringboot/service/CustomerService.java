package com.axxohub.dummyspringboot.service;

import com.axxohub.dummyspringboot.dto.CustomerRequest;
import com.axxohub.dummyspringboot.exception.ResourceNotFoundException;
import com.axxohub.dummyspringboot.model.Customer;
import com.axxohub.dummyspringboot.repository.CustomerRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Customer findById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + id));
    }

    public List<Customer> search(String query) {
        return customerRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(query, query);
    }

    public Customer create(CustomerRequest request) {
        Customer customer = new Customer();
        customer.setName(request.name());
        customer.setEmail(request.email());
        customer.setTier(request.tier());
        customer.setActive(request.active());
        customer.setCreatedAt(LocalDateTime.now());
        return customerRepository.save(customer);
    }

    public Customer update(Long id, CustomerRequest request) {
        Customer customer = findById(id);
        customer.setName(request.name());
        customer.setEmail(request.email());
        customer.setTier(request.tier());
        customer.setActive(request.active());
        return customerRepository.save(customer);
    }

    public void delete(Long id) {
        customerRepository.delete(findById(id));
    }
}
