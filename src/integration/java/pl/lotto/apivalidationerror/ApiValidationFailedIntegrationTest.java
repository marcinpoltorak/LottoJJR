package pl.lotto.apivalidationerror;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;
import pl.lotto.BaseIntegrationTest;
import pl.lotto.infrastructure.apivalidation.ApiValidationErrorDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ApiValidationFailedIntegrationTest extends BaseIntegrationTest {

    @Container
    public static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

    @DynamicPropertySource
    public static void propertyOverride(DynamicPropertyRegistry registry){
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("offers.http.client.config.uri", () -> WIRE_MOCK_HOST);
        registry.add("offers.http.client.config.port", () -> wireMockServer.getPort());
    }

    @Test
    public void should_return_400_bad_request_and_validation_message_when_request_has_empty_input_numbers() throws Exception {
        // given & when
        ResultActions perform = mockMvc.perform(post("/inputNumbers")
                .content("""
                        {
                        "inputNumbers": []
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON)
        );
        // then
        MvcResult mvcResult = perform.andExpect(status().isBadRequest()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        ApiValidationErrorDto result = objectMapper.readValue(json, ApiValidationErrorDto.class);
        assertThat(result.messages()).containsExactlyInAnyOrder("inputNumbers must not be empty");
    }

    @Test
    public void should_return_400_bad_request_and_validation_message_when_request_does_not_have_input_numbers() throws Exception {
        // given & when
        ResultActions perform = mockMvc.perform(post("/inputNumbers")
                .content("""
                        {}
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON)
        );
        // then
        MvcResult mvcResult = perform.andExpect(status().isBadRequest()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        ApiValidationErrorDto result = objectMapper.readValue(json, ApiValidationErrorDto.class);
        assertThat(result.messages()).containsExactlyInAnyOrder(
                "inputNumbers must not be null",
                "inputNumbers must not be empty"
        );
    }
}
