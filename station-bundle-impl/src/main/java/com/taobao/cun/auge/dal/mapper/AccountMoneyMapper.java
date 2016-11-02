package com.taobao.cun.auge.dal.mapper;

import com.taobao.cun.auge.dal.domain.AccountMoney;
import com.taobao.cun.auge.dal.domain.AccountMoneyExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AccountMoneyMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_money
     *
     * @mbggenerated Wed Nov 02 15:07:23 CST 2016
     */
    int countByExample(AccountMoneyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_money
     *
     * @mbggenerated Wed Nov 02 15:07:23 CST 2016
     */
    int deleteByExample(AccountMoneyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_money
     *
     * @mbggenerated Wed Nov 02 15:07:23 CST 2016
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_money
     *
     * @mbggenerated Wed Nov 02 15:07:23 CST 2016
     */
    int insert(AccountMoney record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_money
     *
     * @mbggenerated Wed Nov 02 15:07:23 CST 2016
     */
    int insertSelective(AccountMoney record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_money
     *
     * @mbggenerated Wed Nov 02 15:07:23 CST 2016
     */
    List<AccountMoney> selectByExample(AccountMoneyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_money
     *
     * @mbggenerated Wed Nov 02 15:07:23 CST 2016
     */
    AccountMoney selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_money
     *
     * @mbggenerated Wed Nov 02 15:07:23 CST 2016
     */
    int updateByExampleSelective(@Param("record") AccountMoney record, @Param("example") AccountMoneyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_money
     *
     * @mbggenerated Wed Nov 02 15:07:23 CST 2016
     */
    int updateByExample(@Param("record") AccountMoney record, @Param("example") AccountMoneyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_money
     *
     * @mbggenerated Wed Nov 02 15:07:23 CST 2016
     */
    int updateByPrimaryKeySelective(AccountMoney record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table account_money
     *
     * @mbggenerated Wed Nov 02 15:07:23 CST 2016
     */
    int updateByPrimaryKey(AccountMoney record);
}