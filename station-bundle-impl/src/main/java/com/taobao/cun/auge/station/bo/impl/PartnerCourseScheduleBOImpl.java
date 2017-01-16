package com.taobao.cun.auge.station.bo.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.alibaba.ivy.common.AppAuthDTO;
import com.alibaba.ivy.common.PageDTO;
import com.alibaba.ivy.common.ResultDTO;
import com.alibaba.ivy.service.course.CourseServiceFacade;
import com.alibaba.ivy.service.course.dto.CourseDTO;
import com.alibaba.ivy.service.course.query.CourseQueryDTO;
import com.alibaba.ivy.service.user.TrainingRecordServiceFacade;
import com.alibaba.ivy.service.user.dto.TrainingRecordDTO;
import com.alibaba.ivy.service.user.query.TrainingRecordQueryDTO;
import com.google.common.collect.Lists;
import com.taobao.cun.auge.common.exception.AugeServiceException;
import com.taobao.cun.auge.dal.domain.PartnerCourseSchedule;
import com.taobao.cun.auge.dal.domain.PartnerCourseScheduleExample;
import com.taobao.cun.auge.dal.domain.PartnerCourseScheduleReflect;
import com.taobao.cun.auge.dal.domain.PartnerCourseScheduleReflectExample;
import com.taobao.cun.auge.dal.domain.PartnerCourseScheduleReflectExample.Criteria;
import com.taobao.cun.auge.dal.mapper.PartnerCourseScheduleMapper;
import com.taobao.cun.auge.dal.mapper.PartnerCourseScheduleReflectMapper;
import com.taobao.cun.auge.station.bo.AppResourceBO;
import com.taobao.cun.auge.station.bo.PartnerCourseScheduleBO;
import com.taobao.cun.auge.station.dto.PartnerCourseScheduleDetailDto;
import com.taobao.cun.auge.station.dto.PartnerCourseScheduleReflectDto;
import com.taobao.cun.auge.station.dto.PartnerCourseScheduleShowDto;
import com.taobao.cun.auge.station.enums.PartnerScheduleStatusEnum;
@Component("partnerCourseScheduleBO")
public class PartnerCourseScheduleBOImpl implements PartnerCourseScheduleBO{

	private static final Logger logger = LoggerFactory.getLogger(PartnerCourseScheduleBO.class);

	@Autowired
	PartnerCourseScheduleMapper partnerCourseScheduleMapper;
	
	@Autowired
	PartnerCourseScheduleReflectMapper partnerCourseScheduleReflectMapper;
	
	@Autowired
	CourseServiceFacade courseServiceFacade;
	
	@Value("${partner.peixun.client.code}")
	private String peixunClientCode;
	
	@Value("${partner.peixun.client.key}")
	private String peixunClientKey;
	
	@Autowired
	AppResourceBO appResourceBO;
	
	@Autowired
	TrainingRecordServiceFacade trainingRecordServiceFacade;
	
	@Override
	public void reflectCourseSchedule(PartnerCourseScheduleReflectDto dto) {
		Assert.notNull(dto.getTaobaoUserId());
		Assert.notNull(dto.getCourseCode());
		Assert.notNull(dto.getScheduleId());
		//判断是否已经反馈过
		PartnerCourseScheduleReflectExample example = new PartnerCourseScheduleReflectExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andTaobaoUserIdEqualTo(dto.getTaobaoUserId());
		criteria.andScheduleIdEqualTo(dto.getScheduleId());
		criteria.andCourseCodeEqualTo(dto.getCourseCode());
		List<PartnerCourseScheduleReflect> records=partnerCourseScheduleReflectMapper.selectByExample(example);
		if(records.size()>0){
			throw new AugeServiceException("已经反馈过，请不要重复反馈");
		}
		PartnerCourseScheduleReflect pcsr=new PartnerCourseScheduleReflect();
		BeanUtils.copyProperties(dto, pcsr);
		pcsr.setIsDeleted("n");
		pcsr.setCreator(String.valueOf(dto.getTaobaoUserId()));
		pcsr.setGmtCreate(new Date());
		pcsr.setModifier(String.valueOf(dto.getTaobaoUserId()));
		pcsr.setGmtModified(new Date());
		partnerCourseScheduleReflectMapper.insert(pcsr);
	}

