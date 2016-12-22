package com.taobao.cun.auge.dal.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PartnerLevelTaskBusinessDataExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table partner_level_task_business_data
     *
     * @mbggenerated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table partner_level_task_business_data
     *
     * @mbggenerated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table partner_level_task_business_data
     *
     * @mbggenerated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_level_task_business_data
     *
     * @mbggenerated
     */
    public PartnerLevelTaskBusinessDataExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_level_task_business_data
     *
     * @mbggenerated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_level_task_business_data
     *
     * @mbggenerated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_level_task_business_data
     *
     * @mbggenerated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_level_task_business_data
     *
     * @mbggenerated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_level_task_business_data
     *
     * @mbggenerated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_level_task_business_data
     *
     * @mbggenerated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_level_task_business_data
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
     * This method corresponds to the database table partner_level_task_business_data
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
     * This method corresponds to the database table partner_level_task_business_data
     *
     * @mbggenerated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table partner_level_task_business_data
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
     * This class corresponds to the database table partner_level_task_business_data
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

        public Criteria andProcessInstanceIdIsNull() {
            addCriterion("process_instance_id is null");
            return (Criteria) this;
        }

        public Criteria andProcessInstanceIdIsNotNull() {
            addCriterion("process_instance_id is not null");
            return (Criteria) this;
        }

        public Criteria andProcessInstanceIdEqualTo(String value) {
            addCriterion("process_instance_id =", value, "processInstanceId");
            return (Criteria) this;
        }

        public Criteria andProcessInstanceIdNotEqualTo(String value) {
            addCriterion("process_instance_id <>", value, "processInstanceId");
            return (Criteria) this;
        }

        public Criteria andProcessInstanceIdGreaterThan(String value) {
            addCriterion("process_instance_id >", value, "processInstanceId");
            return (Criteria) this;
        }

        public Criteria andProcessInstanceIdGreaterThanOrEqualTo(String value) {
            addCriterion("process_instance_id >=", value, "processInstanceId");
            return (Criteria) this;
        }

        public Criteria andProcessInstanceIdLessThan(String value) {
            addCriterion("process_instance_id <", value, "processInstanceId");
            return (Criteria) this;
        }

        public Criteria andProcessInstanceIdLessThanOrEqualTo(String value) {
            addCriterion("process_instance_id <=", value, "processInstanceId");
            return (Criteria) this;
        }

        public Criteria andProcessInstanceIdLike(String value) {
            addCriterion("process_instance_id like", value, "processInstanceId");
            return (Criteria) this;
        }

        public Criteria andProcessInstanceIdNotLike(String value) {
            addCriterion("process_instance_id not like", value, "processInstanceId");
            return (Criteria) this;
        }

        public Criteria andProcessInstanceIdIn(List<String> values) {
            addCriterion("process_instance_id in", values, "processInstanceId");
            return (Criteria) this;
        }

        public Criteria andProcessInstanceIdNotIn(List<String> values) {
            addCriterion("process_instance_id not in", values, "processInstanceId");
            return (Criteria) this;
        }

        public Criteria andProcessInstanceIdBetween(String value1, String value2) {
            addCriterion("process_instance_id between", value1, value2, "processInstanceId");
            return (Criteria) this;
        }

        public Criteria andProcessInstanceIdNotBetween(String value1, String value2) {
            addCriterion("process_instance_id not between", value1, value2, "processInstanceId");
            return (Criteria) this;
        }

        public Criteria andTaskNodeIsNull() {
            addCriterion("task_node is null");
            return (Criteria) this;
        }

        public Criteria andTaskNodeIsNotNull() {
            addCriterion("task_node is not null");
            return (Criteria) this;
        }

        public Criteria andTaskNodeEqualTo(Long value) {
            addCriterion("task_node =", value, "taskNode");
            return (Criteria) this;
        }

        public Criteria andTaskNodeNotEqualTo(Long value) {
            addCriterion("task_node <>", value, "taskNode");
            return (Criteria) this;
        }

        public Criteria andTaskNodeGreaterThan(Long value) {
            addCriterion("task_node >", value, "taskNode");
            return (Criteria) this;
        }

        public Criteria andTaskNodeGreaterThanOrEqualTo(Long value) {
            addCriterion("task_node >=", value, "taskNode");
            return (Criteria) this;
        }

        public Criteria andTaskNodeLessThan(Long value) {
            addCriterion("task_node <", value, "taskNode");
            return (Criteria) this;
        }

        public Criteria andTaskNodeLessThanOrEqualTo(Long value) {
            addCriterion("task_node <=", value, "taskNode");
            return (Criteria) this;
        }

        public Criteria andTaskNodeIn(List<Long> values) {
            addCriterion("task_node in", values, "taskNode");
            return (Criteria) this;
        }

        public Criteria andTaskNodeNotIn(List<Long> values) {
            addCriterion("task_node not in", values, "taskNode");
            return (Criteria) this;
        }

        public Criteria andTaskNodeBetween(Long value1, Long value2) {
            addCriterion("task_node between", value1, value2, "taskNode");
            return (Criteria) this;
        }

        public Criteria andTaskNodeNotBetween(Long value1, Long value2) {
            addCriterion("task_node not between", value1, value2, "taskNode");
            return (Criteria) this;
        }

        public Criteria andAuditedPersonIdIsNull() {
            addCriterion("audited_person_id is null");
            return (Criteria) this;
        }

        public Criteria andAuditedPersonIdIsNotNull() {
            addCriterion("audited_person_id is not null");
            return (Criteria) this;
        }

        public Criteria andAuditedPersonIdEqualTo(Long value) {
            addCriterion("audited_person_id =", value, "auditedPersonId");
            return (Criteria) this;
        }

        public Criteria andAuditedPersonIdNotEqualTo(Long value) {
            addCriterion("audited_person_id <>", value, "auditedPersonId");
            return (Criteria) this;
        }

        public Criteria andAuditedPersonIdGreaterThan(Long value) {
            addCriterion("audited_person_id >", value, "auditedPersonId");
            return (Criteria) this;
        }

        public Criteria andAuditedPersonIdGreaterThanOrEqualTo(Long value) {
            addCriterion("audited_person_id >=", value, "auditedPersonId");
            return (Criteria) this;
        }

        public Criteria andAuditedPersonIdLessThan(Long value) {
            addCriterion("audited_person_id <", value, "auditedPersonId");
            return (Criteria) this;
        }

        public Criteria andAuditedPersonIdLessThanOrEqualTo(Long value) {
            addCriterion("audited_person_id <=", value, "auditedPersonId");
            return (Criteria) this;
        }

        public Criteria andAuditedPersonIdIn(List<Long> values) {
            addCriterion("audited_person_id in", values, "auditedPersonId");
            return (Criteria) this;
        }

        public Criteria andAuditedPersonIdNotIn(List<Long> values) {
            addCriterion("audited_person_id not in", values, "auditedPersonId");
            return (Criteria) this;
        }

        public Criteria andAuditedPersonIdBetween(Long value1, Long value2) {
            addCriterion("audited_person_id between", value1, value2, "auditedPersonId");
            return (Criteria) this;
        }

        public Criteria andAuditedPersonIdNotBetween(Long value1, Long value2) {
            addCriterion("audited_person_id not between", value1, value2, "auditedPersonId");
            return (Criteria) this;
        }

        public Criteria andAuditPersonNameIsNull() {
            addCriterion("audit_person_name is null");
            return (Criteria) this;
        }

        public Criteria andAuditPersonNameIsNotNull() {
            addCriterion("audit_person_name is not null");
            return (Criteria) this;
        }

        public Criteria andAuditPersonNameEqualTo(String value) {
            addCriterion("audit_person_name =", value, "auditPersonName");
            return (Criteria) this;
        }

        public Criteria andAuditPersonNameNotEqualTo(String value) {
            addCriterion("audit_person_name <>", value, "auditPersonName");
            return (Criteria) this;
        }

        public Criteria andAuditPersonNameGreaterThan(String value) {
            addCriterion("audit_person_name >", value, "auditPersonName");
            return (Criteria) this;
        }

        public Criteria andAuditPersonNameGreaterThanOrEqualTo(String value) {
            addCriterion("audit_person_name >=", value, "auditPersonName");
            return (Criteria) this;
        }

        public Criteria andAuditPersonNameLessThan(String value) {
            addCriterion("audit_person_name <", value, "auditPersonName");
            return (Criteria) this;
        }

        public Criteria andAuditPersonNameLessThanOrEqualTo(String value) {
            addCriterion("audit_person_name <=", value, "auditPersonName");
            return (Criteria) this;
        }

        public Criteria andAuditPersonNameLike(String value) {
            addCriterion("audit_person_name like", value, "auditPersonName");
            return (Criteria) this;
        }

        public Criteria andAuditPersonNameNotLike(String value) {
            addCriterion("audit_person_name not like", value, "auditPersonName");
            return (Criteria) this;
        }

        public Criteria andAuditPersonNameIn(List<String> values) {
            addCriterion("audit_person_name in", values, "auditPersonName");
            return (Criteria) this;
        }

        public Criteria andAuditPersonNameNotIn(List<String> values) {
            addCriterion("audit_person_name not in", values, "auditPersonName");
            return (Criteria) this;
        }

        public Criteria andAuditPersonNameBetween(String value1, String value2) {
            addCriterion("audit_person_name between", value1, value2, "auditPersonName");
            return (Criteria) this;
        }

        public Criteria andAuditPersonNameNotBetween(String value1, String value2) {
            addCriterion("audit_person_name not between", value1, value2, "auditPersonName");
            return (Criteria) this;
        }

        public Criteria andParticipantsIsNull() {
            addCriterion("participants is null");
            return (Criteria) this;
        }

        public Criteria andParticipantsIsNotNull() {
            addCriterion("participants is not null");
            return (Criteria) this;
        }

        public Criteria andParticipantsEqualTo(String value) {
            addCriterion("participants =", value, "participants");
            return (Criteria) this;
        }

        public Criteria andParticipantsNotEqualTo(String value) {
            addCriterion("participants <>", value, "participants");
            return (Criteria) this;
        }

        public Criteria andParticipantsGreaterThan(String value) {
            addCriterion("participants >", value, "participants");
            return (Criteria) this;
        }

        public Criteria andParticipantsGreaterThanOrEqualTo(String value) {
            addCriterion("participants >=", value, "participants");
            return (Criteria) this;
        }

        public Criteria andParticipantsLessThan(String value) {
            addCriterion("participants <", value, "participants");
            return (Criteria) this;
        }

        public Criteria andParticipantsLessThanOrEqualTo(String value) {
            addCriterion("participants <=", value, "participants");
            return (Criteria) this;
        }

        public Criteria andParticipantsLike(String value) {
            addCriterion("participants like", value, "participants");
            return (Criteria) this;
        }

        public Criteria andParticipantsNotLike(String value) {
            addCriterion("participants not like", value, "participants");
            return (Criteria) this;
        }

        public Criteria andParticipantsIn(List<String> values) {
            addCriterion("participants in", values, "participants");
            return (Criteria) this;
        }

        public Criteria andParticipantsNotIn(List<String> values) {
            addCriterion("participants not in", values, "participants");
            return (Criteria) this;
        }

        public Criteria andParticipantsBetween(String value1, String value2) {
            addCriterion("participants between", value1, value2, "participants");
            return (Criteria) this;
        }

        public Criteria andParticipantsNotBetween(String value1, String value2) {
            addCriterion("participants not between", value1, value2, "participants");
            return (Criteria) this;
        }

        public Criteria andInfoTypeIsNull() {
            addCriterion("info_type is null");
            return (Criteria) this;
        }

        public Criteria andInfoTypeIsNotNull() {
            addCriterion("info_type is not null");
            return (Criteria) this;
        }

        public Criteria andInfoTypeEqualTo(String value) {
            addCriterion("info_type =", value, "infoType");
            return (Criteria) this;
        }

        public Criteria andInfoTypeNotEqualTo(String value) {
            addCriterion("info_type <>", value, "infoType");
            return (Criteria) this;
        }

        public Criteria andInfoTypeGreaterThan(String value) {
            addCriterion("info_type >", value, "infoType");
            return (Criteria) this;
        }

        public Criteria andInfoTypeGreaterThanOrEqualTo(String value) {
            addCriterion("info_type >=", value, "infoType");
            return (Criteria) this;
        }

        public Criteria andInfoTypeLessThan(String value) {
            addCriterion("info_type <", value, "infoType");
            return (Criteria) this;
        }

        public Criteria andInfoTypeLessThanOrEqualTo(String value) {
            addCriterion("info_type <=", value, "infoType");
            return (Criteria) this;
        }

        public Criteria andInfoTypeLike(String value) {
            addCriterion("info_type like", value, "infoType");
            return (Criteria) this;
        }

        public Criteria andInfoTypeNotLike(String value) {
            addCriterion("info_type not like", value, "infoType");
            return (Criteria) this;
        }

        public Criteria andInfoTypeIn(List<String> values) {
            addCriterion("info_type in", values, "infoType");
            return (Criteria) this;
        }

        public Criteria andInfoTypeNotIn(List<String> values) {
            addCriterion("info_type not in", values, "infoType");
            return (Criteria) this;
        }

        public Criteria andInfoTypeBetween(String value1, String value2) {
            addCriterion("info_type between", value1, value2, "infoType");
            return (Criteria) this;
        }

        public Criteria andInfoTypeNotBetween(String value1, String value2) {
            addCriterion("info_type not between", value1, value2, "infoType");
            return (Criteria) this;
        }

        public Criteria andTaskBusinessInfoIsNull() {
            addCriterion("task_business_info is null");
            return (Criteria) this;
        }

        public Criteria andTaskBusinessInfoIsNotNull() {
            addCriterion("task_business_info is not null");
            return (Criteria) this;
        }

        public Criteria andTaskBusinessInfoEqualTo(String value) {
            addCriterion("task_business_info =", value, "taskBusinessInfo");
            return (Criteria) this;
        }

        public Criteria andTaskBusinessInfoNotEqualTo(String value) {
            addCriterion("task_business_info <>", value, "taskBusinessInfo");
            return (Criteria) this;
        }

        public Criteria andTaskBusinessInfoGreaterThan(String value) {
            addCriterion("task_business_info >", value, "taskBusinessInfo");
            return (Criteria) this;
        }

        public Criteria andTaskBusinessInfoGreaterThanOrEqualTo(String value) {
            addCriterion("task_business_info >=", value, "taskBusinessInfo");
            return (Criteria) this;
        }

        public Criteria andTaskBusinessInfoLessThan(String value) {
            addCriterion("task_business_info <", value, "taskBusinessInfo");
            return (Criteria) this;
        }

        public Criteria andTaskBusinessInfoLessThanOrEqualTo(String value) {
            addCriterion("task_business_info <=", value, "taskBusinessInfo");
            return (Criteria) this;
        }

        public Criteria andTaskBusinessInfoLike(String value) {
            addCriterion("task_business_info like", value, "taskBusinessInfo");
            return (Criteria) this;
        }

        public Criteria andTaskBusinessInfoNotLike(String value) {
            addCriterion("task_business_info not like", value, "taskBusinessInfo");
            return (Criteria) this;
        }

        public Criteria andTaskBusinessInfoIn(List<String> values) {
            addCriterion("task_business_info in", values, "taskBusinessInfo");
            return (Criteria) this;
        }

        public Criteria andTaskBusinessInfoNotIn(List<String> values) {
            addCriterion("task_business_info not in", values, "taskBusinessInfo");
            return (Criteria) this;
        }

        public Criteria andTaskBusinessInfoBetween(String value1, String value2) {
            addCriterion("task_business_info between", value1, value2, "taskBusinessInfo");
            return (Criteria) this;
        }

        public Criteria andTaskBusinessInfoNotBetween(String value1, String value2) {
            addCriterion("task_business_info not between", value1, value2, "taskBusinessInfo");
            return (Criteria) this;
        }

        public Criteria andExtendsInfoIsNull() {
            addCriterion("extends_info is null");
            return (Criteria) this;
        }

        public Criteria andExtendsInfoIsNotNull() {
            addCriterion("extends_info is not null");
            return (Criteria) this;
        }

        public Criteria andExtendsInfoEqualTo(String value) {
            addCriterion("extends_info =", value, "extendsInfo");
            return (Criteria) this;
        }

        public Criteria andExtendsInfoNotEqualTo(String value) {
            addCriterion("extends_info <>", value, "extendsInfo");
            return (Criteria) this;
        }

        public Criteria andExtendsInfoGreaterThan(String value) {
            addCriterion("extends_info >", value, "extendsInfo");
            return (Criteria) this;
        }

        public Criteria andExtendsInfoGreaterThanOrEqualTo(String value) {
            addCriterion("extends_info >=", value, "extendsInfo");
            return (Criteria) this;
        }

        public Criteria andExtendsInfoLessThan(String value) {
            addCriterion("extends_info <", value, "extendsInfo");
            return (Criteria) this;
        }

        public Criteria andExtendsInfoLessThanOrEqualTo(String value) {
            addCriterion("extends_info <=", value, "extendsInfo");
            return (Criteria) this;
        }

        public Criteria andExtendsInfoLike(String value) {
            addCriterion("extends_info like", value, "extendsInfo");
            return (Criteria) this;
        }

        public Criteria andExtendsInfoNotLike(String value) {
            addCriterion("extends_info not like", value, "extendsInfo");
            return (Criteria) this;
        }

        public Criteria andExtendsInfoIn(List<String> values) {
            addCriterion("extends_info in", values, "extendsInfo");
            return (Criteria) this;
        }

        public Criteria andExtendsInfoNotIn(List<String> values) {
            addCriterion("extends_info not in", values, "extendsInfo");
            return (Criteria) this;
        }

        public Criteria andExtendsInfoBetween(String value1, String value2) {
            addCriterion("extends_info between", value1, value2, "extendsInfo");
            return (Criteria) this;
        }

        public Criteria andExtendsInfoNotBetween(String value1, String value2) {
            addCriterion("extends_info not between", value1, value2, "extendsInfo");
            return (Criteria) this;
        }

        public Criteria andAuditStatusIsNull() {
            addCriterion("audit_status is null");
            return (Criteria) this;
        }

        public Criteria andAuditStatusIsNotNull() {
            addCriterion("audit_status is not null");
            return (Criteria) this;
        }

        public Criteria andAuditStatusEqualTo(String value) {
            addCriterion("audit_status =", value, "auditStatus");
            return (Criteria) this;
        }

        public Criteria andAuditStatusNotEqualTo(String value) {
            addCriterion("audit_status <>", value, "auditStatus");
            return (Criteria) this;
        }

        public Criteria andAuditStatusGreaterThan(String value) {
            addCriterion("audit_status >", value, "auditStatus");
            return (Criteria) this;
        }

        public Criteria andAuditStatusGreaterThanOrEqualTo(String value) {
            addCriterion("audit_status >=", value, "auditStatus");
            return (Criteria) this;
        }

        public Criteria andAuditStatusLessThan(String value) {
            addCriterion("audit_status <", value, "auditStatus");
            return (Criteria) this;
        }

        public Criteria andAuditStatusLessThanOrEqualTo(String value) {
            addCriterion("audit_status <=", value, "auditStatus");
            return (Criteria) this;
        }

        public Criteria andAuditStatusLike(String value) {
            addCriterion("audit_status like", value, "auditStatus");
            return (Criteria) this;
        }

        public Criteria andAuditStatusNotLike(String value) {
            addCriterion("audit_status not like", value, "auditStatus");
            return (Criteria) this;
        }

        public Criteria andAuditStatusIn(List<String> values) {
            addCriterion("audit_status in", values, "auditStatus");
            return (Criteria) this;
        }

        public Criteria andAuditStatusNotIn(List<String> values) {
            addCriterion("audit_status not in", values, "auditStatus");
            return (Criteria) this;
        }

        public Criteria andAuditStatusBetween(String value1, String value2) {
            addCriterion("audit_status between", value1, value2, "auditStatus");
            return (Criteria) this;
        }

        public Criteria andAuditStatusNotBetween(String value1, String value2) {
            addCriterion("audit_status not between", value1, value2, "auditStatus");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table partner_level_task_business_data
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
     * This class corresponds to the database table partner_level_task_business_data
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