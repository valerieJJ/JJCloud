package vjj.webconsumer.FeignServices;


import models.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@FeignClient("user-module")
public interface UserService {

    @RequestMapping(value = "/userlogin", method = RequestMethod.POST)
    @ResponseBody
    public User login(@RequestBody User usr);

    @RequestMapping(value = "/userupdate", method = RequestMethod.POST)
    @ResponseBody
    public User updateUser(@RequestParam("usr") User usr
            , @RequestParam("request") HttpServletRequest request);

    @RequestMapping(value = "/userregister", method = RequestMethod.POST)
    @ResponseBody
    public User register(@RequestBody User user);

    @RequestMapping(value = "/userdelete", method = RequestMethod.POST)
    @ResponseBody
    public String delete(@RequestBody HttpServletRequest request);


}

