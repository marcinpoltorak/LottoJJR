package pl.lotto.infrastructure.numbergenerator.http;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import pl.lotto.domain.numbergenerator.WinningNumbersGenerable;

import java.time.Duration;

@Configuration
public class RandomGeneratorClientConfig {

    @Bean
    public RestTemplateResponseErrorHandler restTemplateResponseErrorHandler() {
        return new RestTemplateResponseErrorHandler();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateResponseErrorHandler restTemplateResponseErrorHandler,
                                     RandomNumberGeneratorRestTemplateConfigurationProperties properties) {
        return new RestTemplateBuilder()
                .errorHandler(restTemplateResponseErrorHandler)
                .setConnectTimeout(Duration.ofMillis(properties.connectionTimeout()))
                .setReadTimeout(Duration.ofMillis(properties.readTimeout()))
                .build();
    }

    @Bean
    public WinningNumbersGenerable remoteNumberGeneratorClient(RestTemplate restTemplate,
                                                               RandomNumberGeneratorRestTemplateConfigurationProperties properties
                                                             ) {
        return new RandomNumberGeneratorRestTemplate(restTemplate, properties.uri(), properties.port());
    }
}
