package vjj.webconsumer.FeignServices;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import requests.MovieRatingRequest;

import java.util.concurrent.CompletableFuture;

@FeignClient(name = "movie-service")
public interface FeignRatingService {

    @RequestMapping(value = "/rating/update", method = RequestMethod.POST)
    public String updateMovieRating( @RequestParam("uid") Integer uid
                            , @RequestParam("mid") Integer mid
                            , @RequestParam("score") Double score );

    @RequestMapping(value = "/rating/score", method = RequestMethod.GET)
    public String getMovieAverageScores(@RequestParam("mid") Integer mid);

    @RequestMapping(value = "/rating/scoreAsc", method = RequestMethod.GET)
    public CompletableFuture<String> asygetMovieAverageScores(@RequestParam("mid") Integer mid);
}
