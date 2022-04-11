package vjj.webconsumer.FeignServices;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import requests.MovieRatingRequest;

import java.util.concurrent.CompletableFuture;

@FeignClient("movie-service")
public interface RatingService {

    @RequestMapping(value = "/rating/update", method = RequestMethod.POST)
    public boolean updataMovieRating(@RequestBody MovieRatingRequest ratingRequest);

    @RequestMapping(value = "/rating/score", method = RequestMethod.POST)
    public String getMovieAverageScores(Integer mid);

    @RequestMapping(value = "/rating/scoreAsc", method = RequestMethod.POST)
    public CompletableFuture<String> asygetMovieAverageScores(Integer mid);
}
