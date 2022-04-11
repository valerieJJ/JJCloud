package vjj.webconsumer.FeignServices;


import models.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;

@FeignClient("user-module")
public interface UserService {

    @RequestMapping(value = "/userlogin", method = RequestMethod.POST)
    public User login(@RequestBody User user);

    @RequestMapping(value = "/userregister", method = RequestMethod.POST)
    public User register(@RequestBody User usr);

    @RequestMapping(value = "/userupdate", method = RequestMethod.POST)
    public User update(@RequestParam("usr") User usr, @RequestParam("request") HttpServletRequest request);

    @RequestMapping(value = "/userdelete", method = RequestMethod.POST)
    public String delete(@RequestParam("request") HttpServletRequest request);

}

