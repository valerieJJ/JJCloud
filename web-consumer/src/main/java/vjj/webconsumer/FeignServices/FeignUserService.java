package vjj.webconsumer.FeignServices;

import models.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;

@FeignClient("user-module")
public interface FeignUserService {

    @RequestMapping(value = "/userlogin", method = RequestMethod.POST)
    public User login(@RequestBody User user);

    @RequestMapping(value = "/userregister", method = RequestMethod.POST)
    public User register(@RequestBody User usr);

    @RequestMapping(value = "/userupdate", method = RequestMethod.POST)
    public User update(@RequestBody User user);

    @RequestMapping(value = "/updatepassword", method = RequestMethod.POST)
    public User updatePwd(@RequestParam("uid") Integer uid, @RequestParam("password") String password);

    @RequestMapping(value = "/userdelete", method = RequestMethod.POST)
    public String delete(@RequestParam("uid") Integer uid);

    @RequestMapping(value = "/user/uname", method = RequestMethod.GET)
    public User queryByName(String uname);
}

