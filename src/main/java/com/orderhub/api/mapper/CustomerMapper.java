package com.orderhub.api.mapper;

import com.orderhub.api.domain.Customer;
import com.orderhub.api.dtos.CustomerRequestDTO;
import com.orderhub.api.dtos.CustomerResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer toEntity(CustomerRequestDTO dto) {
        Customer customer = new Customer();
        customer.setName(dto.name());
        customer.setDocument(dto.document());
        customer.setEmail(dto.email());
        return customer;
    }

    public CustomerResponseDTO toResponse(Customer entity) {
        return  new CustomerResponseDTO(
                entity.getId(),
                entity.getDocument(),
                entity.getName(),
                entity.getEmail()
        );
    }
}
