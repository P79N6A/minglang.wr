package com.taobao.cun.auge.dal.mapper;

import java.util.List;
import java.util.Map;

import com.taobao.cun.auge.dal.domain.PartnerCourseRecord;
import com.taobao.cun.auge.dal.domain.PartnerCourseRecordExample;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.PartnerPeixunListDetailDto;
import com.taobao.cun.auge.station.dto.PartnerPeixunStatusCountDto;
import com.taobao.cun.crius.exam.dto.ExamInstanceDto;
import com.taobao.cun.crius.exam.dto.ExamInstanceItemDto;
import org.apache.ibatis.annotations.Param;

public interface PartnerCourseRecordMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_course_record
     *
     * @mbggenerated Tue Jul 12 20:13:50 CST 2016
     */
    int countByExample(PartnerCourseRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_course_record
     *
     * @mbggenerated Tue Jul 12 20:13:50 CST 2016
     */
    int deleteByExample(PartnerCourseRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_course_record
     *
     * @mbggenerated Tue Jul 12 20:13:50 CST 2016
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_course_record
     *
     * @mbggenerated Tue Jul 12 20:13:50 CST 2016
     */
    int insert(PartnerCourseRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_course_record
     *
     * @mbggenerated Tue Jul 12 20:13:50 CST 2016
     */
    int insertSelective(PartnerCourseRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_course_record
     *
     * @mbggenerated Tue Jul 12 20:13:50 CST 2016
     */
    List<PartnerCourseRecord> selectByExample(PartnerCourseRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_course_record
     *
     * @mbggenerated Tue Jul 12 20:13:50 CST 2016
     */
    PartnerCourseRecord selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_course_record
     *
     * @mbggenerated Tue Jul 12 20:13:50 CST 2016
     */
    int updateByExampleSelective(@Param("record") PartnerCourseRecord record, @Param("example") PartnerCourseRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_course_record
     *
     * @mbggenerated Tue Jul 12 20:13:50 CST 2016
     */
    int updateByExample(@Param("record") PartnerCourseRecord record, @Param("example") PartnerCourseRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_course_record
     *
     * @mbggenerated Tue Jul 12 20:13:50 CST 2016
     */
    int updateByPrimaryKeySelective(PartnerCourseRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_course_record
     *
     * @mbggenerated Tue Jul 12 20:13:50 CST 2016
     */
    int updateByPrimaryKey(PartnerCourseRecord record);
    /**
     * 数据迁移临时用
     */
    List<ExamInstanceDto> queryExamInstanceList(Map<String,Object> param);
    List<PartnerDto> queryPartnerIden(Map<String,Object> param);
    List<PartnerDto> queryAlilangPartner(Map<String,Object> param);
    List<ExamInstanceItemDto> queryExamInstanceItemList(Map<String,Object> param);
    int insertExamAnswer(Map<String,Object> param);
    int queryExamInstanceAnswer(Map<String,Object> param);

    int updateApplyExamPoint(Map<String,Object> param);
    int updatePartnerBirth(Map<String,Object> param);

    List<PartnerPeixunStatusCountDto> queryPeixunCountByCondition(Map<String,Object> param);
    
    List<PartnerPeixunListDetailDto> queryPeixunList(Map<String,Object> param);
    
    int queryPeixunListCount(Map<String,Object> param);
}