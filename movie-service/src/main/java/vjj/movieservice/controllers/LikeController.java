package vjj.movieservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import requests.LikeRequest;
import vjj.movieservice.services.LikeService;
//import vjj.movieservice.services.exc.PermissionAnnotation;

@RestController
public class LikeController {
    @Autowired
    private LikeService likeService;

    @RequestMapping(value = "/movie/like", method = RequestMethod.GET)
//    @PermissionAnnotation()
    public boolean queryLike(@RequestParam("mid") Integer mid
                            , @RequestParam("uid") Integer uid) {
        boolean state = (likeService.findLike(uid, mid)==null)?false:true;
        return state;
    }

    @RequestMapping(value = "/movie/likerequest", method = RequestMethod.POST)
    public boolean updateLike(@RequestParam("likeRequest") LikeRequest likeRequest) throws IllegalAccessException {
        return likeService.updateLike(likeRequest);
    }
}
