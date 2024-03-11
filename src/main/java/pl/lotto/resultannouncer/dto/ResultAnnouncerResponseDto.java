package pl.lotto.resultannouncer.dto;

public record ResultAnnouncerResponseDto(
        ResponseDto responseDto,
        String message
) {
}
