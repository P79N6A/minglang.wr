package com.taobao.cun.auge.dal.domain;

import java.util.Date;
import javax.persistence.*;

public class Station {
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
    private String creater;

    /**
     * �޸���
     */
    private String modifier;

    /**
     * ����վ����
     */
    private String name;

    /**
     * ����վ���
     */
    private String description;

    /**
     * ֧�����˺�
     */
    @Column(name = "alipay_account")
    private String alipayAccount;

    /**
     * ���Ż�Աnick
     */
    @Column(name = "taobao_nick")
    private String taobaoNick;

    /**
     * �����״̬
     */
    private String state;

    /**
     * �Ƿ�ɾ��
     */
    @Column(name = "is_deleted")
    private String isDeleted;

    /**
     * ʡ����
     */
    private String province;

    /**
     * �б���
     */
    private String city;

    /**
     * ��/������
     */
    private String county;

    /**
     * �������
     */
    private String town;

    /**
     * ʡ��ϸ
     */
    @Column(name = "province_detail")
    private String provinceDetail;

    /**
     * ����ϸ
     */
    @Column(name = "city_detail")
    private String cityDetail;

    /**
     * ��/����ϸ
     */
    @Column(name = "county_detail")
    private String countyDetail;

    /**
     * ������ϸ
     */
    @Column(name = "town_detail")
    private String townDetail;

    /**
     * ��ϸ��ַ
     */
    private String address;

    /**
     * ������֯
     */
    @Column(name = "apply_org")
    private Long applyOrg;

    /**
     * �Ա��û�id
     */
    @Column(name = "taobao_user_id")
    private Long taobaoUserId;

    /**
     * �����
     */
    @Column(name = "station_num")
    private String stationNum;

    /**
     * ����
     */
    private String lng;

    /**
     * γ��
     */
    private String lat;

    /**
     * ��/�������
     */
    private String village;

    /**
     * ��/��������
����
     */
    @Column(name = "village_detail")
    private String villageDetail;

    /**
     * �����˿�
     */
    private String covered;

    /**
     * ��ɫũ����Ʒ
     */
    private String products;

    /**
     * ����״̬
     */
    @Column(name = "logistics_state")
    private String logisticsState;

    /**
     * Ŀǰҵ̬
     */
    private String format;

    /**
     * �̵㣬���߲��̵�
     */
    @Column(name = "area_type")
    private String areaType;

    /**
     * ����Աuser_id
     */
    @Column(name = "manager_id")
    private String managerId;

    /**
     * ������id
     */
    @Column(name = "provider_id")
    private Long providerId;

    /**
     * �������ԣ�������չ����վ����
     */
    private String feature;

    /**
     * �µķ���վ״̬
     */
    private String status;

    /**
     * ���ع̵����� GOV_FIXED �����̵�
TRIPARTITE_FIXED
�����̵�
     */
    @Column(name = "fixed_type")
    private String fixedType;

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
     * @return creater - ������
     */
    public String getCreater() {
        return creater;
    }

