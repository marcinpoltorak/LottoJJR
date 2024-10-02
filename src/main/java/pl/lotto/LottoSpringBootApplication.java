package pl.lotto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.lotto.domain.TestRepository;
import pl.lotto.domain.Ticket;

@SpringBootApplication
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
