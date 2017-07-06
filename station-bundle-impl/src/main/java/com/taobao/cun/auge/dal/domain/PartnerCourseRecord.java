package com.taobao.cun.auge.dal.domain;

import java.util.Date;

public class PartnerCourseRecord {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column partner_course_record.id
     *
     * @mbggenerated Tue Jul 12 20:13:50 CST 2016
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column partner_course_record.gmt_create
     *
     * @mbggenerated Tue Jul 12 20:13:50 CST 2016
     */
    private Date gmtCreate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column partner_course_record.gmt_modified
     *
     * @mbggenerated Tue Jul 12 20:13:50 CST 2016
     */
    private Date gmtModified;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column partner_course_record.creator
     *
     * @mbggenerated Tue Jul 12 20:13:50 CST 2016
     */
    private String creator;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column partner_course_record.modifier
     *
     * @mbggenerated Tue Jul 12 20:13:50 CST 2016
     */
    private String modifier;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column partner_course_record.is_deleted
     *
     * @mbggenerated Tue Jul 12 20:13:50 CST 2016
     */
    private String isDeleted;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column partner_course_record.course_type
     *
     * @mbggenerated Tue Jul 12 20:13:50 CST 2016
     */
    private String courseType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column partner_course_record.course_code
     *
     * @mbggenerated Tue Jul 12 20:13:50 CST 2016
     */
    private String courseCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column partner_course_record.status
     *
     * @mbggenerated Tue Jul 12 20:13:50 CST 2016
     */
    private String status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column partner_course_record.order_num
     *
     * @mbggenerated Tue Jul 12 20:13:50 CST 2016
     */
    private String orderNum;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column partner_course_record.gmt_done
     *
     * @mbggenerated Tue Jul 12 20:13:50 CST 2016
     */
    private Date gmtDone;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column partner_course_record.partner_user_id
     *
     * @mbggenerated Tue Jul 12 20:13:50 CST 2016
     */
    private Long partnerUserId;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column partner_course_record.id
     *
     * @return the value of partner_course_record.id
     *
     * @mbggenerated Tue Jul 12 20:13:50 CST 2016
     */
    
    private String refundNo;
    private String refundReason;
    private String refundStatus;
    
    
    
    public String getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}

	public String getRefundNo() {
		return refundNo;
	}

	public void setRefundNo(String refundNo) {
		this.refundNo = refundNo;
	}

	public String getRefundReason() {
		return refundReason;
	}

	public void setRefundReason(String refundReason) {
		this.refundReason = refundReason;
	}

	public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column partner_course_record.id
     *
     * @param id the value for partner_course_record.id
     *
     * @mbggenerated Tue Jul 12 20:13:50 CST 2016
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column partner_course_record.gmt_create
     *
     * @return the value of partner_course_record.gmt_create
     *
     * @mbggenerated Tue Jul 12 20:13:50 CST 2016
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column partner_course_record.gmt_create
     *
     * @param gmtCreate the value for partner_course_record.gmt_create
     *
     * @mbggenerated Tue Jul 12 20:13:50 CST 2016
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column partner_course_record.gmt_modified
     *
     * @return the value of partner_course_record.gmt_modified
     *
     * @mbggenerated Tue Jul 12 20:13:50 CST 2016
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column partner_course_record.gmt_modified
     *
     * @param gmtModified the value for partner_course_record.gmt_modified
     *
     * @mbggenerated Tue Jul 12 20:13:50 CST 2016
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column partner_course_record.creator
     *
     * @return the value of partner_course_record.creator
     *
     * @mbggenerated Tue Jul 12 20:13:50 CST 2016
     */
    public String getCreator() {
        return creator;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column partner_course_record.creator
     *
     * @param creator the value for partner_course_record.creator
     *
     * @mbggenerated Tue Jul 12 20:13:50 CST 2016
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column partner_course_record.modifier
     *
     * @return the value of partner_course_record.modifier
     *
     * @mbggenerated Tue Jul 12 20:13:50 CST 2016
     */
    public String getModifier() {
        return modifier;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column partner_course_record.modifier
     *
     * @param modifier the value for partner_course_record.modifier
     *
     * @mbggenerated Tue Jul 12 20:13:50 CST 2016
     */
    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column partner_course_record.is_deleted
     *
     * @return the value of partner_course_record.is_deleted
     *
     * @mbggenerated Tue Jul 12 20:13:50 CST 2016
     */
    public String getIsDeleted() {
        return isDeleted;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column partner_course_record.is_deleted
     *
     * @param isDeleted the value for partner_course_record.is_deleted
     *
     * @mbggenerated Tue Jul 12 20:13:50 CST 2016
     */
    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column partner_course_record.course_type
     *
     * @return the value of partner_course_record.course_type
     *
     * @mbggenerated Tue Jul 12 20:13:50 CST 2016
     */
    public String getCourseType() {
        return courseType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column partner_course_record.course_type
     *
     * @param courseType the value for partner_course_record.course_type
     *
     * @mbggenerated Tue Jul 12 20:13:50 CST 2016
     */
    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column partner_course_record.course_code
     *
     * @return the value of partner_course_record.course_code
     *
     * @mbggenerated Tue Jul 12 20:13:50 CST 2016
     */
    public String getCourseCode() {
        return courseCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column partner_course_record.course_code
     *
     * @param courseCode the value for partner_course_record.course_code
     *
     * @mbggenerated Tue Jul 12 20:13:50 CST 2016
     */
    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column partner_course_record.status
     *
     * @return the value of partner_course_record.status
     *
     * @mbggenerated Tue Jul 12 20:13:50 CST 2016
     */
    public String getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column partner_course_record.status
     *
     * @param status the value for partner_course_record.status
     *
     * @mbggenerated Tue Jul 12 20:13:50 CST 2016
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column partner_course_record.order_num
     *
     * @return the value of partner_course_record.order_num
     *
     * @mbggenerated Tue Jul 12 20:13:50 CST 2016
     */
    public String getOrderNum() {
        return orderNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column partner_course_record.order_num
     *
     * @param orderNum the value for partner_course_record.order_num
     *
     * @mbggenerated Tue Jul 12 20:13:50 CST 2016
     */
    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column partner_course_record.gmt_done
     *
     * @return the value of partner_course_record.gmt_done
     *
     * @mbggenerated Tue Jul 12 20:13:50 CST 2016
     */
    public Date getGmtDone() {
        return gmtDone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column partner_course_record.gmt_done
     *
     * @param gmtDone the value for partner_course_record.gmt_done
     *
     * @mbggenerated Tue Jul 12 20:13:50 CST 2016
     */
    public void setGmtDone(Date gmtDone) {
        this.gmtDone = gmtDone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column partner_course_record.partner_user_id
     *
     * @return the value of partner_course_record.partner_user_id
     *
     * @mbggenerated Tue Jul 12 20:13:50 CST 2016
     */
    public Long getPartnerUserId() {
        return partnerUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column partner_course_record.partner_user_id
     *
     * @param partnerUserId the value for partner_course_record.partner_user_id
     *
     * @mbggenerated Tue Jul 12 20:13:50 CST 2016
     */
    public void setPartnerUserId(Long partnerUserId) {
        this.partnerUserId = partnerUserId;
    }
}