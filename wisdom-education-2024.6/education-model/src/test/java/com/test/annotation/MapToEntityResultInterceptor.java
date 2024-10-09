package com.test.annotation;

import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Signature;
import org.springframework.stereotype.Component;

import java.sql.Statement;

/**
 *   
 *   
 *
 */
@Intercepts(
        {@Signature(method = "handleResultSets", type = ResultSetHandler.class, args = {Statement.class})}
)
@Component
public class MapToEntityResultInterceptor {
}