	@Override
	public List<PartnerCourseScheduleShowDto> getCourseSchedule(
			Long taobaoUserId, Date gmtStart, Date gmtEnd) {
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("taobaoUserId", taobaoUserId);
		param.put("gmtStart", gmtStart);
		param.put("gmtEnd", gmtEnd);
		List<PartnerCourseSchedule> list=partnerCourseScheduleMapper.selectByCondition(param);
		if(list==null||list.size()==0){
			return new ArrayList<PartnerCourseScheduleShowDto>();
		}
		List<String> courseCodes=new ArrayList<String>();
		List<Long> scheduleIds=new ArrayList<Long>();
		for(PartnerCourseSchedule sc:list){
			courseCodes.add(sc.getCourseCode());
			scheduleIds.add(sc.getId());
		}
		//批量获取课程信息
		 List<CourseDTO> courses=getCourseDetail(courseCodes);
		//批量获取培训记录
		 List<TrainingRecordDTO> trainRecords=getTrainingRecords(taobaoUserId,courseCodes);
		//批量获取反馈记录
		List<PartnerCourseScheduleReflect> reflects =getReflectList(taobaoUserId,scheduleIds);
        //封装返回结果
		return fillResult(list,taobaoUserId,courses,trainRecords,reflects);
	}

	private List<CourseDTO> getCourseDetail(List<String> courseCodes){
		CourseQueryDTO queryDto=new CourseQueryDTO();
		queryDto.setCodes(courseCodes);
		try {
			ResultDTO<PageDTO<CourseDTO>> coursesResult=courseServiceFacade.find(getAuth(), queryDto, 100, 1);
			if (coursesResult.isSuccess()) {
				return coursesResult.getData().getRows()==null?Lists.newArrayList():coursesResult.getData().getRows();
			} else {
				throw new RuntimeException("query record error,"
						+ coursesResult.getMsg());
			}
		} catch (Exception e) {
			//不影响正常展现
			logger.error("queryPeixunCourseList error", e);
			return new ArrayList<CourseDTO>();
		}
	}
	
	private List<TrainingRecordDTO> getTrainingRecords(Long taobaoUserId,List<String> courseCodes){
		TrainingRecordQueryDTO query = new TrainingRecordQueryDTO();
		query.setCourseCodes(courseCodes);
		query.addTrainee(String.valueOf(taobaoUserId));
		try {
			ResultDTO<PageDTO<TrainingRecordDTO>> result = trainingRecordServiceFacade
					.find(getAuth(), query, 1000, 1);
			if (result.isSuccess()) {
				return result.getData().getRows()==null?Lists.newArrayList():result.getData().getRows();
			} else {
				throw new RuntimeException("query record error,"
						+ result.getMsg());
			}
		} catch (Exception e) {
			//不影响正常展现
			logger.error("queryPeixunRecordList error", e);
			return new ArrayList<TrainingRecordDTO>();
		}
	}
	
	private List<PartnerCourseScheduleReflect> getReflectList(Long taobaoUserId,List<Long> scheduleIds){
		PartnerCourseScheduleReflectExample example = new PartnerCourseScheduleReflectExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andTaobaoUserIdEqualTo(taobaoUserId);
		criteria.andScheduleIdIn(scheduleIds);
		List<PartnerCourseScheduleReflect> records=partnerCourseScheduleReflectMapper.selectByExample(example);
		return records;
	}
	
