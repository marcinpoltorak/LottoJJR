package pl.lotto;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.time.*;

@Configuration
@Profile("integration")
public class IntegrationConfiguration {

    @Bean
    @Primary
    AdjustableClock clock(){
        return AdjustableClock.ofLocalDateAndLocalTime(LocalDate.of(2022, 11, 16), LocalTime.of(11, 0), ZoneId.systemDefault());
    }
}
