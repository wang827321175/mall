package console.contorller;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.FastDFSClient;
import common.IDUtils;
import common.SERVER_URL;
import net.fckeditor.response.UploadResponse;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import pojo.Brand;
import pojo.product.Img;
import pojo.product.Product;
import service.ImgService;
import service.ProductService;


/**
 * 用来处理图片上传
 *
 * @author
 */

@Controller
public class UploadController {

    @Autowired
    ImgService imgService;

    @ResponseBody
    @RequestMapping("/upload/uploadImg.do")
    public String uploadImg(@RequestParam MultipartFile picfile, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String originalFilename = picfile.getOriginalFilename();
        String realPath = request.getSession().getServletContext().getRealPath("/imgs");
        File file1 = new File(realPath);
        if (!file1.exists()) {
            file1.mkdirs();
        }
        String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newName = IDUtils.genImageName() + ext;
        picfile.transferTo(new File(realPath + "/" + newName));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("path", "/imgs/" + newName);
        return jsonObject.toString();
    }

    @ResponseBody
    @RequestMapping("/upload/fastDFSImg.do")
    public String fastDFSImg(@RequestParam MultipartFile picfile, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FastDFSClient fastDFSClient = new FastDFSClient("classpath:properties/fastDFS.properties");
        String originalFilename = picfile.getOriginalFilename();
        String substring = originalFilename.substring(originalFilename.indexOf(".") + 1);
        //保存到数据库
        String path = fastDFSClient.uploadFile(picfile.getBytes(), substring);
        String url = SERVER_URL.IMG_SERVER + path;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("path", path);
        jsonObject.put("url", url);

        return jsonObject.toString();
    }

    /**
     * 回显图片
     */
    @ResponseBody
    @RequestMapping("/img/showImg.do")
    public String showImg(Long id) throws Exception {
        //数据库查询图片路径
        Img img = imgService.findByProductId(id);
        String path = img.getUrl();
        FastDFSClient fastDFSClient = new FastDFSClient("classpath:properties/fastDFS.properties");
        JSONObject jsonObject = new JSONObject();
        String url=SERVER_URL.IMG_SERVER+path;
        jsonObject.put("url",url);
        jsonObject.put("path",path);
        return jsonObject.toString();
    }



    /**
     * 商品图片
     * fck图片上传
     */
    @RequestMapping("/upload/fckImg.do")
    public void fckImg(HttpServletRequest request, HttpServletResponse response) throws Exception {
        FastDFSClient fastDFSClient = new FastDFSClient("classpath:properties/fastDFS.properties");
        //把request包装成multipartRequest
        MultipartRequest multipartRequest = (MultipartRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

        Set<Map.Entry<String, MultipartFile>> entries = fileMap.entrySet();

        for (Map.Entry<String, MultipartFile> entry : entries) {
            MultipartFile pic = entry.getValue();
            String s = fastDFSClient.uploadFile(pic.getBytes(), FilenameUtils.getName(pic.getName()));
            String url = SERVER_URL.IMG_SERVER + s;
            UploadResponse uploadResponse = UploadResponse.getOK(url);
            //写入对象要用print
            response.getWriter().print(uploadResponse);
        }
    }


}