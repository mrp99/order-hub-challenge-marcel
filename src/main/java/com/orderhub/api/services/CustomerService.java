package com.orderhub.api.services;

import com.orderhub.api.domain.Customer;
import com.orderhub.api.dtos.CustomerRequestDTO;
import com.orderhub.api.dtos.CustomerResponseDTO;
import com.orderhub.api.exceptions.BusinessException;
import com.orderhub.api.mapper.CustomerMapper;
import com.orderhub.api.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    private static final String ERROR_MESSAGE = "Já existe um cliente cadastrado com este e-mail.";
    private static final String NOT_FOUND_MESSAGE = "Cliente não encontrado com o ID: ";
    private static final String DOCUMENT_ERROR_MESSAGE = "Já existe um cliente cadastrado com este documento.";

    public CustomerService(
            CustomerRepository customerRepository,
            CustomerMapper customerMapper
    ) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }
    /**
     * Busca um cliente pelo ID.
     * Requisito obrigatório para o endpoint GET /api/customers/{id}
     */
    public CustomerResponseDTO findById(UUID id) {
        return customerRepository.findById(id)
                .map(customerMapper::toResponse)
                .orElseThrow(
                        () -> new BusinessException( NOT_FOUND_MESSAGE + id)
                );
    }
    /**
     * Cria um novo cliente com validação de duplicidade.
     */
    @Transactional
    public CustomerResponseDTO create(CustomerRequestDTO dto) {
        // Verificação de duplicidade
        if (customerRepository.existsByEmail(dto.email())) {
            throw new BusinessException(ERROR_MESSAGE);
        }
        if (customerRepository.existsByDocument(dto.document())) {
            throw new BusinessException(DOCUMENT_ERROR_MESSAGE);
        }
        Customer customer = customerMapper.toEntity(dto);
        return customerMapper.toResponse(customerRepository.save(customer));
    }

    // findAll atualizado para usar o .toList() simplificado
    public List<CustomerResponseDTO> findAll() {
        return customerRepository.findAll().stream()
                .map(customerMapper::toResponse)
                .toList();
    }
}
