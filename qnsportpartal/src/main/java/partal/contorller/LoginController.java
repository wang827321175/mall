package partal.contorller;


import common.Constants;
import common.LocalSessionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.access.LocalStatelessSessionProxyFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pojo.user.User;
import service.LoginService;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * 用户登陆的controller
 *
 * @author
 */
@Controller
public class LoginController {

    @Autowired
    LocalSessionProvider localSessionProvider;

    @Autowired
    LoginService loginService;

    /**
     * 跳转登录页面
     *
     * @return
     */
    @RequestMapping(value = "/login.html", method = RequestMethod.GET)
    public String login(Model model, String url) {
        model.addAttribute("url", url);
        return "buyer/login";
    }

    /**
     * 登录
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/login.html", method = RequestMethod.POST)
    public String login(HttpServletRequest request, HttpServletResponse response, String url,
                        Model model, String username, String password, String code) {
        if (code != null) {
            String scode = localSessionProvider.getAttribute(request, response, Constants.USER_CODE);
            if (scode.equalsIgnoreCase(code)) {
                //判断用户名和密码
                if (username != null && username.trim().length() > 0 && password != null && password.trim().length() > 0) {
                    User user = loginService.getUserByUaernameAndPassword(username, password);
                    if (user != null) {
                        localSessionProvider.setAttribute(request, response, Constants.USER_NAME, username);
                        //跳转页面
                        return "redirect:"+url;
                    }
                } else {
                    model.addAttribute("error", "账号或者密码错误");
                }
            } else {
                model.addAttribute("error", "验证码错误");
            }
        } else {
            model.addAttribute("error", "验证码不能为空");
        }

        return "buyer/login";
    }

    // 生成验证码
    @RequestMapping(value = "/login/getCode.html")
    public void getCodeImage(HttpServletRequest request, HttpServletResponse response) {
        BufferedImage img = new BufferedImage(68, 22, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();

        Random r = new Random();

        Color c = new Color(200, 150, 255);

        g.setColor(c);

        g.fillRect(0, 0, 68, 22);

        StringBuffer sb = new StringBuffer();

        char[] ch = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

        int index, len = ch.length;

        for (int i = 0; i < 4; i++) {

            index = r.nextInt(len);

            g.setColor(new Color(r.nextInt(88), r.nextInt(188), r.nextInt

                    (255)));

            g.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 22));

            g.drawString("" + ch[index], (i * 15) + 3, 18);

            sb.append(ch[index]);

        }
        // 把上面生成的验证码放到Session域中
        localSessionProvider.setAttribute(request, response, Constants.USER_CODE, sb.toString());
        try {
            ImageIO.write(img, "JPG", response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
