package pl.lotto.numbergenerator;

import java.time.LocalDateTime;
import java.util.Optional;

public interface WinningNumbersRepository {
    Optional<WinningNumbers> findNumbersByDate(LocalDateTime date);

    boolean existsByDate(LocalDateTime date);

    WinningNumbers save(WinningNumbers winningNumbers);
}
