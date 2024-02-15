package pl.lotto.numberreceiver;

import lombok.AllArgsConstructor;
import pl.lotto.numberreceiver.dto.NumberReceiverResponseDto;
import pl.lotto.numberreceiver.dto.TicketDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static pl.lotto.numberreceiver.ValidationResult.SUCCESS;
import static pl.lotto.numberreceiver.dto.NumberReceiverResponseDto.*;

// zwraca date losowania
// zwraca wygrane po dacie losowania
@AllArgsConstructor
public class NumberReceiverFacade {

    private final NumberValidator validator;
    private final DrawDateGenerator drawDateGenerator;
    private final HashGenerable hashGenerator;
    private final TicketRepository ticketRepository;

    public NumberReceiverResponseDto inputNumbers(Set<Integer> numbersFromUser){
        List<ValidationResult> validationResultList = validator.validate(numbersFromUser);
        if(!validationResultList.isEmpty()) {
            String resultMessage = validator.createResultMessage();
            return new NumberReceiverResponseDto(null, resultMessage);
        }
        String hash = hashGenerator.getHash();

        LocalDateTime drawDate = drawDateGenerator.getNextDrawDate();

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

    public List<TicketDto> retrieveAllTicketByNextDrawDate(){
        LocalDateTime nextDrawDate = drawDateGenerator.getNextDrawDate();
        return retrieveAllTicketByNextDrawDate(nextDrawDate);
    }

    public List<TicketDto> retrieveAllTicketByNextDrawDate(LocalDateTime date){
        LocalDateTime nextDrawDate = drawDateGenerator.getNextDrawDate();
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
        return drawDateGenerator.getNextDrawDate();
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
