package pl.lotto.resultannouncer;

import pl.lotto.resultchecker.ResultCheckerFacade;

import java.time.Clock;

public class ResultAnnouncerConfiguration {
    public ResultAnnouncerFacade createForTest(ResultCheckerFacade resultCheckerFacade, ResponseRepository responseRepository, Clock clock){
        return new ResultAnnouncerFacade(resultCheckerFacade, responseRepository, clock);
    }
}
