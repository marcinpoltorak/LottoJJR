package pl.lotto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.lotto.domain.numbergenerator.WinningNumbersGeneratorFacadeConfigurationProperties;
import pl.lotto.infrastructure.numbergenerator.http.RandomNumberGeneratorRestTemplateConfigurationProperties;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties({WinningNumbersGeneratorFacadeConfigurationProperties.class, RandomNumberGeneratorRestTemplateConfigurationProperties.class})
@EnableMongoRepositories
public class LottoSpringBootApplication {

    public static void main(String[] args){
        SpringApplication.run(LottoSpringBootApplication.class, args);
    }
}
