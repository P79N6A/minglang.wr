package com.taobao.cun.auge.dal.domain;

import java.util.Date;
import javax.persistence.*;

@Table(name = "partner_station_rel")
public class PartnerStationRel {
    /**
     * ����
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * ����ʱ��
     */
    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * �޸�ʱ��
     */
    @Column(name = "gmt_modified")
    private Date gmtModified;

    /**
     * ������
     */
    private String creator;

    /**
     * �޸���
     */
    private String modifier;

    /**
     * �Ƿ���ɾ��
     */
    @Column(name = "is_deleted")
    private String isDeleted;

    /**
     * �����վid
     */
    @Column(name = "station_id")
    private Long stationId;

    /**
     * ����ʱ��
     */
    @Column(name = "apply_time")
    private Date applyTime;

    /**
     * ����ʼʱ��
     */
    @Column(name = "service_begin_time")
    private Date serviceBeginTime;

    /**
     * �������ʱ��
     */
    @Column(name = "service_end_time")
    private Date serviceEndTime;

    /**
     * �ϻ���id
     */
    @Column(name = "partner_id")
    private Long partnerId;

    /**
     * �԰��������ϻ��˵Ĵ����վid
     */
    @Column(name = "parent_station_id")
    private Long parentStationId;

    /**
     * ״̬
     */
    private String state;

    /**
     * ������
     */
    @Column(name = "applier_id")
    private String applierId;

    /**
     * λ������������ʾ�԰����Ƿ����ɺϻ��˽�������
     */
    private Integer bit;

    /**
     * �ϻ���or���ĵ�
     */
    private String type;

    /**
     * ��ҵʱ��
     */
    @Column(name = "open_date")
    private Date openDate;

    /**
     * �Ƿ��ǵ�ǰ��
     */
    @Column(name = "is_current")
    private String isCurrent;

    /**
     * ���������ͣ�buc������havana
     */
    @Column(name = "applier_type")
    private String applierType;

    /**
     * ͣҵ���ͣ��ϻ��������˳�������С����������
     */
    @Column(name = "close_type")
    private String closeType;

    /**
     * station_aply��������������Ǩ��ʹ��
     */
    @Column(name = "station_apply_id")
    private Long stationApplyId;

    /**
     * ��ȡ����
     *
     * @return id - ����
     */
    public Long getId() {
        return id;
    }

    /**
     * ��������
     *
     * @param id ����
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * ��ȡ����ʱ��
     *
     * @return gmt_create - ����ʱ��
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * ���ô���ʱ��
     *
     * @param gmtCreate ����ʱ��
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * ��ȡ�޸�ʱ��
     *
     * @return gmt_modified - �޸�ʱ��
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * �����޸�ʱ��
     *
     * @param gmtModified �޸�ʱ��
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    /**
     * ��ȡ������
     *
     * @return creator - ������
     */
    public String getCreator() {
        return creator;
    }

    /**
     * ���ô�����
     *
     * @param creator ������
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * ��ȡ�޸���
     *
     * @return modifier - �޸���
     */
    public String getModifier() {
        return modifier;
    }

    /**
     * �����޸���
     *
     * @param modifier �޸���
     */
    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    /**
     * ��ȡ�Ƿ���ɾ��
     *
     * @return is_deleted - �Ƿ���ɾ��
     */
    public String getIsDeleted() {
        return isDeleted;
    }

    /**
     * �����Ƿ���ɾ��
     *
     * @param isDeleted �Ƿ���ɾ��
     */
    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * ��ȡ�����վid
     *
     * @return station_id - �����վid
     */
    public Long getStationId() {
        return stationId;
    }

    /**
     * ���ô����վid
     *
     * @param stationId �����վid
     */
    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    /**
     * ��ȡ����ʱ��
     *
     * @return apply_time - ����ʱ��
     */
    public Date getApplyTime() {
        return applyTime;
    }

    /**
     * ��������ʱ��
     *
     * @param applyTime ����ʱ��
     */
    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    /**
     * ��ȡ����ʼʱ��
     *
     * @return service_begin_time - ����ʼʱ��
     */
    public Date getServiceBeginTime() {
        return serviceBeginTime;
    }

    /**
     * ���÷���ʼʱ��
     *
     * @param serviceBeginTime ����ʼʱ��
     */
    public void setServiceBeginTime(Date serviceBeginTime) {
        this.serviceBeginTime = serviceBeginTime;
    }

