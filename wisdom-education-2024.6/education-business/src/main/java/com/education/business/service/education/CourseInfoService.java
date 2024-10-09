package com.education.business.service.education;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.education.business.mapper.education.CourseInfoMapper;
import com.education.business.service.BaseService;
import com.education.common.constants.EnumConstants;
import com.education.common.model.PageInfo;
import com.education.common.utils.ResultCode;
import com.education.model.dto.CourseInfoDto;
import com.education.model.entity.CourseInfo;
import com.education.model.request.PageParam;
import org.springframework.stereotype.Service;


@Service
public class CourseInfoService extends BaseService<CourseInfoMapper, CourseInfo> {

    public PageInfo<CourseInfoDto> selectPageList(PageParam pageParam, CourseInfo courseInfo) {
        Page<CourseInfoDto> page = new Page<>(pageParam.getPageNumber(), pageParam.getPageSize());
        return selectPage(baseMapper.selectPageList(page, courseInfo));
    }

    public ResultCode deleteById(Integer id) {
        CourseInfo courseInfo = super.getById(id);
        if (courseInfo.getStatus() == EnumConstants.CourseStatus.GROUNDING.getValue()) {
            return new ResultCode(ResultCode.FAIL, "上架课程无法删除");
        }
        super.removeById(id);
        return new ResultCode(ResultCode.SUCCESS, "课程删除成功");
    }
}
