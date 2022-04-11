package vjj.webconsumer.FeignServices;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import requests.MovieRatingRequest;

import java.util.concurrent.CompletableFuture;

@FeignClient("movie-service")
public interface RatingService {

    @RequestMapping(value = "/rating/update", method = RequestMethod.POST)
    public boolean updataMovieRating(@RequestBody MovieRatingRequest ratingRequest);

    @RequestMapping(value = "/rating/score", method = RequestMethod.GET)
    public String getMovieAverageScores(@RequestParam("mid") Integer mid);

    @RequestMapping(value = "/rating/scoreAsc", method = RequestMethod.GET)
    public CompletableFuture<String> asygetMovieAverageScores(@RequestParam("mid") Integer mid);
}
