package vjj.webconsumer.services;

import models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import vjj.webconsumer.FeignServices.FeignUserService;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class LoginIntercepter implements HandlerInterceptor {
    @Autowired
    private FeignUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("This is LoginIntercepter ......");
        Cookie[] cookies = request.getCookies();
        // 如果没有cookie，重定向到登录洁面
        if(null==cookies){
            response.sendRedirect(request.getContextPath() + "/user/login");
            return false;
        }
        String userticket = null;

        for(Cookie item: cookies){
            if(item.getName().equals("userticket")){
                userticket = item.getValue();
                break;
            }
        }
        System.out.println("login-intercepter: userticket is " + userticket);

        // 获取登录后保存在session中的用户信息，如果为null，则说明session已过期
        HttpSession session = request.getSession();
        Object object = (User) session.getAttribute("user");

        // 如果cookie里面没有用户登录信息，重定向到登陆界面
        if (userticket==null&&object==null){
            System.out.println("login-intercepter: redirect to login");
            response.sendRedirect(request.getContextPath() + "/user/login");
            return false;
        }

        if(object==null){
            User user = userService.queryByName(userticket);
            session.setAttribute("user", user);
        }
        return true;
    }
}
