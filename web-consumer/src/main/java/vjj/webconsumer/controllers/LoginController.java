package vjj.webconsumer.controllers;

import VO.MovieVO;
import models.Movie;

import models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import requests.HotMovieRequest;
import requests.LatestMovieRequest;
import requests.LoginUserRequest;
import vjj.webconsumer.FeignServices.FeignUserService;
import vjj.webconsumer.FeignServices.IFeignMovieService;
import vjj.webconsumer.FeignServices.IFeignRecService;
import vjj.webconsumer.services.HistoryService;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


@Controller
public class LoginController {

    @Autowired
    private FeignUserService feignUserService;
    @Autowired
    private HistoryService historyService;
    @Resource
    private IFeignRecService iFeignRecService;

    /****************************  Register  **************************/
    @RequestMapping(value = "/user/register", method = RequestMethod.GET)
    public String register(){
        return "register";
    }

    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    public String register(Model model, HttpServletRequest request) throws ExecutionException, InterruptedException {
        String name = request.getParameter("username");
        String password = request.getParameter("password");

        System.out.println("username is " + name);
        System.out.println("password is " + password);

        User user = feignUserService.update(new User(name,password,"user"));
        if(user==null){
            System.out.println("user already exists, please login");
            model.addAttribute("success",false);
            model.addAttribute("message"," ???????????????????????????");
            return "register";
        }else{
            model.addAttribute("success", true);
//            User user = userService.registerUser(new RegisterUserRequest(name,password));
            model.addAttribute("user", user);
            getRecs2(model);
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            System.out.println("register successful!");
        }
        return "mainIndex";
    }

    @RequestMapping(value = "/user/login", method = RequestMethod.GET)
    public String login(Model model){
        model.addAttribute("user", new User());
        return "login";
    }

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public String login(@ModelAttribute("user") User usr, @ModelAttribute("movie") Movie movieReq
            , HttpServletRequest request, HttpServletResponse response) throws UnknownHostException, ExecutionException, InterruptedException {

        LoginUserRequest loginUserRequest = new LoginUserRequest(usr.getUname(),usr.getPassword());

        HttpSession session = request.getSession();
        User userCheck = (User) session.getAttribute("user");
        if(userCheck!=null){
            System.out.println("?????????");
            return "redirect:main";//??????????????????????????????????????????/user/main
        }
        System.out.println("get usr: " + usr.toString());
        usr.setRole("user");
        User user = feignUserService.login(usr);

        if(user==null || !usr.getPassword().equals(user.getPassword())){
            System.out.println("Account does not exist");
            return "redirect:/"; // "redirect:login" ???????????? "redirect:/user/login"
        }else {
            System.out.println("\nGet username="+user.getUname());
            System.out.println("Get password="+user.getPassword());

            // ??????????????????????????????session???
            session.setAttribute("user", user);

            // ??????cookie?????????????????????
            String userticket = loginUserRequest.getUsername();
            Cookie cookie_user = new Cookie("userticket", userticket);
            cookie_user.setMaxAge(60*60);//??????????????????1?????????????????????
            cookie_user.setPath(request.getContextPath());//??????cookie????????????
            response.addCookie(cookie_user);// ??????????????????cookie

            return "redirect:main";
//            return "mainIndex";
        }
    }

    /****************************  Log out  **************************/
    @RequestMapping("/user/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // ??????cookie
        Cookie userticket = new Cookie("userticket", "");
        userticket.setMaxAge(0);
        userticket.setPath(request.getContextPath());
        response.addCookie(userticket);

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        historyService.clear(user.getUid());
        // ??????????????????session?????????
        session.removeAttribute("user");

        Object info = session.getAttribute("user");

        if(info==null){
            System.out.println("logout success");
        }else{
            System.out.println("failed to logout");
        }
        return "redirect:/";
//        return "index";
//        return "";
    }

    public void getRecs2(Model model) throws ExecutionException, InterruptedException {
        HotMovieRequest hotMovieRequest = new HotMovieRequest(6);//??????6???
        LatestMovieRequest latestMovieRequest = new LatestMovieRequest(6);//??????6???

        Map<String, List<MovieVO>> map = iFeignRecService.getRecommend();
        List<MovieVO> hotmovies = map.get("rechotmovieVOS");
        List<MovieVO> latestmovies = map.get("reclatestmovieVOS");

        model.addAttribute("rechotmovieVOS", hotmovies);
        model.addAttribute("reclatestmovieVOS", latestmovies);
        return ;
    }



}


