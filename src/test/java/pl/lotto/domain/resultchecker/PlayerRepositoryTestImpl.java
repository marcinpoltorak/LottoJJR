package pl.lotto.domain.resultchecker;

import java.util.*;

public class PlayerRepositoryTestImpl implements PlayerRepository {
    private Map<String, Player> playersList = new HashMap<>();
    @Override
    public List<Player> saveAll(List<Player> players) {
        players.forEach(player -> playersList.put(player.hash(), player));
        return players;
    }

    @Override
    public Optional<Player> findById(String hash) {
        return Optional.ofNullable(playersList.get(hash));
    }
}
