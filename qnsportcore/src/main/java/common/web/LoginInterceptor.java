package common.web;

import common.Constants;
import common.LocalSessionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户登录拦截器
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    LocalSessionProvider localSessionProvider;

    /**
     * 进入handle之前拦截
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断访问路径是否需要拦截
        String username = localSessionProvider.getAttribute(request, response, Constants.USER_NAME);
        //获得url
        String url = request.getParameter("url");
        String requestURI = request.getRequestURI();
        //判断是否是购买页面
        if (requestURI.startsWith("/buy")){
            if (username!=null){
                //设置标记
                request.setAttribute("isLogin",true);
            }else {
                //没有登录
                request.setAttribute("isLogin", false);
                if (url != null) {
                    //重定向到登录页
                    response.sendRedirect("/login.html?url=" + url);
                    return false;
                }else{
                    response.sendRedirect("/login.html");
                    return false;
                }
            }
        }else {
            //不需要拦截
            if (username!=null){
                request.setAttribute("isLogin",true);
            }else{
                request.setAttribute("isLogin",false);
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
