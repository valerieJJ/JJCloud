package vjj.webconsumer.FeignServices.Imp;

import VO.MovieVO;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import vjj.webconsumer.FeignServices.IFeignRecService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FeignRecService implements IFeignRecService {
    @Override
    public Map<String, List<MovieVO>> getRecommend() {
        Map<String, List<MovieVO>> map = new HashMap<>();
        map.put("rechotmovieVOS", null);
        map.put("reclatestmovieVOS", null);
        return map;
    }
}
