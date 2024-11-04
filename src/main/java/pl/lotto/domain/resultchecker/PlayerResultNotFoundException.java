package pl.lotto.domain.resultchecker;

public class PlayerResultNotFoundException extends RuntimeException {
    public PlayerResultNotFoundException(String message) {
        super(message);
    }
}
