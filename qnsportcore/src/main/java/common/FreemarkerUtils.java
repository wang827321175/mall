package common;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import javax.servlet.ServletContext;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;

/**
 * @author
 */
@Service
public class FreemarkerUtils implements ServletContextAware {

    @Autowired
    FreeMarkerConfig freeMarkerConfig;

    ServletContext servletContext;

    public void toHTML(Map map) {
        Configuration configuration = freeMarkerConfig.getConfiguration();
        try {
            //使用商品id作为静态页名称
            Long id = (Long) map.get("productId");
            Template template = configuration.getTemplate("productDetail.html");
            String path = "/html/product/" + id + ".html";
            //获取服务器绝对路径
            String realPath = servletContext.getRealPath("");
            String filePath = realPath + path;

            //生成文件的编码方式
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(filePath), "utf-8");

            template.process(map, out);
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
