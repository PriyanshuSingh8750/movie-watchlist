package dev.priyanshu.movies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public Movie createReview(String reviewBody, String imdbId) {
        // Create and save the review
        Review review = reviewRepository.insert(new Review(reviewBody));

        // Update the movie with the new review
        mongoTemplate.update(Movie.class)
                .matching(Query.query(Criteria.where("imdbId").is(imdbId)))
                .apply(new Update().push("reviewIds").value(review))
                .first();

        // Return the updated movie
        return mongoTemplate.findOne(
                new Query(Criteria.where("imdbId").is(imdbId)),
                Movie.class
        );
    }
}
