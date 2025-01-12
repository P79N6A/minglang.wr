package com.taobao.cun.auge.dal.mapper;

import com.taobao.cun.auge.dal.domain.TownLevelStationRule;
import com.taobao.cun.auge.dal.domain.TownLevelStationRuleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TownLevelStationRuleMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table town_level_station_rule
     *
     * @mbggenerated
     */
    int countByExample(TownLevelStationRuleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table town_level_station_rule
     *
     * @mbggenerated
     */
    int deleteByExample(TownLevelStationRuleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table town_level_station_rule
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table town_level_station_rule
     *
     * @mbggenerated
     */
    int insert(TownLevelStationRule record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table town_level_station_rule
     *
     * @mbggenerated
     */
    int insertSelective(TownLevelStationRule record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table town_level_station_rule
     *
     * @mbggenerated
     */
    List<TownLevelStationRule> selectByExample(TownLevelStationRuleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table town_level_station_rule
     *
     * @mbggenerated
     */
    TownLevelStationRule selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table town_level_station_rule
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") TownLevelStationRule record, @Param("example") TownLevelStationRuleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table town_level_station_rule
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") TownLevelStationRule record, @Param("example") TownLevelStationRuleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table town_level_station_rule
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(TownLevelStationRule record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table town_level_station_rule
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(TownLevelStationRule record);
}