package vjj.movieservice.controllers;

import VO.MovieVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import models.Movie;
import models.Rating;
import models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import requests.MovieRatingRequest;
import vjj.movieservice.services.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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


    @RequestMapping(value = "/movie/list", method = RequestMethod.POST)
    public List<MovieVO> getMovieVOS(@RequestBody List<Integer> mids){
        return movieService.getMovieVOS(mids);
    }

    @RequestMapping(value = "/movie/types", method = RequestMethod.POST)
    public HashMap<String, String> getMovieTypes(){
        return movieService.getMovieTypes();
    }

//    @RequestMapping(value = "/movie/query", method = RequestMethod.POST)
    @PostMapping("/movie/query")
    public Movie getMovieById(@RequestParam("mid") int mid) throws ExecutionException, InterruptedException {
        System.out.println("getmovie - get mid = "+mid);
        Movie movie = movieService.findByMID(mid);
        return movie;
    }

    @RequestMapping(value = "/movie/score", method = RequestMethod.POST)
    public Map<String, Object> getScoreById(int mid) throws ExecutionException, InterruptedException {
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
    @RequestMapping(value = "/movie/rate", method = RequestMethod.POST)
    public Model rateMovie(
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
            System.out.println("user "+user.getUname());
            System.out.println("mid = "+mid);
            System.out.println("score = "+score);
        }else {
            model.addAttribute("rating_message", "rating failed");
        }
        Movie movie = (Movie)session.getAttribute("movie");
        String movie_score = ratingService.getMovieAverageScores(mid);
        model.addAttribute("movie", movie);
        model.addAttribute("movie_score", movie_score);
        return model;
    }

    @RequestMapping(value = "/movie/moviefolder", method = RequestMethod.POST)
    public Map<String, Object> goMovieFolder2(String type) throws UnknownHostException {
        ModelAndView modelAndView = new ModelAndView();
        System.out.println("~~~ go to "+ type);
        String field = "genres";
        List<Movie> data = movieService.getCollectionData(field, type);
        Map<String, Object> map = new HashMap<>();
        map.put("movies", data);
        map.put("folder_name", type);
//        modelAndView.setViewName("movieFolder");
        return map;
    }

    @RequestMapping(value = "/movie/movieid", method = RequestMethod.POST)
    public Map<String, Object> getMovieInfo(int mid, HttpServletRequest request) throws ExecutionException, InterruptedException {
        System.out.println("getmovie - get mid = "+mid);
        CompletableFuture<Movie> asy_movie = movieService.asyfindByMID(mid);
        CompletableFuture<String> asy_movie_score = ratingService.asygetMovieAverageScores(mid);
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

    @RequestMapping(value = "/movie/moviefield", method = RequestMethod.POST)
//    @PermissionAnnotation
    public Map<String, Object> searchMovieByField(String fieldname, String value
                            , HttpServletRequest request) throws IOException {
        System.out.println("search movie field = "+fieldname);
        System.out.println("search value = " + value);
        String es_collection = "movietags";
        String[] excludes = {};
        String[] includes = {"mid", "name", "genres", "language", "descri", "issue", "shoot", "directors", "timelong"};

        HashMap<List<MovieVO>, String> map;
        map = esService.fullQuery("match", es_collection, fieldname, value, excludes, includes, 6);
        Set<List<MovieVO>> set = map.keySet();
        List<MovieVO> movieVOList = new ArrayList<>();
        Map<String, Object> res = new HashMap<>();
        for(List<MovieVO> list: set){
            for(MovieVO movieVO: list){
                Integer mid = movieVO.getMid();
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
        }else {
            System.out.println("goto moviename = "+ movieVOList.get(0));
            res.put("movieVOList",movieVOList);
            res.put("number",movieVOList.size());
            res.put("fieldname",fieldname);
            res.put("value", value);
            HttpSession session = request.getSession();
            session.setAttribute("movieVOList", movieVOList);
        }
        return res;
    }


    @RequestMapping(value = "/movie/favor", method = RequestMethod.POST)
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
