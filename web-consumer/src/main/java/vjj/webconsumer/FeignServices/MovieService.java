package vjj.webconsumer.FeignServices;

import models.Movie;
import models.Rating;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@FeignClient(name = "movie-service")
public interface MovieService {

    @GetMapping("/mymovie")
    @ResponseBody
    public Map<String, Object> getMovie(
            @RequestParam(value = "mid") int mid
            , @RequestParam(value = "rating") Rating rating);

}
