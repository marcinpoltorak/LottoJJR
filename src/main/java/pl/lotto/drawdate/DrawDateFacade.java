package pl.lotto.drawdate;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class DrawDateFacade {
    private final DrawDateGenerator drawDateGenerator;

    public LocalDateTime retrieveNextDrawDate(){
        return drawDateGenerator.getNextDrawDate();
    }
}
