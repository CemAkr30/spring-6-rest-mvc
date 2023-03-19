package sa.springframework.spring6restmvc.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sa.springframework.spring6restmvc.entities.Customer;
import sa.springframework.spring6restmvc.genUtils.GenUtilMap;
import sa.springframework.spring6restmvc.model.CustomerDTO;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {
    private Map<UUID, Customer> customerMap;

    public CustomerServiceImpl() {
        Customer customer1 = Customer.builder()
                .id(UUID.randomUUID())
                .name("Customer 1")
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Customer customer2 = Customer.builder()
                .id(UUID.randomUUID())
                .name("Customer 2")
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Customer customer3 = Customer.builder()
                .id(UUID.randomUUID())
                .name("Customer 3")
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        customerMap = new HashMap<>();
        customerMap.put(customer1.getId(), customer1);
        customerMap.put(customer2.getId(), customer2);
        customerMap.put(customer3.getId(), customer3);
    }

    @Override
    public Optional<CustomerDTO> patchCustomerById(UUID customerId, Customer customer) {
        CustomerDTO existing = new GenUtilMap<CustomerDTO,Customer>().pojoToDto(new CustomerDTO(),customerMap.get(customerId));
        if (StringUtils.hasText(customer.getName())) {
            existing.setName(customer.getName());
        }
        return Optional.of(existing);
    }

    @Override
    public Boolean deleteCustomerById(UUID customerId) {
        customerMap.remove(customerId);

        return true;
    }

    @Override
    public Optional<CustomerDTO> updateCustomerById(UUID customerId, Customer customer) {
        CustomerDTO existing = new GenUtilMap<CustomerDTO,Customer>().pojoToDto(new CustomerDTO(),customerMap.get(customerId));
        existing.setName(customer.getName());
        return Optional.of(existing);
    }

    @Override
    public CustomerDTO saveNewCustomer(Customer customer) {

        Customer savedCustomer = Customer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .updateDate(LocalDateTime.now())
                .createdDate(LocalDateTime.now())
                .name(customer.getName())
                .build();

        customerMap.put(savedCustomer.getId(), savedCustomer);

        return new GenUtilMap<CustomerDTO,Customer>().pojoToDto(new CustomerDTO(),savedCustomer);
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID uuid) {
        return Optional.of(new GenUtilMap<CustomerDTO,Customer>().pojoToDto(new CustomerDTO(),customerMap.get(uuid)));
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return new GenUtilMap<CustomerDTO,Customer>().pojoToListDto(new CustomerDTO(),new ArrayList<>(customerMap.values()));
    }

}
