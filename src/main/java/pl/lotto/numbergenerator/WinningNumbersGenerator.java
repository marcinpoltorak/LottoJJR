package pl.lotto.numbergenerator;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class WinningNumbersGenerator implements WinningNumbersGenerable{
    private final int LOWER_BAND = 1;
    private final int UPPER_BAND = 99;
    private final int RANDOM_NUMBER_BOUND = (UPPER_BAND - LOWER_BAND) +1;
    @Override
    public Set<Integer> generateSixRandomNumbers() {
        Set<Integer> randomNumbers = new HashSet<>();
        while(isAmountOfNumbersLowerThanSix(randomNumbers)){
            int randomNumber = generateRandomNumber();
            randomNumbers.add(randomNumber);
        }
        return randomNumbers;
    }

    private int generateRandomNumber() {
        Random random = new SecureRandom();
        return random.nextInt(RANDOM_NUMBER_BOUND) + 1;
    }

    private boolean isAmountOfNumbersLowerThanSix(Set<Integer> randomNumbers) {
        return randomNumbers.size() < 6;
    }


}
