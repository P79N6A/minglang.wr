package com.taobao.cun.auge.station.bo;

import java.util.Date;
import java.util.List;

import com.taobao.cun.auge.alilang.UserProfile;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceTransDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceIsCurrentEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTransStatusEnum;

public interface PartnerInstanceBO {

    /**
     * @param stationId
     * @return
     */
    public Long findPartnerInstanceIdByStationId(Long stationId);

    /**
     * 根据stationId查询stationAPPLYid
     *
     * @param stationId
     * @return
     */
    public PartnerStationRel findPartnerInstanceByStationId(Long stationId);

    /**
     * @param partnerId
     * @param isCurrent
     * @return
     */
    public List<PartnerStationRel> getPartnerStationRelByPartnerId(Long partnerId, String isCurrent);

    /**
     * @param stationId
     * @param isCurrent
     * @return
     */
    public List<PartnerStationRel> getPartnerStationRelByStationId(Long stationId, String isCurrent);



    /**
     * 根据stationapplyId查询实例
     *
     * @param stationApplyId
     * @return
     */
    public PartnerStationRel getPartnerStationRelByStationApplyId(Long stationApplyId);

    /**
     * 根据taobaoUserId 查询合伙人实例表主键id
     *
     * @param taobaoUserId
     * @return
     */
    public Long getInstanceIdByTaobaoUserId(Long taobaoUserId, PartnerInstanceStateEnum instanceState);

    /**
     * 根据taobaoUserId 查询合伙人实例表
     *
     * @param taobaoUserId
     * @return
     */
    public PartnerStationRel getPartnerInstanceByTaobaoUserId(Long taobaoUserId, PartnerInstanceStateEnum
        instanceState)
    ;

    /**
     * 根据实例id查询村点id
     *
     * @param instanceId
     * @return
     */
    public Long findStationIdByInstanceId(Long instanceId);

    /**
     * 根据合伙人实例id，查询实例
     *
     * @param instanceId
     * @return
     */
    public PartnerStationRel findPartnerInstanceById(Long instanceId);

    /**
     * 查询子合伙人数量
     *
     * @param instanceId 父合伙人实例id
     * @param state      子合伙人状态
     * @return
     */
    public int findChildPartners(Long instanceId, PartnerInstanceStateEnum state);

    /**
     * 查询子合伙人
     *
     * @param instanceId
     * @param stateEnums
     * @return
     */
    public List<PartnerStationRel> findChildPartners(Long instanceId, List<PartnerInstanceStateEnum> stateEnums);

    /**
     * 查询一个村点上，所有的实例
     *
     * @param stationId
     * @return
     * @
     */
    public List<PartnerStationRel> findPartnerInstances(Long stationId);

    /**
     * 查询村点上，最后一个结束服务的实例
     *
     * @param stationId
     * @return
     * @
     */
    public PartnerStationRel findLastClosePartnerInstance(Long stationId);

    /**
     * 状态流转
     *
     * @param instanceId 合伙人实例id
     * @param preState   前置状态
     * @param postState  后置状态
     */
    public void changeState(Long instanceId, PartnerInstanceStateEnum preState, PartnerInstanceStateEnum postState,
                            String operator)
    ;

    /**
     * 更新实例
     */
    public void updatePartnerStationRel(PartnerInstanceDto partnerInstanceDto);

    /**
     * 获得当前实例业务数据，包含（partner,station,partnerLifecycleItems）
     *
     * @param instanceId
     * @return
     * @
     */
    public PartnerInstanceDto getPartnerInstanceById(Long instanceId);

    /**
     * 设置开业时间
     *
     * @param instanceId
     * @param openDate
     * @param operator
     * @
     */
    public void updateOpenDate(Long instanceId, Date openDate, String operator);

    /**
     * 新增实例关系
     *
     * @param partnerInstanceDto
     * @return
     * @
     */
    public Long addPartnerStationRel(PartnerInstanceDto partnerInstanceDto);

