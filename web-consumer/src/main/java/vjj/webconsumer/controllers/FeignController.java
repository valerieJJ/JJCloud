package vjj.webconsumer.controllers;

import models.Movie;
import models.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import vjj.webconsumer.FeignServices.MovieService;
import vjj.webconsumer.config.DiscoverServer;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class FeignController {

    @Autowired
    private DiscoverServer discoverServer;
    @Autowired
    private MovieService movieService;

    @RequestMapping(value = "/movieid", method = RequestMethod.GET)
    public ModelAndView tb(
            @ModelAttribute("mid") int mid
            ,@ModelAttribute("rating") Rating rating
            , HttpServletRequest request){
        Map<String, Object> map = movieService.getMovie(mid, rating);
        Movie movie = (Movie) map.get("movie");
        String movie_score = (String) map.get("movie_score");
        map.get("rating_message");

        String name = movie.getName();
        movie.setName(name + ": service from movie recommend");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("movie", movie);
        modelAndView.setViewName("zk");
        return modelAndView;
    }
}
