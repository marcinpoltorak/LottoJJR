package pl.lotto.domain.numberreceiver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.drawdate.DrawDateGenerator;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class NumberReceiverConfiguration {

    @Bean
    Clock clock(){
        return Clock.systemUTC();
    }

    @Bean
    HashGenerable hashGenerable(){
        return new HashGenerator();
    }

    @Bean
    NumberReceiverFacade numberReceiverFacade(HashGenerable hashGenerator, Clock clock, TicketRepository ticketRepository){
        NumberValidator numberValidator = new NumberValidator();
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);
        DrawDateFacade drawDateFacade = new DrawDateFacade(drawDateGenerator);
        return new NumberReceiverFacade(numberValidator, drawDateFacade, hashGenerator, ticketRepository);
    }
}
