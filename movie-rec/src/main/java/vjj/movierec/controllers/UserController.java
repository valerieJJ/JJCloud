package vjj.movierec.controllers;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
//import valerie.mycontrollers.MongodbService;
//import vjj.movierec.myModel.*;
//import vjj.movierec.myModel.VO.MovieVO;
//import vjj.movierec.myModel.requests.*;
import models.*;
import VO.*;
import requests.*;
import vjj.movierec.utils.SpringUtil;
import vjj.movierec.services.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

//@RequestMapping("/user")
@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private MovieService movieService;
    @Autowired
    private RecService recService;
    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    private LikeService likeService;

    public UserController() throws UnknownHostException {
    }

    @RequestMapping("/user/show")
    public String getData(@ModelAttribute("movie") Movie movie, Model model,HttpServletRequest request) throws UnknownHostException {

        String field="language";
        String value = "English";
        List<Movie> data = userService.getCollectionData(field, value);
        model.addAttribute("mongodata", data.toString());

        HttpSession session1 = request.getSession();
        User usr = (User)session1.getAttribute("user");
        if(usr == null){
            usr = userService.getDefaultUser();
        }
        model.addAttribute("user", usr);

        System.out.println("show - port:"+request.getServerPort()+",session:"+session1.getId());
        System.out.println("show - get user from request.session" + usr.getUsername());
        return "show";
    }

