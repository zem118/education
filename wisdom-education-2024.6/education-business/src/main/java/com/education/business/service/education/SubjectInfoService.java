package com.education.business.service.education;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.education.business.mapper.education.SubjectInfoMapper;
import com.education.business.service.BaseService;
import com.education.common.model.PageInfo;
import com.education.model.dto.SubjectInfoDto;
import com.education.model.entity.SubjectInfo;
import com.education.model.request.PageParam;
import org.springframework.stereotype.Service;


@Service
public class SubjectInfoService extends BaseService<SubjectInfoMapper, SubjectInfo> {

    public PageInfo<SubjectInfoDto> selectPageList(PageParam pageParam, SubjectInfo subjectInfo) {
        Page<SubjectInfoDto> page = new Page<>(pageParam.getPageNumber(), pageParam.getPageSize());
        return selectPage(baseMapper.selectPageList(page, subjectInfo));
    }
}
