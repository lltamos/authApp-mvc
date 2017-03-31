package org.xxz.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;

/**
 * <p>Title:${type_inName}<p/>
 * <p>Description:<p/>
 * <p>Company: </p>
 *
 * @author litao
 * @mail llsmpsvn@gmail.com
 * @date on 2017/3/31
 */
@Component
@Aspect
public class AopTest {
    @Around(value = "execution(* org.xxz.controller..*.*(..)))")
    public Object before(ProceedingJoinPoint proceeding ){

        Object obj=null;
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session=request.getSession();

        Object target = proceeding.getTarget();
        String method = proceeding.getSignature().getName();//切入方法名
        Class<?> classz = target.getClass();//切入的类class文件
        Method m = ((MethodSignature) proceeding.getSignature()).getMethod();

        try {
            System.out.println("before");
            obj = proceeding.proceed(proceeding.getArgs());
            System.out.println("before");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return obj;
    }
}
