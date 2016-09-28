package com.taobao.cun.auge.dal.mapper;

import com.taobao.cun.auge.dal.domain.AlilangTopic;
import com.taobao.cun.auge.dal.domain.AlilangTopicExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AlilangTopicMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table alilang_topic
     *
     * @mbggenerated Tue Sep 27 18:52:18 CST 2016
     */
    int countByExample(AlilangTopicExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table alilang_topic
     *
     * @mbggenerated Tue Sep 27 18:52:18 CST 2016
     */
    int deleteByExample(AlilangTopicExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table alilang_topic
     *
     * @mbggenerated Tue Sep 27 18:52:18 CST 2016
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table alilang_topic
     *
     * @mbggenerated Tue Sep 27 18:52:18 CST 2016
     */
    int insert(AlilangTopic record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table alilang_topic
     *
     * @mbggenerated Tue Sep 27 18:52:18 CST 2016
     */
    int insertSelective(AlilangTopic record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table alilang_topic
     *
     * @mbggenerated Tue Sep 27 18:52:18 CST 2016
     */
    List<AlilangTopic> selectByExample(AlilangTopicExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table alilang_topic
     *
     * @mbggenerated Tue Sep 27 18:52:18 CST 2016
     */
    AlilangTopic selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table alilang_topic
     *
     * @mbggenerated Tue Sep 27 18:52:18 CST 2016
     */
    int updateByExampleSelective(@Param("record") AlilangTopic record, @Param("example") AlilangTopicExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table alilang_topic
     *
     * @mbggenerated Tue Sep 27 18:52:18 CST 2016
     */
    int updateByExample(@Param("record") AlilangTopic record, @Param("example") AlilangTopicExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table alilang_topic
     *
     * @mbggenerated Tue Sep 27 18:52:18 CST 2016
     */
    int updateByPrimaryKeySelective(AlilangTopic record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table alilang_topic
     *
     * @mbggenerated Tue Sep 27 18:52:18 CST 2016
     */
    int updateByPrimaryKey(AlilangTopic record);
}