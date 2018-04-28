package com.taobao.cun.auge.lifecycle;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.taobao.cun.auge.statemachine.StateMachineEvent;
import org.springframework.core.annotation.AnnotationUtils;


public class RuntimeMetaInfoCollector {

	
	private static List<PhaseInfo> runtimePhaseInfos = Lists.newCopyOnWriteArrayList();
	
	 public static void collectMetaInfo(Class<?> clazz,Method method){
		 PhaseInfo phaseInfo = getPhaseInfo(clazz);
		 if(phaseInfo == null){
			 phaseInfo = createPhaseInfo(clazz);
			 runtimePhaseInfos.add(phaseInfo);
			 phaseInfo.addStepInfo(createStepInfo(method));
		 }else{
			 phaseInfo.addStepInfo(createStepInfo(method));
		 }
	 }

	 private static PhaseInfo getPhaseInfo(Class<?> clazz){
		 return runtimePhaseInfos.stream().filter(phase -> clazz.getSimpleName().equals(phase.getClassName())).findFirst().orElse(null);
	 }
	 
	 public static Map<String,List<PhaseInfo>> runtimeInfo(){
		 Map<String, List<PhaseInfo>> runtimeInfo =
				 runtimePhaseInfos.stream().collect(Collectors.groupingBy(PhaseInfo::getType));
		 runtimeInfo.values().stream().forEach(phases -> sortPhases(phases));
		 return runtimeInfo;
	 }
	 
	 private static void sortPhases(List<PhaseInfo> phases){
		 phases.sort((PhaseInfo p1, PhaseInfo p2) -> p1.order().compareTo(p2.order()));
	 }
	 
	 private static PhaseInfo createPhaseInfo(Class<?> clazz){
		 PhaseInfo  phaseInfo = new PhaseInfo();
		 phaseInfo.setClassName(clazz.getSimpleName());
		 Phase phase = AnnotationUtils.getAnnotation(clazz, Phase.class);
		 if(phase != null){
			 phaseInfo.setDesc(phase.desc());
			 phaseInfo.setType(phase.type());
			 phaseInfo.setEvent(phase.event().getEvent());
		 }
		 return phaseInfo;
	 }
	 

	private static StepInfo createStepInfo(Method method) {
		StepInfo stepInfo = new StepInfo();
		 stepInfo.setMethodName(method.getName());
		 PhaseStepMeta stepMeta = AnnotationUtils.getAnnotation(method, PhaseStepMeta.class);
		 if(stepMeta != null){
			 stepInfo.setDesc(stepMeta.descr());
		 }
		 return stepInfo;
	}
	
	public static class PhaseInfo {
		private String type;

		private String className;

		private String desc;

		private String event;

		private List<StepInfo> stepInfos = Lists.newArrayList();

		public List<StepInfo> getStepInfos() {
			return stepInfos;
		}

		public Integer order(){
			return StateMachineEvent.valueOfEvent(event).ordinal();
		}
		
		public void setStepInfos(List<StepInfo> stepInfos) {
			this.stepInfos = stepInfos;
		}

		public String getType() {
			return type;
		}

		public void addStepInfo(StepInfo stepInfo) {
			this.stepInfos.add(stepInfo);
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getClassName() {
			return className;
		}

		public void setClassName(String className) {
			this.className = className;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public String getEvent() {
			return event;
		}

		public void setEvent(String event) {
			this.event = event;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((className == null) ? 0 : className.hashCode());
			result = prime * result + ((desc == null) ? 0 : desc.hashCode());
			result = prime * result + ((event == null) ? 0 : event.hashCode());
			result = prime * result + ((type == null) ? 0 : type.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
                return true;
            }
			if (obj == null) {
                return false;
            }
			if (getClass() != obj.getClass()) {
                return false;
            }
			PhaseInfo other = (PhaseInfo) obj;
			if (className == null) {
				if (other.className != null) {
                    return false;
                }
			} else if (!className.equals(other.className)) {
                return false;
            }
			if (desc == null) {
				if (other.desc != null) {
                    return false;
                }
			} else if (!desc.equals(other.desc)) {
                return false;
            }
			if (event == null) {
				if (other.event != null) {
                    return false;
                }
			} else if (!event.equals(other.event)) {
                return false;
            }
			if (type == null) {
				if (other.type != null) {
                    return false;
                }
			} else if (!type.equals(other.type)) {
                return false;
            }
			return true;
		}

	}

	public static class StepInfo{
		private String methodName;
		
		private String desc;
		
		public String getMethodName() {
			return methodName;
		}

		public void setMethodName(String methodName) {
			this.methodName = methodName;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}
	}

	public static List<PhaseInfo> getRuntimePhaseInfos() {
		return runtimePhaseInfos;
	}
	
	

}
