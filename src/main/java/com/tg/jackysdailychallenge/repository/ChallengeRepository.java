package com.tg.jackysdailychallenge.repository;

import com.tg.jackysdailychallenge.model.Challenge;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "challenges", path = "challenges")
public interface ChallengeRepository extends MongoRepository<Challenge, String> {
    List<Challenge> findByTitle(@Param("title") String title);
    List<Challenge> findByScore(@Param("score") int score);
    List<Challenge> findByIsWeekendOnly(@Param("isWeekendOnly") boolean isWeekendOnly);

}
