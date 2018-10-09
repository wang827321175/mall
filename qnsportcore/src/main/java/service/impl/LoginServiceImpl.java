package service.impl;

import common.MD5Utils;
import dao.user.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pojo.user.User;
import pojo.user.UserCriteria;
import service.LoginService;

import java.util.List;

/**
 * 登录的service
 *
 * @author
 */
@Service
@Transactional
public class LoginServiceImpl implements LoginService {

    @Autowired
    UserMapper userMapper;

    /**
     * 根据用户名和密码查询
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public User getUserByUaernameAndPassword(String username, String password) {
        UserCriteria userCriteria = new UserCriteria();
        userCriteria.createCriteria().andUsernameEqualTo(username).andPasswordEqualTo(MD5Utils.md5(password));
        List<User> users = userMapper.selectByExample(userCriteria);
        return users.get(0);
    }
}