    /**
     * ��ȡ�������ʱ��
     *
     * @return service_end_time - �������ʱ��
     */
    public Date getServiceEndTime() {
        return serviceEndTime;
    }

    /**
     * ���÷������ʱ��
     *
     * @param serviceEndTime �������ʱ��
     */
    public void setServiceEndTime(Date serviceEndTime) {
        this.serviceEndTime = serviceEndTime;
    }

    /**
     * ��ȡ�ϻ���id
     *
     * @return partner_id - �ϻ���id
     */
    public Long getPartnerId() {
        return partnerId;
    }

    /**
     * ���úϻ���id
     *
     * @param partnerId �ϻ���id
     */
    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    /**
     * ��ȡ�԰��������ϻ��˵Ĵ����վid
     *
     * @return parent_station_id - �԰��������ϻ��˵Ĵ����վid
     */
    public Long getParentStationId() {
        return parentStationId;
    }

    /**
     * �����԰��������ϻ��˵Ĵ����վid
     *
     * @param parentStationId �԰��������ϻ��˵Ĵ����վid
     */
    public void setParentStationId(Long parentStationId) {
        this.parentStationId = parentStationId;
    }

    /**
     * ��ȡ״̬
     *
     * @return state - ״̬
     */
    public String getState() {
        return state;
    }

    /**
     * ����״̬
     *
     * @param state ״̬
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * ��ȡ������
     *
     * @return applier_id - ������
     */
    public String getApplierId() {
        return applierId;
    }

    /**
     * ����������
     *
     * @param applierId ������
     */
    public void setApplierId(String applierId) {
        this.applierId = applierId;
    }

    /**
     * ��ȡλ������������ʾ�԰����Ƿ����ɺϻ��˽�������
     *
     * @return bit - λ������������ʾ�԰����Ƿ����ɺϻ��˽�������
     */
    public Integer getBit() {
        return bit;
    }

    /**
     * ����λ������������ʾ�԰����Ƿ����ɺϻ��˽�������
     *
     * @param bit λ������������ʾ�԰����Ƿ����ɺϻ��˽�������
     */
    public void setBit(Integer bit) {
        this.bit = bit;
    }

    /**
     * ��ȡ�ϻ���or���ĵ�
     *
     * @return type - �ϻ���or���ĵ�
     */
    public String getType() {
        return type;
    }

    /**
     * ���úϻ���or���ĵ�
     *
     * @param type �ϻ���or���ĵ�
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * ��ȡ��ҵʱ��
     *
     * @return open_date - ��ҵʱ��
     */
    public Date getOpenDate() {
        return openDate;
    }

    /**
     * ���ÿ�ҵʱ��
     *
     * @param openDate ��ҵʱ��
     */
    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    /**
     * ��ȡ�Ƿ��ǵ�ǰ��
     *
     * @return is_current - �Ƿ��ǵ�ǰ��
     */
    public String getIsCurrent() {
        return isCurrent;
    }

    /**
     * �����Ƿ��ǵ�ǰ��
     *
     * @param isCurrent �Ƿ��ǵ�ǰ��
     */
    public void setIsCurrent(String isCurrent) {
        this.isCurrent = isCurrent;
    }

    /**
     * ��ȡ���������ͣ�buc������havana
     *
     * @return applier_type - ���������ͣ�buc������havana
     */
    public String getApplierType() {
        return applierType;
    }

    /**
     * �������������ͣ�buc������havana
     *
     * @param applierType ���������ͣ�buc������havana
     */
    public void setApplierType(String applierType) {
        this.applierType = applierType;
    }

    /**
     * ��ȡͣҵ���ͣ��ϻ��������˳�������С����������
     *
     * @return close_type - ͣҵ���ͣ��ϻ��������˳�������С����������
     */
    public String getCloseType() {
        return closeType;
    }

    /**
     * ����ͣҵ���ͣ��ϻ��������˳�������С����������
     *
     * @param closeType ͣҵ���ͣ��ϻ��������˳�������С����������
     */
    public void setCloseType(String closeType) {
        this.closeType = closeType;
    }

    /**
     * ��ȡstation_aply��������������Ǩ��ʹ��
     *
     * @return station_apply_id - station_aply��������������Ǩ��ʹ��
     */
    public Long getStationApplyId() {
        return stationApplyId;
    }

    /**
     * ����station_aply��������������Ǩ��ʹ��
     *
     * @param stationApplyId station_aply��������������Ǩ��ʹ��
     */
    public void setStationApplyId(Long stationApplyId) {
        this.stationApplyId = stationApplyId;
    }
}