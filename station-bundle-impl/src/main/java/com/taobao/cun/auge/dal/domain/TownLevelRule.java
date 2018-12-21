package com.taobao.cun.auge.dal.domain;

import java.util.Date;

public class TownLevelRule {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column town_level_rule.id
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column town_level_rule.gmt_create
     *
     * @mbggenerated
     */
    private Date gmtCreate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column town_level_rule.gmt_modified
     *
     * @mbggenerated
     */
    private Date gmtModified;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column town_level_rule.level
     *
     * @mbggenerated
     */
    private String level;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column town_level_rule.station_type_code
     *
     * @mbggenerated
     */
    private String stationTypeCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column town_level_rule.station_type_name
     *
     * @mbggenerated
     */
    private String stationTypeName;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column town_level_rule.id
     *
     * @return the value of town_level_rule.id
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column town_level_rule.id
     *
     * @param id the value for town_level_rule.id
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column town_level_rule.gmt_create
     *
     * @return the value of town_level_rule.gmt_create
     *
     * @mbggenerated
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column town_level_rule.gmt_create
     *
     * @param gmtCreate the value for town_level_rule.gmt_create
     *
     * @mbggenerated
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column town_level_rule.gmt_modified
     *
     * @return the value of town_level_rule.gmt_modified
     *
     * @mbggenerated
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column town_level_rule.gmt_modified
     *
     * @param gmtModified the value for town_level_rule.gmt_modified
     *
     * @mbggenerated
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column town_level_rule.level
     *
     * @return the value of town_level_rule.level
     *
     * @mbggenerated
     */
    public String getLevel() {
        return level;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column town_level_rule.level
     *
     * @param level the value for town_level_rule.level
     *
     * @mbggenerated
     */
    public void setLevel(String level) {
        this.level = level;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column town_level_rule.station_type_code
     *
     * @return the value of town_level_rule.station_type_code
     *
     * @mbggenerated
     */
    public String getStationTypeCode() {
        return stationTypeCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column town_level_rule.station_type_code
     *
     * @param stationTypeCode the value for town_level_rule.station_type_code
     *
     * @mbggenerated
     */
    public void setStationTypeCode(String stationTypeCode) {
        this.stationTypeCode = stationTypeCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column town_level_rule.station_type_name
     *
     * @return the value of town_level_rule.station_type_name
     *
     * @mbggenerated
     */
    public String getStationTypeName() {
        return stationTypeName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column town_level_rule.station_type_name
     *
     * @param stationTypeName the value for town_level_rule.station_type_name
     *
     * @mbggenerated
     */
    public void setStationTypeName(String stationTypeName) {
        this.stationTypeName = stationTypeName;
    }
}