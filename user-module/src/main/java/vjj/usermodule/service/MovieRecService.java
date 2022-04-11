package vjj.usermodule.service;

import VO.MovieVO;
import models.Movie;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


@FeignClient(name="movie-service")
public interface MovieRecService {

    @RequestMapping(path = "/rec", method = RequestMethod.GET)
    public Map<String, List<MovieVO>> welcomePage();

    @RequestMapping(value = "/movie/query", method = RequestMethod.GET)
    public Movie getMovieById(@RequestParam("mid") int mid);

    @RequestMapping(value = "/movie/types", method = RequestMethod.GET)
    public HashMap<String, String> getMovieTypes();

    @RequestMapping(value = "/movie/list", method = RequestMethod.GET)
    public List<MovieVO> getMovieVOS(@RequestParam("mids") List<Integer> mids);
}
