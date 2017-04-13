package org.xxz.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.xxz.anotation.RoleControl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;

/**
 * <p>Title:${type_inName}<p/>
 * <p>Description:<p/>
 * <p>Company: </p>
 *
 * @author litao
 * @mail llsmpsvn@gmail.com
 * @date on 2017/4/13
 */
public class GlobalInterceptor extends HandlerInterceptorAdapter {

    private final String SESSION_USERTYPE_KEY = "user";
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession s=request.getSession();

        //角色权限控制访问
        return roleControl(request,response,handler);
    }
    /**角色权限控制访问*/
    private boolean roleControl(HttpServletRequest request,HttpServletResponse response, Object handler){
        HttpSession session=request.getSession();
        System.out.println(handler.getClass().getName());
        if(handler instanceof HandlerMethod){
            HandlerMethod hm=(HandlerMethod)handler;
            Object target=hm.getBean();
            Class<?> clazz=hm.getBeanType();
            Method m=hm.getMethod();
            try {
                if (clazz!=null && m != null ) {
                    boolean isClzAnnotation= clazz.isAnnotationPresent(RoleControl.class);
                    boolean isMethondAnnotation=m.isAnnotationPresent(RoleControl.class);
                    RoleControl rc=null;
                    //如果方法和类声明中同时存在这个注解，那么方法中的会覆盖类中的设定。
                    if(isMethondAnnotation){
                        rc=m.getAnnotation(RoleControl.class);
                    }else if(isClzAnnotation){
                        rc=clazz.getAnnotation(RoleControl.class);
                    }
                    String value=rc.value();
                    Object obj=session.getAttribute(SESSION_USERTYPE_KEY);
                    String curUserType=obj==null?"":obj.toString();
                    //进行角色访问的权限控制，只有当前用户是需要的角色才予以访问。
                    boolean isEquals= StringUtils.equals(value, curUserType);
                    if(!isEquals){
                        //401未授权访问
                        response.setStatus(401);
                        return false;
                    }
                }
            }catch(Exception ignored){

            }
        }

        return true;
    }
}
