package com.education.common.base;

import com.education.common.cache.CacheBean;
import org.springframework.beans.factory.annotation.Value;
import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 *   
 *
 */
public abstract class BaseController {


    @Resource(name = "redisCacheBean")
    protected CacheBean cacheBean;
    @Resource
    protected CacheBean ehcacheBean;
    @Value("file.uploadPath")
    protected String baseUploadPath;

    protected static final Set<String> excelTypes = new HashSet<String>() {
        {
            add("application/x-xls");
            add("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            add("application/vnd.ms-excel");
            add("text/xml");
        }
    };

    protected static final Set<String> textTypes = new HashSet<String>() {
        {
            add("text/plain");
        }
    };



}
