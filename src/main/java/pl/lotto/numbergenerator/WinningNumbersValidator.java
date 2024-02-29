package pl.lotto.numbergenerator;

import java.util.Set;

public class WinningNumbersValidator {
    private final int LOWER_BAND = 0;
    private final int UPPER_BAND = 99;

    public Set<Integer> validate(Set<Integer> winningNumbers){
        if(isOutOfRange(winningNumbers)){
            throw new IllegalStateException("Number out of range");
        }
        return winningNumbers;
    }

    private boolean isOutOfRange(Set<Integer> winningNumbers) {
        return winningNumbers.stream().anyMatch(number -> number < LOWER_BAND || number > UPPER_BAND);
    }
}
