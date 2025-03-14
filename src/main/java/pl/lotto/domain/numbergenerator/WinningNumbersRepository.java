package pl.lotto.domain.numbergenerator;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface WinningNumbersRepository extends MongoRepository<WinningNumbers, String> {
    Optional<WinningNumbers> findWinningNumbersByDate(LocalDateTime date);

    boolean existsByDate(LocalDateTime date);

//    WinningNumbers save(WinningNumbers winningNumbers);
}
