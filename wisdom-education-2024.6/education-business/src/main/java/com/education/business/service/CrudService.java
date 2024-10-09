package com.education.business.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.education.common.annotation.Unique;
import com.education.common.cache.CacheBean;
import com.education.common.exception.BusinessException;
import com.education.common.model.PageInfo;
import com.education.common.utils.ObjectUtils;
import com.education.common.utils.ResultCode;
import com.education.model.entity.BaseEntity;
import com.education.model.request.PageParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public abstract class CrudService <M extends BaseMapper<T>, T> extends ServiceImpl<M, T> {

    @Autowired
    @Qualifier("ehcacheBean")
    protected CacheBean cacheBean;

    public T selectFirst(QueryWrapper<T> queryWrapper) {
        queryWrapper.last(" limit 1");
        return super.getOne(queryWrapper);
    }

    public T selectByCacheId(Serializable id) {
        TableInfo tableInfo = getTable();
        String cacheName = tableInfo.getTableName();
        T object = cacheBean.get(cacheName, id);
        if (object == null) {
            object = super.getById(id);
            cacheBean.putValue(cacheName, id, object);
        }
        return object;
    }

    public TableInfo getTable() {
        return SqlHelper.table(entityClass);
    }

    public boolean deleteById(Serializable id) {
        boolean success = super.removeById(id);
        if (success) {
            TableInfo tableInfo = getTable();
            String cacheName = tableInfo.getTableName();
            cacheBean.remove(cacheName, id);
        }
        return success;
    }

    public T selectByCache(String cacheName, Object key, QueryWrapper<T> queryWrapper) {
        T entity = cacheBean.get(cacheName, key);
        if (entity == null) {
            entity = this.selectFirst(queryWrapper);
            cacheBean.putValue(cacheName, key, entity);
        }
        return entity;
    }

    /**
     * 单表分页缓存查询
     * @param pageParam
     * @param entity
     * @return
     */
    public PageInfo<T> selectPageByCache(String cacheName, Object key, PageParam pageParam, T entity) {
        PageInfo<T> pageInfo = cacheBean.get(cacheName, key);
        if (pageInfo == null) {
            pageInfo = this.selectPage(pageParam, entity);
            cacheBean.putValue(cacheName, key, pageInfo);
        }
        return pageInfo;
    }

    public PageInfo<T> selectPageByCache(String cacheName, Object key, PageParam pageParam, Wrapper<T> queryWrapper) {
        PageInfo<T> pageInfo = cacheBean.get(cacheName, key);
        if (pageInfo == null) {
            pageInfo = this.selectPage(pageParam, queryWrapper);
            cacheBean.putValue(cacheName, key, pageInfo);
        }
        return pageInfo;
    }

    /**
     * 单表分页查询
     * @param pageParam
     * @param entity
     * @return
     */
    public PageInfo<T> selectPage(PageParam pageParam, T entity) {
        return this.selectPage(pageParam, Wrappers.query(entity));
    }

    /**
     * 条件列表分页查询
     * @param pageParam
     * @param queryWrapper
     * @return
     */
    public PageInfo<T> selectPage(PageParam pageParam, Wrapper<T> queryWrapper) {
        PageInfo<T> pageInfo = new PageInfo();
        Integer pageNumber = pageParam.getPageNumber();
        Integer pageSize = pageParam.getPageSize();
        if (ObjectUtils.isEmpty(pageNumber) && ObjectUtils.isEmpty(pageSize)) {
            List<T> list = baseMapper.selectList(queryWrapper);
            pageInfo.setDataList(list);
            pageInfo.setTotal(ObjectUtils.isEmpty(list) ? 0 : list.size());
        } else {
            Page<T> page = new Page<>(pageNumber, pageSize);
            Page<T> listPage = super.page(page, queryWrapper);
            pageInfo.setTotal(listPage.getTotal());
            pageInfo.setDataList(page.getRecords());
        }
        return pageInfo;
    }

    public <V> PageInfo<V> selectPage(Page<V> page) {
        PageInfo<V> pageInfo = new PageInfo();
        pageInfo.setTotal(page.getTotal());
        pageInfo.setDataList(page.getRecords());
        return pageInfo;
    }

    public List<T> selectListByCache(String cacheName, Object key, QueryWrapper<T> queryWrapper) {
        List<T> entity = cacheBean.get(cacheName, key);
        if (entity == null) {
            entity = this.list(queryWrapper);
            cacheBean.putValue(cacheName, key, entity);
        }
        return entity;
    }

    @Override
    public boolean saveOrUpdate(T entity) {
        if (entity instanceof BaseEntity) {
            Date now = new Date();
            BaseEntity baseEntity = (BaseEntity) entity;
            Map uniqueFieldMap = this.getUniqueField(entity);
            BaseEntity result = null;
            if (uniqueFieldMap.size() > 0) {
                QueryWrapper queryWrapper = Wrappers.<T>query()
                        .select("id")
                        .allEq(uniqueFieldMap);
                result = (BaseEntity) super.getOne(queryWrapper);
            }
            if (ObjectUtils.isNotEmpty(result)) {
                ResultCode resultCode = new ResultCode(ResultCode.FAIL, "该数据已存在,请勿重复添加");
                if (baseEntity.getId() != null) {
                    // 修改的数据id 不一样，存在相同数据
                    if (baseEntity.getId().intValue() != result.getId().intValue()) {
                        throw new BusinessException(resultCode);
                    }
                } else {
                    throw new BusinessException(resultCode);
                }
            }

            Integer id = baseEntity.getId();
            if (baseEntity.getId() == null) {
                baseEntity.setCreateDate(now);
            } else {
                baseEntity.setUpdateDate(now);
            }

            boolean success = super.saveOrUpdate(entity);
            if (success && id != null) {
                TableInfo tableInfo = this.getTable();
                String cacheName = tableInfo.getTableName();
                cacheBean.putValue(cacheName, id, entity);
            }
            return success;
        }
        throw new RuntimeException(entityClass + " must be extends BaseEntity");
    }

    /**
     * 获取需要进行唯一约束校验的字段
     * @return
     */
    private Map<String, Object> getUniqueField(T entity) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(entity.getClass());
        List<TableFieldInfo> fieldList = tableInfo.getFieldList();
        Map uniqueField = new HashMap<>();
        fieldList.forEach(tableFieldInfo -> {
            Field field = tableFieldInfo.getField();
            Unique unique = field.getAnnotation(Unique.class);
            if (ObjectUtils.isNotEmpty(unique)) {
                try {
                    field.setAccessible(true);
                    String column = ObjectUtils.isEmpty(unique.value()) ? field.getName() : unique.value();
                    uniqueField.put(column, field.get(entity));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        return uniqueField;
    }
}
