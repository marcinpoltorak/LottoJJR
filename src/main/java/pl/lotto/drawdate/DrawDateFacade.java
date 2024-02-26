package pl.lotto.drawdate;

import lombok.AllArgsConstructor;
import pl.lotto.drawdate.dto.DrawDateResponseDto;

@AllArgsConstructor
public class DrawDateFacade {
    private final DrawDateGenerator drawDateGenerator;

    public DrawDateResponseDto getNextDrawDate(){
        return DrawDateResponseDto
                .builder()
                .drawDate(
                        drawDateGenerator.getNextDrawDate()
                )
                .build();
    }
}
