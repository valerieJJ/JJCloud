package vjj.webconsumer.FeignServices;

import models.Favorite;
import models.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import requests.FavoriteRequest;
import vjj.webconsumer.FeignServices.Imp.FeignFavorService;

import java.util.List;
import java.util.Set;

@FeignClient(name="movie-service", fallback = FeignFavorService.class)
public interface IFeignFavorService {
    @RequestMapping(value = "/favor/list", method = RequestMethod.GET)
    List<Favorite> getFavoriteHistory(@RequestParam("uid") Integer uid);

    @RequestMapping(value = "/rank",method = RequestMethod.GET)
    Set<String> getRank();

    @RequestMapping(value = "/favor/update", method = RequestMethod.POST)
    boolean updateFavor(@RequestParam("uid") Integer uid, @RequestParam("mid") Integer mid);

    @RequestMapping(value = "/favor/delete", method = RequestMethod.POST)
    boolean deleteFavor(@RequestParam("uid") Integer uid, @RequestParam("mid") Integer mid);

    @RequestMapping(value = "/favor/query", method = RequestMethod.GET)
    boolean query(@RequestParam("uid") Integer uid, @RequestParam("mid") Integer mid);


}
