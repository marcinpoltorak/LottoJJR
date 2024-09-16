package pl.lotto.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
