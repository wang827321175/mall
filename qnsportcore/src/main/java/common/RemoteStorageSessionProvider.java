package common;

import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 远程session实现类
 * @author
 */
public class RemoteStorageSessionProvider implements SessionProvider {

    @Autowired
    JedisPool jedisPool;

    @Override
    public void setAttribute(HttpServletRequest request, HttpServletResponse response, String name, String value) {
        Jedis jedis = jedisPool.getResource();
        if(name.equals(Constants.USER_NAME)){
            jedis.set(name+getSessionId(request,response),value);
            //名称缓存30分钟
            jedis.expire(name+getSessionId(request,response),60*30);
        }else{
            //是验证码缓存一分钟
            jedis.set(name+getSessionId(request,response),value);
            //名称缓存半小时
            jedis.expire(name+getSessionId(request,response),60);
        }
        jedis.close();
    }

    @Override
    public String getAttribute(HttpServletRequest request, HttpServletResponse response, String name) {
        Jedis jedis = jedisPool.getResource();
        String value = jedis.get(name + getSessionId(request, response));
        jedis.close();
        return value;
    }

    /**
     * 自定义sessionId
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    public String getSessionId(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String c = findByCookie(cookies, Constants.USER_SESSION);
        if (c != null) {
            //不是第一次
            return c;
        } else {
            //第一次访问
            String sessionId = IDUtils.getSessoinId();
            Cookie cookie = new Cookie(Constants.USER_SESSION, sessionId);
            cookie.setPath("/");
            response.addCookie(cookie);
            return sessionId;
        }


    }

    public String findByCookie(Cookie[] cookies, String name) {
        for (Cookie cookie : cookies) {
            if (cookie.equals(name)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
