package pl.lotto.resultannouncer;

import pl.lotto.resultannouncer.dto.ResponseDto;

public class ResultMapper {
    public static ResponseDto mapToDto(ResultResponse resultResponse){
        return ResponseDto.builder()
                .hash(resultResponse.hash())
                .numbers(resultResponse.numbers())
                .hitNumbers(resultResponse.hitNumbers())
                .drawDate(resultResponse.drawDate())
                .isWinner(resultResponse.isWinner())
                .build();
    }
}
