package vjj.webconsumer.FeignServices;

import VO.MovieVO;
import models.Movie;
import models.Rating;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@FeignClient(name = "movie-service")
public interface MovieService {

    @RequestMapping(value = "/movie/movieid", method = RequestMethod.GET)
    public Map<String, Object> getMovieInfo(@RequestParam("mid") int mid, @RequestParam("request") HttpServletRequest request);

    @RequestMapping(value = "/movie/list", method = RequestMethod.GET)
    public List<MovieVO> getMovieVOS(@RequestParam("mids") List<Integer> mids);

    @RequestMapping(value = "/movie/types", method = RequestMethod.GET)
    public HashMap<String, String> getMovieTypes();

    @RequestMapping(value = "/movie/query", method = RequestMethod.GET)
    public Movie getMovieById(@RequestParam("mid") int mid);

    @RequestMapping(value = "/movie/score", method = RequestMethod.GET)
    public String getScoreById(@RequestParam("mid") int mid);

    @RequestMapping(value = "/movie/rate", method = RequestMethod.POST)
    public Model rateMovie( @RequestParam("rating") Rating ratingReq,
                            @RequestParam("model") Model model,
                            @RequestParam("request") HttpServletRequest request);

    @RequestMapping(value = "/movie/folder", method = RequestMethod.GET)
    public List<Movie> goMovieFolder(@RequestParam("type") String type);

    @RequestMapping(value = "/movie/moviefield", method = RequestMethod.GET)
//    @PermissionAnnotation
    public Map<String, Object> searchMovieByField(
            @RequestParam("fieldname") String fieldname
            ,@RequestParam("value") String value
            ,@RequestParam("request") HttpServletRequest request);

//    @RequestMapping(value = "/movie/query", method = RequestMethod.POST)
//    public Movie getMovie(int mid);
//
//    @RequestMapping(value = "/movie/score", method = RequestMethod.POST)
//    public Map<String, Object> getScoreById(int mid);
//
//    @RequestMapping(value = "/movie/moviefolder", method = RequestMethod.POST)
//    public Map<String, Object> goMovieFolder(String type);
//
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
