package com.taobao.cun.auge.store.bo;

import com.taobao.cun.auge.store.dto.StoreCreateDto;
import com.taobao.cun.auge.store.dto.StoreGroupInfoDto;

import java.util.List;
import java.util.Map;

/**
 * 创建门店  新接口
 */
public interface StoreWriteV2BO {


    /**
     * 创建店铺 新接口  目前体验店使用
     * @param stationId
     * @return 返回店铺ID
     */
    public Long createByStationId(Long stationId);


    /**
     * 创建补货村点门店  新接口  优品站和电器店使用
     * @param stationId
     * @return
     **/
    public Long createSupplyStoreByStationId(Long stationId);

    /**
     * 更新服务站信息
     * @param instanceId
     * @return
     */
    public void modifyStationInfoForStore(Long instanceId);

    /**
     * 停用门店
     */
    public void closeStore(Long stationId);

    /**
     * 上传门店 效果图片
     * @param shareStoreId
     */
    public void uploadStoreImage(Long shareStoreId);

    /**
     * 上传门店主图
     * @param shareStoreId
     */
    public void uploadStoreMainImage(Long shareStoreId);

    /**
     * 上传门店其他图片
     * @param shareStoreId
     */
    public void uploadStoreSubImage(Long shareStoreId);

    /**
     *  创建门店库
     * @param title
     * @param comment
     * @return
     */

    public StoreGroupInfoDto createStoreGroup(String title, String comment);

    /**
     *  绑定门店库
     * @param groupId
     * @param shareStoreIds
     */

    public Boolean  bindStoreGroup(Long groupId,List<Long> shareStoreIds);

    /**
     * 解绑门店库
     * @param groupId
     * @param shareStoreIds
     */
    public Boolean  unBindStoreGroup(Long groupId,List<Long> shareStoreIds);

    /**
     * 门店信息补全
     * 更新的信息如下：门店信息创建时的变更点：
     *门店主图全部使用默认的图片，图片链接由运营提供
     *门店营业时间调整为：早上10点到晚上19点。
     *门店的联系电话：取村小二的电话
     *门店的导购员旺旺默认为村小二的账号，且职称为店长
     * 增加NEED_OPERATE_PHYSICAL_STORE(210000) 标
     * @param stationId
     * @return
     */
    public Boolean syncAddStoreInfo(List<Long> stationId);

    /**
     * 初始化门店小程序
     * @param storeId
     * @return
     */
    public Map<String, Object> initSingleMiniapp(Long storeId);
}
