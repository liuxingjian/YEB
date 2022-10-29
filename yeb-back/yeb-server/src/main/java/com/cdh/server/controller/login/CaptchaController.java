package com.cdh.server.controller.login;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author by cdh
 * @description:生成验证码
 * @Date: Created in 22:11 on 2021/4/18
 */
@Api(tags = "生成验证码")
@RestController
public class CaptchaController {

    @Autowired
    private DefaultKaptcha defaultKaptcha;

    @ApiOperation(value = "验证码",produces = "image/jpeg")
    @GetMapping("captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response){
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store,no-cache,must-revalidate");
        response.addHeader("Cache-Control", "post-check=0,pre-check0");
        response.setHeader("Pragma","no-cache");
        response.setContentType("image/jpeg");

        //------------------------生成验证码 begin-------------------------
        String verificationCode = defaultKaptcha.createText();
        System.out.println("验证码内容:"+verificationCode);
        request.getSession().setAttribute("captcha", verificationCode);
        // 根据文本验证码内容生成图形验证码
        BufferedImage verificationImage = defaultKaptcha.createImage(verificationCode);
        ServletOutputStream sos = null;
        try {
            sos = response.getOutputStream();
            // 输出流输出图片，格式为jpg
            ImageIO.write(verificationImage, "jpg", sos);
            sos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (null != sos){
                try {
                    sos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //------------------------生成验证码 end---------------------------
    }
}
