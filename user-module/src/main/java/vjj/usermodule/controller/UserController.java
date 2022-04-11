package vjj.usermodule.controller;

import VO.MovieVO;
import models.Movie;
import models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import vjj.usermodule.service.MovieRecService;
import vjj.usermodule.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private MovieRecService movieRecService;

    @RequestMapping(value = "/recc",method = RequestMethod.GET)
    @ResponseBody
    public Map<String, List<MovieVO>> welcome(){
        Map<String, List<MovieVO>> map = movieRecService.welcomePage();
        return map;
    }

    @RequestMapping(value = "/mlist", method = RequestMethod.GET)
    @ResponseBody
    public List<MovieVO> getMovieVOS(){
        List<Integer> mids = Arrays.asList(2546,2548);
        return movieRecService.getMovieVOS(mids);
    }

    @RequestMapping(value = "/types", method = RequestMethod.GET)
    @ResponseBody
    public HashMap<String, String> getTypes(){
        return movieRecService.getMovieTypes();
    }

    @RequestMapping(value = "/mquery",method = RequestMethod.GET)
    @ResponseBody
    public Movie getMovieById(){
        return movieRecService.getMovieById(2546);
    };

    @GetMapping("/login")
    public String gologin(Model model){
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public ModelAndView login(@ModelAttribute User usr
            ,HttpServletRequest request) {
        HttpSession session = request.getSession();
        ModelAndView modelAndView = new ModelAndView();
        User user = userService.queryByName(usr.getUname());
        if(usr.getPassword().equals(user.getPassword())){
            modelAndView.addObject("user",user);
            session.setAttribute("tuser", user);
            modelAndView.setViewName("account");
        }else{
            modelAndView.addObject("user",null);
            modelAndView.setViewName("index");
        }
        return modelAndView;
    }

    @GetMapping("/register")
    public String goRegister(Model model){
        model.addAttribute("user", new User());
        return "register";
    }
    @PostMapping("/register")
    public ModelAndView register(@ModelAttribute User usr
            ,HttpServletRequest request, HttpServletResponse response) throws UnknownHostException, ExecutionException, InterruptedException {
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        user.setUname(usr.getUname());
        user.setPassword(usr.getPassword());
        User res = userService.addUser(user);
        ModelAndView modelAndView1 = new ModelAndView();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("userView");
        return modelAndView;
    }

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public String update(Model model){
        model.addAttribute("user", new User());
        return "account";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ModelAndView updateUser(@ModelAttribute User usr
            , HttpServletResponse response, HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("tuser");
        user.setPassword(usr.getPassword());
        user.setRole((usr.getRole()==null)?"user":user.getRole());
        userService.updateUser(user);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("userView");
        return modelAndView;
    }


    @RequestMapping(value = "/user/uid", method = RequestMethod.GET)
    public User queryByUid2(Integer uid) {
        User user = userService.queryById(uid);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("userView");
        return user;
    }

    @RequestMapping(value = "/user/uname", method = RequestMethod.GET)
    public User queryByName(String uname) {
        User user = userService.queryByName(uname);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("userView");
        return user;
    }

    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
    public String deleteUser(Model model
            , HttpServletResponse response, HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("tuser");
        userService.deleteById(user.getUid());
        session.removeAttribute("tuser");
        System.out.println("delete successful");
        return "index";
    }
    @RequestMapping(value = "/user/{uid}", produces = "application/json", method = RequestMethod.GET )
    public ModelAndView queryByUid(
            @PathVariable("uid")Integer uid, HttpServletRequest request, HttpServletResponse response
            , Model model) throws IllegalAccessException, ServletException, IOException {
//            User user = userService.findByUsername(username);
        User user = userService.queryById(uid);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("userView");
        return modelAndView;
    }


    @RequestMapping(value = "/user/{uid}", method = RequestMethod.POST)
    public ModelAndView addUser(@PathVariable("uid")Integer uid
            , @PathParam("uname") String uname
            , @PathParam("password") String password){
        User user = new User();
        user.setUid(uid);
        user.setUname(uname);
        user.setPassword(password);
        user.setRole("user");

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("userView");
        return modelAndView;
    }


    @RequestMapping(value = "/userlogin", method = RequestMethod.POST)
    @ResponseBody
    public User userlogin(@RequestBody User user) {
        return userService.queryByName(user.getUname());
    }

    @RequestMapping(value = "/userregister", method = RequestMethod.POST)
    @ResponseBody
    public User register2(@RequestBody User usr) {
        usr.setRole((usr.getRole()==null)?"user":usr.getRole());
        User res = userService.addUser(usr);
        return res;
    }

    @RequestMapping(value = "/userupdate", method = RequestMethod.POST)
    @ResponseBody
    public User updateUser2(@RequestParam("usr") User usr, @RequestParam("request") HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("tuser");
        user.setPassword(usr.getPassword());
        user.setRole((usr.getRole()==null)?"user":user.getRole());
        userService.updateUser(user);
        return user;
    }

    @RequestMapping(value = "/userdelete", method = RequestMethod.POST)
    @ResponseBody
    public String deleteUser2(@RequestParam("request") HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("tuser");
        userService.deleteById(user.getUid());
        session.removeAttribute("tuser");
        System.out.println("delete successful");
        return "delete success";
    }

}