package vjj.movieservice.controllers;

import VO.MovieVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import models.Movie;
import models.Rating;
import models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import requests.MovieRatingRequest;
import vjj.movieservice.services.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RequestMapping("/movie")
@RestController
public class MovieController {
    @Autowired
    private MovieService movieService;
    @Autowired
    private UserService userService;
    @Autowired
    private RatingService ratingService;
    @Autowired
    private ESService esService;
    @Autowired
    private FavoriteService favoriteService;


    @RequestMapping(value = "/mymovie")
    public Map<String, Object> getMovieById(
            int mid
            , Rating rating) throws ExecutionException, InterruptedException {
        System.out.println("getmovie - get mid = "+mid);
        CompletableFuture<Movie> asy_movie = movieService.asyfindByMID(mid);
        CompletableFuture<String> asy_movie_score = ratingService.asygetMovieAverageScores(mid);
        CompletableFuture.allOf(asy_movie,asy_movie_score).join();
        Movie movie = asy_movie.get();
        String movie_score = asy_movie_score.get();

        Map<String, Object> map = new HashMap<>();

        if(movie==null){
            System.out.println("movie not found");
            map.put("movie", null);
            map.put("movie_score", null);
        }else {
            map.put("movie", movie);
            map.put("movie_score", movie_score);
        }
        return map;
    }

    @RequestMapping("rate")
    public String rateMovie(
            @ModelAttribute("rating") Rating ratingReq,
            Model model,
            HttpServletRequest request
    ) throws JsonProcessingException, IllegalAccessException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        int mid = ratingReq.getMid();
        double score = ratingReq.getScore();

        MovieRatingRequest ratingRequest = new MovieRatingRequest(user.getUid(), mid, score);
        boolean done = ratingService.updataMovieRating(ratingRequest);
        if(done){
            model.addAttribute("rating_message", "rating successful");
            System.out.println("rating updated successful!");
            System.out.println("user "+user.getUsername());
            System.out.println("mid = "+mid);
            System.out.println("score = "+score);
        }else {
            model.addAttribute("rating_message", "rating failed");
        }

        Movie movie = (Movie)session.getAttribute("movie");

        String movie_score = ratingService.getMovieAverageScores(mid);
        model.addAttribute("movie", movie);

        model.addAttribute("movie_score", movie_score);
        return "movieInfo";
    }

    @RequestMapping("/moviefolder")
    public ModelAndView goMovieFolder(String type) throws UnknownHostException {
        ModelAndView modelAndView = new ModelAndView();

        System.out.println("~~~ go to "+ type);
//        String field="language";
        String field = "genres";
        List<Movie> data = movieService.getCollectionData(field, type);
        modelAndView.addObject("movies", data);
        modelAndView.addObject("folder_name", type);
        modelAndView.setViewName("movieFolder");
        return modelAndView;
    }

    @RequestMapping("/movieid")
    public ModelAndView getMovieInfo(
            @ModelAttribute("mid") int mid
            ,@ModelAttribute("rating") Rating rating
            , HttpServletRequest request
            , ModelAndView modelAndView) throws ExecutionException, InterruptedException {
        System.out.println("getmovie - get mid = "+mid);
        CompletableFuture<Movie> asy_movie = movieService.asyfindByMID(mid);
        CompletableFuture<String> asy_movie_score = ratingService.asygetMovieAverageScores(mid);
        CompletableFuture.allOf(asy_movie,asy_movie_score).join();
        Movie movie = asy_movie.get();
        String movie_score = asy_movie_score.get();

        if(movie==null){
            System.out.println("movie not found");
            modelAndView.setViewName("show");
        }else {
            modelAndView.addObject("movie",movie);
            modelAndView.addObject("movie_score", movie_score);
        }
        modelAndView.addObject("rating_message", "how do u like it?");
        HttpSession session = request.getSession();
        session.setAttribute("movie", movie);

        User user = (User) session.getAttribute("user");
        boolean state = favoriteService.favoriteExistMongo(user.getUid(), mid);
        modelAndView.addObject("state", state);

        System.out.println("get state2: "+ state);
        modelAndView.setViewName("movieInfo");
        return modelAndView;
    }

    @RequestMapping("/moviefield")
    @PermissionAnnotation
    public ModelAndView searchMovieByName(String fieldname
                                          , String value
            , HttpServletRequest request, HttpServletResponse response) throws IOException {
        ModelAndView modelAndView = new ModelAndView();
        System.out.println("search movie field = "+fieldname);
        System.out.println("search value = " + value);
        String es_collection = "movietags";
        String[] excludes = {};
        String[] includes = {"mid", "name", "genres", "language", "descri", "issue", "shoot", "directors", "timelong"};

        HashMap<List<MovieVO>, String> map;
        map = esService.fullQuery("match", es_collection,
                fieldname, value, excludes, includes, 6);

        Set<List<MovieVO>> set = map.keySet();
        List<MovieVO> movieVOList = new ArrayList<>();
        for(List<MovieVO> list: set){
            for(MovieVO movieVO: list){
                Integer mid = movieVO.getMid();
//                MovieVO movieVO = new MovieVO();
                Movie mov = movieService.findByMID(mid);
                String movie_score = ratingService.getMovieAverageScores(mid);
                movieVO.setScore(movie_score);
                movieVO.setIssue(mov.getIssue());
                movieVO.setGenres(mov.getGenres());
                movieVOList.add(movieVO);
            }
        }
        System.out.println("es get movieVOList.size() = "+movieVOList.size());
        System.out.println("es get movieVOList.get(0) = "+movieVOList.get(0).getName());

        if(movieVOList==null){
            System.out.println("movie not found");
            modelAndView.setViewName("show");
        }else {
            System.out.println("goto moviename = "+ movieVOList.get(0));
            modelAndView.addObject("movieVOList",movieVOList);
            modelAndView.addObject("number",movieVOList.size());
            modelAndView.addObject("fieldname",fieldname);
            modelAndView.addObject("value", value);
            modelAndView.setViewName("movieList");
//            modelAndView.setViewName("movieInfo");
            HttpSession session = request.getSession();
            session.setAttribute("movieVOList", movieVOList);
        }
        return modelAndView;
    }


    @RequestMapping("/favor")
    public void doFavor(
            HttpServletRequest request
            , Model model) {
        int mid = (int) request.getAttribute("mid");
        System.out.println("getmovie - get mid = "+mid);

        Movie movie = movieService.findByMID(mid);
        if(movie==null){
        }else {
            model.addAttribute("movie",movie);
        }
        HttpSession session = request.getSession();
        session.setAttribute("movie", movie);

        User user = (User) session.getAttribute("user");
        boolean state = favoriteService.favoriteExistMongo(user.getUid(), mid);
        model.addAttribute("state", state);

        System.out.println("get state2: "+ state);
    }


}
