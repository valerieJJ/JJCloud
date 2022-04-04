package vjj.usermodule.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import vjj.usermodule.model.Movie;
import vjj.usermodule.model.TB1;
import vjj.usermodule.service.TBServiceImp1;


@Controller
public class TBController {
    @Autowired
    TBServiceImp1 tbServiceImp1;

    @RequestMapping("/")
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
