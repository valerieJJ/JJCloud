package vjj.movieservice.controllers;

import VO.MovieVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import models.Movie;
import models.Rating;
import models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import requests.HotMovieRequest;
import requests.MovieRatingRequest;
import vjj.movieservice.services.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@RestController
public class MovieController {
    @Autowired
    private MovieService movieService;
    @Autowired
    private RatingService ratingService;
    @Autowired
    private ESService esService;
    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private Executor executor;

    @RequestMapping(value = "/movie/list", method = RequestMethod.GET)
    public List<MovieVO> getMovieVOS(@RequestParam("mids") List<Integer> mids){
        return movieService.getMovieVOS(mids);
    }

    @RequestMapping(value = "/movie/types", method = RequestMethod.GET)
    public HashMap<String, String> getMovieTypes(){
        return movieService.getMovieTypes();
    }

    @RequestMapping(value = "/movie/query", method = RequestMethod.GET)
    public Movie getMovieById(@RequestParam("mid") int mid) throws ExecutionException, InterruptedException {
        System.out.println("qeury movie - get mid = "+mid);

        Movie movie = movieService.findByMID(mid);
        return movie;
    }

    @RequestMapping(value = "/movie/score", method = RequestMethod.GET)
    public String getScoreById(@RequestParam("mid") Integer mid) throws ExecutionException, InterruptedException {
        System.out.println("getmovie - get mid = "+mid);
//        CompletableFuture<Movie> asy_movie = movieService.asyfindByMID(mid);
//        CompletableFuture<String> asy_movie_score = ratingService.asygetMovieAverageScores(mid);

        CompletableFuture<Movie> asy_movie = CompletableFuture.supplyAsync(()->{
            Movie m = movieService.findByMID(mid);
            log.info("completable-future get the movie...: " + m.getName());
            return  m;
        }, executor);
        CompletableFuture<String> asy_movie_score = CompletableFuture.supplyAsync(()->{
            String score = ratingService.getMovieAverageScores(mid);
            log.info("completable-future get its score...: " + score);
            return  score;
        }, executor);

        CompletableFuture.allOf(asy_movie,asy_movie_score).get(); // 也可以用.join()

        System.out.println("completableFuture get all");
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
        return movie_score;
    }

    @RequestMapping(value = "/movie/folder", method = RequestMethod.GET)
    public List<Movie> goMovieFolder2(@RequestParam("type") String type) throws UnknownHostException {
        ModelAndView modelAndView = new ModelAndView();
        System.out.println("~~~ go to "+ type);
        String field = "genres";
        List<Movie> data = movieService.getCollectionData(field, type);
        return data;
    }

    @RequestMapping(value = "/movie/movieid", method = RequestMethod.GET)
    public Map<String, Object> getMovieInfo(@RequestParam("mid")int mid, @RequestParam("request") HttpServletRequest request) throws ExecutionException, InterruptedException {
        System.out.println("getmovie - get mid = "+mid);

        CompletableFuture<Movie> asy_movie = movieService.asyfindByMID(mid);
        CompletableFuture<String> asy_movie_score = ratingService.asygetMovieAverageScores(mid);

//        CompletableFuture<Movie> asy_movie = CompletableFuture.supplyAsync(()->{
//            return movieService.findByMID(mid);
//            return  m;
//        }, executor);
//        CompletableFuture<String> asy_movie_score = CompletableFuture.supplyAsync(()->{
//            return ratingService.getMovieAverageScores(mid);
//        }, executor);

        CompletableFuture.allOf(asy_movie,asy_movie_score).join();
        Movie movie = asy_movie.get();
        String movie_score = asy_movie_score.get();

        Map<String, Object> res = new HashMap<>();
        res.put("movie", null);
        res.put("movie_score", 0);

        if(movie==null){
            System.out.println("movie not found");
        }else {
            res.put("movie",movie);
            res.put("movie_score", movie_score);
        }
        res.put("rating_message", "how do u like it?");
        HttpSession session = request.getSession();
        session.setAttribute("movie", movie);

        User user = (User) session.getAttribute("user");
        boolean state = favoriteService.favoriteExistMongo(user.getUid(), mid);
        res.put("state", state);
        System.out.println("get state2: "+ state);
        return res;
    }

    @RequestMapping(value = "/movie/field", method = RequestMethod.GET)
    public List<MovieVO> searchMovieByField(
            @RequestParam("fieldname") String fieldname,
            @RequestParam("value") String value
            )
            throws IOException {
        log.info("movie-service field = "+fieldname);
        System.out.println("search value = " + value);
        String es_collection = "movietags";
        String[] excludes = {};
        String[] includes = {"mid", "name", "genres", "language", "descri", "issue", "shoot", "directors", "timelong"};

//        HashMap<List<MovieVO>, String> map = new HashMap<>();
//        map= esService.fullQuery("match", es_collection, fieldname, value, excludes, includes, 6);
//        Set<List<MovieVO>> set = map.keySet();
//        set.add()

        HashMap<String, Object> res = new HashMap<>();
        List<MovieVO> movieVOList = new ArrayList<>();

                MovieVO movieVO = new MovieVO();
                int mid = 2758;
                Movie mov = movieService.findByMID(mid);
                String movie_score = ratingService.getMovieAverageScores(mid);
                movieVO.setScore(movie_score);
                movieVO.setIssue(mov.getIssue());
                movieVO.setGenres(mov.getGenres());
                movieVOList.add(movieVO);

        log.info("es get movieVOList.size() = "+movieVOList.size());

        if(movieVOList==null){
            System.out.println("movie not found");
        }else {
            System.out.println("goto moviename = "+ movieVOList.get(0));
            res.put("movieVOList",movieVOList);
            res.put("number",movieVOList.size());
            res.put("fieldname",fieldname);
            res.put("value", value);
        }
        return movieVOList;
    }


    @RequestMapping(value = "/movie/favor", method = RequestMethod.GET)
    public boolean doFavor(HttpServletRequest request, Model model) {
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
        return  state;
    }

}
