package com.taobao.cun.auge.lifecycle;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.taobao.cun.auge.statemachine.StateMachineEvent;
import com.taobao.cun.auge.station.dto.PhaseInfo;
import com.taobao.cun.auge.station.dto.StepInfo;
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
		 phases.sort(Comparator.comparingInt(p -> StateMachineEvent.valueOfEvent(p.getEvent()).ordinal()));
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

	public static List<PhaseInfo> getRuntimePhaseInfos() {
		return runtimePhaseInfos;
	}

}
