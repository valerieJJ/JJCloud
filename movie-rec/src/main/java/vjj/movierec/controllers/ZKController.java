package vjj.movierec.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import vjj.movierec.domain.TB1;
//import vjj.movierec.myModel.Movie;
import models.*;
import vjj.movierec.services.imp.TBServiceImp1;

import java.util.UUID;

@Controller
public class ZKController {

    @Value("${server.port}")
    private String serverPort;
    @Autowired
    private TBServiceImp1 tbServiceImp1;

    @RequestMapping("/zk")
    public ModelAndView tb(){
        TB1 tb1 = tbServiceImp1.queryByMid(1001);

        ModelAndView modelAndView = new ModelAndView();
        Movie movie = new Movie();
        movie.set_id(tb1.getMid()+"");
//        movie.setName(tb1.getMname());
        movie.setName("movie service with zookeeper："+ UUID.randomUUID().toString());
        modelAndView.addObject("movie", movie);
        modelAndView.setViewName("zkindex");
        System.out.println(movie.getName());
        return modelAndView;
    }

    @RequestMapping(value = "/zkk", method = RequestMethod.GET)
    @ResponseBody
    public Movie zk_tb(){
        TB1 tb1 = tbServiceImp1.queryByMid(1001);

        ModelAndView modelAndView = new ModelAndView();
        Movie movie = new Movie();
        movie.set_id(tb1.getMid()+"");
//        movie.setName(tb1.getMname());
        movie.setName("movie service with zookeeper："+ UUID.randomUUID().toString());
        modelAndView.addObject("movie", movie);
        modelAndView.setViewName("zkindex");
        System.out.println(movie.getName());
        return movie;
    }
}
