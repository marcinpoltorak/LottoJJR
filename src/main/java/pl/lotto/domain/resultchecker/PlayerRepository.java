package pl.lotto.domain.resultchecker;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PlayerRepository extends MongoRepository<Player, String> {
//    List<Player> saveAll(List<Player> players);
//    Optional<Player> findById(String hash);
}
