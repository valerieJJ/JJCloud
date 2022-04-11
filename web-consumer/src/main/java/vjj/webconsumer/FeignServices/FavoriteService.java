package vjj.webconsumer.FeignServices;

import models.Favorite;
import models.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import requests.FavoriteRequest;

import java.util.List;
import java.util.Set;

@FeignClient("movie-service")
public interface FavoriteService {
    @RequestMapping(value = "/favor/list", method = RequestMethod.GET)
    public List<Favorite> getFavoriteHistory(@RequestParam("uid") Integer uid);

    @RequestMapping(value = "/rank",method = RequestMethod.GET)
    public Set<String> getRank();

    @RequestMapping(value = "/favor/update", method = RequestMethod.POST)
    public boolean updateFavor(@RequestBody FavoriteRequest favoriteRequest);

    @RequestMapping(value = "/favor/delete", method = RequestMethod.POST)
    public boolean deleteFavor(@RequestBody FavoriteRequest favoriteRequest);

    @RequestMapping(value = "/favor/query", method = RequestMethod.GET)
    public boolean query(@RequestParam("uid") Integer uid, @RequestParam("mid") Integer mid);


}
