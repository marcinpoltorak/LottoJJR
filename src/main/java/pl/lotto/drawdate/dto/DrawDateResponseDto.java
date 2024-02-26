package pl.lotto.drawdate.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record DrawDateResponseDto(LocalDateTime drawDate) {
}
