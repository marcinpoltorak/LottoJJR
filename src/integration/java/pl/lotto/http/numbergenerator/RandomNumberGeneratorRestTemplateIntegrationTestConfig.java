package pl.lotto.http.numbergenerator;

import org.springframework.web.client.RestTemplate;
import pl.lotto.domain.numbergenerator.WinningNumbersGenerable;
import pl.lotto.infrastructure.numbergenerator.http.RandomGeneratorClientConfig;
import pl.lotto.infrastructure.numbergenerator.http.RandomNumberGeneratorRestTemplateConfigurationProperties;

public class RandomNumberGeneratorRestTemplateIntegrationTestConfig extends RandomGeneratorClientConfig {



    public WinningNumbersGenerable remoteNumberGeneratorClient(RandomNumberGeneratorRestTemplateConfigurationProperties properties){
        RestTemplate restTemplate = restTemplate(restTemplateResponseErrorHandler(), properties);
        return remoteNumberGeneratorClient(restTemplate, properties);
    }
}
