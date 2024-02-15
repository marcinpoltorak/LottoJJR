package pl.lotto.numberreceiver;

public enum ValidationResult {
    WRONG_QUANTITY_OF_NUMBERS("YOU SHOULD GIVE 6 NUMBERS"),
    NOT_IN_RANGE("NUMBERS SHOULD BE IN RANGE OF 1 TO 99"),
    SUCCESS("SUCCESS");

    final String info;

    ValidationResult(String info){
        this.info = info;
    }
}
