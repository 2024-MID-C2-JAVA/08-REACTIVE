package co.sofka;

import co.sofka.data.account.AccountDto;
import co.sofka.data.account.ResponseAccountMs;
import co.sofka.handler.AccountHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/account")
public class AccountRestController {

    private final AccountHandler accountHandler;

    public AccountRestController(AccountHandler accountHandler) {
        this.accountHandler = accountHandler;
    }

    @PostMapping("/create")
    public Mono<ResponseAccountMs> createAccount(@RequestBody RequestMs<AccountDto> dto) {
        return accountHandler.createAccount(dto.getDinBody())
                .map(account -> new ResponseAccountMs(
                        dto.getDinHeader(),
                        dto.getDinBody(),
                        new DinError(DinErrorEnum.ACCOUNT_CREATED)))
                .onErrorResume(error -> Mono.just(new ResponseAccountMs(
                        dto.getDinHeader(),
                        dto.getDinBody(),
                        new DinError(DinErrorEnum.CREATION_ERROR)
                )));
    }

    @PostMapping("/delete")
    public Mono<ResponseEntity<ResponseAccountMs>> deleteAccount(@RequestBody RequestMs<AccountDto> dto) {
        return accountHandler.deleteAccount(dto.getDinBody())
                .then(Mono.just(ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(new ResponseAccountMs(dto.getDinHeader(), dto.getDinBody(),
                                new DinError(DinErrorEnum.ACCOUNT_DELETED)))))
                .onErrorResume(e -> Mono.just(ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseAccountMs(dto.getDinHeader(), dto.getDinBody(),
                                new DinError(DinErrorEnum.DELETE_ERROR)))));
    }

    @PostMapping("/get")
    public Mono<ResponseEntity<ResponseAccountMs>> getAccountById(@RequestBody RequestMs<AccountDto> dto) {
        return accountHandler.getAccountById(dto.getDinBody())
                .map(accountDto -> ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseAccountMs(dto.getDinHeader(), dto.getDinBody(),
                                new DinError(DinErrorEnum.SUCCESS))))
                .onErrorResume(e -> Mono.just(ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseAccountMs(dto.getDinHeader(), dto.getDinBody(),
                                new DinError(DinErrorEnum.ACCOUNT_NOT_FOUND)))));
    }

}
