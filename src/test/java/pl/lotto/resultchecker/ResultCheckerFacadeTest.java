package pl.lotto.resultchecker;

import org.junit.jupiter.api.Test;
import pl.lotto.numbergenerator.WinningNumbersGeneratorFacade;
import pl.lotto.numbergenerator.dto.WinningNumbersDto;
import pl.lotto.numberreceiver.NumberReceiverFacade;
import pl.lotto.numberreceiver.dto.TicketDto;
import pl.lotto.resultchecker.dto.PlayersDto;
import pl.lotto.resultchecker.dto.ResultDto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ResultCheckerFacadeTest {

    private final PlayerRepository playerRepository = new PlayerRepositoryTestImpl();
    private final NumberReceiverFacade numberReceiverFacade = mock(NumberReceiverFacade.class);
    private final WinningNumbersGeneratorFacade winningNumbersGeneratorFacade = mock(WinningNumbersGeneratorFacade.class);

    @Test
    public void it_should_generate_all_players_with_correct_message(){
        // given
        LocalDateTime drawDate = LocalDateTime.of(2024, 2, 24, 12, 0, 0);
        when(winningNumbersGeneratorFacade.generateWinningNumbers()).thenReturn(WinningNumbersDto.builder()
                .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .build());
        ResultCheckerFacade resultCheckerFacade = new ResultCheckerConfiguration().createForTest(numberReceiverFacade, winningNumbersGeneratorFacade, playerRepository);
        when(numberReceiverFacade.retrieveAllTicketsByNextDrawDate()).thenReturn(
                List.of(TicketDto.builder()
                        .hash("01")
                        .drawDate(drawDate)
                        .numbers(Set.of(1, 2, 3, 4, 5, 6))
                        .build(),
                        TicketDto.builder()
                        .hash("02")
                        .drawDate(drawDate)
                        .numbers(Set.of(7, 9, 3, 8, 5, 6))
                        .build(),
                        TicketDto.builder()
                        .hash("03")
                        .drawDate(drawDate)
                        .numbers(Set.of(7, 8, 9, 11, 13, 16))
                        .build()
                )
        );
        // when
        PlayersDto playersDto = resultCheckerFacade.generateWinners();
        // then
        List<ResultDto> results = playersDto.results();
        ResultDto resultDto = ResultDto.builder()
                .hash("01")
                .numbers(Set.of(1, 2, 3, 4, 5, 6))
                .hitNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .drawDate(drawDate)
                .isWinner(true)
                .build();
        ResultDto resultDto1 = ResultDto.builder()
                .hash("02")
                .numbers(Set.of(7, 9, 3, 8, 5, 6))
                .hitNumbers(Set.of(3, 5, 6))
                .drawDate(drawDate)
                .isWinner(true)
                .build();
        ResultDto resultDto2 = ResultDto.builder()
                .hash("02")
                .numbers(Set.of(7, 9, 3, 8, 5, 6))
                .hitNumbers(Set.of(3, 5, 6))
                .drawDate(drawDate)
                .isWinner(true)
                .build();
        assertThat(results).contains(resultDto, resultDto1, resultDto2);
        String message = playersDto.message();
        assertThat(message).isEqualTo("Winners succeeded to retrieve");
    }

    @Test
    public void it_should_generate_fail_message_when_winningNumbers_equal_null(){
        // given
        ResultCheckerFacade resultCheckerFacade = new ResultCheckerConfiguration().createForTest(numberReceiverFacade, winningNumbersGeneratorFacade, playerRepository);
        when(winningNumbersGeneratorFacade.generateWinningNumbers()).thenReturn(WinningNumbersDto.builder()
                .winningNumbers(null)
                .build());
        // when
        PlayersDto playersDto = resultCheckerFacade.generateWinners();
        // then
        String message = playersDto.message();
        assertThat(message).isEqualTo("Winners failed to retrieve");
    }

    @Test
    public void it_should_generate_fail_message_when_winningNumbers_is_empty(){
        // given
        ResultCheckerFacade resultCheckerFacade = new ResultCheckerConfiguration().createForTest(numberReceiverFacade, winningNumbersGeneratorFacade, playerRepository);
        when(winningNumbersGeneratorFacade.generateWinningNumbers()).thenReturn(WinningNumbersDto.builder()
                .winningNumbers(Collections.emptySet())
                .build());
        // when
        PlayersDto playersDto = resultCheckerFacade.generateWinners();
        // then
        String message = playersDto.message();
        assertThat(message).isEqualTo("Winners failed to retrieve");
    }

    @Test
    public void it_should_return_result_with_correct_credentials(){
        // given
        LocalDateTime drawDate = LocalDateTime.of(2024, 2, 24, 12, 0, 0);
        when(winningNumbersGeneratorFacade.generateWinningNumbers()).thenReturn(WinningNumbersDto.builder()
                .winningNumbers(Set.of(1, 2, 3, 4, 5, 6))
                .build());
        String hash = "01";
        when(numberReceiverFacade.retrieveAllTicketsByNextDrawDate()).thenReturn(
                List.of(TicketDto.builder()
                                .hash(hash)
                                .drawDate(drawDate)
                                .numbers(Set.of(7, 8, 9, 10, 11, 12))
                                .build(),
                        TicketDto.builder()
                                .hash("02")
                                .drawDate(drawDate)
                                .numbers(Set.of(7, 9, 3, 8, 5, 6))
                                .build(),
                        TicketDto.builder()
                                .hash("03")
                                .drawDate(drawDate)
                                .numbers(Set.of(7, 8, 9, 11, 13, 16))
                                .build()
                )
        );
        ResultCheckerFacade resultCheckerFacade = new ResultCheckerConfiguration().createForTest(numberReceiverFacade, winningNumbersGeneratorFacade, playerRepository);
        resultCheckerFacade.generateWinners();
        // when
        ResultDto resultDto = resultCheckerFacade.findByHash(hash);
        // then
        ResultDto expectedResult = ResultDto.builder()
                .hash(hash)
                .numbers(Set.of(7, 8, 9, 10, 11, 12))
                .hitNumbers(Set.of())
                .drawDate(drawDate)
                .isWinner(false)
                .build();
        assertThat(resultDto).isEqualTo(expectedResult);
    }
}