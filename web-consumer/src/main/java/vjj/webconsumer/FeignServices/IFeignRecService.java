package vjj.webconsumer.FeignServices;

import VO.MovieVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import requests.HotMovieRequest;
import requests.LatestMovieRequest;
import vjj.webconsumer.FeignServices.Imp.FeignRecService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@FeignClient(name = "movie-service", fallback = FeignRecService.class)
public interface IFeignRecService {

    @RequestMapping(value = "/rec", method = RequestMethod.GET)
    Map<String, List<MovieVO>> getRecommend();
}
