package co.sofka.command;

import co.sofka.Event;
import co.sofka.command.create.SaveCustumerHandler;
import co.sofka.command.dto.request.CustomerSaveDTO;
import co.sofka.event.Notification;
import co.sofka.utils.JsonToObjectConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
@Data
public class ReadRabbitQuee {

    private static final Logger logger = LoggerFactory.getLogger(ReadRabbitQuee.class);

   private final SaveCustumerHandler saveCustumerHandler;

@SneakyThrows
@RabbitListener(queues = "${general.config.rabbitmq.queueCustomer}")
public void readLog(Notification message) throws JsonProcessingException {
    logger.info("Mensaje recibido: " + message);

    String item = message.getMessage();

    logger.info("Mensaje recibido: " + item);

    CustomerSaveDTO customerSaveDTO = JsonToObjectConverter.convertJsonToObject(item, CustomerSaveDTO.class);

    saveCustumerHandler.save(customerSaveDTO);


}


}
