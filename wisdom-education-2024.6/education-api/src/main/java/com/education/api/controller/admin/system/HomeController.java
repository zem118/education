package com.education.api.controller.admin.system;

import com.education.business.service.system.HomeService;
import com.education.common.base.BaseController;
import com.education.common.utils.Result;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页数据接口
 *   

 */
@RequestMapping("/system/home")
@RestController
@Api(tags = "首页数据接口")
public class HomeController extends BaseController {

    @Autowired
    private HomeService homeService;

    @GetMapping("headDataCount")
    public Result count() {
        return Result.success(homeService.countData());
    }
}
