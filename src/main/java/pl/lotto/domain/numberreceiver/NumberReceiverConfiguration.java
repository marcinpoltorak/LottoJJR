package pl.lotto.domain.numberreceiver;

import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.drawdate.DrawDateGenerator;

import java.time.Clock;

public class NumberReceiverConfiguration {

    NumberReceiverFacade createForTest(HashGenerable hashGenerator, Clock clock, TicketRepository ticketRepository){
        NumberValidator numberValidator = new NumberValidator();
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);
        DrawDateFacade drawDateFacade = new DrawDateFacade(drawDateGenerator);
        return new NumberReceiverFacade(numberValidator, drawDateFacade, hashGenerator, ticketRepository);
    }
}
