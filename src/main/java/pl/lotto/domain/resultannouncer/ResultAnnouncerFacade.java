package pl.lotto.domain.resultannouncer;

import lombok.AllArgsConstructor;
import pl.lotto.domain.resultannouncer.dto.ResponseDto;
import pl.lotto.domain.resultannouncer.dto.ResultAnnouncerResponseDto;
import pl.lotto.domain.resultchecker.ResultCheckerFacade;
import pl.lotto.domain.resultchecker.dto.ResultDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

import static pl.lotto.domain.resultannouncer.MessageResponse.*;

@AllArgsConstructor
public class ResultAnnouncerFacade {
    private final ResultCheckerFacade resultChecker;
    private final ResponseRepository responseRepository;
    private final Clock clock;

    public ResultAnnouncerResponseDto checkResult(String hash){
        if(responseRepository.existsById(hash)){
            Optional<ResultResponse> resultResponseCached = responseRepository.findById(hash);
            if(resultResponseCached.isPresent()){
                return new ResultAnnouncerResponseDto(ResultMapper.mapToDto(resultResponseCached.get()), ALREADY_CHECKED.info);
            }
        }
        ResultDto resultDto = resultChecker.findById(hash);
        if (resultDto==null){
            return new ResultAnnouncerResponseDto(null, HASH_DOES_NOT_EXIST_MESSAGE.info);
        }
        ResponseDto responseDto = buildResponseDto(resultDto);
        responseRepository.save(buildResponse(responseDto));
        if (responseRepository.existsById(hash) && !isAfterResultAnnouncementTime(resultDto)){
            return new ResultAnnouncerResponseDto(responseDto, WAIT_MESSAGE.info);
        }
        if(resultChecker.findById(hash).isWinner()){
            return new ResultAnnouncerResponseDto(responseDto, WIN_MESSAGE.info);
        }
        return new ResultAnnouncerResponseDto(responseDto, LOSE_MESSAGE.info);
    }

    private boolean isAfterResultAnnouncementTime(ResultDto resultDto) {
        LocalDateTime announcementDateTime = resultDto.drawDate();
        return LocalDateTime.now(clock).isAfter(announcementDateTime);
    }

    private static ResultResponse buildResponse(ResponseDto responseDto) {
        return ResultResponse.builder()
                .hash(responseDto.hash())
                .numbers(responseDto.numbers())
                .hitNumbers(responseDto.hitNumbers())
                .drawDate(responseDto.drawDate())
                .isWinner(responseDto.isWinner())
                .build();
    }

    private static ResponseDto buildResponseDto(ResultDto resultDto) {
        return ResponseDto.builder()
                .hash(resultDto.hash())
                .numbers(resultDto.numbers())
                .hitNumbers(resultDto.hitNumbers())
                .drawDate(resultDto.drawDate())
                .isWinner(resultDto.isWinner())
                .build();
    }
}
