package dev.priyanshu.movies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/watchlist")
public class WatchListController {

    @Autowired
    private WatchListService watchListService;

    // Add a movie to the user's watchlist
    @PostMapping
    public ResponseEntity<String> addToWatchList(@RequestParam String userId, @RequestParam String imdbId) {
        watchListService.addToWatchList(userId, imdbId);
        return new ResponseEntity<>("Movie added to watchlist!", HttpStatus.OK);
    }

    // Get the user's watchlist
    @GetMapping("/{userId}")
    public ResponseEntity<List<Movie>> getWatchList(@PathVariable String userId) {
        List<Movie> watchList = watchListService.getWatchList(userId);
        return new ResponseEntity<>(watchList, HttpStatus.OK);
    }

    // Remove a movie from the user's watchlist
    @DeleteMapping
    public ResponseEntity<String> removeFromWatchList(@RequestParam String userId, @RequestParam String imdbId) {
        watchListService.removeFromWatchList(userId, imdbId);
        return new ResponseEntity<>("Movie removed from watchlist!", HttpStatus.OK);
    }
}
