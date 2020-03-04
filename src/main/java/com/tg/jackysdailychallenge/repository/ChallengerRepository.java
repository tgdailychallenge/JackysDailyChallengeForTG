package com.tg.jackysdailychallenge.repository;

import com.tg.jackysdailychallenge.model.Challenger;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "challengers", path = "challengers")
public interface ChallengerRepository extends MongoRepository<Challenger, String> {
    List<Challenger> findByUserId(@Param("userId") int userId);
}
