package co.sofka;

import co.sofka.data.customer.CustomerDto;
import co.sofka.data.customer.ResponseCustomerMs;
import co.sofka.handler.CustomerHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/customer")
public class CustomerRestController {

    private final CustomerHandler customerHandler;

    public CustomerRestController(CustomerHandler customerHandler) {
        this.customerHandler = customerHandler;
    }

    @PostMapping("/delete")
    public Mono<ResponseEntity<ResponseCustomerMs>> deleteCustomer(@RequestBody RequestMs<CustomerDto> deleteCustomerDTO) {
        return customerHandler.deleteCustomer(deleteCustomerDTO.getDinBody())
                .then(Mono.just(ResponseEntity.ok(
                        new ResponseCustomerMs(deleteCustomerDTO.getDinHeader(), deleteCustomerDTO.getDinBody(),
                                new DinError(DinErrorEnum.CUSTOMER_DELETED)))))
                .onErrorResume(e -> Mono.just(ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseCustomerMs(new DinError(DinErrorEnum.DELETE_ERROR)))));
    }


    @PostMapping("/get")
    public Mono<ResponseEntity<ResponseCustomerMs>> getCustomerById(@RequestBody RequestMs<CustomerDto> dto) {
        return customerHandler.getCustomerById(dto.getDinBody())
                .map(customerDto -> {
                    dto.setDinBody(new CustomerDto(customerDto.getId(),customerDto.getName(),customerDto.getCreatedAt()));
                    return ResponseEntity.ok(
                            new ResponseCustomerMs(dto.getDinHeader(), dto.getDinBody(),
                                    new DinError(DinErrorEnum.SUCCESS)));
                })
                .onErrorResume(e -> Mono.just(ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseCustomerMs(dto.getDinHeader(), dto.getDinBody(),
                                new DinError(DinErrorEnum.CUSTOMER_NOT_FOUND)))));
    }

}
