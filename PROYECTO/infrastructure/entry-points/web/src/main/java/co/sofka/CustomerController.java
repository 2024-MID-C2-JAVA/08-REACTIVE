package co.sofka;

import co.sofka.command.dto.CurstomerByUsername;
import co.sofka.command.dto.CustomerDTO;
import co.sofka.command.dto.request.RequestMs;
import co.sofka.command.dto.response.ResponseMs;
import co.sofka.command.query.CustomerByUserNameHandler;
import co.sofka.command.query.ListAllCustomerHandler;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/client")
@AllArgsConstructor
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    private final ListAllCustomerHandler handler;


    private final CustomerByUserNameHandler customerByUserNameHandler;


    @PostMapping("/all")
    public Mono<ResponseMs<List<CustomerDTO>>> getAll(@RequestBody RequestMs<Void> request) {
        logger.info("Buscando todos los Customer");
        return handler.getAll(request);
    }



    @PostMapping("/findByUserName")

    public Mono<ResponseMs<CustomerDTO>> findByUserName(@RequestBody RequestMs<CurstomerByUsername> request) {
        logger.info("Buscando Customer by username");
        return customerByUserNameHandler.apply(request);
    }




}
