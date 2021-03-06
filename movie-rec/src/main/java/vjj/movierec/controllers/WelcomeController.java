package vjj.movierec.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import models.*;
import VO.*;
import requests.*;
import vjj.movierec.services.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Controller
public class WelcomeController {
    @Autowired
    private RecService recService;

    @RequestMapping("")
    public ModelAndView welcomePage() throws ExecutionException, InterruptedException {

        HotMovieRequest hotMovieRequest = new HotMovieRequest(6);//取出6个
        CompletableFuture<List<MovieVO>> hotMovieVOS = recService.getHotRecommendations(hotMovieRequest);

        LatestMovieRequest latestMovieRequest = new LatestMovieRequest(6);//取出6个
        CompletableFuture<List<MovieVO>> latestMovieVOS = recService.getLatestRecommendations(latestMovieRequest);

        CompletableFuture.allOf(hotMovieVOS, latestMovieVOS).join();
        List<MovieVO> hotmovies = hotMovieVOS.get();
        List<MovieVO> latestmovies = latestMovieVOS.get();

        ModelAndView mv = new ModelAndView();

        mv.addObject("rechotmovieVOS", hotmovies);
        mv.addObject("reclatestmovieVOS", latestmovies);
        mv.setViewName("index");
        return mv;
    }
}
