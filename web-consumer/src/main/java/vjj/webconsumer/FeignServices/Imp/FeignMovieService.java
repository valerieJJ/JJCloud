package vjj.webconsumer.FeignServices.Imp;

import VO.MovieVO;
import models.Movie;
import org.springframework.stereotype.Component;
import vjj.webconsumer.FeignServices.IFeignMovieService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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
        List<MovieVO> res = new ArrayList<>();
        return res;
    }

    @Override
    public HashMap<String, String> getMovieTypes() {
        HashMap<String, String> res = new HashMap<>();
        return res;
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
        List<Movie> res = new ArrayList<>();
        return res;
    }

    @Override
    public List<MovieVO> searchMovieByField(String fieldname, String value) {
        List<MovieVO> res = new ArrayList<>();
        return res;
    }

}
