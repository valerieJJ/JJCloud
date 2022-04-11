package vjj.webconsumer.FeignServices;

import VO.MovieVO;
import models.Movie;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@FeignClient(name = "movie-service")
public interface MovieService {

    @RequestMapping(value = "/movie/query", method = RequestMethod.POST)
    @ResponseBody
    public Movie getMovie(int mid);

    @RequestMapping(value = "/movie/score", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getScoreById(int mid);

    @RequestMapping(value = "/movie/moviefolder", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> goMovieFolder(String type);

    @RequestMapping(value = "/movie/movieid", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getMovieInfo(@RequestParam("id") int mid
                                ,@RequestParam("reqeust") HttpServletRequest request);

    @RequestMapping(value = "/movie/moviefield", method = RequestMethod.POST)
//    @ResponseBody
    public Map<String, Object> searchMovieByField(
            @RequestParam("fieldname") String fieldname
            ,@RequestParam("value") String value
            ,@RequestParam("request") HttpServletRequest request);

    @RequestMapping(value = "/movie/favor", method = RequestMethod.POST)
//    @ResponseBody
    public boolean doFavor(@RequestParam("request") HttpServletRequest request
            , @RequestParam("model") Model model);

    @RequestMapping(value = "/movie/types", method = RequestMethod.POST)
//    @ResponseBody
    public HashMap<String, String> getMovieTypes();

    @RequestMapping(value = "/movie/list", method = RequestMethod.POST)
//    @ResponseBody
    public List<MovieVO> getMovieVOS(@RequestBody List<Integer> mids);
}
