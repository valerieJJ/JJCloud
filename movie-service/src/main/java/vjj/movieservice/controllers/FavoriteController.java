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

    @RequestMapping(value = "/favor/list", method = RequestMethod.POST)
    public List<Favorite> getFavorite(@RequestParam("uid") Integer uid){
        List<Favorite> favoriteList = favoriteService.getFavoriteHistory(uid);
        return favoriteList;
    }

    @RequestMapping(value = "/rank",method = RequestMethod.POST)
    public Set<String> getRank(){
        Set<String> rank = favoriteService.getZsetRank();
        return rank;
    }

    @RequestMapping(value = "/favor/update", method = RequestMethod.POST)
    public boolean updateFavor(@RequestBody FavoriteRequest favoriteRequest) throws IllegalAccessException {
        boolean state = favoriteService.updateFavorite(favoriteRequest);
        return state;
    }

    @RequestMapping(value = "/favor/delete", method = RequestMethod.POST)
    public boolean deleteFavor(@RequestBody FavoriteRequest favoriteRequest) throws IllegalAccessException {
        boolean state = favoriteService.dropFavorite(favoriteRequest);
        return state;
    }

//    @RequestMapping(value = "/favor/query", method = RequestMethod.POST)
//    public boolean query(Integer mid, HttpServletRequest request){
//        HttpSession session = request.getSession();
//        User user = (User) session.getAttribute("user");
//        boolean state = (favoriteService.findFavorite2Mongo(user.getUid(), mid)==null)?false:true;
//        return state;
//    }
//
    @RequestMapping(value = "/favor/query", method = RequestMethod.GET)
    public boolean query(@RequestParam("uid") Integer uid, @RequestParam("mid") Integer mid){
        return (favoriteService.findFavorite2Mongo(uid, mid)==null)?false:true;
    }
}
