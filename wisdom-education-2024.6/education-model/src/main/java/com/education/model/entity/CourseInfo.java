package com.education.model.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.education.common.annotation.Unique;

@TableName("course_info")
public class CourseInfo extends BaseEntity<CourseInfo> {

	@Unique
	private String name;

	@Unique("grade_info_id")
	@TableField("grade_info_id")
	private Integer gradeInfoId;

	@TableField("school_type")
	private Integer schoolType;

	@TableField("subject_id")
	@Unique("subject_id")
	private Integer subjectId;

	private String represent;
	private String code;
	private Integer sort;
	private Integer status;

	@TableField("study_number")
	private Integer studyNumber;

	@TableField("recommend_index_flag")
	private Integer recommendIndexFlag;

	@TableField("head_img")
	private String headImg;



	public String getRepresent() {
		return represent;
	}

	public void setRepresent(String represent) {
		this.represent = represent;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStudyNumber() {
		return studyNumber;
	}

	public void setStudyNumber(Integer studyNumber) {
		this.studyNumber = studyNumber;
	}

	public Integer getRecommendIndexFlag() {
		return recommendIndexFlag;
	}

	public void setRecommendIndexFlag(Integer recommendIndexFlag) {
		this.recommendIndexFlag = recommendIndexFlag;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getGradeInfoId() {
		return gradeInfoId;
	}

	public void setGradeInfoId(Integer gradeInfoId) {
		this.gradeInfoId = gradeInfoId;
	}

	public Integer getSchoolType() {
		return schoolType;
	}

	public void setSchoolType(Integer schoolType) {
		this.schoolType = schoolType;
	}

	public Integer getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getSort() {
		return sort;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

}