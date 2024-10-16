package pl.lotto.domain.numbergenerator;

import java.util.Set;

public class WinningNumberGeneratorTestImpl implements WinningNumbersGenerable {
    private Set<Integer> generatedNumbers;

    public WinningNumberGeneratorTestImpl() {
        generatedNumbers = Set.of(1, 2, 3, 4, 5, 6);
    }

    WinningNumberGeneratorTestImpl(Set<Integer> generatedNumbers) {
        this.generatedNumbers = generatedNumbers;
    }

    @Override
    public SixRandomNumbersDto generateSixRandomNumbers(int lowerBand, int upperBand, int count) {
        return SixRandomNumbersDto.builder().numbers(generatedNumbers).build();
    }
}
