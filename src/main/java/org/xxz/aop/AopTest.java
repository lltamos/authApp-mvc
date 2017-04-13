package org.xxz.aop;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.xxz.anotation.RoleControl;

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

    private final String SESSION_USERTYPE_KEY = "user";

    @Around(value = "execution(* org.xxz.controller..*.*(..)))")
    public Object before(ProceedingJoinPoint proceeding) {


        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();

        Object target = proceeding.getTarget();
        String method = proceeding.getSignature().getName();//切入方法名
        Class<?> classz = target.getClass();//切入的类class文件
        Method m = ((MethodSignature) proceeding.getSignature()).getMethod();
        try {
            if (classz != null && m != null) {
                boolean isClzAnnotation = classz.isAnnotationPresent(RoleControl.class);
                boolean isMethondAnnotation = m.isAnnotationPresent(RoleControl.class);
                RoleControl rc = null;
                //如果方法和类声明中同时存在这个注解，那么方法中的会覆盖类中的设定。
                if (isMethondAnnotation) {
                    rc = m.getAnnotation(RoleControl.class);
                } else if (isClzAnnotation) {
                    rc = classz.getAnnotation(RoleControl.class);
                }
                String value = rc.value();
                Object obj = session.getAttribute(SESSION_USERTYPE_KEY);
                String curUserType = obj == null ? "" : obj.toString();
                //进行角色访问的权限控制，只有当前用户是需要的角色才予以访问。
                boolean isEquals = StringUtils.equals(value, curUserType);
                if (isEquals) {
                    try {
                        return proceeding.proceed();   //放行
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
