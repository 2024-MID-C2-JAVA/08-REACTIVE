package co.sofka.handler;

import co.sofka.Customer;
import co.sofka.data.customer.CustomerDto;
import co.sofka.in.customer.DeleteCustomerUseCase;
import co.sofka.in.customer.GetCustomerByIdUseCase;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CustomerHandler {

    private final DeleteCustomerUseCase deleteCustomerUseCase;
    private final GetCustomerByIdUseCase getCustomerByIdUseCase;


    public CustomerHandler(DeleteCustomerUseCase deleteCustomerUseCase, GetCustomerByIdUseCase getCustomerByIdUseCase) {
        this.deleteCustomerUseCase = deleteCustomerUseCase;
        this.getCustomerByIdUseCase = getCustomerByIdUseCase;
    }

    public Mono<Customer>deleteCustomer(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setId(customerDto.getId());
        return deleteCustomerUseCase.apply(customer);
    }

    public Mono<Customer>getCustomerById(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setId(customerDto.getId());
        return getCustomerByIdUseCase.apply(customer);
    }
}
