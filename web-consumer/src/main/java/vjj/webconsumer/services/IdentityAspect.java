package vjj.webconsumer.services;

import models.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.stream.Stream;

@Aspect
@Component
@Order(0)
public class IdentityAspect {
    //定义一个AOP切面类时，在类上加个@Aspect注解，然后用@Component注解将该类交给SPring管理
    @Pointcut("@annotation(vjj.webconsumer.services.IdentityAnnotation)")
    private void identityCheck(){
    }

    //ProceedingJoinPoint 只有环绕通知有
    @Around("identityCheck()")
    public Object identityChecking(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] objects = joinPoint.getArgs();//获取接口类的请求参数

        System.out.println("user identity checking...Target = "+joinPoint.getTarget());
        Stream.of(objects).forEach(x->System.out.println(x.toString()));

        HttpServletRequest request = (HttpServletRequest) objects[0];
        HttpServletResponse response = (HttpServletResponse) objects[1];

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        try {
            if(user==null || user.getUname().equals("michael"))
                throw new Exception("no privilege");
        } catch (Exception ex) {
            System.out.println("no privilege");
            response.sendRedirect(request.getContextPath() + "/user/login");
        }
//        if(user==null || user.getUid()<0 || mid<0){
//            return "{\"message\":\"illegal id\",\"code\":403}";
//        }
        return joinPoint.proceed();

    }

    //异常通知（只有在joinPoint.proceed()方法执行向外面抛出了异常，才会执行该通知）
    @AfterThrowing("identityCheck()")
    public void afterThrowing(){
        System.out.println("IdentityAspect exception...");
    }

}
