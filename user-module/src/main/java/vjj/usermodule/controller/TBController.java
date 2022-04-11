package vjj.usermodule.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import models.*;
import vjj.usermodule.service.DiscoverServer;
import vjj.usermodule.service.MovieRecService;

@RestController
public class TBController {
    @Autowired
    private MovieRecService movieRecService;

    @Autowired
    private DiscoverServer discoverServer;

//    @RequestMapping(value = "/zk", method = RequestMethod.GET)
//    public ModelAndView tb(){
//        Movie movie = discoverServer.getMovie();
//        String name = movie.getName();
//        movie.setName(name + ": service from movie recommend");
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.addObject("movie", movie);
//        modelAndView.setViewName("zk");
//        return modelAndView;
//    }
//    public ModelAndView tb(){
////        TB1 tb = restTemplate.getForObject(serviceUrl+"/zkk", TB1.class);
//        String tb1 = iTbService.queryByMid();
////        TB1 tb1 = iTbService.queryByMid();
//        ModelAndView modelAndView = new ModelAndView();
//        Movie movie = new Movie();
////        movie.set_id(tb1.getMid()+"");
//        movie.set_id(tb1);
//        movie.setName("service from movie recommend："+UUID.randomUUID().toString());
//        modelAndView.addObject("movie", movie);
//        modelAndView.setViewName("zk");
//        System.out.println(movie.getName());
//        return modelAndView;
//    }
}
