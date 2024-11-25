package co.sofka.middleware;

import co.sofka.command.dto.DinHeader;
import lombok.Data;

@Data
public class ErrorDecryptingDataException extends RuntimeException{

    private final int code;

    public ErrorDecryptingDataException(String message,int code) {
        super(message);
        this.code = code;
    }


}
