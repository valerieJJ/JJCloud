package vjj.webconsumer.FeignServices;

import VO.MovieVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import requests.HotMovieRequest;
import requests.LatestMovieRequest;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@FeignClient(name = "movie-service")
public interface RecService {

    @RequestMapping(value = "/rec", method = RequestMethod.GET)
    public Map<String, List<MovieVO>> getRecommend();
}
