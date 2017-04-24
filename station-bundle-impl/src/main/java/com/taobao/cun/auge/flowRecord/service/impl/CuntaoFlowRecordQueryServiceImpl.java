package com.taobao.cun.auge.flowRecord.service.impl;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.PageDtoUtil;
import com.taobao.cun.auge.dal.domain.CuntaoFlowRecord;
import com.taobao.cun.auge.dal.domain.CuntaoFlowRecordExample;
import com.taobao.cun.auge.dal.domain.CuntaoFlowRecordExample.Criteria;
import com.taobao.cun.auge.dal.mapper.CuntaoFlowRecordMapper;
import com.taobao.cun.auge.flowRecord.condition.CuntaoFlowRecordPageCondition;
import com.taobao.cun.auge.flowRecord.convert.CuntaoFlowRecordEventConverter;
import com.taobao.cun.auge.flowRecord.dto.CuntaoFlowRecordDto;
import com.taobao.cun.auge.flowRecord.enums.CuntaoFlowRecordTargetTypeEnum;
import com.taobao.cun.auge.flowRecord.service.CuntaoFlowRecordQueryService;
import com.taobao.cun.auge.validator.BeanValidator;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;


@Service("cuntaoFlowRecordQueryService")
@HSFProvider(serviceInterface = CuntaoFlowRecordQueryService.class)
public class CuntaoFlowRecordQueryServiceImpl implements CuntaoFlowRecordQueryService{
	
	private static final Logger logger = LoggerFactory.getLogger(CuntaoFlowRecordQueryService.class);
	
	@Autowired
	CuntaoFlowRecordMapper cuntaoFlowRecordMapper;
	
	@Override
	public PageDto<CuntaoFlowRecordDto> queryByPage(CuntaoFlowRecordPageCondition pageCondition) {
		try {
			// 参数校验
			BeanValidator.validateWithThrowable(pageCondition);

			CuntaoFlowRecordExample example = new CuntaoFlowRecordExample();
			example.setOrderByClause("id DESC");
			Criteria criteria = example.createCriteria();
			
			criteria.andTargetIdEqualTo(pageCondition.getTargetId());
			criteria.andTargetTypeEqualTo(pageCondition.getTargetType().getCode());
			
			PageHelper.startPage(pageCondition.getPageNum(), pageCondition.getPageSize());
			Page<CuntaoFlowRecord> page = (Page<CuntaoFlowRecord>)cuntaoFlowRecordMapper.selectByExample(example);
			
			PageDto<CuntaoFlowRecordDto> result = PageDtoUtil.success(page,
					page.stream().map(CuntaoFlowRecordEventConverter::toCuntaoFlowRecordDto).collect(Collectors.toList()));
			return result;
		} catch (Exception e) {
			logger.error("Failed to query cuntao flow record.CuntaoFlowRecordQueryCondition = "+JSONObject.toJSONString(pageCondition), e);
			return PageDtoUtil.unSuccess(pageCondition.getPageNum(), pageCondition.getPageSize());
		}
	}

	public void insertRecord(CuntaoFlowRecordDto recordDto) {
		try {
			CuntaoFlowRecord record = new CuntaoFlowRecord();
			DomainUtils.beforeInsert(record, recordDto.getOperatorWorkid());
			record.setOperatorName(recordDto.getOperatorName());
			record.setOperatorWorkid(recordDto.getOperatorWorkid());
			record.setOperateTime(recordDto.getOperateTime());
			record.setNodeTitle(recordDto.getNodeTitle());
			record.setTargetId(recordDto.getTargetId());
			record.setTargetType("santong-dzwl");
			record.setOperateOpinion(recordDto.getOperateOpinion());
			record.setRemarks(recordDto.getRemarks());
			cuntaoFlowRecordMapper.insertSelective(record);
		} catch (Exception e) {
			logger.error("insertRecord error recordDto = "+JSONObject.toJSONString(recordDto), e);
		}
	}
}
