package co.sofka;

import co.sofka.events.CreateUserEvent;
import co.sofka.events.DomainEvent;
import co.sofka.handler.UserHandler;
import co.sofka.rabbitMq.listener.UserBusListener;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserEventListener implements UserBusListener {

    private final UserHandler userHandler;

    public UserEventListener(UserHandler userHandler) {
        this.userHandler = userHandler;
    }

    @Override
    @RabbitListener(queues = "queue.user")
    public void receive(CreateUserEvent createUserEvent) {
        UserRequest userRequest=new UserRequest();
        userRequest.setFirstname(createUserEvent.getFirstname());
        userRequest.setLastname(createUserEvent.getLastname());
        userRequest.setEmail(createUserEvent.getEmail());
        userRequest.setPassword(createUserEvent.getPassword());
        userRequest.setRole(createUserEvent.getRole());

       userHandler.register(userRequest).subscribe();
    }
}
