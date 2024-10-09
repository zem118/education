package com.education.common.base;

import com.education.common.model.ModelBeanMap;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 *   

 */
public interface BaseCommonMapper<T> {

    ModelBeanMap queryOne(Map params);

    ModelBeanMap findById(Integer id);

    List<ModelBeanMap> queryList(Map params);

    List<ModelBeanMap> treeList();

    int save(@Param("params") Map params);

  //  int save(SystemRole systemRole);

    int batchSave(Map params);

    int batchUpdate(Map params);

    int update(@Param("params") Map params);

    int batchDelete(Map params);

    int deleteById(Integer id);
}
