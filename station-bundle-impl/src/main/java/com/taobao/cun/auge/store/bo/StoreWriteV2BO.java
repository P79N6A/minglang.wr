package com.taobao.cun.auge.store.bo;

import com.taobao.cun.auge.store.dto.StoreCreateDto;
import com.taobao.cun.auge.store.dto.StoreGroupInfoDto;

import java.util.List;

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


}
