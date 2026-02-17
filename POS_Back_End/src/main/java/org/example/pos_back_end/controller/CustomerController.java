package org.example.pos_back_end.controller;

import lombok.RequiredArgsConstructor;
import org.example.pos_back_end.dto.CustomerDTO;
import org.example.pos_back_end.service.custom.CustomerService;
import org.example.pos_back_end.util.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers")
@CrossOrigin
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<APIResponse<String>> saveCustomer(@RequestBody CustomerDTO customerDTO){
        customerService.saveCustomer(customerDTO);
        return new ResponseEntity<>(new APIResponse<>(
                201,"Customer Saved",null), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<APIResponse<Iterable<CustomerDTO>>> getAllCustomers(){
        return new ResponseEntity<>(new APIResponse<>(
                200,"All Customers",customerService.getAllCustomer()), HttpStatus.OK);
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<APIResponse<String>> deleteCustomer(@PathVariable String customerId){
        customerService.deleteCustomer(customerId);
        return new ResponseEntity<>(new APIResponse<>(
                200,"Customer Deleted",null), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<APIResponse<String>> updateCustomer(@RequestBody CustomerDTO customerDTO){
        customerService.updateCustomer(customerDTO);
        return new ResponseEntity<>(new APIResponse<>(
                200,"Customer Updated",null), HttpStatus.OK);
    }
}