package co.com.sofka.cuentaflex.apps.accounts.configuration;

import co.com.sofka.cuentaflex.libs.domain.use_cases.ReactiveCommandHandler;
import co.com.sofka.cuentaflex.libs.domain.use_cases.common.AccountTransactor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
        basePackages = "co.com.sofka",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = ReactiveCommandHandler.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = AccountTransactor.class)
        }
)
public class CommandHandlersConfiguration {
}
