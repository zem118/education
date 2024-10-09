package com.education.api.controller.admin.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.education.business.service.system.SystemLogService;
import com.education.common.base.BaseController;
import com.education.common.model.PageInfo;
import com.education.common.utils.Result;
import com.education.model.entity.SystemLog;
import com.education.model.request.PageParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 系统日志管理
 *   
 *   
 */
@RestController
@RequestMapping("/system/log")
public class SystemLogController extends BaseController {

    @Autowired
    private SystemLogService systemLogService;

    @GetMapping("list")
    @RequiresPermissions("system:log:list")
    public Result<PageInfo<SystemLog>> list(PageParam pageParam, SystemLog systemLog) {
        LambdaQueryWrapper queryWrapper = Wrappers.lambdaQuery(systemLog)
                .orderByDesc(SystemLog::getId);
        return Result.success(systemLogService.selectPage(pageParam, queryWrapper));
    }

    @DeleteMapping("{id}")
    @RequiresPermissions("system:log:deleteById")
    public Result deleteById(@PathVariable Integer id) {
        systemLogService.removeById(id);
        return Result.success();
    }
}
