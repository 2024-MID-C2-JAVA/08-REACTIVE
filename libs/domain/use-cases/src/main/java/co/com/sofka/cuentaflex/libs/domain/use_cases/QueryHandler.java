package co.com.sofka.cuentaflex.libs.domain.use_cases;

import java.util.function.Function;

public interface QueryHandler<QueryType extends Query<ResponseType>, ResponseType> extends Function<QueryType, ResponseType> {
}
