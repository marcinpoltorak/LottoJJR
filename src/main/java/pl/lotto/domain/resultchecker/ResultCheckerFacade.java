package pl.lotto.domain.resultchecker;

import lombok.AllArgsConstructor;
import pl.lotto.domain.numbergenerator.WinningNumbersGeneratorFacade;
import pl.lotto.domain.numbergenerator.dto.WinningNumbersDto;
import pl.lotto.domain.numberreceiver.NumberReceiverFacade;
import pl.lotto.domain.numberreceiver.dto.TicketDto;
import pl.lotto.domain.resultchecker.dto.PlayersDto;
import pl.lotto.domain.resultchecker.dto.ResultDto;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class ResultCheckerFacade {
    private final NumberReceiverFacade numberReceiverFacade;
    private final WinningNumbersGeneratorFacade winningNumbersGeneratorFacade;
    private final WinnerRetriever winnerRetriever;
    private final PlayerRepository playerRepository;


    public PlayersDto generateWinners(){
        List<TicketDto> allTicketByDrawDate = numberReceiverFacade.retrieveAllTicketsByNextDrawDate();
        List<Ticket> tickets = ResultCheckerMapper.mapTicketDto(allTicketByDrawDate);
        WinningNumbersDto winningNumbersDto = winningNumbersGeneratorFacade.generateWinningNumbers();
        Set<Integer> winningNumbers = winningNumbersDto.getWinningNumbers();
        if(winningNumbers == null || winningNumbers.isEmpty()){
            return PlayersDto.builder()
                    .message("Winners failed to retrieve")
                    .build();
        }
        List<Player> winners = winnerRetriever.retrieveWinners(tickets, winningNumbers);
        playerRepository.saveAll(winners);
        return PlayersDto.builder()
                .results(ResultCheckerMapper.mapPlayersToResultDto(winners))
                .message("Winners succeeded to retrieve")
                .build();
    }

    public ResultDto findByHash(String hash){
        Player player = playerRepository.findById(hash)
                .orElseThrow(() -> new PlayerResultNotFoundException("Not found for id: " + hash));
        return ResultDto.builder()
                .hash(player.hash())
                .numbers(player.numbers())
                .hitNumbers(player.hitNumbers())
                .drawDate(player.drawDate())
                .isWinner(player.isWinner())
                .build();
    }
}
