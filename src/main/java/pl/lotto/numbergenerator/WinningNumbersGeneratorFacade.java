package pl.lotto.numbergenerator;

import lombok.AllArgsConstructor;
import pl.lotto.drawdate.DrawDateFacade;
import pl.lotto.numbergenerator.dto.WinningNumbersDto;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
public class WinningNumbersGeneratorFacade {
    private final WinningNumbersGenerable winningNumbersGenerator;
    private final WinningNumbersValidator winningNumbersValidator;
    private final WinningNumbersRepository winningNumbersRepository;
    private final DrawDateFacade drawDateFacade;

    public WinningNumbersDto generateWinningNumbers(){
        LocalDateTime drawDate = drawDateFacade.retrieveNextDrawDate();
        Set<Integer> winningNumbers = winningNumbersGenerator.generateSixRandomNumbers();
        winningNumbersValidator.validate(winningNumbers);
        winningNumbersRepository.save(WinningNumbers.builder()
                .winningNumbers(winningNumbers)
                .drawDate(drawDate)
                .build());
        return WinningNumbersDto.builder()
                .winningNumbers(winningNumbers)
                .build();
    }

    public WinningNumbersDto retrieveWinningNumbersByDate(LocalDateTime date){
        WinningNumbers winningNumbers = winningNumbersRepository.findNumbersByDate(date)
                .orElseThrow(() -> new WinningNumbersNotFoundException("Not Found"));
        return WinningNumbersDto.builder()
                .winningNumbers(winningNumbers.winningNumbers())
                .date(winningNumbers.drawDate())
                .build();
    }

    public boolean areWinningNumbersGeneratedByDate(){
        LocalDateTime nextDrawDate = drawDateFacade.retrieveNextDrawDate();
        return winningNumbersRepository.existsByDate(nextDrawDate);
    }
}
