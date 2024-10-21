package pl.lotto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.lotto.domain.TestRepository;
import pl.lotto.domain.Ticket;
import pl.lotto.domain.numbergenerator.WinningNumbersGeneratorFacadeConfigurationProperties;
import pl.lotto.infrastructure.numbergenerator.http.RandomNumberGeneratorRestTemplateConfigurationProperties;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties({WinningNumbersGeneratorFacadeConfigurationProperties.class, RandomNumberGeneratorRestTemplateConfigurationProperties.class})
public class LottoSpringBootApplication implements CommandLineRunner {

    @Autowired
    TestRepository repository;

    public static void main(String[] args){
        SpringApplication.run(LottoSpringBootApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        repository.save(new Ticket("tid"));
    }
}
