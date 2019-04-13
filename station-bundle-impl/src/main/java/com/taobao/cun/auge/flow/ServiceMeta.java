package com.taobao.cun.auge.flow;

import java.util.Objects;

/**
 * 服务元数据
 * 
 * @author chengyu.zhoucy
 *
 */
public class ServiceMeta {
	private String taskCode;
	
	private String serviceInterface;
	
	private String serviceMethod;
	
	private String serviceVersion;
	
	private Integer timeout;

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	public String getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}

	public String getServiceInterface() {
		return serviceInterface;
	}

	public void setServiceInterface(String serviceInterface) {
		this.serviceInterface = serviceInterface;
	}

	public String getServiceMethod() {
		return serviceMethod;
	}

	public void setServiceMethod(String serviceMethod) {
		this.serviceMethod = serviceMethod;
	}

	public String getServiceVersion() {
		return serviceVersion;
	}

	public void setServiceVersion(String serviceVersion) {
		this.serviceVersion = serviceVersion;
	}

	@Override
	public int hashCode() {
		return Objects.hash(serviceInterface, serviceMethod, serviceVersion, taskCode, timeout);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ServiceMeta other = (ServiceMeta) obj;
		return Objects.equals(serviceInterface, other.serviceInterface)
				&& Objects.equals(serviceMethod, other.serviceMethod)
				&& Objects.equals(serviceVersion, other.serviceVersion) && Objects.equals(taskCode, other.taskCode)
				&& Objects.equals(timeout, other.timeout);
	}

	@Override
	public String toString() {
		return "ServiceMeta [taskCode=" + taskCode + ", serviceInterface=" + serviceInterface + ", serviceMethod="
				+ serviceMethod + ", serviceVersion=" + serviceVersion + ", timeout=" + timeout + "]";
	}
}
