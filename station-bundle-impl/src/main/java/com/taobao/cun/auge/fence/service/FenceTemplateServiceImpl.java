package com.taobao.cun.auge.fence.service;

import java.util.List;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.fence.bo.FenceEntityBO;
import com.taobao.cun.auge.fence.bo.FenceTemplateBO;
import com.taobao.cun.auge.fence.convert.JobConvertor;
import com.taobao.cun.auge.fence.dto.FenceBatchOpDto;
import com.taobao.cun.auge.fence.dto.FenceSingleOpDto;
import com.taobao.cun.auge.fence.dto.FenceTemplateEditDto;
import com.taobao.cun.auge.fence.dto.FenceTemplateDto;
import com.taobao.cun.auge.fence.dto.FenceTemplateListDto;
import com.taobao.cun.auge.fence.dto.FenceTemplateQueryCondition;
import com.taobao.cun.auge.fence.dto.FenceTemplateStation;
import com.taobao.cun.auge.fence.dto.job.ConditionCreateFenceInstanceJob;
import com.taobao.cun.auge.fence.dto.job.ConditionDeleteFenceInstanceJob;
import com.taobao.cun.auge.fence.dto.job.StationCreateFenceInstanceJob;
import com.taobao.cun.auge.fence.dto.job.StationDeleteFenceInstanceJob;
import com.taobao.cun.auge.fence.dto.job.TemplateCloseFenceInstanceJob;
import com.taobao.cun.auge.fence.dto.job.TemplateDeleteFenceInstanceJob;
import com.taobao.cun.auge.fence.dto.job.TemplateOpenFenceInstanceJob;
import com.taobao.cun.auge.fence.dto.job.TemplateUpdateFenceInstanceJob;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by xiao on 18/6/15.
 */
@HSFProvider(serviceInterface = FenceTemplateService.class)
public class FenceTemplateServiceImpl implements FenceTemplateService {

    @Autowired
    private FenceTemplateBO fenceTemplateBO;

    @Autowired
    private FenceEntityBO fenceEntityBO;

    @Autowired
    private FenceInstanceJobService jobService;

    @Override
    public Long addFenceTemplate(FenceTemplateEditDto detailDto) {
        return fenceTemplateBO.addFenceTemplate(detailDto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFenceTemplateList(List<Long> idList, String operator) {
        fenceTemplateBO.deleteFenceTemplateList(idList, operator);
        TemplateDeleteFenceInstanceJob job = new TemplateDeleteFenceInstanceJob();
        job.setTemplateIds(idList);
        job.setCreator(operator);
        jobService.createJob(job);
    }

    @Override
    public void updateFenceTemplate(FenceTemplateEditDto detailDto) {
        fenceTemplateBO.updateFenceTemplate(detailDto);
        jobService.createJob((TemplateUpdateFenceInstanceJob)
            JobConvertor.convertToFenceTemplateJob(detailDto.getId(), JobConvertor.TEMPLATE_UPDATE, detailDto.getOperator()));
    }

    @Override
    public PageDto<FenceTemplateListDto> getFenceTemplateList(FenceTemplateQueryCondition condition) {
        return fenceTemplateBO.getFenceTemplateList(condition);
    }

    @Override
    public FenceTemplateDto getTemplateDetail(Long id) {
        return fenceTemplateBO.getFenceTemplateById(id);
    }

    @Override
    public PageDto<FenceTemplateStation> getFenceTemplateStationList(FenceTemplateQueryCondition condition) {
        return fenceTemplateBO.getFenceTemplateStationList(condition);
    }

    @Override
    public void enableFenceTemplate(Long id, String operator) {
        fenceTemplateBO.enableFenceTemplate(id, operator);
        jobService.createJob((TemplateOpenFenceInstanceJob)
            JobConvertor.convertToFenceTemplateJob(id, JobConvertor.TEMPLATE_OPEN, operator));
    }

    @Override
    public void disableFenceTemplate(Long id, String operator) {
        fenceTemplateBO.disableFenceTemplate(id, operator);
        jobService.createJob((TemplateCloseFenceInstanceJob)
            JobConvertor.convertToFenceTemplateJob(id, JobConvertor.TEMPLATE_CLOSE, operator));
    }

    @Override
    public List<FenceTemplateDto> getFenceTemplateListByStationId(Long stationId) {
        List<Long> templateIdList = fenceEntityBO.getTemplateIdListByStationId(stationId);
        return fenceTemplateBO.getFenceTemplateListByIdList(templateIdList);
    }

    @Override
    public void singleOpStation(FenceSingleOpDto opDto) {
        if (FenceBatchOpDto.BATCH_DELETE.equals(opDto.getOpType())) {
            jobService.createJob((StationDeleteFenceInstanceJob)
                JobConvertor.convertToFenceStationJob(opDto.getTemplateIdList(), opDto.getStationId(), opDto.getOpType(), opDto.getOperator()));
        } else {
            jobService.createJob((StationCreateFenceInstanceJob)
                JobConvertor.convertToFenceStationJob(opDto.getTemplateIdList(), opDto.getStationId(), opDto.getOpType(), opDto.getOperator()));
        }
    }

    @Override
    public void batchOpStation(FenceBatchOpDto opDto) {
        if (FenceBatchOpDto.BATCH_DELETE.equals(opDto.getOpType())) {
            jobService.createJob((ConditionDeleteFenceInstanceJob)
                JobConvertor.convertToConditionFenceInstanceJob(opDto.getTemplateIdList(), opDto.getCondition(), opDto.getOpType(), opDto.getOperator()));
        } else {
            jobService.createJob((ConditionCreateFenceInstanceJob)
                JobConvertor.convertToConditionFenceInstanceJob(opDto.getTemplateIdList(), opDto.getCondition(), opDto.getOpType(), opDto.getOperator()));
        }
    }

}
