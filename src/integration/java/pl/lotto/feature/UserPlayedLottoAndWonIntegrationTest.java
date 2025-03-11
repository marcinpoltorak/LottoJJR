package pl.lotto.feature;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;
import pl.lotto.BaseIntegrationTest;
import pl.lotto.domain.numbergenerator.WinningNumbersGeneratorFacade;
import pl.lotto.domain.numbergenerator.WinningNumbersNotFoundException;
import pl.lotto.domain.numberreceiver.dto.NumberReceiverResponseDto;
import pl.lotto.domain.resultannouncer.dto.ResultAnnouncerResponseDto;
import pl.lotto.domain.resultchecker.PlayerResultNotFoundException;
import pl.lotto.domain.resultchecker.ResultCheckerFacade;
import pl.lotto.domain.resultchecker.dto.ResultDto;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserPlayedLottoAndWonIntegrationTest extends BaseIntegrationTest {

    @Autowired
    public WinningNumbersGeneratorFacade winningNumbersGeneratorFacade;

    @Autowired
    public ResultCheckerFacade resultCheckerFacade;

    @Test
    public void should_user_win_and_system_should_generate_winners() throws Exception {
        //step 1: external service returns 6 random numbers (1,2,3,4,5,6)
        // given
        wireMockServer.stubFor(WireMock.get("/api/v1.0/randomnumber?min=1&max=99&count=14")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                [1, 2, 3, 4, 5, 6, 55, 57, 64, 87, 23, 15, 25, 74]
                                """.trim()
                        )));
        // when
        // then
        //step 2: system fetched winning numbers for draw date: 19.11.2022 12:00
        // given
        LocalDateTime drawDate = LocalDateTime.of(2022, 11, 19, 12, 0, 0);
        // when && then
        await()
                .atMost(Duration.ofSeconds(20))
                .pollInterval(Duration.ofSeconds(1))
                .until(() -> {
                        try {
                            return !winningNumbersGeneratorFacade.retrieveWinningNumbersByDate(drawDate).getWinningNumbers().isEmpty();
                        } catch (WinningNumbersNotFoundException e){
                            return false;
                        }
                    }
                );
        //step 3: user made POST /inputNumbers with 6 numbers (1, 2, 3, 4, 5, 6) at 16-11-2022 10:00 and system returned OK(200) with message: “success” and Ticket (DrawDate:19.11.2022 12:00 (Saturday), TicketId: sampleTicketId)
        // when
        ResultActions perform = mockMvc.perform(post("/inputNumbers")
                .content("""
                        {
                        "inputNumbers": [1, 2, 3, 4, 5, 6]
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON)
        );
        // then
        MvcResult mvcResult = perform.andExpect(status().isOk()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        NumberReceiverResponseDto numberReceiverResponseDto = objectMapper.readValue(json, NumberReceiverResponseDto.class);
        String ticketId = numberReceiverResponseDto.ticketDto().hash();
        assertAll(
                () -> assertThat(numberReceiverResponseDto.ticketDto().drawDate()).isEqualTo(drawDate),
                () -> assertThat(ticketId).isNotNull(),
                () -> assertThat(numberReceiverResponseDto.message()).isEqualTo("SUCCESS")
        );
        // step 4: user made GET /results/notExistingId and system returned 404(NOT_FOUND) and body with (message: Not found for hash: notExistingId and status NOT_FOUND)
        // given & when
        ResultActions performGetResultsWithNotExistingId = mockMvc.perform(get("/results/notExistingId"));
        // then
        performGetResultsWithNotExistingId.andExpect(status().isNotFound())
                .andExpect(content().json("""
                        {
                        "message": "Not found for id: notExistingId",
                        "status": "NOT_FOUND"
                        }
                        """.trim()
                ));
        // step 5: 3 days and 55 minutes passed, and it is 5 minutes before the draw date (19.11.2022 11:55)
        clock.plusDaysAndMinutes(3, 55);
        // step 6: system generated result for TicketId: sampleTicketId with draw date 19.11.2022 12:00, and saved it with 6 hits
        await().atMost(20, TimeUnit.SECONDS)
                .pollInterval(Duration.ofSeconds(1))
                .until(
                        () -> {
                            try {
                                ResultDto resultDto = resultCheckerFacade.findById(ticketId);
                                return !resultDto.numbers().isEmpty();
                            } catch (PlayerResultNotFoundException exception){
                                return false;
                            }
                        }
                );
        // step 7: 6 minutes passed, and it is 1 minute after draw date (19.11.2022 12:01)
        clock.plusMinutes(6);
        // step 8: user made GET /results/sampleTicketId and system returned 200 (OK)
        // given && when
        ResultActions performGetResult = mockMvc.perform(get("/results/"+ticketId));
        // then
        MvcResult performMvcResult = performGetResult.andExpect(status().isOk()).andReturn();
        String resultJson = performMvcResult.getResponse().getContentAsString();
        ResultAnnouncerResponseDto resultAnnouncerResponseDto = objectMapper.readValue(resultJson, ResultAnnouncerResponseDto.class);
        assertAll(
                () -> assertThat(resultAnnouncerResponseDto.responseDto().hitNumbers()).hasSize(6),
                () -> assertThat(resultAnnouncerResponseDto.message()).isEqualTo("Congratulations, you won!"),
                () -> assertThat(resultAnnouncerResponseDto.responseDto().hash()).isEqualTo(ticketId)
        );
    }
}
