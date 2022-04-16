package vjj.webconsumer.services;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(1)
public class PermissionAspect {

    @Pointcut("@annotation(vjj.webconsumer.services.PermissionAnnotation)")
    private void permissionCheck(){
    }

    @Around("permissionCheck()")
    public Object permissionchecking(ProceedingJoinPoint joinPoint) throws Throwable {

        System.out.println("user permission checking...");
        Object[] objs = joinPoint.getArgs();

        return joinPoint.proceed();
    }

    @AfterThrowing("permissionCheck()")
    public void afterThrow(){
        System.out.println("PermissionAspect exception...");
    }
}
