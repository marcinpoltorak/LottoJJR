package pl.lotto.domain.resultannouncer;

import org.junit.jupiter.api.Test;
import pl.lotto.domain.resultannouncer.dto.ResponseDto;
import pl.lotto.domain.resultannouncer.dto.ResultAnnouncerResponseDto;
import pl.lotto.domain.resultchecker.ResultCheckerFacade;
import pl.lotto.domain.resultchecker.dto.ResultDto;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static pl.lotto.domain.resultannouncer.MessageResponse.*;

class ResultAnnouncerFacadeTest {
    ResponseRepository responseRepository = new ResponseRepositoryTestImpl();
    ResultCheckerFacade resultCheckerFacade = mock(ResultCheckerFacade.class);

    @Test
    public void it_should_return_response_with_lose_message_if_ticket_is_not_winning_ticket(){
        // given
        LocalDateTime drawDate = LocalDateTime.of(2024, 2, 24, 12, 0, 0);
        String hash = "001";
        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerConfiguration().createForTest(resultCheckerFacade, responseRepository, Clock.systemUTC());
        ResultDto resultDto = ResultDto.builder()
                .hash(hash)
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of())
                .drawDate(drawDate)
                .isWinner(false)
                .build();
        when(resultCheckerFacade.findByHash(hash)).thenReturn(resultDto);
        // when
        ResultAnnouncerResponseDto resultAnnouncerResponseDto = resultAnnouncerFacade.checkResult(hash);
        // then
        ResponseDto responseDto = ResponseDto.builder()
                .hash(hash)
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of())
                .drawDate(drawDate)
                .isWinner(false)
                .build();
        ResultAnnouncerResponseDto expectedResponse = new ResultAnnouncerResponseDto(responseDto, LOSE_MESSAGE.info);
        assertThat(resultAnnouncerResponseDto).isEqualTo(expectedResponse);
    }

    @Test
    public void it_should_return_response_with_win_message_if_ticket_is_winning_ticket(){
        // given
        LocalDateTime drawDate = LocalDateTime.of(2024, 2, 24, 12, 0, 0);
        String hash = "001";
        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerConfiguration().createForTest(resultCheckerFacade, responseRepository, Clock.systemUTC());
        ResultDto resultDto = ResultDto.builder()
                .hash(hash)
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of(1, 2, 3))
                .drawDate(drawDate)
                .isWinner(true)
                .build();
        when(resultCheckerFacade.findByHash(hash)).thenReturn(resultDto);
        // when
        ResultAnnouncerResponseDto resultAnnouncerResponseDto = resultAnnouncerFacade.checkResult(hash);
        // then
        ResponseDto responseDto = ResponseDto.builder()
                .hash(hash)
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of(1, 2, 3))
                .drawDate(drawDate)
                .isWinner(true)
                .build();
        ResultAnnouncerResponseDto expectedResponse = new ResultAnnouncerResponseDto(responseDto, WIN_MESSAGE.info);
        assertThat(resultAnnouncerResponseDto).isEqualTo(expectedResponse);
    }

    @Test
    public void it_should_return_response_with_wait_message_if_date_is_before_announcement_time(){
        // given
        LocalDateTime drawDate = LocalDateTime.of(2024, 2, 24, 12, 0, 0);
        Clock clock = Clock.fixed(LocalDateTime.of(2024, 2, 20, 12, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
        String hash = "001";
        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerConfiguration().createForTest(resultCheckerFacade, responseRepository, clock);
        ResultDto resultDto = ResultDto.builder()
                .hash(hash)
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of())
                .drawDate(drawDate)
                .isWinner(false)
                .build();
        when(resultCheckerFacade.findByHash(hash)).thenReturn(resultDto);
        // when
        ResultAnnouncerResponseDto resultAnnouncerResponseDto = resultAnnouncerFacade.checkResult(hash);
        // then
        ResponseDto responseDto = ResponseDto.builder()
                .hash(hash)
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of())
                .drawDate(drawDate)
                .isWinner(false)
                .build();
        ResultAnnouncerResponseDto expectedResponse = new ResultAnnouncerResponseDto(responseDto, WAIT_MESSAGE.info);
        assertThat(resultAnnouncerResponseDto).isEqualTo(expectedResponse);
    }

    @Test
    public void it_should_return_response_with_hash_does_not_exist_if_hash_does_not_exist(){
        // given
        String hash = "001";
        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerConfiguration().createForTest(resultCheckerFacade, responseRepository, Clock.systemUTC());
        when(resultCheckerFacade.findByHash(hash)).thenReturn(null);
        // when
        ResultAnnouncerResponseDto resultAnnouncerResponseDto =  resultAnnouncerFacade.checkResult(hash);
        // then
        ResultAnnouncerResponseDto expectedResult = new ResultAnnouncerResponseDto(null, HASH_DOES_NOT_EXIST_MESSAGE.info);
        assertThat(resultAnnouncerResponseDto).isEqualTo(expectedResult);
    }

    @Test
    public void it_should_return_response_with_already_checked_message_if_response_is_saved_to_db(){
        // given
        LocalDateTime drawDate = LocalDateTime.of(2024, 2, 24, 12, 0, 0);
        String hash = "001";
        ResultDto resultDto = ResultDto.builder()
                .hash(hash)
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .drawDate(drawDate)
                .isWinner(true)
                .build();
        when(resultCheckerFacade.findByHash(hash)).thenReturn(resultDto);
        ResultAnnouncerFacade resultAnnouncerFacade = new ResultAnnouncerConfiguration().createForTest(resultCheckerFacade, responseRepository, Clock.systemUTC());
        ResultAnnouncerResponseDto resultAnnouncerResponseDto = resultAnnouncerFacade.checkResult(hash);
        String underTest = resultAnnouncerResponseDto.responseDto().hash();
        // when
        ResultAnnouncerResponseDto resultAnnouncerResponseDto1 = resultAnnouncerFacade.checkResult(underTest);
        // then
        ResultAnnouncerResponseDto expectedResponse = new ResultAnnouncerResponseDto(
                resultAnnouncerResponseDto1.responseDto(),
                ALREADY_CHECKED.info
        );
        assertThat(resultAnnouncerResponseDto1).isEqualTo(expectedResponse);
    }
}