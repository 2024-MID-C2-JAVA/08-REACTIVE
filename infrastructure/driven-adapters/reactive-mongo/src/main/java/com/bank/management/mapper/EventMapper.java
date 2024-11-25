package com.bank.management.mapper;

import com.bank.management.data.EventDocument;
import com.bank.management.generic.DomainEvent;

public class EventMapper {

    public static EventDocument toDocument(DomainEvent event) {
        return new EventDocument(
                event.getWhen(),
                event.getUuid(),
                event.getType(),
                event.getAggregateRootId(),
                event.getAggregate(),
                event.getVersionType(),
                event.getBody()
        );
    }

    public static DomainEvent toDomain(EventDocument document) {
        return new DomainEvent(
                document.getWhen(),
                document.getUuid(),
                document.getType(),
                document.getAggregateRootId(),
                document.getAggregate(),
                document.getVersionType(),
                document.getBody()
        );
    }
}
