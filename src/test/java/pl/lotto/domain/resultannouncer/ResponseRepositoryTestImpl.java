package pl.lotto.domain.resultannouncer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ResponseRepositoryTestImpl implements ResponseRepository {
    private Map<String, ResultResponse> resultResponseMap = new HashMap<>();
    @Override
    public ResultResponse save(ResultResponse resultResponse) {
        resultResponseMap.put(resultResponse.hash(), resultResponse);
        return resultResponse;
    }

    @Override
    public boolean existsById(String hash) {
        return resultResponseMap.containsKey(hash);
    }

    @Override
    public Optional<ResultResponse> findById(String hash) {
        return Optional.ofNullable(resultResponseMap.get(hash));
    }
}
