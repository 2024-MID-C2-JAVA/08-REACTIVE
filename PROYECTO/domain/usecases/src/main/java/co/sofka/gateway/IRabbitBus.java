package co.sofka.gateway;

import co.sofka.LogEvent;
import reactor.core.publisher.Mono;

public interface IRabbitBus {
    void send(LogEvent logEvent);
}
