package pl.lotto.domain.resultchecker;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class WinnerRetriever {
    public List<Player> retrieveWinners(List<Ticket> tickets, Set<Integer> winningNumbers){
        return tickets.stream().map(ticket -> {
            Set<Integer> hitNumbers = calculateHits(winningNumbers, ticket);
            return buildPlayer(ticket, hitNumbers);
        }).toList();
    }

    private Player buildPlayer(Ticket ticket, Set<Integer> hitNumbers) {
        return Player.builder()
                .hash(ticket.hash())
                .numbers(ticket.numbers())
                .hitNumbers(hitNumbers)
                .drawDate(ticket.drawDate())
                .isWinner(isWinner(hitNumbers))
                .build();
    }

    private boolean isWinner(Set<Integer> hitNumbers) {
        return hitNumbers.size() >= 3;
    }

    private Set<Integer> calculateHits(Set<Integer> winningNumbers, Ticket ticket) {
        return ticket.numbers().stream().filter(winningNumbers::contains).collect(Collectors.toSet());
    }
}
