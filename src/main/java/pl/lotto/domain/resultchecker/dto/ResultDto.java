package pl.lotto.domain.resultchecker.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record ResultDto(
        String hash,
        LocalDateTime drawDate,
        Set<Integer> numbers,
        Set<Integer> hitNumbers,
        boolean isWinner
) {
}
