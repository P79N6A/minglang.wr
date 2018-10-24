package com.taobao.cun.auge.station.transfer.process;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.taobao.cun.auge.dal.domain.CountyStation;
import com.taobao.cun.auge.log.BizActionEnum;
import com.taobao.cun.auge.log.BizActionLogDto;
import com.taobao.cun.auge.log.bo.BizActionLogBo;
import com.taobao.cun.auge.station.bo.CountyStationBO;
import com.taobao.cun.auge.station.convert.CountyStationConverter;
import com.taobao.cun.auge.station.dto.CountyStationDto;
import com.taobao.cun.auge.station.transfer.StationTransferBo;
import com.taobao.cun.auge.station.transfer.TransferJobBo;
import com.taobao.cun.auge.station.transfer.dto.CountyStationTransferCondition;
import com.taobao.cun.auge.station.transfer.dto.CountyStationTransferPhase;
import com.taobao.cun.auge.station.transfer.dto.TransferStation;
import com.taobao.cun.auge.station.transfer.state.CountyTransferStateMgrBo;

/**
 * 交接准备流程准备
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class TransferProcessPrepareBo {
	private static final int SERVICING_NUM = 20;
	@Resource
	private CountyTransferStateMgrBo countyTransferStateMgrBo;
	@Resource
	private CountyStationBO countyStationBO;
	@Resource
	private StationTransferBo stationTransferBo;
	@Resource
	private BizActionLogBo bizActionLogBo;
	@Resource
	private TransferJobBo transferJobBo;
	
	private Map<CountyStationTransferPhase, DependStatePrepare> dependStatePrepares = Maps.newHashMap();
	
	public TransferProcessPrepareBo() {
		dependStatePrepares.put(CountyStationTransferPhase.COUNTY_TRANSING, new TransingStatePrepare());
		dependStatePrepares.put(CountyStationTransferPhase.COUNTY_AUTO_TRANSED, new AutoTransedStatePrepare());
		dependStatePrepares.put(CountyStationTransferPhase.COUNTY_NOT_TRANS, new NotTransStatePrepare());
		dependStatePrepares.put(CountyStationTransferPhase.COUNTY_TRANSED, new TransedStatePrepare());
	}
	
	public CountyStationTransferCondition prepare(Long countyStationId) {
		CountyStationTransferPhase countyStationTransferPhase = countyTransferStateMgrBo.getTransferPhase(countyStationId);
		return dependStatePrepares.get(countyStationTransferPhase).prepare(countyStationId);
	}
	
	interface DependStatePrepare{
		CountyStationTransferCondition prepare(Long countyStationId);
	}
	
	/**
	 * 县点正在交接
	 * @author chengyu.zhoucy
	 *
	 */
	class TransingStatePrepare implements DependStatePrepare{
		@Override
		public CountyStationTransferCondition prepare(Long countyStationId) {
			CountyStationTransferCondition countyStationTransferCondition = new CountyStationTransferCondition();
			countyStationTransferCondition.setMemo("县点正在交接中，暂时不可发起新交接");
			return countyStationTransferCondition;
		}
	}
	
	/**
	 * 县点已经完成了N+75的交接
	 * 
	 * @author chengyu.zhoucy
	 *
	 */
	class AutoTransedStatePrepare implements DependStatePrepare{
		@Override
		public CountyStationTransferCondition prepare(Long countyStationId) {
			CountyStationTransferCondition countyStationTransferCondition = new CountyStationTransferCondition();
			countyStationTransferCondition.setMemo("县点已经归属运营部，不可交接");
			return countyStationTransferCondition;
		}
	}
	
	abstract class AbstractStatePrepare implements DependStatePrepare{
		protected CountyStationDto getCountyStationDto(Long countyStationId) {
			CountyStation countyStation = countyStationBO.getCountyStationById(countyStationId);
			return CountyStationConverter.toCountyStationDto(countyStation);
		}
		
		protected List<TransferStation> getTransferableStations(Long countyStationId){
			CountyStation countyStation = countyStationBO.getCountyStationById(countyStationId);
			return stationTransferBo.getTransferableStations(countyStation.getOrgId());
		}
	}
	
	/**
	 * 县点未交接过
	 * @author chengyu.zhoucy
	 *
	 */
	class NotTransStatePrepare extends AbstractStatePrepare{
		@Override
		public CountyStationTransferCondition prepare(Long countyStationId) {
			CountyStationTransferCondition countyStationTransferCondition = new CountyStationTransferCondition();
			CountyStation countyStation = countyStationBO.getCountyStationById(countyStationId);
			int servicingStationNum = stationTransferBo.countServicing(countyStation.getOrgId());
			if(servicingStationNum < SERVICING_NUM) {
				countyStationTransferCondition.setMemo("服务中站点数：" + servicingStationNum + ",不满足：" + SERVICING_NUM + "个的条件");
				return countyStationTransferCondition;
			}
			
			List<TransferStation> transferableStations = getTransferableStations(countyStationId);
			if(transferableStations.isEmpty()) {
				countyStationTransferCondition.setMemo("没有可交接村点");
				return countyStationTransferCondition;
			}
			countyStationTransferCondition.setCountyTransfer(true);
			countyStationTransferCondition.setTransferStations(transferableStations);
			countyStationTransferCondition.setCountyStationDto(getCountyStationDto(countyStationId));
			return countyStationTransferCondition;
		}
	}
	
	/**
	 * 县点已经交接，但还处于N+75之间
	 * @author chengyu.zhoucy
	 *
	 */
	class TransedStatePrepare extends AbstractStatePrepare{
		@Override
		public CountyStationTransferCondition prepare(Long countyStationId) {
			//检查是否发起了提前交接
			if(isAdvanceTransfering(countyStationId)) {
				CountyStationTransferCondition countyStationTransferCondition = new CountyStationTransferCondition();
				countyStationTransferCondition.setMemo("县点处于提前交接期，不能发起新交接流程");
				return countyStationTransferCondition;
			}
			
			return doPrepare(countyStationId);
		}

		private CountyStationTransferCondition doPrepare(Long countyStationId) {
			CountyStationTransferCondition countyStationTransferCondition = new CountyStationTransferCondition();
			//取出县点交接时间
			List<BizActionLogDto> bizActionLogDtos = bizActionLogBo.getBizActionLogDtos(countyStationId, "county", BizActionEnum.countystation_transfer_finished);
			if(CollectionUtils.isNotEmpty(bizActionLogDtos)) {
				BizActionLogDto bizActionLogDto = bizActionLogDtos.get(0);
				countyStationTransferCondition.setCountyTransferDate(bizActionLogDto.getGmtCreate());
			}
			countyStationTransferCondition.setAdvanceTransfer(true);
			countyStationTransferCondition.setCountyTransfer(false);
			countyStationTransferCondition.setCountyStationDto(getCountyStationDto(countyStationId));
			//获取可交接的村点
			List<TransferStation> transferableStations = getTransferableStations(countyStationId);
			if(!transferableStations.isEmpty()) {
				countyStationTransferCondition.setStationTransfer(true);
				countyStationTransferCondition.setTransferStations(transferableStations);
			}
			
			return countyStationTransferCondition;
		}

		private boolean isAdvanceTransfering(Long countyStationId) {
			return CollectionUtils.isNotEmpty(transferJobBo.getCountyStationTransferJobs(countyStationId, "advance", "NEW"));
		}
	}
}

