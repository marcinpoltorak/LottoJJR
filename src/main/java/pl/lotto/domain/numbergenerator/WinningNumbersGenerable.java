package pl.lotto.domain.numbergenerator;

import java.util.Set;

public interface WinningNumbersGenerable {
    SixRandomNumbersDto generateSixRandomNumbers(int lowerBand, int upperBand, int count);
}
