package co.sofka.handler;

import co.sofka.*;
import co.sofka.appservice.customer.GetCustomerByIdUseCaseView;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Component
public class CustomerViewHandler {

    private final GetCustomerByIdUseCaseView getCustomerByIdUseCaseView;

    public CustomerViewHandler(GetCustomerByIdUseCaseView getCustomerByIdUseCaseView) {
        this.getCustomerByIdUseCaseView = getCustomerByIdUseCaseView;
    }

    public Mono<ServerResponse> getCustomerById(ServerRequest request) {
        return request.bodyToMono(new ParameterizedTypeReference<RequestMs<Customer>>() {})
                .flatMap(requestMs->{
                    Customer customer=new Customer();
                    customer.setId(requestMs.getDinBody().getId());
                    return getCustomerByIdUseCaseView.apply(customer)
                            .flatMap(customer1 -> {
                                ResponseMs<Customer>responseC=new ResponseMs<>();
                                responseC.setDinHeader(requestMs.getDinHeader());
                                responseC.setDinBody(customer1);
                                responseC.setDinError(new DinError(DinErrorEnum.SUCCESS));
                                return ServerResponse
                                        .status(HttpStatus.OK)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(responseC);
                            });
                });
    }
}
