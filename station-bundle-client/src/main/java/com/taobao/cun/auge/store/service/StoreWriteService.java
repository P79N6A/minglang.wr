package com.taobao.cun.auge.store.service;

import com.taobao.cun.auge.store.dto.StoreCategory;
import com.taobao.cun.auge.store.dto.StoreCreateDto;
import com.taobao.cun.auge.store.dto.StoreGroupInfoDto;

import java.util.List;
import java.util.Map;

/**
 * 店铺的写操作
 */
public interface StoreWriteService {
    /**
     * 创建店铺   历史接口  新接口是createByDto
     *
     * @param dto
     * @return 返回店铺ID
     */
    @Deprecated
    Long create(StoreCreateDto dto) throws StoreException;

    /**
     * 测试接口不要调用
     *
     * @param shareStoreId
     * @param category
     * @return
     */
    Boolean updateStoreTag(Long shareStoreId, StoreCategory category);

    /**
     * 创建拍样门店
     *
     * @param stationId
     * @return
     */
    Boolean createSampleStore(Long stationId);

    /**
     * 创建补货村点门店   历史接口  不要再使用 请使用
     * Boolean createSupplyStoreByStationId(Long stationId);
     *
     * @param stationId
     * @return
     */
    @Deprecated
    public Boolean createSupplyStore(Long stationId);

    /**
     * 创建拍样库存
     *
     * @param stationId
     * @return
     */
    public Boolean initSampleWarehouse(Long stationId);

    /**
     * 构建门店库存
     *
     * @param stationId
     * @return
     */
    public Boolean initStoreWarehouse(Long stationId);


    public Boolean batchInitStoreWarehouse(List<Long> stationIds);

    /**
     * 批量初始化补货村点门店
     *
     * @param stationIds
     * @return
     */
    public Boolean batchCreateSupplyStore(List<Long> stationIds);

    /**
     * 淘标国标地址转换
     *
     * @param taobaocode
     * @return
     */
    public Long tb2gbCode(Long taobaocode);


    public Boolean batchUpdateStore(List<Long> sharedStoreIds);

    /**
     * 批量删除菜鸟标
     *
     * @param stationIds
     * @return
     */
    public Boolean batchRemoveCainiaoFeature(List<Long> stationIds);

    /**
     * 同步村点Endor组织
     *
     * @param station
     * @return
     */
    public Boolean initEndorOrg(Long station);

    /**
     * 批量同步门店组织
     */
    public void batchInitStoreEndorOrg();

    public void initStoreEmployees(Long stationId);

    public void batchInitStoreEmployees(List<Long> stationId);

    public void batchInitStoreEndorOrg(List<Long> stationId);

    /**
     * 初始化菜鸟自提标
     *
     * @param stationId
     */
    public void initGoodSupplyFeature(Long stationId);

    public Integer getCountyCode(String countyCode, String countyDetail, String cityCode);


    public void batchInitStoreEmployee();

    public void syncStore(Long stationId);

    public void syncStore();

    /**
     * 去除配送接单能力
     *
     * @return
     */
    public Boolean disableByTabaoUserId(Long taobaoUserId);

    /**
     * 增加服务白名单 送货入户。
     */
    public Boolean addWhiteListForSHRH(Long taobaoUserId);

    /**
     * 增加服务白名单 送货入户。
     */
    public Boolean addWhiteListForSHRHByStationIds(List<Long> stationIds);


    /**
     * 停用门店
     */
    public void closeStore(Long stationId);

    /**
     * 创建店铺 新接口  目前体验店使用
     *
     * @param stationId
     * @return 返回店铺ID
     */
    public Long createByStationId(Long stationId);

    /**
     * 创建补货村点门店  新接口   目前 天猫优品 和电器店使用
     *
     * @param stationId
     * @return
     */
    public Long createSupplyStoreByStationId(Long stationId);

    /**
     * 修改门店信息
     *
     * @param instanceId
     * @throws StoreException
     */
    public void modifyStationInfoForStore(Long instanceId);

    /**
     * 上传门店 效果图片
     *
     * @param shareStoreId
     */
    public void uploadStoreImage(Long shareStoreId);

    /**
     * 上传门店主图
     *
     * @param shareStoreId
     */
    public void uploadStoreMainImage(Long shareStoreId);

    /**
     * 上传门店其他图片
     *
     * @param shareStoreId
     */
    public void uploadStoreSubImage(Long shareStoreId);

    /**
     * 门店数据同步
     *
     * @param stationIds
     * @return
     */
    public Boolean syncAddStoreInfo(List<Long> stationIds);

    /**
     * 创建门店库
     *
     * @param title
     * @param comment
     * @return
     */

    public StoreGroupInfoDto createStoreGroup(String title, String comment);

    /**
     * 绑定门店库
     *
     * @param groupId
     * @param shareStoreIds
     */

    public Boolean bindStoreGroup(Long groupId, List<Long> shareStoreIds);

    /**
     * 解绑门店库
     *
     * @param groupId
     * @param shareStoreIds
     */
    public Boolean unBindStoreGroup(Long groupId, List<Long> shareStoreIds);

    /**
     * 初始化小程序
     *
     * @param storeId
     * @return
     */
    public Map<String, Object> initSingleMiniapp(Long storeId);

    /**
     * 初始化小程序
     *
     * @param storeIds
     * @return
     */
    public void  batchInitSingleMiniapp(List<Long> storeIds);


    /**
     * 根据主键更新淘宝userId  重新入住已有站点使用
     */
    public void  updateTaobaoUserIdById(Long id,Long taobaoUserId);


    /**
     * 初始化轻店
     * @param taobaoUserId
     * @param subImageList
     * @param taskInstanceId
     * @return
     */
    public Boolean initLightStore(Long taobaoUserId,List<String> subImageList,Long taskInstanceId);

    /**
     *
     * @param taobaoUserId
     * @return
     */
    public Boolean checkOpenLightStore(Long taobaoUserId);

}