//    @RequestMapping("/accountPage")
//    public String goAccountPage(Model model, HttpServletRequest request) throws UnknownHostException {
//        HttpSession session = request.getSession();
//        System.out.println("accountPage - port:"+request.getServerPort()+",session:"+session.getId());
//        User user = (User)session.getAttribute("user");
//        if(user==null){
//            System.out.println("account - no log in");
//            return "index";
//        }else{
//            model.addAttribute("user", user);
//            return "accountPage";
//        }
//    }


    @RequestMapping("/user/whoisit")
    public String getNickname(Model model, HttpServletRequest request) throws UnknownHostException {
        HttpSession session = request.getSession();
        System.out.println("whoisit - port:"+request.getServerPort()+",session:"+session.getId());
        User user = (User)session.getAttribute("user");

        if(user==null){
            System.out.println("no log in");
            return "index";
        }else{
            model.addAttribute("user", user);
            return "whoisit";
        }
    }



    @RequestMapping("/user/account")
    public String accountPage(Model model, HttpServletRequest request){//

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        System.out.println("account action: sessionID" + session.getId());
        HashMap<String, String> movie_types = movieService.getMovieTypes();
        if(user==null){
            System.out.println("please log in first");
            return "index";
        }else{
            System.out.println("account action: user " + user.getUsername());
            model.addAttribute("user", user);
            model.addAttribute("movie_types", movie_types);
        }

        List<Favorite> favoriteList = favoriteService.getFavoriteHistory(user.getUid());
        List<Integer> mids = new ArrayList<>();
        for (Favorite favorite: favoriteList){
            Integer mid = favorite.getMid();
            mids.add(mid);
        }
        List<MovieVO> favoriteMovieVOS =movieService.getMovieVOS(mids);
        model.addAttribute("favoriteMovieVOS", favoriteMovieVOS);

        return "accountPage";
    }

    @RequestMapping("/user/main")
    public ModelAndView goIndex(@ModelAttribute("movie") Movie movie, HttpServletRequest request) throws ExecutionException, InterruptedException {
        HttpSession session = request.getSession();
        ModelAndView mv = new ModelAndView();
        User user = (User) session.getAttribute("user");

        Set<String> rank = favoriteService.getZsetRank();
        List<Integer> rankmids = rank.stream().limit(5).map(x->Integer.parseInt(x)).collect(Collectors.toList());
        List<MovieVO> rankMovieVOS = movieService.getMovieVOS(rankmids);

        if(user==null){// || session.getAttribute("user")==null
            mv.setViewName("index");
            System.out.println("no log in");
        }else{
            System.out.println("goIndex: username = "+user.getUsername());
            getRecs(mv);
            mv.addObject("user", user);
            mv.addObject("rankmovieVOS", rankMovieVOS);
            mv.setViewName("mainIndex");
        }
        return mv;
    }

    @RequestMapping(value = "/user/{mid}/favor", produces = "application/json", method = RequestMethod.POST )
    @PermissionAnnotation()
    public void doFavorite(
            @PathVariable("mid")int mid, @RequestParam("favoption")boolean favoption, HttpServletRequest request, HttpServletResponse response
            , Model model) throws IllegalAccessException, ServletException, IOException {
//            User user = userService.findByUsername(username);
        System.out.print("doFavorite......");
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            FavoriteRequest favoriteRequest = new FavoriteRequest(user.getUid(), mid);
            boolean succ = false;


            if(favoption){
                succ = favoriteService.updateFavorite(favoriteRequest);
            }else {
                succ = favoriteService.dropFavorite(favoriteRequest);
            }
            boolean state = (favoriteService.findFavorite2Mongo(user.getUid(), mid)==null)?false:true;

            model.addAttribute("success",succ);
            model.addAttribute("state", state);

        String toUrl = "/movie/movieid?mid="+mid;
        request.getRequestDispatcher(toUrl).forward(request, response);
    }

    @RequestMapping(value = "/user/{mid}/like", produces = "application/json", method = RequestMethod.POST )
    @PermissionAnnotation()
    public void like(@PathVariable("mid")int mid, @RequestParam("likeoption")boolean likeoption, HttpServletRequest request, HttpServletResponse response
            , Model model) throws IllegalAccessException, ServletException, IOException {
        LikeService likeService = SpringUtil.getBean(LikeService.class);
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        LikeRequest likeRequest = new LikeRequest(user.getUid(), mid);
        LikeService ls = SpringUtil.getBean(LikeService.class);

        boolean succ = false;

        if(likeoption){
            succ = likeService.updateLike(likeRequest);
        }else {
            succ =likeService.updateLike(likeRequest);
        }
        boolean state = (likeService.findLike(user.getUid(), mid)==null)?false:true;

        model.addAttribute("success",succ);
        model.addAttribute("state", state);

        String toUrl = "/movie/movieid?mid="+mid;
        request.getRequestDispatcher(toUrl).forward(request, response);
    }

    public void getRecs(ModelAndView mv) throws ExecutionException, InterruptedException {
        HotMovieRequest hotMovieRequest = new HotMovieRequest(6);//取出6个
        CompletableFuture<List<MovieVO>> hotMovieVOS = recService.getHotRecommendations(hotMovieRequest);
        LatestMovieRequest latestMovieRequest = new LatestMovieRequest(6);//取出6个
        CompletableFuture<List<MovieVO>> latestMovieVOS = recService.getLatestRecommendations(latestMovieRequest);

        CompletableFuture.allOf(hotMovieVOS, latestMovieVOS).join();
        List<MovieVO> hotmovies = hotMovieVOS.get();
        List<MovieVO> latestmovies = latestMovieVOS.get();
            mv.addObject("rechotmovieVOS", hotmovies);
            mv.addObject("reclatestmovieVOS", latestmovies);
        return ;
    }

    public void getRecs2(Model model) throws ExecutionException, InterruptedException {
        HotMovieRequest hotMovieRequest = new HotMovieRequest(6);//取出6个
        CompletableFuture<List<MovieVO>> hotMovieVOS = recService.getHotRecommendations(hotMovieRequest);
        LatestMovieRequest latestMovieRequest = new LatestMovieRequest(6);//取出6个
        CompletableFuture<List<MovieVO>> latestMovieVOS = recService.getLatestRecommendations(latestMovieRequest);

        CompletableFuture.allOf(hotMovieVOS, latestMovieVOS).join();
        List<MovieVO> hotmovies = hotMovieVOS.get();
        List<MovieVO> latestmovies = latestMovieVOS.get();

            model.addAttribute("rechotmovieVOS", hotmovies);
            model.addAttribute("reclatestmovieVOS", latestmovies);
        return ;
    }
}
