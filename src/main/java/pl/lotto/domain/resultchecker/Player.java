package pl.lotto.domain.resultchecker;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record Player(String hash,
                     LocalDateTime drawDate,
                     Set<Integer> numbers,
                     Set<Integer> hitNumbers,
                     boolean isWinner) {
}
