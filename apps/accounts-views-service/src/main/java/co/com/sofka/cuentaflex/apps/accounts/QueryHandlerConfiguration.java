package co.com.sofka.cuentaflex.apps.accounts;

import co.com.sofka.cuentaflex.libs.domain.use_cases.QueryHandler;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
        basePackages = "co.com.sofka",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = {QueryHandler.class})
        }
)
public class QueryHandlerConfiguration {
}
