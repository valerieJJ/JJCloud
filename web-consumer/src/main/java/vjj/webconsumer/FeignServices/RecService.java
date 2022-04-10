package vjj.webconsumer.FeignServices;

import VO.MovieVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@FeignClient(name = "movie-service")
public interface RecService {
    @GetMapping("/rec")
    @ResponseBody
    public Map<String, List<MovieVO>> getRecommend();
}
