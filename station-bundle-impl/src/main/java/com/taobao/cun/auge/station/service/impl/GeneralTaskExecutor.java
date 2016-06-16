package com.taobao.cun.auge.station.service.impl;

import java.lang.reflect.Method;
import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.taobao.cun.chronus.dto.TaskExecuteDto;
import com.taobao.cun.chronus.enums.ExecuteStateEnum;
import com.taobao.cun.chronus.service.TaskExecutor;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service("taskExecutor")
@HSFProvider(serviceInterface = TaskExecutor.class, clientTimeout = 12000)
public class GeneralTaskExecutor implements TaskExecutor, ApplicationContextAware {

	private ApplicationContext applicationContext;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public TaskExecuteDto execute(TaskExecuteDto taskExecute) throws Exception {
		Object targetObject = this.applicationContext.getBean(taskExecute.getBeanName());
		String parameterType = taskExecute.getParameterType();
		Class parameterClass = null;
		parameterClass = Class.forName(parameterType);
		Method method = targetObject.getClass().getMethod(taskExecute.getMethodName(), parameterClass);
		Object arguments = JSON.parseObject(taskExecute.getParameter(), parameterClass);

		try {
			method.invoke(targetObject, arguments);
		} catch (Exception e) {
			throw new java.lang.RuntimeException(e.getCause().getMessage(), e.getCause());
		}

		taskExecute.setExecuteEndTime(new Date());
		taskExecute.setExecuteState(ExecuteStateEnum.EXECUTE_OK.getValue());
		return taskExecute;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
