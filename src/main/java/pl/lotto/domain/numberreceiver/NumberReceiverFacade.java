package pl.lotto.domain.numberreceiver;

import lombok.AllArgsConstructor;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numberreceiver.dto.NumberReceiverResponseDto;
import pl.lotto.domain.numberreceiver.dto.TicketDto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static pl.lotto.domain.numberreceiver.ValidationResult.SUCCESS;

// zwraca date losowania
// zwraca wygrane po dacie losowania
@AllArgsConstructor
public class NumberReceiverFacade {

    private final NumberValidator validator;
    private final DrawDateFacade drawDateFacade;
    private final HashGenerable hashGenerator;
    private final TicketRepository ticketRepository;

    public NumberReceiverResponseDto inputNumbers(Set<Integer> numbersFromUser){
        List<ValidationResult> validationResultList = validator.validate(numbersFromUser);
        if(!validationResultList.isEmpty()) {
            String resultMessage = validator.createResultMessage();
            return new NumberReceiverResponseDto(null, resultMessage);
        }
        String hash = hashGenerator.getHash();

        LocalDateTime drawDate = drawDateFacade.retrieveNextDrawDate();

        TicketDto generatedTicket = TicketDto
                .builder()
                .hash(hash)
                .drawDate(drawDate)
                .numbers(numbersFromUser)
                .build();

        Ticket savedTicket = Ticket
                .builder()
                .hash(hash)
                .numbers(generatedTicket.numbers())
                .drawDate(generatedTicket.drawDate())
                .build();

        ticketRepository.save(savedTicket);

        return new NumberReceiverResponseDto(generatedTicket, SUCCESS.info);
    }

    public List<TicketDto> retrieveAllTicketsByNextDrawDate(){
        LocalDateTime nextDrawDate = drawDateFacade.retrieveNextDrawDate();
        return retrieveAllTicketsByNextDrawDate(nextDrawDate);
    }

    public List<TicketDto> retrieveAllTicketsByNextDrawDate(LocalDateTime date){
        LocalDateTime nextDrawDate = drawDateFacade.retrieveNextDrawDate();
        if(date.isAfter(nextDrawDate)){
            return Collections.emptyList();
        }
        return ticketRepository.findAllTicketsByDrawDate(date)
                .stream()
                .filter(ticket -> ticket.drawDate().isEqual(date))
                .map(ticket -> TicketDto
                        .builder()
                        .hash(ticket.hash())
                        .numbers(ticket.numbers())
                        .drawDate(ticket.drawDate())
                        .build())
                .collect(Collectors.toList());
    }

    public LocalDateTime retrieveNextDrawDate(){
        return drawDateFacade.retrieveNextDrawDate();
    }

    public TicketDto findByHash(String hash){
        Ticket ticket = ticketRepository.findByHash(hash);
        return TicketDto
                .builder()
                .hash(ticket.hash())
                .numbers(ticket.numbers())
                .drawDate(ticket.drawDate())
                .build();
    }
}
