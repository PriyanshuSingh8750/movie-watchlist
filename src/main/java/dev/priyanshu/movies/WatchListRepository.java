package dev.priyanshu.movies;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WatchListRepository extends MongoRepository<User, String> {
}
