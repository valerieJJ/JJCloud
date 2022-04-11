package vjj.webconsumer.FeignServices;


import models.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import requests.LikeRequest;

@FeignClient("movie-service")
public interface LikeService {

    @RequestMapping(value = "/movie/like", method = RequestMethod.GET)
    public boolean queryLike(@RequestParam("mid")Integer mid
            , @RequestParam("uid") Integer uid);


    @RequestMapping(value = "/movie/likerequest", method = RequestMethod.POST)
    public boolean updateLike(@RequestParam LikeRequest likeRequest);

}
