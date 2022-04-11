package vjj.movieservice.controllers;

import models.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import vjj.movieservice.domain.TB1;
import vjj.movieservice.services.imp.TBServiceImp1;

@RestController
public class TBController {
    @Autowired
    TBServiceImp1 tbServiceImp1;

    @RequestMapping("/sql")
    public ModelAndView tb(){
        TB1 tb1 = tbServiceImp1.queryByMid(1001);

        ModelAndView modelAndView = new ModelAndView();
        Movie movie = new Movie();
        movie.set_id(tb1.getMid()+"");
        movie.setName(tb1.getMname());
        modelAndView.addObject("movie", movie);
        modelAndView.setViewName("index");
        System.out.println(movie.getName());
        return modelAndView;
    }
}
