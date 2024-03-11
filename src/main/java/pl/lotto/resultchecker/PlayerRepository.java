package pl.lotto.resultchecker;

import pl.lotto.resultchecker.dto.ResultDto;

import java.util.List;
import java.util.Optional;

public interface PlayerRepository {
    List<Player> saveAll(List<Player> players);
    Optional<Player> findById(String hash);
}
