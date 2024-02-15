package pl.lotto.numberreceiver;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketRepository {
    Ticket save(Ticket ticket);
    List<Ticket> findAllTicketsByDrawDate(LocalDateTime drawDate);
    Ticket findByHash(String hash);
}
