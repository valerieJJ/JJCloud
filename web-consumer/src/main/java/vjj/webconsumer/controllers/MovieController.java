package vjj.webconsumer.controllers;

import VO.MovieVO;
import lombok.extern.slf4j.Slf4j;
import models.Movie;
import models.Rating;
import models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import vjj.webconsumer.FeignServices.*;
import vjj.webconsumer.services.HistoryService;
import vjj.webconsumer.services.IdentityAnnotation;
import vjj.webconsumer.services.PermissionAnnotation;
//import vjj.webconsumer.services.PermissionAnnotation;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;


@Slf4j
@Controller
public class MovieController {
//    @Autowired
//    private IFeignMovieService IFeignMovieService;
    @Autowired
    private FeignRatingService feignRatingService;
    @Autowired
    private HistoryService historyService;

    @Resource
    private IFeignFavorService iFeignFavorService;
    @Resource
    private IFeignMovieService iFeignMovieService;
    @Resource
    private IFeignRecService iFeignRecService;

    @RequestMapping("/movie/rate")
    @IdentityAnnotation
    public String rateMovie(
            HttpServletRequest request,
            HttpServletResponse response,
            Model model,
            @ModelAttribute("rating") Rating ratingReq) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        int mid = ratingReq.getMid();
        double score = ratingReq.getScore();
//        MovieRatingRequest ratingRequest = new MovieRatingRequest(user.getUid(), mid, score);
        String new_score = feignRatingService.updateMovieRating(user.getUid(), mid, score);
        if(new_score==null||new_score.equals("") ){
            model.addAttribute("rating_message", "rating failed");
        }else {
            model.addAttribute("rating_message", "rating successful");
            System.out.println("rating updated successful!");
            System.out.println("user "+user.getUname());
            System.out.println("mid = "+mid);
            System.out.println("score = "+score);
        }
        Movie movie = (Movie) session.getAttribute("movie");
        model.addAttribute("movie", movie);
        model.addAttribute("movie_score", new_score);
        return "movieInfo";
    }

    @RequestMapping("/movie/favor")
    @IdentityAnnotation
    @PermissionAnnotation
    public void doFavor(
            HttpServletRequest request,
            HttpServletResponse response,
            Model model) {
        int mid = (int) request.getAttribute("mid");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        boolean state = iFeignFavorService.query(user.getUid(), mid);
        model.addAttribute("state", state);
        System.out.println("get state2: "+ state);
    }

    @RequestMapping("/movie/moviefolder/{type}")
    @IdentityAnnotation
    @PermissionAnnotation
    public ModelAndView goMovieFolder(
            HttpServletRequest request
            ,HttpServletResponse response
            ,@PathVariable("type") String type
    ) {
        ModelAndView modelAndView = new ModelAndView();
        System.out.println("~~~ go to "+ type);
        List<Movie> data = iFeignMovieService.goMovieFolder(type);
        modelAndView.addObject("movies", data);
        modelAndView.addObject("folder_name", type);
        modelAndView.setViewName("movieFolder");
        return modelAndView;
    }

    @RequestMapping(value = {"/movie/{mid}"})
    public ModelAndView getMovieInfo(
//            @ModelAttribute("mid") int mid
            @PathVariable("mid") int mid
            ,@ModelAttribute("rating") Rating rating
            , HttpServletRequest request
            , ModelAndView modelAndView) {

        System.out.println("getmovie - get mid = "+mid);

        Movie movie = iFeignMovieService.getMovieById(mid);
        String movie_score = iFeignMovieService.getScoreById(mid);

        if(movie==null){
            System.out.println("movie not found");
            modelAndView.setViewName("show");
        }else {
            modelAndView.addObject("movie", movie);
            modelAndView.addObject("movie_score", movie_score);
        }
        modelAndView.addObject("rating_message", "how do u like it?");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        historyService.addHistory(user.getUid(), mid);
        session.setAttribute("movie", movie);

        boolean state = iFeignFavorService.query(user.getUid(), mid);
        modelAndView.addObject("state", state);

        System.out.println("get state2: "+ state);
        modelAndView.setViewName("movieInfo");
        return modelAndView;
    }

    @RequestMapping(value = "/movie/field", method = RequestMethod.GET)
    public ModelAndView searchMovieByName(@RequestParam("fieldname") String fieldname,
                                          @RequestParam("value")String value
                        , HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        System.out.println("movie field = "+fieldname);
        System.out.println("search value = " + value);
        List<MovieVO> res = iFeignMovieService.searchMovieByField(fieldname, value);

//        int num = (int) res.get("number");

        if(res==null){
            log.info("movie not found");
            modelAndView.setViewName("mainIndex");
        }else {
            List<MovieVO> movieVOList = res;
            System.out.println("movieVOList.toString() ="+movieVOList.toString());
            log.info("goto moviename = "+ movieVOList.get(0));
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



}
