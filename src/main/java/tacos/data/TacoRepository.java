package tacos.data;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import tacos.Taco;

public interface TacoRepository extends ReactiveMongoRepository<Taco, Long> {
    Flux<Taco> findByOrderByCreatedAtDesc();
}
