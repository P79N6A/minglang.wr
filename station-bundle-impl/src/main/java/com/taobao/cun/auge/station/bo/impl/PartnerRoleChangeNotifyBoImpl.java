package com.taobao.cun.auge.station.bo.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.taobao.cun.auge.dal.domain.SyncLog;
import com.taobao.cun.auge.event.EventDispatcherUtil;
import com.taobao.cun.auge.log.bo.SyncLogBo;
import com.taobao.cun.auge.station.bo.PartnerRoleChangeNotifyBo;
import com.taobao.cun.auge.station.dto.PartnerDetailDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum.PartnerInstanceType;
import com.taobao.cun.auge.station.service.PartnerService;
import com.taobao.cun.auge.station.service.StationQueryService;
import com.taobao.cun.auge.store.bo.StoreReadBO;
import com.taobao.cun.auge.store.dto.StoreDto;
import com.taobao.cun.crius.event.ExtEvent;

@Component
public class PartnerRoleChangeNotifyBoImpl implements PartnerRoleChangeNotifyBo {
	private static final String EVENT_PARTNER_ROLE_ADD = "PARTNER_ROLE_ADD";
	private static final String EVENT_PARTNER_ROLE_REMOVE = "PARTNER_ROLE_REMOVE";
	@Resource
	private PartnerService partnerService;
	@Resource
	private StationQueryService stationQueryService;
	@Resource
	private SyncLogBo syncLogBo;
	@Resource
	private StoreReadBO storeReadBO;
	
	@Override
	public void sendAddRoleMsg(Long taobaoUserId, PartnerInstanceType partnerInstanceType) {
		PartnerRoleMessage partnerRoleMessage = new PartnerRoleMessage();
		partnerRoleMessage.setRoleDesc(PartnerInstanceTypeEnum.valueof(partnerInstanceType.name()).getDesc());
		partnerRoleMessage.setRoleName(PartnerInstanceTypeEnum.valueof(partnerInstanceType.name()).getCode());
		partnerRoleMessage.setTaobaouserId(String.valueOf(taobaoUserId));
		
		PartnerDetailDto partnerDetailDto = partnerService.getPartnerDetail(taobaoUserId);
		partnerRoleMessage.setUserName(partnerDetailDto.getName());
		partnerRoleMessage.setTaobaoNick(partnerDetailDto.getTaobaoNick());
		partnerRoleMessage.setStationId(partnerDetailDto.getStationId());
		partnerRoleMessage.setStationName(partnerDetailDto.getStationName());
		
		if(partnerInstanceType.equals(PartnerInstanceType.TPS)) {
			StoreDto storeDto = storeReadBO.getStoreDtoByTaobaoUserId(taobaoUserId);
			partnerRoleMessage.setShareStoreId(storeDto.getShareStoreId());
			partnerRoleMessage.setStoreName(storeDto.getName());
			partnerRoleMessage.setStoreCategory(storeDto.getStoreCategory().getCategory());
		}
		
		String content = JSON.toJSONString(partnerRoleMessage);
		try {
			EventDispatcherUtil.dispatch(EVENT_PARTNER_ROLE_ADD, new ExtEvent(content));
		}catch(Exception e) {
			addLog(content, EVENT_PARTNER_ROLE_ADD, "fail", e.getMessage());
			return;
		}
		addLog(content, EVENT_PARTNER_ROLE_ADD, "success", null);
	}
	
	@Override
	public void sendRemoveRoleMsg(Long taobaoUserId, PartnerInstanceType partnerInstanceType) {
		PartnerRoleMessage partnerRoleMessage = new PartnerRoleMessage();
		partnerRoleMessage.setRoleDesc(PartnerInstanceTypeEnum.valueof(partnerInstanceType.name()).getDesc());
		partnerRoleMessage.setRoleName(PartnerInstanceTypeEnum.valueof(partnerInstanceType.name()).getCode());
		partnerRoleMessage.setTaobaouserId(String.valueOf(taobaoUserId));
		
		String content = JSON.toJSONString(partnerRoleMessage);
		try {
			EventDispatcherUtil.dispatch(EVENT_PARTNER_ROLE_REMOVE, new ExtEvent(content));
		}catch(Exception e) {
			addLog(content, EVENT_PARTNER_ROLE_REMOVE, "fail", e.getMessage());
			return;
		}
		addLog(content, EVENT_PARTNER_ROLE_REMOVE, "success", null);
	}
	
	private void addLog(String content, String type, String state, String errorMsg) {
		SyncLog syncLog = new SyncLog();
		syncLog.setContent(content);
		syncLog.setGmtCreate(new Date());
		syncLog.setGmtModified(new Date());
		syncLog.setType(type);
		syncLog.setState(state);
		syncLog.setErrorMsg(errorMsg);
		syncLogBo.addLog(syncLog);
	}

	public static class PartnerRoleMessage{
		private String taobaouserId;
		
		private String userName;
		
		private String taobaoNick;
		
		private String roleName;
		
		private String roleDesc;
		
		private Long stationId;
		
		private String stationName;
		
		private Long shareStoreId;
		
		private String storeName;
		
		private String storeCategory;

		public String getStationName() {
			return stationName;
		}

		public void setStationName(String stationName) {
			this.stationName = stationName;
		}

		public String getStoreCategory() {
			return storeCategory;
		}

		public void setStoreCategory(String storeCategory) {
			this.storeCategory = storeCategory;
		}

		public String getTaobaoNick() {
			return taobaoNick;
		}

		public void setTaobaoNick(String taobaoNick) {
			this.taobaoNick = taobaoNick;
		}

		public String getTaobaouserId() {
			return taobaouserId;
		}

		public void setTaobaouserId(String taobaouserId) {
			this.taobaouserId = taobaouserId;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getRoleName() {
			return roleName;
		}

		public void setRoleName(String roleName) {
			this.roleName = roleName;
		}

		public String getRoleDesc() {
			return roleDesc;
		}

		public void setRoleDesc(String roleDesc) {
			this.roleDesc = roleDesc;
		}

		public Long getStationId() {
			return stationId;
		}

		public void setStationId(Long stationId) {
			this.stationId = stationId;
		}

		public Long getShareStoreId() {
			return shareStoreId;
		}

		public void setShareStoreId(Long shareStoreId) {
			this.shareStoreId = shareStoreId;
		}

		public String getStoreName() {
			return storeName;
		}

		public void setStoreName(String storeName) {
			this.storeName = storeName;
		}
	}
}
