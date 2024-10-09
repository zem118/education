package com.education.core.controller;

import com.education.common.base.BaseController;
import com.education.common.model.Captcha;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *   

 */
@RestController
public class ImageController extends BaseController {

    @GetMapping("/api/image")
    public void image(HttpServletRequest request, HttpServletResponse response) {
        String key = request.getParameter("key");
        Captcha captcha = new Captcha(cacheBean, key);
        captcha.render(response);
    }
}
