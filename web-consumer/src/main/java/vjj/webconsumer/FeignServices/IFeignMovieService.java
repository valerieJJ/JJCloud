package vjj.webconsumer.FeignServices;

import VO.MovieVO;
import models.Movie;
import models.Rating;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import requests.MovieRatingRequest;
import vjj.webconsumer.FeignServices.Imp.FeignMovieService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@FeignClient(name = "movie-service", fallback = FeignMovieService.class)
public interface IFeignMovieService {

    @RequestMapping(value = "/movie/movieid", method = RequestMethod.GET)
    Map<String, Object> getMovieInfo(@RequestParam("mid") int mid, @RequestParam("request") HttpServletRequest request);

    @RequestMapping(value = "/movie/list", method = RequestMethod.GET)
    List<MovieVO> getMovieVOS(@RequestParam("mids") List<Integer> mids);

    @RequestMapping(value = "/movie/types", method = RequestMethod.GET)
    HashMap<String, String> getMovieTypes();

    @RequestMapping(value = "/movie/query", method = RequestMethod.GET)
    Movie getMovieById(@RequestParam("mid") int mid);

    @RequestMapping(value = "/movie/score", method = RequestMethod.GET)
    String getScoreById(@RequestParam("mid") int mid);

//    @RequestMapping(value = "/movie/rate", method = RequestMethod.POST)
//    public String updateRating( @RequestParam("uid") Integer uid
//                            , @RequestParam("mid") Integer mid
//                            , @RequestParam("score") Double score );

    @RequestMapping(value = "/movie/folder", method = RequestMethod.GET)
    List<Movie> goMovieFolder(@RequestParam("type") String type);

    @RequestMapping(value = "/movie/field", method = RequestMethod.GET)
    List<MovieVO> searchMovieByField(
            @RequestParam("fieldname") String fieldname
            , @RequestParam("value") String value);

//    @RequestMapping(value = "/movie/movieid", method = RequestMethod.POST)
//    public Map<String, Object> getMovieInfo(@RequestParam("id") int mid
//                                ,@RequestParam("reqeust") HttpServletRequest request);
//
//    @RequestMapping(value = "/movie/moviefield", method = RequestMethod.POST)
//    public Map<String, Object> searchMovieByField(
//            @RequestParam("fieldname") String fieldname
//            ,@RequestParam("value") String value
//            ,@RequestParam("request") HttpServletRequest request);
//
//    @RequestMapping(value = "/movie/favor", method = RequestMethod.POST)
//    public boolean doFavor(@RequestParam("request") HttpServletRequest request
//            , @RequestParam("model") Model model);
//
//    @RequestMapping(value = "/movie/types", method = RequestMethod.POST)
//    public HashMap<String, String> getMovieTypes();
//
//    @RequestMapping(value = "/movie/list", method = RequestMethod.POST)
//    public List<MovieVO> getMovieVOS(@RequestBody List<Integer> mids);
}
