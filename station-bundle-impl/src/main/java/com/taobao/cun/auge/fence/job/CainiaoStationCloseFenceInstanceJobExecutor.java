package com.taobao.cun.auge.fence.job;

import com.taobao.cun.auge.dal.domain.FenceEntity;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.fence.bo.FenceEntityBO;
import com.taobao.cun.auge.fence.dto.job.CainiaoStationCloseFenceInstanceJob;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.service.CaiNiaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 菜鸟站点（县仓、菜鸟站点）停业，要关闭相关围栏
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class CainiaoStationCloseFenceInstanceJobExecutor extends AbstractFenceInstanceJobExecutor<CainiaoStationCloseFenceInstanceJob> {
	protected Logger logger = LoggerFactory.getLogger(CainiaoStationCloseFenceInstanceJobExecutor.class);
	@Resource
	private StationBO stationBO;
	@Resource
	private FenceEntityBO fenceEntityBO;
	@Resource
	protected CaiNiaoService caiNiaoService;

	@Override
	protected int doExecute(CainiaoStationCloseFenceInstanceJob fenceInstanceJob) {
		final AtomicInteger counter = new AtomicInteger();
		Flux.create(this::queryStations)
				.parallel(2)
				.filter(this::isCainiaoStationClosed)
				.flatMap(s->queryFenceEntities(s, fenceInstanceJob.getFenceTypes()))
				.subscribe(f->{
					deleteFenceEntity(f);
					counter.incrementAndGet();
				});
		return counter.get();
	}

	private void queryStations(FluxSink<Station> emitter){
		stationBO.getServicingStations().forEach(s->emitter.next(s));
		emitter.complete();
	}

	/**
	 * 找出菜鸟站点关闭或者县已经关闭的站点
	 * @param station
	 * @return
	 */
	private boolean isCainiaoStationClosed(Station station){
		try {
			return !(caiNiaoService.checkCainiaoStationIsOperating(station.getId()) && caiNiaoService.checkCainiaoCountyIsOperating(station.getId()));
		} catch (Exception e) {
			return false;
		}
	}

	private Flux<FenceEntity> queryFenceEntities(Station station, List<String> fenceTypes){
		return Flux.fromIterable(fenceEntityBO.getStationFenceEntitiesByFenceType(station.getId(), fenceTypes));
	}
}
