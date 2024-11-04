package pl.lotto.domain.numberreceiver;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketRepository extends MongoRepository<Ticket, String> {
//    Ticket save(Ticket ticket);
    List<Ticket> findAllTicketsByDrawDate(LocalDateTime drawDate);
    Ticket findByHash(String hash);
}
