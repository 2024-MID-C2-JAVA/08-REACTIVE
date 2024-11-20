package co.sofka;


//import co.sofka.command.create.SaveCustumerHandler;

import co.sofka.command.create.SaveCustumerHandler;
import co.sofka.command.dto.CurstomerByUsername;
import co.sofka.command.dto.CustomerDTO;
import co.sofka.command.dto.request.CustomerSaveDTO;
import co.sofka.command.dto.request.RequestMs;
import co.sofka.command.dto.response.ResponseMs;
import co.sofka.command.query.CustomerByUserNameHandler;
import co.sofka.command.query.ListAllCustomerHandler;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/client")
@AllArgsConstructor
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    private final ListAllCustomerHandler handler;

    private final SaveCustumerHandler saveCustumerHandler;

    private final CustomerByUserNameHandler customerByUserNameHandler;


    @PostMapping("/all")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Request example",
            required = true,
            content = {
                    @Content(
                            schema = @Schema(implementation = RequestMs.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "Ejemplo JSON",
                                            value = "{\"dinHeader\":{\"dispositivo\":\"PC\",\"idioma\":\"es\",\"uuid\":\"02e3eb27-6fb1-e542-e157-c301cc77ad2c\",\"ip\":\"localhost\",\"horaTransaccion\":\"string\",\"llaveSimetrica\":\"xaqVyedHolrJB9vW4lIj5u9nuWIiaPpAQoOK4hm2j+Q=\",\"vectorInicializacion\":\"Gz3MLPvKU1T5Pc3FfmNYPe9nuWIiaPpAQoOK4hm2j+Q=\"},\"dinBody\":{\"id\":\"1\"}}",
                                            summary = "Full request")})})

    @ApiResponse(
            responseCode = "200",
            content = @Content(schema = @Schema(implementation = ResponseMs.class)))
    public Mono<ResponseMs<List<CustomerDTO>>> getAll(

            @RequestBody RequestMs<Void> request
    ) {
        logger.info("Buscando todos los Customer");
        return handler.getAll(request);
    }



    @PostMapping("/findByUserName")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Request example",
            required = true,
            content = {
                    @Content(
                            schema = @Schema(implementation = RequestMs.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "Ejemplo JSON",
                                            value = "{\"dinHeader\":{\"dispositivo\":\"PC\",\"idioma\":\"es\",\"uuid\":\"02e3eb27-6fb1-e542-e157-c301cc77ad2c\",\"ip\":\"localhost\",\"horaTransaccion\":\"string\",\"llaveSimetrica\":\"xaqVyedHolrJB9vW4lIj5u9nuWIiaPpAQoOK4hm2j+Q=\",\"vectorInicializacion\":\"Gz3MLPvKU1T5Pc3FfmNYPe9nuWIiaPpAQoOK4hm2j+Q=\"},\"dinBody\":{\"username\":\"pablo\"}}",
                                            summary = "Full request")})})

    @ApiResponse(
            responseCode = "200",
            content = @Content(schema = @Schema(implementation = ResponseMs.class)))
    public Mono<ResponseMs<CustomerDTO>> findByUserName(

            @RequestBody RequestMs<CurstomerByUsername> request
    ) {
        logger.info("Buscando Customer by username");
        return customerByUserNameHandler.apply(request);
    }

//
    @PostMapping("/save")
    @ApiResponse(
            responseCode = "200",
            content = @Content(schema = @Schema(implementation = ResponseMs.class)))
    public Mono<ResponseMs<CustomerDTO>> save(

            @RequestBody RequestMs<CustomerSaveDTO> request
    ) {
        logger.info("Save Customer");
        return saveCustumerHandler.save(request);
    }



}
