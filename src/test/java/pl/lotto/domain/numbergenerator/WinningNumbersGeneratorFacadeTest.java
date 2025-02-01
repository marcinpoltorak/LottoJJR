package pl.lotto.domain.numbergenerator;

import org.junit.jupiter.api.Test;
import pl.lotto.domain.drawdate.DrawDateFacade;
import pl.lotto.domain.numbergenerator.dto.WinningNumbersDto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WinningNumbersGeneratorFacadeTest {

    private final  WinningNumbersRepository winningNumbersRepository = new WinningNumbersRepositoryTestImpl();

    DrawDateFacade drawDateFacade = mock(DrawDateFacade.class);

    @Test
    void it_should_return_set_of_required_size() {
        // given
        when(drawDateFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        WinningNumbersGenerable generator = new WinningNumberGeneratorTestImpl();
        WinningNumbersGeneratorFacade generatorFacade = new NumberGeneratorConfiguration().createForTest(generator, winningNumbersRepository, drawDateFacade);

        // when
        WinningNumbersDto winningNumbersDto = generatorFacade.generateWinningNumbers();

        // then
        assertThat(winningNumbersDto.getWinningNumbers().size()).isEqualTo(6);
    }

    @Test
    void it_should_return_set_of_numbers_in_required_range() {
        // given
        when(drawDateFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        WinningNumbersGenerable generator = new WinningNumberGeneratorTestImpl();
        WinningNumbersGeneratorFacade winningNumbersGeneratorFacade = new NumberGeneratorConfiguration().createForTest(generator, winningNumbersRepository, drawDateFacade);
        // when
        WinningNumbersDto winningNumbersDto = winningNumbersGeneratorFacade.generateWinningNumbers();
        // then
        int upperBand = 99;
        int lowerBand = 1;
        Set<Integer> winningNumbers = winningNumbersDto.getWinningNumbers();
        boolean numbersInRange = winningNumbers.stream().allMatch(number -> number >= lowerBand && number <=upperBand);
        assertThat(numbersInRange).isTrue();
    }

    @Test
    void it_should_throw_and_exception_when_number_not_in_range() {
        // given
        when(drawDateFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        Set<Integer> numbersOutOfRange = Set.of(1, 2, 3, 4, 5, 100);
        WinningNumbersGenerable generator = new WinningNumberGeneratorTestImpl(numbersOutOfRange);
        WinningNumbersGeneratorFacade winningNumbersGeneratorFacade = new NumberGeneratorConfiguration().createForTest(generator, winningNumbersRepository, drawDateFacade);
        // when
        // then
        assertThrows(IllegalStateException.class, winningNumbersGeneratorFacade::generateWinningNumbers, "Number out of range");
    }

    @Test
    void it_should_return_collection_of_unique_values(){
        // given
        WinningNumbersGenerable generator = new WinningNumberGeneratorTestImpl();
        when(drawDateFacade.retrieveNextDrawDate()).thenReturn(LocalDateTime.now());
        WinningNumbersGeneratorFacade numbersGeneratorFacade = new NumberGeneratorConfiguration().createForTest(generator, winningNumbersRepository, drawDateFacade);
        // when
        WinningNumbersDto winningNumbersDto = numbersGeneratorFacade.generateWinningNumbers();
        // then
        int generatedNumbersSize = new HashSet<>(winningNumbersDto.getWinningNumbers()).size();
        assertThat(generatedNumbersSize).isEqualTo(6);
    }

    @Test
    void it_should_return_winning_numbers_by_date(){
        // given
        LocalDateTime drawDate = LocalDateTime.of(2024, 2, 24, 12, 0, 0);
        Set<Integer> generatedWinningNumbers = Set.of(1, 2, 3, 4, 5, 6);
        String id = UUID.randomUUID().toString();
        WinningNumbers winningNumbers = WinningNumbers.builder()
                .id(id)
                .winningNumbers(generatedWinningNumbers)
                .date(drawDate)
                .build();
        winningNumbersRepository.save(winningNumbers);
        when(drawDateFacade.retrieveNextDrawDate()).thenReturn(drawDate);
        WinningNumbersGenerable generator = new WinningNumberGeneratorTestImpl();
        WinningNumbersGeneratorFacade numbersGeneratorFacade = new NumberGeneratorConfiguration().createForTest(generator, winningNumbersRepository, drawDateFacade);
        // when
        WinningNumbersDto winningNumbersDto = numbersGeneratorFacade.retrieveWinningNumbersByDate(drawDate);
        // then
        WinningNumbersDto expectedWinningNumbers = WinningNumbersDto.builder()
                .winningNumbers(generatedWinningNumbers)
                .date(drawDate)
                .build();
        assertThat(winningNumbersDto).isEqualTo(expectedWinningNumbers);
    }

    @Test
    void it_should_throw_an_exception_when_fail_to_retrieve_numbers_by_given_date(){
        // given
        LocalDateTime drawDate = LocalDateTime.of(2024, 1, 6, 12, 0, 0);
        when(drawDateFacade.retrieveNextDrawDate()).thenReturn(drawDate);
        WinningNumbersGenerable generator = new WinningNumberGeneratorTestImpl();
        WinningNumbersGeneratorFacade numbersGeneratorFacade = new NumberGeneratorConfiguration().createForTest(generator, winningNumbersRepository, drawDateFacade);
        // when
        // then
        assertThrows(WinningNumbersNotFoundException.class, () -> numbersGeneratorFacade.retrieveWinningNumbersByDate(drawDate), "Not Found");
    }

    @Test
    void it_should_return_true_when_numbers_are_generated_by_given_date(){
        // given
        LocalDateTime drawDate = LocalDateTime.of(2024, 2, 24, 12, 0 ,0);
        when(drawDateFacade.retrieveNextDrawDate()).thenReturn(drawDate);
        WinningNumbersGenerable generator = new WinningNumberGeneratorTestImpl();
        WinningNumbersGeneratorFacade numbersGeneratorFacade = new NumberGeneratorConfiguration().createForTest(generator, winningNumbersRepository, drawDateFacade);
        numbersGeneratorFacade.generateWinningNumbers();
        // when
        boolean winningNumberGenerated = numbersGeneratorFacade.areWinningNumbersGeneratedByDate();
        // then
        assertThat(winningNumberGenerated).isTrue();
    }
}