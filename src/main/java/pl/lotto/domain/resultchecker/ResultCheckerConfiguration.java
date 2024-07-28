package pl.lotto.domain.resultchecker;

import pl.lotto.domain.numbergenerator.WinningNumbersGeneratorFacade;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;

public class ResultCheckerConfiguration {

    ResultCheckerFacade createForTest(NumberReceiverFacade numberReceiverFacade, WinningNumbersGeneratorFacade numbersGeneratorFacade, PlayerRepository playerRepository){
        WinnerRetriever winnerRetriever = new WinnerRetriever();
        return new ResultCheckerFacade(numberReceiverFacade, numbersGeneratorFacade, winnerRetriever, playerRepository);
    }
}
