package vjj.webconsumer.controllers;

import VO.MovieVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import models.Movie;
import models.Rating;
import models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import requests.MovieRatingRequest;

import vjj.webconsumer.FeignServices.*;
import vjj.webconsumer.services.PermissionAnnotation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;


@RequestMapping("/movie")
@Controller
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
        }else {
            model.addAttribute("rating_message", "rating failed");
        }

        Movie movie = (Movie)session.getAttribute("movie");
        Map<String, Object> res = movieService.getScoreById(mid);
        model.addAttribute("movie", movie);
        model.addAttribute("movie_score", res.get("movie_score"));
        return "movieInfo";
    }

    @RequestMapping("/moviefolder")
    public ModelAndView goMovieFolder(String type) {
        ModelAndView modelAndView = new ModelAndView();
        System.out.println("~~~ go to "+ type);
        Map<String, Object> map = movieService.goMovieFolder(type);
        List<Movie> data = (List<Movie>) map.get("movies");
        modelAndView.addObject("movies", data);
        modelAndView.addObject("folder_name", map.get("folder_name"));
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

        Map<String,Object> map = movieService.getMovieInfo(mid, request);
        Movie movie = (Movie) map.get("movie");
        String movie_score = (String) map.get("movie_score");

        if(movie==null){
            System.out.println("movie not found");
            modelAndView.setViewName("show");
        }else {
            modelAndView.addObject("movie", movie);
            modelAndView.addObject("movie_score", movie_score);
        }
        modelAndView.addObject("rating_message", "how do u like it?");
        HttpSession session = request.getSession();
        session.setAttribute("movie", movie);

        User user = (User) session.getAttribute("user");
        boolean state = favoriteService.query(user.getUid(), mid);
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
        Map<String, Object> res = movieService.searchMovieByField(fieldname, value, request);

        List<MovieVO> movieVOList = (List<MovieVO>) res.get("movieVOList");
        int num = (int) res.get("number");

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
            HttpSession session = request.getSession();
            session.setAttribute("movieVOList", movieVOList);
        }
        return modelAndView;
    }

    @RequestMapping("/favor")
//    @PermissionAnnotation
    public void doFavor(
            HttpServletRequest request
            , Model model) {
        int mid = (int) request.getAttribute("mid");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        boolean state = favoriteService.query(user.getUid(), mid);
        model.addAttribute("state", state);
        System.out.println("get state2: "+ state);
    }


}
