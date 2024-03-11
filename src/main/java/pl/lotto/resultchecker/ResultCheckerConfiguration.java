package pl.lotto.resultchecker;

import pl.lotto.numbergenerator.WinningNumbersGeneratorFacade;
import pl.lotto.numberreceiver.NumberReceiverFacade;

public class ResultCheckerConfiguration {

    ResultCheckerFacade createForTest(NumberReceiverFacade numberReceiverFacade, WinningNumbersGeneratorFacade numbersGeneratorFacade, PlayerRepository playerRepository){
        WinnerRetriever winnerRetriever = new WinnerRetriever();
        return new ResultCheckerFacade(numberReceiverFacade, numbersGeneratorFacade, winnerRetriever, playerRepository);
    }
}
