package com.taobao.cun.auge.fence.bo.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.taobao.cun.auge.common.utils.BeanCopyUtils;
import com.taobao.cun.auge.dal.domain.FenceInstanceJobEntity;
import com.taobao.cun.auge.dal.domain.FenceInstanceJobEntityExample;
import com.taobao.cun.auge.dal.mapper.FenceInstanceJobEntityMapper;
import com.taobao.cun.auge.fence.bo.FenceInstanceJobBo;
import com.taobao.cun.auge.fence.dto.FenceInstanceJobUpdateDto;
import com.taobao.cun.auge.fence.dto.job.FenceInstanceJob;
import com.taobao.cun.auge.validator.BeanValidator;

/**
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class FenceInstanceJobBoImpl implements FenceInstanceJobBo {
	@Resource
	private FenceInstanceJobEntityMapper fenceInstanceJobEntityMapper;
	
	@Override
	public void insertJob(FenceInstanceJob fenceInstanceJob) {
		BeanValidator.validateWithThrowable(fenceInstanceJob);
		
		FenceInstanceJobEntity entity = new FenceInstanceJobEntity();
		entity.setGmtCreate(new Date());
		entity.setGmtModified(new Date());
		entity.setJobArg(JSON.toJSONString(fenceInstanceJob));
		entity.setJobType(fenceInstanceJob.getClass().getSimpleName());
		entity.setInstanceNum(0);
		entity.setState("NEW");
		entity.setCreator(fenceInstanceJob.getCreator());
		fenceInstanceJobEntityMapper.insert(entity);
	}

	@Override
	public List<FenceInstanceJob> getNewFenceInstanceJobs() {
		FenceInstanceJobEntityExample example = new FenceInstanceJobEntityExample();
		example.createCriteria().andStateEqualTo("NEW");
		List<FenceInstanceJobEntity> entities = fenceInstanceJobEntityMapper.selectByExampleWithBLOBs(example);
		
		List<FenceInstanceJob> fenceInstanceJobs = Lists.newArrayList();
		if(!CollectionUtils.isEmpty(entities)) {
			for(FenceInstanceJobEntity entity : entities) {
				FenceInstanceJob fenceInstanceJob = createFenceInstanceJob(entity);
				fenceInstanceJob.setId(entity.getId());
				fenceInstanceJob.setJobType(entity.getJobType());
				fenceInstanceJobs.add(fenceInstanceJob);
			}
		}
		return fenceInstanceJobs;
	}

	@SuppressWarnings("unchecked")
	private FenceInstanceJob createFenceInstanceJob(FenceInstanceJobEntity entity) {
		String className = FenceInstanceJob.class.getPackage().getName() + "." + entity.getJobType();
		Class<FenceInstanceJob> clazz;
		try {
			clazz = (Class<FenceInstanceJob>) ClassUtils.forName(className, getClass().getClassLoader());
		} catch (Exception e) {
			throw Throwables.propagate(e);
		}
		return JSON.parseObject(entity.getJobArg(), clazz);
	}

	@Override
	public void updateJob(FenceInstanceJobUpdateDto fenceInstanceJobUpdateDto) {
		FenceInstanceJobEntity entity = fenceInstanceJobEntityMapper.selectByPrimaryKey(fenceInstanceJobUpdateDto.getId());
		if(entity != null) {
			BeanCopyUtils.copyNotNullProperties(fenceInstanceJobUpdateDto, entity);
			entity.setGmtModified(new Date());
			fenceInstanceJobEntityMapper.updateByPrimaryKeyWithBLOBs(entity);
		}
	}
}
