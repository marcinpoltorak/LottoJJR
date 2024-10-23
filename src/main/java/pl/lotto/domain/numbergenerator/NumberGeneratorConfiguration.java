package pl.lotto.domain.numbergenerator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.drawdate.DrawDateGenerator;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

@Configuration
public class NumberGeneratorConfiguration {

    @Bean
    Clock clock(){
        return Clock.systemUTC();
    }
    @Bean
    DrawDateFacade drawDateFacade(Clock clock){
        return new DrawDateFacade(new DrawDateGenerator(clock));
    }

    @Bean
    WinningNumbersGeneratorFacade winningNumbersGeneratorFacade(WinningNumbersRepository winningNumbersRepository, WinningNumbersGenerable winningNumbersGenerator, DrawDateFacade drawDateFacade, WinningNumbersGeneratorFacadeConfigurationProperties properties){
        WinningNumbersValidator winningNumbersValidator = new WinningNumbersValidator();
        return new WinningNumbersGeneratorFacade(winningNumbersGenerator, winningNumbersValidator, winningNumbersRepository, drawDateFacade, properties);
    }

    WinningNumbersGeneratorFacade createForTest(WinningNumbersGenerable generator, WinningNumbersRepository repository, DrawDateFacade drawDateFacade){
        WinningNumbersValidator validator = new WinningNumbersValidator();
        WinningNumbersGeneratorFacadeConfigurationProperties properties = WinningNumbersGeneratorFacadeConfigurationProperties.builder()
                .lowerBand(1)
                .upperBand(99)
                .count(6)
                .build();
        return winningNumbersGeneratorFacade(repository, generator, drawDateFacade, properties);
    }
}
