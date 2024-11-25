package co.sofka.command.dto.response;

import co.sofka.command.dto.DinHeader;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ResponseMs<T> {
    private DinHeader dinHeader;
    private T dinBody;
    private DinError dinError;
}
