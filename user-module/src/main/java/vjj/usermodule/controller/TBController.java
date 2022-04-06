package vjj.usermodule.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import vjj.movierec.domain.TB1;
import vjj.movierec.myModel.Movie;
import vjj.usermodule.service.DiscoverServer;
import vjj.usermodule.service.ITbService;

import java.util.UUID;

@RestController
public class TBController {
    @Value("${server.port}")
    private String serverPort;

    private String serviceUrl = "http://movie-recommend-service";

//    @Bean
//    public RestTemplate restTemplate(RestTemplateBuilder builder){
//        return builder.build();
//    }
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ITbService iTbService;

//    @RequestMapping("/zk")
//    public ModelAndView tb(){
//
////        System.out.println(serviceUrl()+"\n");
//
//        TB1 tb1 = restTemplate.getForObject(serviceUrl+"/zkk", TB1.class);
//        ModelAndView modelAndView = new ModelAndView();
//        Movie movie = new Movie();
//        movie.set_id(tb1.getMid()+"");
////        movie.setName(tb1.getMname());
//        movie.setName(tb1.getMname()+", service with zookeeper："+UUID.randomUUID().toString());
//        modelAndView.addObject("movie", movie);
//        modelAndView.setViewName("index");
//        System.out.println(movie.getName());
//        return modelAndView;
//    }

    @Autowired
    private DiscoverServer discoverServer;

    @RequestMapping(value = "/zk", method = RequestMethod.GET)
    public String tb(){
        Movie movie = new Movie();
        String s = discoverServer.getTB();
        movie.set_id(s + "");
        movie.setName("service from movie recommend："+UUID.randomUUID().toString());
//        movie.setName("service from movie recommend："+UUID.randomUUID().toString());
        return discoverServer.getTB();
    }
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
