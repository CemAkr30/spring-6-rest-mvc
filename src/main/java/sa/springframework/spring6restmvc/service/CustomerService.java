package sa.springframework.spring6restmvc.service;

import sa.springframework.spring6restmvc.entities.Customer;
import sa.springframework.spring6restmvc.model.CustomerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    Optional<CustomerDTO> getCustomerById(UUID uuid);

    List<CustomerDTO> getAllCustomers();

    CustomerDTO saveNewCustomer(Customer customer);

    Optional<CustomerDTO> updateCustomerById(UUID customerId, Customer customer);

    Boolean deleteCustomerById(UUID customerId);

    Optional<CustomerDTO> patchCustomerById(UUID customerId, Customer customer);
}
