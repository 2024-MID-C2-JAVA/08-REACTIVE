package co.com.sofka.cuentaflex.libs.domain.use_cases;

import java.util.function.Function;

public interface QueryHandler<QueryType, ResponseType> extends Function<QueryType, ResponseType> {
}
