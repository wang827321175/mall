package common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * sessionProvider接口
 * 提供本地和远程session存储的实现
 *
 * @author
 */
public interface SessionProvider {

    public void setAttribute(HttpServletRequest request, HttpServletResponse response, String name, String value);

    public String getAttribute(HttpServletRequest request, HttpServletResponse response, String name);

    public String getSessionId(HttpServletRequest request, HttpServletResponse response);
}
