package pl.lotto.numberreceiver;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

public class DrawDateGenerator {
    private static final LocalTime DRAW_TIME = LocalTime.of(12, 0);
    private static final TemporalAdjuster NEXT_DRAW_DAY = TemporalAdjusters.next(DayOfWeek.SATURDAY);

    private final Clock clock;

    public DrawDateGenerator(Clock clock) {
        this.clock = clock;
    }

    public LocalDateTime getNextDrawDate(){
        LocalDateTime currentDateTime = LocalDateTime.now(clock);
        if(isSaturdayAndBeforeDrawTime(currentDateTime)){
            return LocalDateTime.of(currentDateTime.toLocalDate(), DRAW_TIME);
        }
        LocalDateTime drawDate = currentDateTime.with(NEXT_DRAW_DAY);
        return LocalDateTime.of(drawDate.toLocalDate(), DRAW_TIME);
    }

    private static boolean isSaturdayAndBeforeDrawTime(LocalDateTime currentDateTime) {
        return currentDateTime.getDayOfWeek().equals(DayOfWeek.SATURDAY) && currentDateTime.toLocalTime().isBefore(DRAW_TIME);
    }
}
