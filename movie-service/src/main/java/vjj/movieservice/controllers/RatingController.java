package vjj.movieservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import requests.MovieRatingRequest;
import vjj.movieservice.services.RatingService;

import java.util.concurrent.CompletableFuture;

@RestController
public class RatingController {
    @Autowired
    private RatingService ratingService;

    @RequestMapping(value = "/rating/update", method = RequestMethod.POST)
    public String updataMovieRating(@RequestParam("uid") Integer uid
            , @RequestParam("mid") Integer mid
            , @RequestParam("score") Double score ) throws JsonProcessingException, IllegalAccessException {

        MovieRatingRequest ratingRequest = new MovieRatingRequest(uid, mid, score);
        boolean done = ratingService.updataMovieRating(ratingRequest);
        String movie_score = ratingService.getMovieAverageScores(mid);
        return movie_score;
    }

    @RequestMapping(value = "/rating/score", method = RequestMethod.GET)
    public String getMovieAverageScores(@RequestParam("mid") Integer mid){
        return ratingService.getMovieAverageScores(mid);
    }

    @RequestMapping(value = "/rating/scoreAsc", method = RequestMethod.GET)
    public CompletableFuture<String> asygetMovieAverageScores(@RequestParam("mid") Integer mid){
        return ratingService.asygetMovieAverageScores(mid);
    }
}
