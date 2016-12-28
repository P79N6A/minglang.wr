package com.taobao.cun.auge.dal.mapper;

import com.taobao.cun.auge.dal.domain.DingtalkUser;
import com.taobao.cun.auge.dal.domain.DingtalkUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DingtalkUserMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dingtalk_user
     *
     * @mbggenerated Tue Jun 14 11:22:12 GMT+08:00 2016
     */
    int countByExample(DingtalkUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dingtalk_user
     *
     * @mbggenerated Tue Jun 14 11:22:12 GMT+08:00 2016
     */
    int deleteByExample(DingtalkUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dingtalk_user
     *
     * @mbggenerated Tue Jun 14 11:22:12 GMT+08:00 2016
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dingtalk_user
     *
     * @mbggenerated Tue Jun 14 11:22:12 GMT+08:00 2016
     */
    int insert(DingtalkUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dingtalk_user
     *
     * @mbggenerated Tue Jun 14 11:22:12 GMT+08:00 2016
     */
    int insertSelective(DingtalkUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dingtalk_user
     *
     * @mbggenerated Tue Jun 14 11:22:12 GMT+08:00 2016
     */
    List<DingtalkUser> selectByExample(DingtalkUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dingtalk_user
     *
     * @mbggenerated Tue Jun 14 11:22:12 GMT+08:00 2016
     */
    DingtalkUser selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dingtalk_user
     *
     * @mbggenerated Tue Jun 14 11:22:12 GMT+08:00 2016
     */
    int updateByExampleSelective(@Param("record") DingtalkUser record, @Param("example") DingtalkUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dingtalk_user
     *
     * @mbggenerated Tue Jun 14 11:22:12 GMT+08:00 2016
     */
    int updateByExample(@Param("record") DingtalkUser record, @Param("example") DingtalkUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dingtalk_user
     *
     * @mbggenerated Tue Jun 14 11:22:12 GMT+08:00 2016
     */
    int updateByPrimaryKeySelective(DingtalkUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dingtalk_user
     *
     * @mbggenerated Tue Jun 14 11:22:12 GMT+08:00 2016
     */
    int updateByPrimaryKey(DingtalkUser record);
}