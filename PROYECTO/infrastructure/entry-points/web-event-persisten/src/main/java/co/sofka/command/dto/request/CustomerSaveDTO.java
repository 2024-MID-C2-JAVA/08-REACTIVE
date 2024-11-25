package co.sofka.command.dto.request;

import co.sofka.command.dto.AccountDTO;
import lombok.Data;

import java.util.List;

@Data
public class CustomerSaveDTO {

    private String username;

    private String pwd;

    private String rol;

    private List<AccountDTO> accounts;


}
