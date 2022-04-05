package vjj.usermodule.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import vjj.movierec.myModel.Movie;
import vjj.movierec.myModel.PermissionAnnotation;
import vjj.movierec.myModel.requests.LoginUserRequest;
import vjj.usermodule.model.User;
import vjj.usermodule.service.UserService;
import vjj.usermodule.service.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
//
    @GetMapping("/login")
    public String gologin(Model model){
        model.addAttribute("user", new User());
        return "login";
    }
    @PostMapping("/login")
    public ModelAndView login(@ModelAttribute User usr
            ,HttpServletRequest request, HttpServletResponse response) throws UnknownHostException, ExecutionException, InterruptedException {
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

        userService.update(user);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("userView");
        return modelAndView;
    }

    @GetMapping("/deleteUser")
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

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("userView");
        return modelAndView;
    }

}
