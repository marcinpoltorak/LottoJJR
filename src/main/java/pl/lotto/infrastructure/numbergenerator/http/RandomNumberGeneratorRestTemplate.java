package pl.lotto.infrastructure.numbergenerator.http;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import pl.lotto.domain.numbergenerator.SixRandomNumbersDto;
import pl.lotto.domain.numbergenerator.WinningNumbersGenerable;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Log4j2
public class RandomNumberGeneratorRestTemplate implements WinningNumbersGenerable {

    private final RestTemplate restTemplate;
    private final String uri;
    private final int port;

    @Override
    public SixRandomNumbersDto generateSixRandomNumbers(int lowerBand, int upperBand, int count) {
        log.info("Started fetching winning numbers using http client");
        HttpHeaders headers = new HttpHeaders();
        final HttpEntity<HttpHeaders> requestEntity = new HttpEntity<>(headers);
        try {
            final ResponseEntity<List<Integer>> response = makeGetRequest(lowerBand, upperBand, count, requestEntity);
            Set<Integer> sixDistinctNumbers = getSixDistinctNumbers(response);
            if(sixDistinctNumbers.size() != 6){
                log.error("Set is less than: {} Have to request one more time", count);
                return generateSixRandomNumbers(lowerBand, upperBand, count);
            }
            return SixRandomNumbersDto.builder().numbers(sixDistinctNumbers).build();
        } catch (ResourceAccessException e){
            log.error("Error while fetching winning numbers using http client: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Set<Integer> getSixDistinctNumbers(ResponseEntity<List<Integer>> response) {
        List<Integer> numbers = response.getBody();
        if(numbers == null) {
            log.error("Response numbers was null");
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        log.info("Success Response Body Returned: " + response);
        Set<Integer> distinctNumbers = new LinkedHashSet<>(numbers);
        return distinctNumbers.stream().limit(6).collect(Collectors.toSet());
    }

    private ResponseEntity<List<Integer>> makeGetRequest(int lowerBand, int upperBand, int count, HttpEntity<HttpHeaders> requestEntity) {
        return restTemplate.exchange(
                UriComponentsBuilder.fromHttpUrl(getUrlForService("/api/v1.0/randomnumber"))
                        .queryParam("min", lowerBand)
                        .queryParam("max", upperBand)
                        .queryParam("count", count)
                        .toUriString(),
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {
                });
    }

    private String getUrlForService(String service) {
        return uri + ":" + port + service;
    }
}
