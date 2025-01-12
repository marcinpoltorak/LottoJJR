package pl.lotto.domain.resultchecker;

import lombok.Builder;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
record Ticket(@Id
        String hash,
        LocalDateTime drawDate,
        Set<Integer> numbers
) {
}