    /**
     * ���ô�����
     *
     * @param creater ������
     */
    public void setCreater(String creater) {
        this.creater = creater;
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
     * ��ȡ����վ����
     *
     * @return name - ����վ����
     */
    public String getName() {
        return name;
    }

    /**
     * ���÷���վ����
     *
     * @param name ����վ����
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * ��ȡ����վ���
     *
     * @return description - ����վ���
     */
    public String getDescription() {
        return description;
    }

    /**
     * ���÷���վ���
     *
     * @param description ����վ���
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * ��ȡ֧�����˺�
     *
     * @return alipay_account - ֧�����˺�
     */
    public String getAlipayAccount() {
        return alipayAccount;
    }

    /**
     * ����֧�����˺�
     *
     * @param alipayAccount ֧�����˺�
     */
    public void setAlipayAccount(String alipayAccount) {
        this.alipayAccount = alipayAccount;
    }

    /**
     * ��ȡ���Ż�Աnick
     *
     * @return taobao_nick - ���Ż�Աnick
     */
    public String getTaobaoNick() {
        return taobaoNick;
    }

    /**
     * ���ü��Ż�Աnick
     *
     * @param taobaoNick ���Ż�Աnick
     */
    public void setTaobaoNick(String taobaoNick) {
        this.taobaoNick = taobaoNick;
    }

    /**
     * ��ȡ�����״̬
     *
     * @return state - �����״̬
     */
    public String getState() {
        return state;
    }

    /**
     * ���÷����״̬
     *
     * @param state �����״̬
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * ��ȡ�Ƿ�ɾ��
     *
     * @return is_deleted - �Ƿ�ɾ��
     */
    public String getIsDeleted() {
        return isDeleted;
    }

    /**
     * �����Ƿ�ɾ��
     *
     * @param isDeleted �Ƿ�ɾ��
     */
    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * ��ȡʡ����
     *
     * @return province - ʡ����
     */
    public String getProvince() {
        return province;
    }

    /**
     * ����ʡ����
     *
     * @param province ʡ����
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * ��ȡ�б���
     *
     * @return city - �б���
     */
    public String getCity() {
        return city;
    }

    /**
     * �����б���
     *
     * @param city �б���
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * ��ȡ��/������
     *
     * @return county - ��/������
     */
    public String getCounty() {
        return county;
    }

    /**
     * ������/������
     *
     * @param county ��/������
     */
    public void setCounty(String county) {
        this.county = county;
    }

    /**
     * ��ȡ�������
     *
     * @return town - �������
     */
    public String getTown() {
        return town;
    }

    /**
     * �����������
     *
     * @param town �������
     */
    public void setTown(String town) {
        this.town = town;
    }

    /**
     * ��ȡʡ��ϸ
     *
     * @return province_detail - ʡ��ϸ
     */
    public String getProvinceDetail() {
        return provinceDetail;
    }

    /**
     * ����ʡ��ϸ
     *
     * @param provinceDetail ʡ��ϸ
     */
    public void setProvinceDetail(String provinceDetail) {
        this.provinceDetail = provinceDetail;
    }

    /**
     * ��ȡ����ϸ
     *
     * @return city_detail - ����ϸ
     */
    public String getCityDetail() {
        return cityDetail;
    }

    /**
     * ��������ϸ
     *
     * @param cityDetail ����ϸ
     */
    public void setCityDetail(String cityDetail) {
        this.cityDetail = cityDetail;
    }

    /**
     * ��ȡ��/����ϸ
     *
     * @return county_detail - ��/����ϸ
     */
    public String getCountyDetail() {
        return countyDetail;
    }

    /**
     * ������/����ϸ
     *
     * @param countyDetail ��/����ϸ
     */
    public void setCountyDetail(String countyDetail) {
        this.countyDetail = countyDetail;
    }

    /**
     * ��ȡ������ϸ
     *
     * @return town_detail - ������ϸ
     */
    public String getTownDetail() {
        return townDetail;
    }

    /**
     * ����������ϸ
     *
     * @param townDetail ������ϸ
     */
    public void setTownDetail(String townDetail) {
        this.townDetail = townDetail;
    }

    /**
     * ��ȡ��ϸ��ַ
     *
     * @return address - ��ϸ��ַ
     */
    public String getAddress() {
        return address;
    }

    /**
     * ������ϸ��ַ
     *
     * @param address ��ϸ��ַ
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * ��ȡ������֯
     *
     * @return apply_org - ������֯
     */
    public Long getApplyOrg() {
        return applyOrg;
    }

    /**
     * ����������֯
     *
     * @param applyOrg ������֯
     */
    public void setApplyOrg(Long applyOrg) {
        this.applyOrg = applyOrg;
    }

    /**
     * ��ȡ�Ա��û�id
     *
     * @return taobao_user_id - �Ա��û�id
     */
    public Long getTaobaoUserId() {
        return taobaoUserId;
    }

    /**
     * �����Ա��û�id
     *
     * @param taobaoUserId �Ա��û�id
     */
    public void setTaobaoUserId(Long taobaoUserId) {
        this.taobaoUserId = taobaoUserId;
    }

    /**
     * ��ȡ�����
     *
     * @return station_num - �����
     */
    public String getStationNum() {
        return stationNum;
    }

    /**
     * ���ô����
     *
     * @param stationNum �����
     */
    public void setStationNum(String stationNum) {
        this.stationNum = stationNum;
    }

    /**
     * ��ȡ����
     *
     * @return lng - ����
     */
    public String getLng() {
        return lng;
    }

    /**
     * ���þ���
     *
     * @param lng ����
     */
    public void setLng(String lng) {
        this.lng = lng;
    }

    /**
     * ��ȡγ��
     *
     * @return lat - γ��
     */
    public String getLat() {
        return lat;
    }

    /**
     * ����γ��
     *
     * @param lat γ��
     */
    public void setLat(String lat) {
        this.lat = lat;
    }

    /**
     * ��ȡ��/�������
     *
     * @return village - ��/�������
     */
    public String getVillage() {
        return village;
    }

    /**
     * ���ô�/�������
     *
     * @param village ��/�������
     */
    public void setVillage(String village) {
        this.village = village;
    }

    /**
     * ��ȡ��/��������
����
     *
     * @return village_detail - ��/��������
����
     */
    public String getVillageDetail() {
        return villageDetail;
    }

    /**
     * ���ô�/��������
����
     *
     * @param villageDetail ��/��������
����
     */
    public void setVillageDetail(String villageDetail) {
        this.villageDetail = villageDetail;
    }

    /**
     * ��ȡ�����˿�
     *
     * @return covered - �����˿�
     */
    public String getCovered() {
        return covered;
    }

    /**
     * ���ø����˿�
     *
     * @param covered �����˿�
     */
    public void setCovered(String covered) {
        this.covered = covered;
    }

    /**
     * ��ȡ��ɫũ����Ʒ
     *
     * @return products - ��ɫũ����Ʒ
     */
    public String getProducts() {
        return products;
    }

    /**
     * ������ɫũ����Ʒ
     *
     * @param products ��ɫũ����Ʒ
     */
    public void setProducts(String products) {
        this.products = products;
    }

    /**
     * ��ȡ����״̬
     *
     * @return logistics_state - ����״̬
     */
    public String getLogisticsState() {
        return logisticsState;
    }

    /**
     * ��������״̬
     *
     * @param logisticsState ����״̬
     */
    public void setLogisticsState(String logisticsState) {
        this.logisticsState = logisticsState;
    }

    /**
     * ��ȡĿǰҵ̬
     *
     * @return format - Ŀǰҵ̬
     */
    public String getFormat() {
        return format;
    }

    /**
     * ����Ŀǰҵ̬
     *
     * @param format Ŀǰҵ̬
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * ��ȡ�̵㣬���߲��̵�
     *
     * @return area_type - �̵㣬���߲��̵�
     */
    public String getAreaType() {
        return areaType;
    }

    /**
     * ���ù̵㣬���߲��̵�
     *
     * @param areaType �̵㣬���߲��̵�
     */
    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    /**
     * ��ȡ����Աuser_id
     *
     * @return manager_id - ����Աuser_id
     */
    public String getManagerId() {
        return managerId;
    }

    /**
     * ���ù���Աuser_id
     *
     * @param managerId ����Աuser_id
     */
    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    /**
     * ��ȡ������id
     *
     * @return provider_id - ������id
     */
    public Long getProviderId() {
        return providerId;
    }

    /**
     * ���÷�����id
     *
     * @param providerId ������id
     */
    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    /**
     * ��ȡ�������ԣ�������չ����վ����
     *
     * @return feature - �������ԣ�������չ����վ����
     */
    public String getFeature() {
        return feature;
    }

    /**
     * �����������ԣ�������չ����վ����
     *
     * @param feature �������ԣ�������չ����վ����
     */
    public void setFeature(String feature) {
        this.feature = feature;
    }

    /**
     * ��ȡ�µķ���վ״̬
     *
     * @return status - �µķ���վ״̬
     */
    public String getStatus() {
        return status;
    }

    /**
     * �����µķ���վ״̬
     *
     * @param status �µķ���վ״̬
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * ��ȡ���ع̵����� GOV_FIXED �����̵�
TRIPARTITE_FIXED
�����̵�
     *
     * @return fixed_type - ���ع̵����� GOV_FIXED �����̵�
TRIPARTITE_FIXED
�����̵�
     */
    public String getFixedType() {
        return fixedType;
    }

    /**
     * ���ó��ع̵����� GOV_FIXED �����̵�
TRIPARTITE_FIXED
�����̵�
     *
     * @param fixedType ���ع̵����� GOV_FIXED �����̵�
TRIPARTITE_FIXED
�����̵�
     */
    public void setFixedType(String fixedType) {
        this.fixedType = fixedType;
    }
}