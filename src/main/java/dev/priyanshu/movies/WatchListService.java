package dev.priyanshu.movies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WatchListService {

    @Autowired
    private MongoTemplate mongoTemplate;

    // Add a movie to the watchlist
    public void addToWatchList(String userId, String imdbId) {
        Query query = new Query(Criteria.where("userId").is(userId));
        Update update = new Update().addToSet("watchList", imdbId);
        mongoTemplate.upsert(query, update, User.class);
    }

    // Get the user's watchlist
    public List<Movie> getWatchList(String userId) {
        Query query = new Query(Criteria.where("userId").is(userId));
        User user = mongoTemplate.findOne(query, User.class);
        if (user != null) {
            return mongoTemplate.find(new Query(Criteria.where("imdbId").in(user.getWatchList())), Movie.class);
        }
        return List.of();
    }

    // Remove a movie from the watchlist
    public void removeFromWatchList(String userId, String imdbId) {
        Query query = new Query(Criteria.where("userId").is(userId));
        Update update = new Update().pull("watchList", imdbId);
        mongoTemplate.updateFirst(query, update, User.class);
    }
}
