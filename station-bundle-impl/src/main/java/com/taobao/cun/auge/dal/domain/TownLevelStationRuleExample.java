package com.taobao.cun.auge.dal.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TownLevelStationRuleExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table town_level_station_rule
     *
     * @mbggenerated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table town_level_station_rule
     *
     * @mbggenerated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table town_level_station_rule
     *
     * @mbggenerated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table town_level_station_rule
     *
     * @mbggenerated
     */
    public TownLevelStationRuleExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table town_level_station_rule
     *
     * @mbggenerated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table town_level_station_rule
     *
     * @mbggenerated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table town_level_station_rule
     *
     * @mbggenerated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table town_level_station_rule
     *
     * @mbggenerated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table town_level_station_rule
     *
     * @mbggenerated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table town_level_station_rule
     *
     * @mbggenerated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table town_level_station_rule
     *
     * @mbggenerated
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table town_level_station_rule
     *
     * @mbggenerated
     */
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table town_level_station_rule
     *
     * @mbggenerated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table town_level_station_rule
     *
     * @mbggenerated
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table town_level_station_rule
     *
     * @mbggenerated
     */
    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andGmtCreateIsNull() {
            addCriterion("gmt_create is null");
            return (Criteria) this;
        }

        public Criteria andGmtCreateIsNotNull() {
            addCriterion("gmt_create is not null");
            return (Criteria) this;
        }

        public Criteria andGmtCreateEqualTo(Date value) {
            addCriterion("gmt_create =", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateNotEqualTo(Date value) {
            addCriterion("gmt_create <>", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateGreaterThan(Date value) {
            addCriterion("gmt_create >", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateGreaterThanOrEqualTo(Date value) {
            addCriterion("gmt_create >=", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateLessThan(Date value) {
            addCriterion("gmt_create <", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateLessThanOrEqualTo(Date value) {
            addCriterion("gmt_create <=", value, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateIn(List<Date> values) {
            addCriterion("gmt_create in", values, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateNotIn(List<Date> values) {
            addCriterion("gmt_create not in", values, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateBetween(Date value1, Date value2) {
            addCriterion("gmt_create between", value1, value2, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtCreateNotBetween(Date value1, Date value2) {
            addCriterion("gmt_create not between", value1, value2, "gmtCreate");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedIsNull() {
            addCriterion("gmt_modified is null");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedIsNotNull() {
            addCriterion("gmt_modified is not null");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedEqualTo(Date value) {
            addCriterion("gmt_modified =", value, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedNotEqualTo(Date value) {
            addCriterion("gmt_modified <>", value, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedGreaterThan(Date value) {
            addCriterion("gmt_modified >", value, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedGreaterThanOrEqualTo(Date value) {
            addCriterion("gmt_modified >=", value, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedLessThan(Date value) {
            addCriterion("gmt_modified <", value, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedLessThanOrEqualTo(Date value) {
            addCriterion("gmt_modified <=", value, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedIn(List<Date> values) {
            addCriterion("gmt_modified in", values, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedNotIn(List<Date> values) {
            addCriterion("gmt_modified not in", values, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedBetween(Date value1, Date value2) {
            addCriterion("gmt_modified between", value1, value2, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andGmtModifiedNotBetween(Date value1, Date value2) {
            addCriterion("gmt_modified not between", value1, value2, "gmtModified");
            return (Criteria) this;
        }

        public Criteria andLevelIsNull() {
            addCriterion("level is null");
            return (Criteria) this;
        }

        public Criteria andLevelIsNotNull() {
            addCriterion("level is not null");
            return (Criteria) this;
        }

        public Criteria andLevelEqualTo(String value) {
            addCriterion("level =", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelNotEqualTo(String value) {
            addCriterion("level <>", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelGreaterThan(String value) {
            addCriterion("level >", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelGreaterThanOrEqualTo(String value) {
            addCriterion("level >=", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelLessThan(String value) {
            addCriterion("level <", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelLessThanOrEqualTo(String value) {
            addCriterion("level <=", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelLike(String value) {
            addCriterion("level like", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelNotLike(String value) {
            addCriterion("level not like", value, "level");
            return (Criteria) this;
        }

        public Criteria andLevelIn(List<String> values) {
            addCriterion("level in", values, "level");
            return (Criteria) this;
        }

        public Criteria andLevelNotIn(List<String> values) {
            addCriterion("level not in", values, "level");
            return (Criteria) this;
        }

        public Criteria andLevelBetween(String value1, String value2) {
            addCriterion("level between", value1, value2, "level");
            return (Criteria) this;
        }

        public Criteria andLevelNotBetween(String value1, String value2) {
            addCriterion("level not between", value1, value2, "level");
            return (Criteria) this;
        }

        public Criteria andStationTypeCodeIsNull() {
            addCriterion("station_type_code is null");
            return (Criteria) this;
        }

        public Criteria andStationTypeCodeIsNotNull() {
            addCriterion("station_type_code is not null");
            return (Criteria) this;
        }

        public Criteria andStationTypeCodeEqualTo(String value) {
            addCriterion("station_type_code =", value, "stationTypeCode");
            return (Criteria) this;
        }

        public Criteria andStationTypeCodeNotEqualTo(String value) {
            addCriterion("station_type_code <>", value, "stationTypeCode");
            return (Criteria) this;
        }

        public Criteria andStationTypeCodeGreaterThan(String value) {
            addCriterion("station_type_code >", value, "stationTypeCode");
            return (Criteria) this;
        }

        public Criteria andStationTypeCodeGreaterThanOrEqualTo(String value) {
            addCriterion("station_type_code >=", value, "stationTypeCode");
            return (Criteria) this;
        }

        public Criteria andStationTypeCodeLessThan(String value) {
            addCriterion("station_type_code <", value, "stationTypeCode");
            return (Criteria) this;
        }

        public Criteria andStationTypeCodeLessThanOrEqualTo(String value) {
            addCriterion("station_type_code <=", value, "stationTypeCode");
            return (Criteria) this;
        }

        public Criteria andStationTypeCodeLike(String value) {
            addCriterion("station_type_code like", value, "stationTypeCode");
            return (Criteria) this;
        }

        public Criteria andStationTypeCodeNotLike(String value) {
            addCriterion("station_type_code not like", value, "stationTypeCode");
            return (Criteria) this;
        }

        public Criteria andStationTypeCodeIn(List<String> values) {
            addCriterion("station_type_code in", values, "stationTypeCode");
            return (Criteria) this;
        }

        public Criteria andStationTypeCodeNotIn(List<String> values) {
            addCriterion("station_type_code not in", values, "stationTypeCode");
            return (Criteria) this;
        }

        public Criteria andStationTypeCodeBetween(String value1, String value2) {
            addCriterion("station_type_code between", value1, value2, "stationTypeCode");
            return (Criteria) this;
        }

        public Criteria andStationTypeCodeNotBetween(String value1, String value2) {
            addCriterion("station_type_code not between", value1, value2, "stationTypeCode");
            return (Criteria) this;
        }

        public Criteria andStationTypeDescIsNull() {
            addCriterion("station_type_desc is null");
            return (Criteria) this;
        }

        public Criteria andStationTypeDescIsNotNull() {
            addCriterion("station_type_desc is not null");
            return (Criteria) this;
        }

        public Criteria andStationTypeDescEqualTo(String value) {
            addCriterion("station_type_desc =", value, "stationTypeDesc");
            return (Criteria) this;
        }

        public Criteria andStationTypeDescNotEqualTo(String value) {
            addCriterion("station_type_desc <>", value, "stationTypeDesc");
            return (Criteria) this;
        }

        public Criteria andStationTypeDescGreaterThan(String value) {
            addCriterion("station_type_desc >", value, "stationTypeDesc");
            return (Criteria) this;
        }

        public Criteria andStationTypeDescGreaterThanOrEqualTo(String value) {
            addCriterion("station_type_desc >=", value, "stationTypeDesc");
            return (Criteria) this;
        }

        public Criteria andStationTypeDescLessThan(String value) {
            addCriterion("station_type_desc <", value, "stationTypeDesc");
            return (Criteria) this;
        }

        public Criteria andStationTypeDescLessThanOrEqualTo(String value) {
            addCriterion("station_type_desc <=", value, "stationTypeDesc");
            return (Criteria) this;
        }

        public Criteria andStationTypeDescLike(String value) {
            addCriterion("station_type_desc like", value, "stationTypeDesc");
            return (Criteria) this;
        }

        public Criteria andStationTypeDescNotLike(String value) {
            addCriterion("station_type_desc not like", value, "stationTypeDesc");
            return (Criteria) this;
        }

        public Criteria andStationTypeDescIn(List<String> values) {
            addCriterion("station_type_desc in", values, "stationTypeDesc");
            return (Criteria) this;
        }

        public Criteria andStationTypeDescNotIn(List<String> values) {
            addCriterion("station_type_desc not in", values, "stationTypeDesc");
            return (Criteria) this;
        }

        public Criteria andStationTypeDescBetween(String value1, String value2) {
            addCriterion("station_type_desc between", value1, value2, "stationTypeDesc");
            return (Criteria) this;
        }

        public Criteria andStationTypeDescNotBetween(String value1, String value2) {
            addCriterion("station_type_desc not between", value1, value2, "stationTypeDesc");
            return (Criteria) this;
        }

        public Criteria andAreaCodeIsNull() {
            addCriterion("area_code is null");
            return (Criteria) this;
        }

        public Criteria andAreaCodeIsNotNull() {
            addCriterion("area_code is not null");
            return (Criteria) this;
        }

        public Criteria andAreaCodeEqualTo(String value) {
            addCriterion("area_code =", value, "areaCode");
            return (Criteria) this;
        }

        public Criteria andAreaCodeNotEqualTo(String value) {
            addCriterion("area_code <>", value, "areaCode");
            return (Criteria) this;
        }

        public Criteria andAreaCodeGreaterThan(String value) {
            addCriterion("area_code >", value, "areaCode");
            return (Criteria) this;
        }

        public Criteria andAreaCodeGreaterThanOrEqualTo(String value) {
            addCriterion("area_code >=", value, "areaCode");
            return (Criteria) this;
        }

        public Criteria andAreaCodeLessThan(String value) {
            addCriterion("area_code <", value, "areaCode");
            return (Criteria) this;
        }

        public Criteria andAreaCodeLessThanOrEqualTo(String value) {
            addCriterion("area_code <=", value, "areaCode");
            return (Criteria) this;
        }

        public Criteria andAreaCodeLike(String value) {
            addCriterion("area_code like", value, "areaCode");
            return (Criteria) this;
        }

        public Criteria andAreaCodeNotLike(String value) {
            addCriterion("area_code not like", value, "areaCode");
            return (Criteria) this;
        }

        public Criteria andAreaCodeIn(List<String> values) {
            addCriterion("area_code in", values, "areaCode");
            return (Criteria) this;
        }

        public Criteria andAreaCodeNotIn(List<String> values) {
            addCriterion("area_code not in", values, "areaCode");
            return (Criteria) this;
        }

        public Criteria andAreaCodeBetween(String value1, String value2) {
            addCriterion("area_code between", value1, value2, "areaCode");
            return (Criteria) this;
        }

        public Criteria andAreaCodeNotBetween(String value1, String value2) {
            addCriterion("area_code not between", value1, value2, "areaCode");
            return (Criteria) this;
        }

        public Criteria andMemoIsNull() {
            addCriterion("memo is null");
            return (Criteria) this;
        }

        public Criteria andMemoIsNotNull() {
            addCriterion("memo is not null");
            return (Criteria) this;
        }

        public Criteria andMemoEqualTo(String value) {
            addCriterion("memo =", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoNotEqualTo(String value) {
            addCriterion("memo <>", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoGreaterThan(String value) {
            addCriterion("memo >", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoGreaterThanOrEqualTo(String value) {
            addCriterion("memo >=", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoLessThan(String value) {
            addCriterion("memo <", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoLessThanOrEqualTo(String value) {
            addCriterion("memo <=", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoLike(String value) {
            addCriterion("memo like", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoNotLike(String value) {
            addCriterion("memo not like", value, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoIn(List<String> values) {
            addCriterion("memo in", values, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoNotIn(List<String> values) {
            addCriterion("memo not in", values, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoBetween(String value1, String value2) {
            addCriterion("memo between", value1, value2, "memo");
            return (Criteria) this;
        }

        public Criteria andMemoNotBetween(String value1, String value2) {
            addCriterion("memo not between", value1, value2, "memo");
            return (Criteria) this;
        }

        public Criteria andRuleIsNull() {
            addCriterion("rule is null");
            return (Criteria) this;
        }

        public Criteria andRuleIsNotNull() {
            addCriterion("rule is not null");
            return (Criteria) this;
        }

        public Criteria andRuleEqualTo(String value) {
            addCriterion("rule =", value, "rule");
            return (Criteria) this;
        }

        public Criteria andRuleNotEqualTo(String value) {
            addCriterion("rule <>", value, "rule");
            return (Criteria) this;
        }

        public Criteria andRuleGreaterThan(String value) {
            addCriterion("rule >", value, "rule");
            return (Criteria) this;
        }

        public Criteria andRuleGreaterThanOrEqualTo(String value) {
            addCriterion("rule >=", value, "rule");
            return (Criteria) this;
        }

        public Criteria andRuleLessThan(String value) {
            addCriterion("rule <", value, "rule");
            return (Criteria) this;
        }

        public Criteria andRuleLessThanOrEqualTo(String value) {
            addCriterion("rule <=", value, "rule");
            return (Criteria) this;
        }

        public Criteria andRuleLike(String value) {
            addCriterion("rule like", value, "rule");
            return (Criteria) this;
        }

        public Criteria andRuleNotLike(String value) {
            addCriterion("rule not like", value, "rule");
            return (Criteria) this;
        }

        public Criteria andRuleIn(List<String> values) {
            addCriterion("rule in", values, "rule");
            return (Criteria) this;
        }

        public Criteria andRuleNotIn(List<String> values) {
            addCriterion("rule not in", values, "rule");
            return (Criteria) this;
        }

        public Criteria andRuleBetween(String value1, String value2) {
            addCriterion("rule between", value1, value2, "rule");
            return (Criteria) this;
        }

        public Criteria andRuleNotBetween(String value1, String value2) {
            addCriterion("rule not between", value1, value2, "rule");
            return (Criteria) this;
        }

        public Criteria andRuleDataIsNull() {
            addCriterion("rule_data is null");
            return (Criteria) this;
        }

        public Criteria andRuleDataIsNotNull() {
            addCriterion("rule_data is not null");
            return (Criteria) this;
        }

        public Criteria andRuleDataEqualTo(String value) {
            addCriterion("rule_data =", value, "ruleData");
            return (Criteria) this;
        }

        public Criteria andRuleDataNotEqualTo(String value) {
            addCriterion("rule_data <>", value, "ruleData");
            return (Criteria) this;
        }

        public Criteria andRuleDataGreaterThan(String value) {
            addCriterion("rule_data >", value, "ruleData");
            return (Criteria) this;
        }

        public Criteria andRuleDataGreaterThanOrEqualTo(String value) {
            addCriterion("rule_data >=", value, "ruleData");
            return (Criteria) this;
        }

        public Criteria andRuleDataLessThan(String value) {
            addCriterion("rule_data <", value, "ruleData");
            return (Criteria) this;
        }

        public Criteria andRuleDataLessThanOrEqualTo(String value) {
            addCriterion("rule_data <=", value, "ruleData");
            return (Criteria) this;
        }

        public Criteria andRuleDataLike(String value) {
            addCriterion("rule_data like", value, "ruleData");
            return (Criteria) this;
        }

        public Criteria andRuleDataNotLike(String value) {
            addCriterion("rule_data not like", value, "ruleData");
            return (Criteria) this;
        }

        public Criteria andRuleDataIn(List<String> values) {
            addCriterion("rule_data in", values, "ruleData");
            return (Criteria) this;
        }

        public Criteria andRuleDataNotIn(List<String> values) {
            addCriterion("rule_data not in", values, "ruleData");
            return (Criteria) this;
        }

        public Criteria andRuleDataBetween(String value1, String value2) {
            addCriterion("rule_data between", value1, value2, "ruleData");
            return (Criteria) this;
        }

        public Criteria andRuleDataNotBetween(String value1, String value2) {
            addCriterion("rule_data not between", value1, value2, "ruleData");
            return (Criteria) this;
        }

        public Criteria andUpgradeRuleIsNull() {
            addCriterion("upgrade_rule is null");
            return (Criteria) this;
        }

        public Criteria andUpgradeRuleIsNotNull() {
            addCriterion("upgrade_rule is not null");
            return (Criteria) this;
        }

        public Criteria andUpgradeRuleEqualTo(String value) {
            addCriterion("upgrade_rule =", value, "upgradeRule");
            return (Criteria) this;
        }

        public Criteria andUpgradeRuleNotEqualTo(String value) {
            addCriterion("upgrade_rule <>", value, "upgradeRule");
            return (Criteria) this;
        }

        public Criteria andUpgradeRuleGreaterThan(String value) {
            addCriterion("upgrade_rule >", value, "upgradeRule");
            return (Criteria) this;
        }

        public Criteria andUpgradeRuleGreaterThanOrEqualTo(String value) {
            addCriterion("upgrade_rule >=", value, "upgradeRule");
            return (Criteria) this;
        }

        public Criteria andUpgradeRuleLessThan(String value) {
            addCriterion("upgrade_rule <", value, "upgradeRule");
            return (Criteria) this;
        }

        public Criteria andUpgradeRuleLessThanOrEqualTo(String value) {
            addCriterion("upgrade_rule <=", value, "upgradeRule");
            return (Criteria) this;
        }

        public Criteria andUpgradeRuleLike(String value) {
            addCriterion("upgrade_rule like", value, "upgradeRule");
            return (Criteria) this;
        }

        public Criteria andUpgradeRuleNotLike(String value) {
            addCriterion("upgrade_rule not like", value, "upgradeRule");
            return (Criteria) this;
        }

        public Criteria andUpgradeRuleIn(List<String> values) {
            addCriterion("upgrade_rule in", values, "upgradeRule");
            return (Criteria) this;
        }

        public Criteria andUpgradeRuleNotIn(List<String> values) {
            addCriterion("upgrade_rule not in", values, "upgradeRule");
            return (Criteria) this;
        }

        public Criteria andUpgradeRuleBetween(String value1, String value2) {
            addCriterion("upgrade_rule between", value1, value2, "upgradeRule");
            return (Criteria) this;
        }

        public Criteria andUpgradeRuleNotBetween(String value1, String value2) {
            addCriterion("upgrade_rule not between", value1, value2, "upgradeRule");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table town_level_station_rule
     *
     * @mbggenerated do_not_delete_during_merge
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table town_level_station_rule
     *
     * @mbggenerated
     */
    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}