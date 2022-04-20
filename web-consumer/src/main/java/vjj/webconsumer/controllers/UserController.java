package vjj.webconsumer.controllers;

import VO.MovieVO;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import models.Favorite;
import models.Movie;
import models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import requests.LikeRequest;

import vjj.webconsumer.FeignServices.*;
import vjj.webconsumer.services.HistoryService;
import vjj.webconsumer.services.IdentityAnnotation;
import vjj.webconsumer.services.PermissionAnnotation;
//import vjj.webconsumer.services.PermissionAnnotation;

import javax.annotation.Resource;
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
@DefaultProperties(defaultFallback = "Payment_defaultFallbackMethod")
public class UserController {

    @Autowired
    private FeignUserService feignUserService;
    @Autowired
    private FeignLikeService feignLikeService;
    @Autowired
    private HistoryService historyService;

    @Resource
    private IFeignFavorService iFeignFavorService;
    @Resource
    private IFeignMovieService iFeignMovieService;
    @Resource
    private IFeignRecService iFeignRecService;

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

    @HystrixCommand(fallbackMethod = "account_fallback",commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "5000"),
            @HystrixProperty(name = "circuitBreaker.enabled",value = "true"),// 是否开启断路器
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "4"),// 请求次数
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "10000"), // 时间窗口期
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "60"),// 失败率达到多少后跳闸
    })
    @RequestMapping(value = "/user/account", method = RequestMethod.GET)
    public String accountPage(Model model, HttpServletRequest request){//
//        try {
//            Thread.sleep(6000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        System.out.println("account action: sessionID" + session.getId());

        HashMap<String, String> movie_types = iFeignMovieService.getMovieTypes();
        if(user==null){
            System.out.println("please log in first");
            return "index";
        }else{
            System.out.println("account action: user " + user.getUname());
            model.addAttribute("user", user);
            model.addAttribute("movie_types", movie_types);
        }
        List<Favorite> favoriteList = iFeignFavorService.getFavoriteHistory(user.getUid());
        List<Integer> mids = new ArrayList<>();
        for (Favorite favorite: favoriteList){
            Integer mid = favorite.getMid();
            mids.add(mid);
        }
        List<MovieVO> favoriteMovieVOS = iFeignMovieService.getMovieVOS(mids);
        model.addAttribute("favoriteMovieVOS", favoriteMovieVOS);
        return "accountPage";
    }

    @RequestMapping(value = "/user/history", method = RequestMethod.GET)
    public ModelAndView getHistory(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        List<Integer> history = historyService.getHistory(user.getUid());
        ModelAndView modelAndView = new ModelAndView();

        if(history==null){
            System.out.println("no history");
            modelAndView.addObject("movieVOList",null);
            modelAndView.addObject("fieldname","no history");
            modelAndView.addObject("value", "total: 0");
            modelAndView.addObject("number",0);
        }else {
            List<MovieVO> movieVOList = iFeignMovieService.getMovieVOS(history);
            modelAndView.addObject("movieVOList",movieVOList);
            modelAndView.addObject("number",movieVOList.size());
            modelAndView.addObject("fieldname","Browsing History");
            modelAndView.addObject("value", "total: "+history.size());
        }
        modelAndView.setViewName("movieList");
        return modelAndView;
    }

    @RequestMapping("/user/main")
    public ModelAndView goIndex(@ModelAttribute("movie") Movie movie, HttpServletRequest request) {
        HttpSession session = request.getSession();
        ModelAndView mv = new ModelAndView();
        User user = (User) session.getAttribute("user");

        Set<String> rank = iFeignFavorService.getRank();
        List<Integer> rankmids = rank.stream().limit(5).map(x->Integer.parseInt(x)).collect(Collectors.toList());
        List<MovieVO> rankMovieVOS = iFeignMovieService.getMovieVOS(rankmids);

        if(user==null){// || session.getAttribute("user")==null
            mv.setViewName("index");
            System.out.println("no log in");
        }else{
            System.out.println("goIndex: username = "+user.getUname());

            Map<String , List<MovieVO>> res = iFeignRecService.getRecommend();
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
                succ = iFeignFavorService.updateFavor(user.getUid(), mid);
            }else {
                succ = iFeignFavorService.deleteFavor(user.getUid(), mid);
            }
            boolean state = iFeignFavorService.query(user.getUid(), mid);

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

    @RequestMapping(value = "/user/update", method = RequestMethod.GET)
    public String update(){
        return "userSetting";
    }

    @RequestMapping(value = "/user/updatePassword", method = RequestMethod.POST)
    @PermissionAnnotation
    public String updatePassword(@RequestParam("password") String password, HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        user.setPassword(password);
        user.setRole("user");
        System.out.println("web: "+user.toString()+"\n\n");
//        feignUserService.updatePwd(user.getUid(), password);
        feignUserService.update(user);
        System.out.println("password reseted ");

        session.setAttribute("user", user);

        List<Favorite> favoriteList = iFeignFavorService.getFavoriteHistory(user.getUid());
        List<Integer> mids = new ArrayList<>();
        for (Favorite favorite: favoriteList){
            Integer mid = favorite.getMid();
            mids.add(mid);
        }
        List<MovieVO> favoriteMovieVOS = iFeignMovieService.getMovieVOS(mids);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("favoriteMovieVOS", favoriteMovieVOS);

        return  "redirect:/user/account";
    }

    @RequestMapping(value = "/user/updateName", method = RequestMethod.POST)
    public String updateName(@RequestParam("name") String name, HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        user.setPassword(name);
        feignUserService.update(user);
        session.setAttribute("user", user);
        return  "redirect:/user/account";
    }

    @RequestMapping(value = "/user/delete")
    @IdentityAnnotation
    @PermissionAnnotation
    public String delete(HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        System.out.println("delete uid="+user.getUid());

        feignUserService.delete(user.getUid());
        session.removeAttribute("user");
        return "redirect:/";
    }

    public void getRecs(ModelAndView mv) throws ExecutionException, InterruptedException {
        Map<String , List<MovieVO>> res = iFeignRecService.getRecommend();
        mv.addObject("rechotmovieVOS", res.get("rechotmovieVOS"));
        mv.addObject("reclatestmovieVOS", res.get("reclatestmovieVOS"));
        return;
    }

    public String account_fallback(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        System.out.println("account action: sessionID" + session.getId());

        if(user==null){
            System.out.println("please log in first");
            return "index";
        }else{
            System.out.println("account action: user " + user.getUname());
            model.addAttribute("user", user);
            model.addAttribute("movie_types", null);
        }

        model.addAttribute("favoriteMovieVOS", null);
        return "accountPage";
    }
}
