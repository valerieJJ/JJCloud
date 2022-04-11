package vjj.webconsumer.controllers;

import VO.MovieVO;
import models.Favorite;
import models.Movie;
import models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import requests.FavoriteRequest;
import requests.LikeRequest;
import vjj.webconsumer.FeignServices.*;
//import vjj.webconsumer.services.PermissionAnnotation;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Controller
public class UserController {

    @Autowired
    private FeignUserService feignUserService;
    @Autowired
    private FeignMovieService feignMovieService;
    @Autowired
    private RecService recService;
    @Autowired
    private FeignFavorService feignFavorService;
    @Autowired
    private FeignLikeService feignLikeService;

    public UserController() throws UnknownHostException {
    }

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
        HashMap<String, String> movie_types = feignMovieService.getMovieTypes();
        if(user==null){
            System.out.println("please log in first");
            return "index";
        }else{
            System.out.println("account action: user " + user.getUname());
            model.addAttribute("user", user);
            model.addAttribute("movie_types", movie_types);
        }

        List<Favorite> favoriteList = feignFavorService.getFavoriteHistory(user.getUid());
        List<Integer> mids = new ArrayList<>();
        for (Favorite favorite: favoriteList){
            Integer mid = favorite.getMid();
            mids.add(mid);
        }
        List<MovieVO> favoriteMovieVOS = feignMovieService.getMovieVOS(mids);
        model.addAttribute("favoriteMovieVOS", favoriteMovieVOS);

        return "accountPage";
    }

    @RequestMapping("/user/main")
    public ModelAndView goIndex(@ModelAttribute("movie") Movie movie, HttpServletRequest request) throws ExecutionException, InterruptedException {
        HttpSession session = request.getSession();
        ModelAndView mv = new ModelAndView();
        User user = (User) session.getAttribute("user");

        Set<String> rank = feignFavorService.getRank();
        List<Integer> rankmids = rank.stream().limit(5).map(x->Integer.parseInt(x)).collect(Collectors.toList());
        List<MovieVO> rankMovieVOS = feignMovieService.getMovieVOS(rankmids);

        if(user==null){// || session.getAttribute("user")==null
            mv.setViewName("index");
            System.out.println("no log in");
        }else{
            System.out.println("goIndex: username = "+user.getUname());

            Map<String , List<MovieVO>> res = recService.getRecommend();
            mv.addObject("rechotmovieVOS", res.get("rechotmovieVOS"));
            mv.addObject("reclatestmovieVOS", res.get("reclatestmovieVOS"));
//            getRecs(mv);
            mv.addObject("user", user);
            mv.addObject("rankmovieVOS", rankMovieVOS);
            mv.setViewName("mainIndex");
        }
        return mv;
    }

    @RequestMapping(value = "/user/{mid}/favor", produces = "application/json", method = RequestMethod.POST )
//    @PermissionAnnotation()
    public void doFavorite(
            @PathVariable("mid")int mid, @RequestParam("favoption")boolean favoption, HttpServletRequest request, HttpServletResponse response
            , Model model) throws IllegalAccessException, ServletException, IOException {
//            User user = userService.findByUsername(username);
        System.out.print("doFavorite......");
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
//            FavoriteRequest favoriteRequest = new FavoriteRequest(user.getUid(), mid);
            boolean succ = false;

            if(favoption){
                succ = feignFavorService.updateFavor(user.getUid(), mid);
            }else {
                succ = feignFavorService.deleteFavor(user.getUid(), mid);
            }
            boolean state = feignFavorService.query(user.getUid(), mid);

            model.addAttribute("success",succ);
            model.addAttribute("state", state);

        String toUrl = "/movie/movieid?mid="+mid;
        request.getRequestDispatcher(toUrl).forward(request, response);
    }

    @RequestMapping(value = "/user/{mid}/like", produces = "application/json", method = RequestMethod.POST )
//    @PermissionAnnotation()
    public void like(@PathVariable("mid")int mid, @RequestParam("likeoption")boolean likeoption, HttpServletRequest request, HttpServletResponse response
            , Model model) throws IllegalAccessException, ServletException, IOException {
//        LikeService likeService = SpringUtil.getBean(LikeService.class);
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        LikeRequest likeRequest = new LikeRequest(user.getUid(), mid);
//        LikeService ls = SpringUtil.getBean(LikeService.class);

        boolean succ = false;

        if(likeoption){
            succ = feignLikeService.updateLike(likeRequest);
        }else {
            succ = feignLikeService.updateLike(likeRequest);
        }
        boolean state = feignLikeService.queryLike(user.getUid(), mid);

        model.addAttribute("success",succ);
        model.addAttribute("state", state);

        String toUrl = "/movie/movieid?mid="+mid;
        request.getRequestDispatcher(toUrl).forward(request, response);
    }

    public void getRecs(ModelAndView mv) throws ExecutionException, InterruptedException {
        Map<String , List<MovieVO>> res = recService.getRecommend();
        mv.addObject("rechotmovieVOS", res.get("rechotmovieVOS"));
        mv.addObject("reclatestmovieVOS", res.get("reclatestmovieVOS"));
        return;
    }
}
