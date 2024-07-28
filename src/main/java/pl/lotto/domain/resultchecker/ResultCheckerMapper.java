package pl.lotto.domain.resultchecker;

import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.resultchecker.dto.ResultDto;

import java.util.List;

public class ResultCheckerMapper {
    public static List<Ticket> mapTicketDto(List<TicketDto> allTicketByDrawDate){
        return allTicketByDrawDate.stream().map(ticketDto -> Ticket.builder()
                .drawDate(ticketDto.drawDate())
                .hash(ticketDto.hash())
                .numbers(ticketDto.numbers())
                .build()).toList();
    }

    public static List<ResultDto> mapPlayersToResultDto(List<Player> winners) {
        return winners.stream().map(player -> ResultDto.builder()
                .hash(player.hash())
                .numbers(player.numbers())
                .hitNumbers(player.hitNumbers())
                .drawDate(player.drawDate())
                .isWinner(player.isWinner())
                .build()).toList();
    }
}