	private List<PartnerCourseScheduleShowDto> fillResult(List<PartnerCourseSchedule> list,Long taobaoUserId,List<CourseDTO> courses,List<TrainingRecordDTO> trainRecords,List<PartnerCourseScheduleReflect> reflects){
		List<PartnerCourseScheduleShowDto> result=new ArrayList<PartnerCourseScheduleShowDto>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
		String onlineCourseUrl=appResourceBO.queryAppValueNotAllowNull("PARTNER_PEIXUN", "ONLINE_COURSE_URL");

		for(PartnerCourseSchedule schedule:list){
			String date=dateFormat.format(schedule.getGmtCourse());
			PartnerCourseScheduleShowDto dto=getFromResult(date,result);
			if(dto==null){
				dto=new PartnerCourseScheduleShowDto();
				dto.setCourseDateDesc(date);
				dto.setWeekendDesc(dateFm.format(schedule.getGmtCourse()));
				dto.setCourses(new ArrayList<PartnerCourseScheduleDetailDto>());
				result.add(dto);
			}
			PartnerCourseScheduleDetailDto courseDetail=new PartnerCourseScheduleDetailDto();
			courseDetail.setCourseType(schedule.getCourseType());
			courseDetail.setCourseCode(schedule.getCourseCode());
			courseDetail.setGmtBegin(schedule.getGmtStart());
			courseDetail.setGmtEnd(schedule.getGmtEnd());
			courseDetail.setGmtCourse(schedule.getGmtCourse());
			courseDetail.setSeqNum(schedule.getSeqNum());
			courseDetail.setId(schedule.getId());
			CourseDTO  course=getCourseDetail(schedule.getCourseCode(),courses);
			if(course!=null){
				courseDetail.setCourseName(course.getName());
				courseDetail.setCourseDesc(course.getSummary());
				courseDetail.setCourseUrl(onlineCourseUrl+course.getCode());
			}else{
				continue;
			}
			setStatus(courseDetail,trainRecords,reflects);
			dto.getCourses().add(courseDetail);
		}
		
		return result;
		
	}
	
	private void setStatus(PartnerCourseScheduleDetailDto dto,List<TrainingRecordDTO> trainRecords,List<PartnerCourseScheduleReflect> reflects){
		Date now=new Date();
		//进行中
		if(now.after(dto.getGmtBegin())&&now.before(dto.getGmtEnd())){
			dto.setStatus(PartnerScheduleStatusEnum.LIVING.getCode());
		}else if(now.before(dto.getGmtBegin())){
			dto.setStatus(PartnerScheduleStatusEnum.WAIT_ATTEMP.getCode());
		}else if(hasAttemp(trainRecords,dto)){
			if(hasReflect(reflects,dto)){
				dto.setStatus(PartnerScheduleStatusEnum.HAS_REFLECT.getCode());
			}else{
				dto.setStatus(PartnerScheduleStatusEnum.NOT_REFLECT.getCode());
			}
		}else{
			dto.setStatus(PartnerScheduleStatusEnum.NOT_ATTEMP.getCode());
		}
	}
	
	private Boolean hasAttemp(List<TrainingRecordDTO> trainRecords,PartnerCourseScheduleDetailDto dto){
		for(TrainingRecordDTO train:trainRecords){
			if(train.getCourseCode().equals(dto.getCourseCode())){
				return true;
			}
		}
		return false;
	}
	private Boolean hasReflect(List<PartnerCourseScheduleReflect> reflects,PartnerCourseScheduleDetailDto dto){
		for(PartnerCourseScheduleReflect reflect:reflects){
			if(reflect.getScheduleId().compareTo(dto.getId())==0){
				return true;
			}
		}
		return false;
	}
	
	private CourseDTO getCourseDetail(String courseCode,List<CourseDTO> courses){
		for(CourseDTO course:courses){
			if(course.getCode().equals(courseCode)){
				return course;
			}
		}
		return null;
	}
	private PartnerCourseScheduleShowDto getFromResult(String date,List<PartnerCourseScheduleShowDto> result){
		for(PartnerCourseScheduleShowDto dto:result){
			if(dto.getCourseDateDesc().equals(date)){
				return dto;
			}
		}
		return null;
	}
	
	private AppAuthDTO getAuth(){
		AppAuthDTO auth = new AppAuthDTO();
		auth.setAuthkey(peixunClientKey);
		auth.setCode(peixunClientCode);
		return auth;
	}

	@Override
	public List<PartnerCourseSchedule> getScheduleByCourseCode(String courseCode) {
		// TODO Auto-generated method stub
		PartnerCourseScheduleExample example = new PartnerCourseScheduleExample();
		com.taobao.cun.auge.dal.domain.PartnerCourseScheduleExample.Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andCourseCodeEqualTo(courseCode);
		List<PartnerCourseSchedule> records=partnerCourseScheduleMapper.selectByExample(example);
		return records;
	}
}
