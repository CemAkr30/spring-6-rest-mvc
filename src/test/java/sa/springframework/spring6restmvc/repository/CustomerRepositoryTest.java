package sa.springframework.spring6restmvc.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import sa.springframework.spring6restmvc.entities.Customer;
import sa.springframework.spring6restmvc.genUtils.GenUtilMap;
import sa.springframework.spring6restmvc.model.CustomerDTO;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void testSaveCustomer(){
        CustomerDTO savedCustomer = new GenUtilMap<CustomerDTO,Customer>()
                .pojoToDto(new CustomerDTO(),customerRepository.save(Customer.builder().name("My CustomerDTO").build()));
        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isNotNull();
    }
}