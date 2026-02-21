package org.example.pos_back_end.service.custom.impl;

import org.example.pos_back_end.dto.CustomerDTO;
import lombok.RequiredArgsConstructor;
import org.example.pos_back_end.entity.Customer;
import org.example.pos_back_end.repository.CustomerRepository;
import org.example.pos_back_end.service.custom.CustomerService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void saveCustomer(CustomerDTO customerDTO){
        if (customerDTO == null){
            throw new NullPointerException("Customer is null");
        }

        customerRepository.save(
                modelMapper.map(customerDTO, Customer.class));
    }

    @Override
    public void updateCustomer(CustomerDTO customerDTO) {
        if (customerDTO == null){
            throw new NullPointerException("Customer is null");
        }

        customerRepository.save(
                modelMapper.map(customerDTO,Customer.class));
    }

    @Override
    public void deleteCustomer(String customerId) {customerRepository.deleteById(customerId);}

    @Override
    public List<CustomerDTO> getAllCustomer() {
        List<Customer> customers = customerRepository.findAll();
        return modelMapper.map(customers,new TypeToken<List<CustomerDTO>>(){}.getType());
    }

    @Override
    public CustomerDTO getCustomer(String id) {
        return customerRepository.findById(id)
                .map(customer -> modelMapper.map(customer, CustomerDTO.class))
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }
}
