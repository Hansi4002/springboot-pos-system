package org.example.pos_back_end.service.custom;

import org.example.pos_back_end.dto.CustomerDTO;

import java.util.List;

public interface CustomerService {
    public void saveCustomer(CustomerDTO customerDTO);
    public void updateCustomer(CustomerDTO customerDTO);
    public void deleteCustomer(String customerId);
    public List<CustomerDTO> getAllCustomer();
}