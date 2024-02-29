package pl.lotto.numbergenerator;

import pl.lotto.drawdate.DrawDateFacade;

public class NumberGeneratorConfiguration {

    WinningNumbersGeneratorFacade createForTest(WinningNumbersGenerable generator, WinningNumbersRepository repository, DrawDateFacade drawDateFacade){
        WinningNumbersValidator validator = new WinningNumbersValidator();
        return new WinningNumbersGeneratorFacade(generator, validator, repository, drawDateFacade);
    }
}
