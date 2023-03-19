package sa.springframework.spring6restmvc.mapper;


import org.mapstruct.Mapper;
import sa.springframework.spring6restmvc.entities.Customer;
import sa.springframework.spring6restmvc.model.CustomerDTO;

@Mapper
public interface CustomerMapper {

    Customer customerDtoToCustomer(CustomerDTO dto);

    CustomerDTO customerToCustomerDto(Customer customer);

}
