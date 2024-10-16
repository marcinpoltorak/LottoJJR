package pl.lotto.domain.numbergenerator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.drawdate.DrawDateGenerator;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

@Configuration
public class NumberGeneratorConfiguration {

    @Bean
    WinningNumbersRepository repository(){
        return new WinningNumbersRepository() {
            @Override
            public Optional<WinningNumbers> findNumbersByDate(LocalDateTime date) {
                return Optional.empty();
            }

            @Override
            public boolean existsByDate(LocalDateTime date) {
                return false;
            }

            @Override
            public WinningNumbers save(WinningNumbers winningNumbers) {
                return null;
            }
        };
    }

    @Bean
    DrawDateFacade drawDateFacade(){
        return new DrawDateFacade(new DrawDateGenerator(Clock.systemUTC()));
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
