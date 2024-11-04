package pl.lotto.domain.resultchecker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lotto.domain.numbergenerator.WinningNumbersGeneratorFacade;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;

@Configuration
public class ResultCheckerConfiguration {

    @Bean
    WinnerRetriever winnerRetriever(){
        return new WinnerRetriever();
    }

    @Bean
    ResultCheckerFacade resultCheckerFacade(NumberReceiverFacade numberReceiverFacade, WinningNumbersGeneratorFacade winningNumbersGeneratorFacade, WinnerRetriever winnerRetriever, PlayerRepository playerRepository){
        return new ResultCheckerFacade(numberReceiverFacade, winningNumbersGeneratorFacade, winnerRetriever, playerRepository);
    }

    ResultCheckerFacade createForTest(NumberReceiverFacade numberReceiverFacade, WinningNumbersGeneratorFacade numbersGeneratorFacade, PlayerRepository playerRepository){
        WinnerRetriever winnerRetriever = new WinnerRetriever();
        return new ResultCheckerFacade(numberReceiverFacade, numbersGeneratorFacade, winnerRetriever, playerRepository);
    }
}
