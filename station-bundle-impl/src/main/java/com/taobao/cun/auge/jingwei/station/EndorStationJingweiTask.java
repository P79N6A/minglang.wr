package com.taobao.cun.auge.jingwei.station;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.middleware.jingwei.client.Client;
import com.alibaba.middleware.jingwei.client.ClientFactory;
import com.alibaba.middleware.jingwei.client.custom.DeleteEvent;
import com.alibaba.middleware.jingwei.client.custom.EventMessage;
import com.alibaba.middleware.jingwei.client.custom.InsertEvent;
import com.alibaba.middleware.jingwei.client.custom.SimpleMessageListener;
import com.alibaba.middleware.jingwei.client.custom.UpdateEvent;

import com.taobao.cun.auge.dal.domain.SyncLog;
import com.taobao.cun.auge.log.bo.SyncLogBo;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.endor.dto.OrgDto;
import com.taobao.cun.endor.service.OrgService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

/**
 * Station状态为NEW的时候将他同步到ENDOR，作为一个组织
 * 当状态变化QUIT的时候，从ENDOR删除
 * 
 * @author chengyu.zhoucy
 *
 */
//@Component
public class EndorStationJingweiTask implements InitializingBean{
	@Value("${jingwei.taskid.endor.station}")
	private String taskId;
	
	private Client client;
	@Value("${org.station.rootid}")
	private Long rootId;
	
	@Resource
	private OrgService orgService;
	@Resource
	private SyncLogBo syncLogBo;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		client = ClientFactory.create(taskId);
		client.registerMessageListener(new SimpleMessageListener() {
			@Override
			public Result onReceiveMessage(List<EventMessage> messages) {
				for(EventMessage msg : messages){
					handleMesssage(msg);
				}
				return Result.ACK_AND_NEXT;
			}

			private void handleMesssage(EventMessage msg) {
				SyncLog syncLog = null;
				try{
					if(msg instanceof UpdateEvent){
						UpdateEvent updateEvent = (UpdateEvent) msg;
						List<Map<String, Serializable>> modifiedRows = updateEvent.getModifyRowDataMaps();
						for(int index = 0; index < modifiedRows.size(); index++){
							Map<String, Serializable> modifiedRow = modifiedRows.get(index);
							Map<String, Serializable> rowDataMap = updateEvent.getRowDataMaps().get(index);
							syncLog = syncLogBo.addLog(new JingweiMessage("UPDATE", "station", rowDataMap, modifiedRow).toSyncLog());
							if(modifiedRow.containsKey("is_deleted") && "y".equals(modifiedRow.get("is_deleted"))){
								deleteOrg((Long)(rowDataMap.get("id")));
							}else if(modifiedRow.containsKey("status")){
								String status = (String) modifiedRow.get("status");
								if(StationStatusEnum.QUIT.getCode().equals(status) || StationStatusEnum.INVALID.getCode().equals(status)){
									deleteOrg((Long)(rowDataMap.get("id")));
								}else if(StationStatusEnum.NEW.getCode().equals(status)){
									addOrg(rowDataMap);
								}
							}
						}
					}else if(msg instanceof InsertEvent){
						List<Map<String, Serializable>> rowDataMaps = msg.getRowDataMaps();
						for(Map<String, Serializable> rowDataMap : rowDataMaps){
							syncLog = syncLogBo.addLog(new JingweiMessage("UPDATE", "station", rowDataMap, null).toSyncLog());
							String status = (String) rowDataMap.get("status");
							if(!StationStatusEnum.QUIT.getCode().equals(status) 
									&& !StationStatusEnum.TEMP.getCode().equals(status)
									&& !StationStatusEnum.INVALID.getCode().equals(status)){//无论是怎样插入的（数据订正、创建服务站...）只要不是QUITE状态，那么都同步到ENDOR
								addOrg(rowDataMap);
							}
							
						}
					}else if(msg instanceof DeleteEvent){
						List<Map<String, Serializable>> rowDataMaps = msg.getRowDataMaps();
						for(Map<String, Serializable> rowDataMap : rowDataMaps){
							syncLog = syncLogBo.addLog(new JingweiMessage("UPDATE", "station", rowDataMap, null).toSyncLog());
							deleteOrg((Long)(rowDataMap.get("id")));
						}
					}
					syncLog.setState("success");
					syncLogBo.updateState(syncLog);
				}catch(Exception e){
					syncLog.setState("fail");
					syncLog.setErrorMsg(e.getMessage());
					syncLogBo.updateState(syncLog);
				}
			}

			private void addOrg(Map<String, Serializable> rowDataMap) {
				OrgDto org = new OrgDto();
				org.setBizOrgId((Long) rowDataMap.get("id"));
				org.setBizParentId(rootId);
				org.setName((String) rowDataMap.get("name"));
				org.setCreator((String) rowDataMap.get("creater"));
				org.setModifier((String) rowDataMap.get("modifier"));
				orgService.insert("cuntaostore", org);
			}

			private void deleteOrg(Long bizOrgId) {
				orgService.delete("cuntaostore", bizOrgId);
			}});
		client.startTask();
	}
	
}
