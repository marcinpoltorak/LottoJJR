package pl.lotto.domain.resultannouncer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lotto.domain.numbergenerator.WinningNumbersGeneratorFacade;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.resultchecker.PlayerRepository;
import pl.lotto.domain.resultchecker.ResultCheckerFacade;
import pl.lotto.domain.resultchecker.WinnerRetriever;

import java.time.Clock;

@Configuration
public class ResultAnnouncerConfiguration {

    @Bean
    ResultAnnouncerFacade resultAnnouncerFacade(ResultCheckerFacade resultCheckerFacade, ResponseRepository responseRepository, Clock clock){
        return new ResultAnnouncerFacade(resultCheckerFacade, responseRepository, clock);
    }

    public ResultAnnouncerFacade createForTest(ResultCheckerFacade resultCheckerFacade, ResponseRepository responseRepository, Clock clock){
        return new ResultAnnouncerFacade(resultCheckerFacade, responseRepository, clock);
    }
}
