package vjj.movieservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import requests.MovieRatingRequest;
import vjj.movieservice.services.RatingService;

import java.util.concurrent.CompletableFuture;

@RestController
public class RatingController {
    @Autowired
    private RatingService ratingService;

    @RequestMapping(value = "/rating/update", method = RequestMethod.POST)
    public boolean updataMovieRating(@RequestBody MovieRatingRequest ratingRequest) throws JsonProcessingException, IllegalAccessException {
        return ratingService.updataMovieRating(ratingRequest);
    }

    @RequestMapping(value = "/rating/score", method = RequestMethod.POST)
    public String getMovieAverageScores(Integer mid){
        return ratingService.getMovieAverageScores(mid);
    }

    @RequestMapping(value = "/rating/scoreAsc", method = RequestMethod.POST)
    public CompletableFuture<String> asygetMovieAverageScores(Integer mid){
        return ratingService.asygetMovieAverageScores(mid);
    }
}
