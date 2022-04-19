package vjj.webconsumer.FeignServices.Imp;

import VO.MovieVO;
import models.Movie;
import org.springframework.stereotype.Component;
import vjj.webconsumer.FeignServices.IFeignMovieService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FeignMovieService implements IFeignMovieService {
    @Override
    public Map<String, Object> getMovieInfo(int mid, HttpServletRequest request) {
        return null;
    }

    @Override
    public List<MovieVO> getMovieVOS(List<Integer> mids) {
        return null;
    }

    @Override
    public HashMap<String, String> getMovieTypes() {
        return null;
    }

    @Override
    public Movie getMovieById(int mid) {
        return null;
    }

    @Override
    public String getScoreById(int mid) {
        return null;
    }

    @Override
    public List<Movie> goMovieFolder(String type) {
        return null;
    }

    @Override
    public Map<String, Object> searchMovieByField(String fieldname, String value, HttpServletRequest request) {
        return null;
    }
}
