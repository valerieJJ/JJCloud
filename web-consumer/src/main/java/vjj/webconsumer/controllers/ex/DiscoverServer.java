package vjj.webconsumer.controllers.ex;

import VO.MovieVO;
import models.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import vjj.webconsumer.FeignServices.MovieService;
import vjj.webconsumer.FeignServices.RecService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Configuration
public class DiscoverServer {
    @Autowired
    private MovieService movieService;
    @Autowired
    private RecService recService;

    public Map<String, Object> getMovie(
            int mid
            , Rating rating){
        return movieService.getScoreById(mid);
    }

    public Map<String, List<MovieVO>> getRecommend() {
        return recService.getRecommend();
    }


}
