package pl.lotto.infrastructure.numbergenerator.http;

import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.lotto.domain.numbergenerator.SixRandomNumbersDto;
import pl.lotto.domain.numbergenerator.WinningNumbersGenerable;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class RandomNumberGeneratorRestTemplate implements WinningNumbersGenerable {

    private final RestTemplate restTemplate;
    private final String uri;
    private final int port;

    @Override
    public SixRandomNumbersDto generateSixRandomNumbers() {
        String urlForService = getUrlForService("/api/v1.0/randomnumber");
        HttpHeaders headers = new HttpHeaders();
        final HttpEntity<HttpHeaders> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<List<Integer>> response = restTemplate.exchange(
                UriComponentsBuilder.fromHttpUrl(urlForService)
                        .queryParam("min", 1)
                        .queryParam("max", 99)
                        .queryParam("count", 6)
                        .toUriString(),
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {
                });
        System.out.println(response.getBody());
        return SixRandomNumbersDto.builder().numbers(response.getBody().stream().collect(Collectors.toSet())).build();
    }

    private String getUrlForService(String service) {
        return uri + ":" + port + service;
    }
}