    /**
     * 检查taobaouserid 是否可以入驻
     *
     * @param taobaoUserId
     * @return
     * @
     */
    public boolean checkSettleQualification(Long taobaoUserId);

    /**
     * 删除关系表数据
     *
     * @param instanceId
     */
    public void deletePartnerStationRel(Long instanceId, String operator);

    /**
     * 获得待开业数据
     *
     * @param fetchNum
     * @return
     */
    public List<Long> getWaitOpenStationList(int fetchNum);

    /**
     * 获得待解冻保证金数据
     *
     * @param fetchNum
     * @return
     * @
     */
    public List<Long> getWaitThawMoneyList(int fetchNum);

    /**
     * 获得状态为活跃[settling,decorating,servicing,closing,closed,quitting(推出待解冻除外)]
     * 的合伙人实例
     *
     * @param taobaoUserId
     * @return
     */
    public PartnerStationRel getActivePartnerInstance(Long taobaoUserId);

    /**
     * 获得有效的的淘帮手
     *
     * @param parentStationId
     * @return
     */
    public int getActiveTpaByParentStationId(Long parentStationId);

    public void finishCourse(Long taobaoUserId);

    /**
     * 是否所有合伙人都处于退出待解冻、已退出状态
     *
     * @param stationId
     * @
     */
    public boolean isAllPartnerQuit(Long stationId);

    /**
     * 同一个站点，除instanceId的合伙人，其他合伙人是否都处于退出待解冻、已退出状态
     *
     * @param instanceId
     * @return
     * @
     */
    public boolean isOtherPartnerQuit(Long instanceId);

    /**
     * 获得当前实例业务数据，包含（partner,station,partnerLifecycleItems）
     *
     * @param partnerId
     * @return
     * @
     */
    public PartnerInstanceDto getCurrentPartnerInstanceByPartnerId(Long partnerId);

    /**
     * 获得当前人的历史对应关系
     */
    public List<PartnerInstanceDto> getHistoryPartnerInstanceByPartnerId(Long partnerId);

    /**
     * 获得当前服务站的历史对应关系
     */
    public List<PartnerInstanceDto> getHistoryPartnerInstanceByStationId(Long stationId);

    /**
     * 停业中，重新进入服务中
     *
     * @param instanceId
     * @param preState
     * @param postState
     * @param operator
     * @
     */
    public void reService(Long instanceId, PartnerInstanceStateEnum preState, PartnerInstanceStateEnum postState,
                          String operator);

    /**
     * @param taobaoUserId
     * @return
     * @
     */
    public PartnerStationRel getCurrentPartnerInstanceByTaobaoUserId(Long taobaoUserId);

    public void updateIsCurrentByInstanceId(Long instanceId, PartnerInstanceIsCurrentEnum isCurrentEnum);

    /**
     * 查询该账号，上一次进入的村淘的实例，以服务结束时间，最新的为准
     *
     * @param taobaoUserId
     * @return
     */
    public PartnerInstanceDto getLastPartnerInstance(Long taobaoUserId);

    public Long findStationIdByStationApplyId(Long stationApplyId);

    public List<PartnerStationRel> getBatchActivePartnerInstance(
        List<Long> taobaoUserId, List<String> instanceType, List<String> statusList);

    public List<UserProfile> queryUserProfileForAlilangMeeting(Long orgId, String name);

    public Boolean judgeMobileUseble(Long taobaoUserId, Long partnerId, String mobile);

    public Partner getPartnerByStationId(Long stationId);

    public List<PartnerStationRel> queryTpaPartnerInstances(Long parentStationId);


	/**
	 * 根据stationapplyId查询实例id
	 *
	 * @param stationApplyId
	 * @return
	 */
	public Long getInstanceIdByStationApplyId(Long stationApplyId);


	public List<PartnerStationRel> queryTpaPartnerInstances(Long parentStationId,PartnerInstanceStateEnum state);
	/**
	 * 村小二转型升级状态更新
	 */
	public void updateTransStatusByInstanceId(Long instanceId, PartnerInstanceTransStatusEnum transStatus, String operator);
	
}
