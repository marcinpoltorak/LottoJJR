package pl.lotto.domain.resultannouncer;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ResponseRepository extends MongoRepository<ResultResponse, String> {
//    ResultResponse save(ResultResponse resultResponse);

    boolean existsByHash(String hash);

//    Optional<ResultResponse> findById(String hash);
}
