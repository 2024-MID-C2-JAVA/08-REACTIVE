package co.sofka.handler;

import co.sofka.Customer;
import co.sofka.RequestMs;
import co.sofka.in.customer.DeleteCustomerUseCase;
import co.sofka.in.customer.GetCustomerByIdUseCase;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Component
public class CustomerHandler {

    private final Function<Customer, Mono<Customer>> deleteCustomerUseCase;
    private final Function<Customer, Mono<Customer>> getCustomerByIdUseCase;

    public CustomerHandler(Function<Customer, Mono<Customer>> deleteCustomerUseCase,
                           Function<Customer, Mono<Customer>> getCustomerByIdUseCase) {
        this.deleteCustomerUseCase = deleteCustomerUseCase;
        this.getCustomerByIdUseCase = getCustomerByIdUseCase;
    }

    private Mono<ServerResponse> processCustomerRequest(ServerRequest request,
                                                        Function<Customer, Mono<Customer>> useCase) {
        return request.bodyToMono(new ParameterizedTypeReference<RequestMs<Customer>>() {})
                .flatMap(requestMs -> {
                    Customer customer = new Customer();
                    customer.setId(requestMs.getDinBody().getId());
                    return useCase.apply(customer)
                            .flatMap(result -> ServerResponse
                                    .status(HttpStatus.OK)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(result));
                });
    }

    public Mono<ServerResponse> deleteCustomer(ServerRequest request) {
        return processCustomerRequest(request, deleteCustomerUseCase);
    }

    public Mono<ServerResponse> getCustomerById(ServerRequest request) {
        return processCustomerRequest(request, getCustomerByIdUseCase);
    }
}
