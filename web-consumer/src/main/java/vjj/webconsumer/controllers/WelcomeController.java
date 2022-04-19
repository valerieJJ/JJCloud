package vjj.webconsumer.controllers;

import VO.MovieVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import vjj.webconsumer.FeignServices.IFeignMovieService;
import vjj.webconsumer.FeignServices.IFeignRecService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
public class WelcomeController {
    @Resource
    private IFeignRecService iFeignRecService;

    @RequestMapping("/")
    public ModelAndView welcomePage() {
        Map<String, List<MovieVO>> map = iFeignRecService.getRecommend();

        List<MovieVO> hotmovies = map.get("rechotmovieVOS");
        List<MovieVO> latestmovies = map.get("reclatestmovieVOS");

        ModelAndView mv = new ModelAndView();

        mv.addObject("rechotmovieVOS", hotmovies);
        mv.addObject("reclatestmovieVOS", latestmovies);
        mv.setViewName("index");
        return mv;
    }
}
