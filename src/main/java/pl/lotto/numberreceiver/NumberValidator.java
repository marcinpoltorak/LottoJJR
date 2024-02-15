package pl.lotto.numberreceiver;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class NumberValidator {
    private static final int MINIMUN_NUMBER = 1;
    private static final int MAXIMUN_NUMBER = 99;
    private static final int QUANTITY_OF_NUMBERS_FROM_USER = 6;

    List<ValidationResult> errors = new LinkedList<>();
    List<ValidationResult> validate(Set<Integer> numbersFromUser){
        if(!isCorrectNumbersSize(numbersFromUser)){
            errors.add(ValidationResult.WRONG_QUANTITY_OF_NUMBERS);
        }
        if(!isNumberInRange(numbersFromUser)){
            errors.add(ValidationResult.NOT_IN_RANGE);
        }
        return errors;
    }

    String createResultMessage(){
        return this.errors
                .stream()
                .map(validationResult -> validationResult.info)
                .collect(Collectors.joining(","));
    }

    boolean isCorrectNumbersSize(Set<Integer> numbersFromUser){
        return numbersFromUser.size() == QUANTITY_OF_NUMBERS_FROM_USER;
    }

    boolean isNumberInRange(Set<Integer> numbersFromUser) {
        return numbersFromUser.stream().allMatch(number -> number >= MINIMUN_NUMBER && number <= MAXIMUN_NUMBER);
    }

}
