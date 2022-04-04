package vjj.usermodule.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

//import vjj.movierec.domain.TB1;
//import vjj.movierec.myModel.Movie;
//import vjj.usermodule.service.ITbService;
import vjj.usermodule.model.Movie;
import vjj.usermodule.model.TB1;
import vjj.usermodule.service.TBServiceImp1;

import java.util.UUID;

@Controller
public class TBController {
    @Value("${server.port}")
    private String serverPort;

    @Autowired
    TBServiceImp1 tbServiceImp1;

//    @Autowired
//    private ITbService tbServiceImp1;

    @RequestMapping("/user/zk")
    public ModelAndView tb(){
        TB1 tb1 = tbServiceImp1.queryByMid(1001);

        ModelAndView modelAndView = new ModelAndView();
        Movie movie = new Movie();
        movie.set_id(tb1.getMid()+"");
//        movie.setName(tb1.getMname());
        movie.setName("service with zookeeper："+UUID.randomUUID().toString());
        modelAndView.addObject("movie", movie);
        modelAndView.setViewName("index");
        System.out.println(movie.getName());
        return modelAndView;
    }
}
