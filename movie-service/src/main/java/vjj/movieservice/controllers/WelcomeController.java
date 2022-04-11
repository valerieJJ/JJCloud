package vjj.movieservice.controllers;

import VO.MovieVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import requests.HotMovieRequest;
import requests.LatestMovieRequest;
import vjj.movieservice.services.RecService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class WelcomeController {
    @Autowired
    private RecService recService;

    @RequestMapping(value = "/rec", method = RequestMethod.GET)
    public Map<String, List<MovieVO>> welcomePage() throws ExecutionException, InterruptedException {
        HotMovieRequest hotMovieRequest = new HotMovieRequest(6);
        LatestMovieRequest latestMovieRequest = new LatestMovieRequest(6);
        CompletableFuture<List<MovieVO>> hotMovieVOS = recService.getHotRecommendations(hotMovieRequest);
        CompletableFuture<List<MovieVO>> latestMovieVOS = recService.getLatestRecommendations(latestMovieRequest);
        CompletableFuture.allOf(hotMovieVOS, latestMovieVOS).join();
        List<MovieVO> hotmovies = hotMovieVOS.get();
        List<MovieVO> latestmovies = latestMovieVOS.get();

        Map<String, List<MovieVO>> map = new HashMap<>();
        map.put("rechotmovieVOS", hotmovies);
        map.put("reclatestmovieVOS", latestmovies);

        return map;
    }
}
