package pl.lotto.domain.numberreceiver;

import org.junit.jupiter.api.Test;
import pl.lotto.AdjustableClock;
import pl.lotto.domain.numberreceiver.dto.NumberReceiverResponseDto;
import pl.lotto.domain.numberreceiver.dto.TicketDto;

import java.time.*;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class NumberReceiverFacadeTest {
    private final TicketRepository ticketRepository = new TicketRepositoryTestImpl();
    Clock clock = Clock.systemUTC();

    @Test
    public void should_return_success_response_when_user_gave_six_numbers_in_range(){
        // given
        HashGenerable hashGenerator = new HashGeneratorTestImpl();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1,2,3,4,5,6);

        TicketDto generatedTicket = TicketDto
                .builder()
                .hash(hashGenerator.getHash())
                .numbers(numbersFromUser)
                .drawDate(numberReceiverFacade.retrieveNextDrawDate())
                .build();
        // when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        NumberReceiverResponseDto expectedResponse = new NumberReceiverResponseDto(generatedTicket, ValidationResult.SUCCESS.info);
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    public void should_return_failed_message_when_user_gave_six_number_but_one_number_is_out_of_range(){
        // given
        HashGenerator hashGenerator = new HashGenerator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1,2,3,4,100, 6);
        // when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        NumberReceiverResponseDto expectedResponse = new NumberReceiverResponseDto(null, ValidationResult.NOT_IN_RANGE.info);
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    public void should_return_failed_message_when_user_gave_six_number_but_one_number_is_negative_and_out_of_range(){
        // given
        HashGenerator hashGenerator = new HashGenerator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1,2,-10,4,88, 6);
        // when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        NumberReceiverResponseDto expectedResponse = new NumberReceiverResponseDto(null, ValidationResult.NOT_IN_RANGE.info);
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    public void should_return_failed_message_when_user_gave_more_numbers_than_six(){
        // given
        HashGenerator hashGenerator = new HashGenerator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1,2,3,4,5,6,7);
        // when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        NumberReceiverResponseDto expectedResponse = new NumberReceiverResponseDto(null, ValidationResult.WRONG_QUANTITY_OF_NUMBERS.info);
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    public void should_return_failed_message_when_user_gave_less_numbers_than_six(){
        // given
        HashGenerator hashGenerator = new HashGenerator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1,2,3,4,5);
        // when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        NumberReceiverResponseDto expectedResponse = new NumberReceiverResponseDto(null, ValidationResult.WRONG_QUANTITY_OF_NUMBERS.info);
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    public void it_should_return_correct_hash(){
        // given
        HashGenerator hashGenerator = new HashGenerator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1,2,3,4,5,6);
        // when
        String response = numberReceiverFacade.inputNumbers(numbersFromUser).ticketDto().hash();
        // then
        assertThat(response).hasSize(36);
        assertThat(response).isNotNull();
    }

    @Test
    public void it_should_return_correct_draw_date(){
        // given
        Clock clock = Clock.fixed(LocalDateTime.of(2024, 2, 17, 10,0, 0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/London"));
        HashGenerable hashGenerator = new HashGenerator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, ticketRepository);
        Set<Integer> numberFromUser = Set.of(1, 2, 3, 4, 5, 6);
        // when
        LocalDateTime drawDate = numberReceiverFacade.inputNumbers(numberFromUser).ticketDto().drawDate();
        // then
        LocalDateTime expectedDrawDate = LocalDateTime.of(2024, 2, 17, 12,0, 0);
        assertThat(drawDate).isEqualTo(expectedDrawDate);
    }

    @Test
    public void it_should_return_next_Saturday_draw_date_when_date_is_Saturday_noon(){
        // given
        Clock clock = Clock.fixed(LocalDateTime.of(2024, 2, 17, 12, 0 ,0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/London"));
        HashGenerator hashGenerator = new HashGenerator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        // when
        LocalDateTime drawDate = numberReceiverFacade.inputNumbers(numbersFromUser).ticketDto().drawDate();
        // then
        LocalDateTime expectedDrawDate = LocalDateTime.of(2024, 2, 24, 12, 0, 0);
        assertThat(drawDate).isEqualTo(expectedDrawDate);
    }

    @Test
    public void it_should_return_next_Saturday_draw_date_when_date_is_Saturday_afternoon() {
        // given
        Clock clock = Clock.fixed(LocalDateTime.of(2024, 2, 17, 16, 0 ,0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/London"));
        HashGenerator hashGenerator = new HashGenerator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        // when
        LocalDateTime drawDate = numberReceiverFacade.inputNumbers(numbersFromUser).ticketDto().drawDate();
        // then
        LocalDateTime expectedDrawDate = LocalDateTime.of(2024, 2, 24, 12, 0, 0);
        assertThat(drawDate).isEqualTo(expectedDrawDate);
    }

    @Test
    public void it_should_return_tickets_with_correct_draw_date() {
        // given
        HashGenerable hashGenerator = new HashGenerator();
        Instant instant = LocalDateTime.of(2024, 2, 16, 10, 0, 0).toInstant(ZoneOffset.UTC);
        ZoneId zoneId = ZoneId.of("Europe/London");
        AdjustableClock clock = new AdjustableClock(instant, zoneId);
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        NumberReceiverResponseDto numberReceiverResponseDto = numberReceiverFacade.inputNumbers(numbersFromUser);
        clock.plusDays(1);
        NumberReceiverResponseDto numberReceiverResponseDto1 = numberReceiverFacade.inputNumbers(numbersFromUser);
        clock.plusDays(1);
        NumberReceiverResponseDto numberReceiverResponseDto2 = numberReceiverFacade.inputNumbers(numbersFromUser);
        clock.plusDays(1);
        NumberReceiverResponseDto numberReceiverResponseDto3 = numberReceiverFacade.inputNumbers(numbersFromUser);
        clock.plusDays(1);
        NumberReceiverResponseDto numberReceiverResponseDto4 = numberReceiverFacade.inputNumbers(numbersFromUser);
        LocalDateTime drawDate = numberReceiverResponseDto.ticketDto().drawDate();
        TicketDto ticketDto = numberReceiverResponseDto.ticketDto();
        TicketDto ticketDto1 = numberReceiverResponseDto1.ticketDto();
        // when
        List<TicketDto> ticketDtosByDrawDate = numberReceiverFacade.retrieveAllTicketsByNextDrawDate(drawDate);
        // then
        assertThat(ticketDtosByDrawDate).containsOnly(ticketDto, ticketDto1);
    }

    @Test
    public void it_should_return_empty_list_when_there_are_no_tickets() {
        // given
        HashGenerator hashGenerator = new HashGenerator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, ticketRepository);

        // when
        List<TicketDto> ticketDtoList = numberReceiverFacade.retrieveAllTicketsByNextDrawDate();
        // then
        assertThat(ticketDtoList).isEmpty();
    }

    @Test
    public void it_should_return_empty_list_when_given_next_draw_date() {
        // given
        HashGenerator hashGenerator = new HashGenerator();
        Clock clock = Clock.fixed(LocalDateTime.of(2024, 2, 15, 12, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/London"));
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, ticketRepository);
        LocalDateTime drawDate = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6)).ticketDto().drawDate();
        // when
        List<TicketDto> ticketDtoList = numberReceiverFacade.retrieveAllTicketsByNextDrawDate(drawDate.plusWeeks(1L));
        // then
        assertThat(ticketDtoList).isEmpty();
    }
}