package com.taobao.cun.auge.dal.mapper;

import com.taobao.cun.auge.dal.domain.UnionNewuserOrder;
import com.taobao.cun.auge.dal.domain.UnionNewuserOrderExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UnionNewuserOrderMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table union_newuser_order
     *
     * @mbggenerated
     */
    int deleteByExample(UnionNewuserOrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table union_newuser_order
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table union_newuser_order
     *
     * @mbggenerated
     */
    int insert(UnionNewuserOrder record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table union_newuser_order
     *
     * @mbggenerated
     */
    int insertSelective(UnionNewuserOrder record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table union_newuser_order
     *
     * @mbggenerated
     */
    List<UnionNewuserOrder> selectByExample(UnionNewuserOrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table union_newuser_order
     *
     * @mbggenerated
     */
    UnionNewuserOrder selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table union_newuser_order
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") UnionNewuserOrder record, @Param("example") UnionNewuserOrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table union_newuser_order
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") UnionNewuserOrder record, @Param("example") UnionNewuserOrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table union_newuser_order
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(UnionNewuserOrder record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table union_newuser_order
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(UnionNewuserOrder record);
}