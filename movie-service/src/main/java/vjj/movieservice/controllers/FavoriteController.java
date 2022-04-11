package vjj.movieservice.controllers;

import models.Favorite;
import models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import requests.FavoriteRequest;
import vjj.movieservice.services.FavoriteService;

import java.util.List;
import java.util.Set;

@RestController
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @RequestMapping(value = "/favor/list", method = RequestMethod.GET)
    public List<Favorite> getFavorite(@RequestParam("uid") Integer uid){
        List<Favorite> favoriteList = favoriteService.getFavoriteHistory(uid);
        return favoriteList;
    }

    @RequestMapping(value = "/rank",method = RequestMethod.GET)
    public Set<String> getRank(){
        Set<String> rank = favoriteService.getZsetRank();
        return rank;
    }

    @RequestMapping(value = "/favor/update", method = RequestMethod.POST)
    public boolean updateFavor(@RequestParam("uid") Integer uid, @RequestParam("mid") Integer mid) throws IllegalAccessException {
        FavoriteRequest favoriteRequest = new FavoriteRequest(uid, mid);
        boolean state = favoriteService.updateFavorite(favoriteRequest);
        return state;
    }

    @RequestMapping(value = "/favor/delete", method = RequestMethod.POST)
    public boolean deleteFavor(@RequestParam("uid") Integer uid, @RequestParam("mid") Integer mid) throws IllegalAccessException {
        FavoriteRequest favoriteRequest = new FavoriteRequest(uid, mid);
        boolean state = favoriteService.dropFavorite(favoriteRequest);
        return state;
    }

    @RequestMapping(value = "/favor/query", method = RequestMethod.GET)
    public boolean query(@RequestParam("uid") Integer uid, @RequestParam("mid") Integer mid){
        return (favoriteService.findFavorite2Mongo(uid, mid)==null)?false:true;
    }
}
