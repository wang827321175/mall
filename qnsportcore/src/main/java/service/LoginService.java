package service;

import pojo.user.User;

/**
 * 登录功能的service
 */
public interface LoginService {
    User getUserByUaernameAndPassword(String username, String password);
}
