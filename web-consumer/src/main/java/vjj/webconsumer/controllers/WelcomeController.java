package vjj.webconsumer.controllers;

import VO.MovieVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import requests.HotMovieRequest;
import requests.LatestMovieRequest;
import vjj.webconsumer.FeignServices.RecService;
import vjj.webconsumer.config.DiscoverServer;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Controller
public class WelcomeController {
    @Autowired
    private RecService recService;

    @RequestMapping("")
    public ModelAndView welcomePage() throws ExecutionException, InterruptedException {
        Map<String, List<MovieVO>> map = recService.getRecommend();
        List<MovieVO> hotmovies = map.get("rechotmovieVOS");
        List<MovieVO> latestmovies = map.get("reclatestmovieVOS");

        ModelAndView mv = new ModelAndView();

        mv.addObject("rechotmovieVOS", hotmovies);
        mv.addObject("reclatestmovieVOS", latestmovies);
        mv.setViewName("index");
        return mv;
    }
}
