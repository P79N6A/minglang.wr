package com.taobao.cun.auge.county.bo.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.common.lang.StringUtil;
import com.taobao.biz.common.division.ChinaDivisionManager;
import com.taobao.biz.common.division.DivisionVO;
import com.taobao.cun.auge.cache.TairCache;
import com.taobao.cun.auge.common.Address;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.county.bo.CountyBO;
import com.taobao.cun.auge.county.dto.CountyDto;
import com.taobao.cun.auge.dal.domain.CountyStation;
import com.taobao.cun.auge.dal.domain.CountyStationExample;
import com.taobao.cun.auge.dal.domain.CountyStationExample.Criteria;
import com.taobao.cun.auge.dal.domain.CuntaoCainiaoStationRel;
import com.taobao.cun.auge.dal.domain.CuntaoOrg;
import com.taobao.cun.auge.dal.domain.CuntaoOrgAdminAddress;
import com.taobao.cun.auge.dal.domain.CuntaoOrgAdminAddressExample;
import com.taobao.cun.auge.dal.domain.CuntaoOrgExample;
import com.taobao.cun.auge.dal.mapper.CountyStationMapper;
import com.taobao.cun.auge.dal.mapper.CuntaoOrgAdminAddressMapper;
import com.taobao.cun.auge.dal.mapper.CuntaoOrgMapper;
import com.taobao.cun.auge.org.bo.CuntaoOrgBO;
import com.taobao.cun.auge.station.adapter.CaiNiaoAdapter;
import com.taobao.cun.auge.station.bo.AttachementBO;
import com.taobao.cun.auge.station.bo.CountyStationBO;
import com.taobao.cun.auge.station.bo.CuntaoCainiaoStationRelBO;
import com.taobao.cun.auge.station.dto.AttachementDeleteDto;
import com.taobao.cun.auge.station.dto.CaiNiaoStationDto;
import com.taobao.cun.auge.station.dto.CuntaoCainiaoStationRelDto;
import com.taobao.cun.auge.station.enums.AttachementBizTypeEnum;
import com.taobao.cun.auge.station.enums.CountyStationManageModelEnum;
import com.taobao.cun.auge.station.enums.CountyStationManageStatusEnum;
import com.taobao.cun.auge.station.enums.CuntaoCainiaoStationRelTypeEnum;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.common.exception.BusinessException;
import com.taobao.cun.common.exception.ParamException;
import com.taobao.cun.common.util.ListUtils;
import com.taobao.cun.dto.org.enums.CuntaoOrgDeptProEnum;
import com.taobao.cun.dto.org.enums.CuntaoOrgTypeEnum;
import com.taobao.cun.service.mc.MessageCenterService;
import com.taobao.uic.common.domain.BaseUserDO;
import com.taobao.uic.common.domain.ResultDO;
import com.taobao.uic.common.service.userinfo.client.UicReadServiceClient;
@Component("countyBO")
public class CountyBOImpl implements CountyBO {

	ChinaDivisionManager chinaDivisionManager;
	@Autowired
	CountyStationMapper countyStationMapper;
	@Autowired
	CuntaoOrgMapper cuntaoOrgMapper;
	@Autowired
	CuntaoOrgAdminAddressMapper cuntaoOrgAdminAddressMapper;
	@Autowired
	CuntaoOrgBO cuntaoOrgBO;
	TairCache tairCache;
    UicReadServiceClient uicReadServiceClient;
	@Autowired
	CountyStationBO countyStationBO;
	@Autowired
	CuntaoCainiaoStationRelBO cuntaoCainiaoStationRelBO;
	CaiNiaoAdapter caiNiaoAdapter;
    MessageCenterService messageCenterService;
    
    @Autowired
    AttachementBO attachementBO;
	private static final String TEMPLATE_ID = "580107779";
    private static final String SOURCE_ID = "cuntao_org*edit_addr";
    private static final String MESSAGE_TYPE_ID = "120975556";
	
	
	public CountyDto saveCountyStation(String operator,CountyDto countyDto){
		validateSaveCountyStationParam(countyDto);
		//TODO 解决前台没有传入detail问题
        converDetail(countyDto);
        //在绑定组织树之前做
        if (countyDto.getOrgId() == null && countyDto.getId() != null) {
        	CountyStation old=countyStationMapper.selectByPrimaryKey(countyDto.getId());
            if (old != null) {
            	countyDto.setOrgId(old.getOrgId());
            }
        }
        sameNameValidate(countyDto);
        //绑定 县运营中心 到村淘组织树上
        bindCuntaoCountyOrg(operator, countyDto);
        Long taobaoUserId = getTaobaoUserId(countyDto.getTaobaoNick());
        //新建县服务中心
        if (countyDto.getId() == null || countyDto.getId() <= 0) {
            CountyStation countyStation = toCountyStationDOAddNew(countyDto, taobaoUserId,operator);
            countyStationBO.addCountyStation(countyStation);
            countyDto.setId(countyStation.getId());
            // 同步菜鸟县仓
            syncNewCountyStationToCainiao(operator, countyDto, taobaoUserId);
            //新增绑定附件
            if(!ListUtils.isEmpty(countyDto.getAttachements())){
            	OperatorDto operatorDto =new OperatorDto();
            	operatorDto.setOperator(operator);
            	attachementBO.addAttachementBatch(countyDto.getAttachements(), countyStation.getId(), AttachementBizTypeEnum.COUNTY_STATION, operatorDto);
            }
            //自动绑定村淘组织和行政地址
            bindOrg2Address(countyStation,operator);
        } else {
            //修改县服务中心
            CountyStation old = countyStationMapper.selectByPrimaryKey(countyDto.getId());
            CountyStation countyStation = toCountyStationDOUpdate(operator, countyDto, old, taobaoUserId);
            countyStationMapper.updateByPrimaryKeySelective(countyStation);
            //如果是运营中 修改同步菜鸟驿站
            if ((CountyStationManageStatusEnum.OPERATING.equals(CountyStationManageStatusEnum.valueof(old.getManageStatus())))) {
                syncModifiedCountyStationToCainiao(old, countyDto);
            }
            //因为修改县服务中心，不知道有没有修改附件，一律删除，再新增
            if(!ListUtils.isEmpty(countyDto.getAttachements())) {
            	OperatorDto operatorDto =new OperatorDto();
            	operatorDto.setOperator(operator);
            	operatorDto.setOperatorType(OperatorTypeEnum.BUC);
            	AttachementDeleteDto deletedDto=new AttachementDeleteDto();
            	deletedDto.setObjectId(countyStation.getId());
            	deletedDto.setBizType( AttachementBizTypeEnum.COUNTY_STATION);
            	attachementBO.deleteAttachement(deletedDto);
            	attachementBO.addAttachementBatch(countyDto.getAttachements(), countyStation.getId(), AttachementBizTypeEnum.COUNTY_STATION, operatorDto);
            }
        }
        return countyDto;
	}
	
	public static void validateSaveCountyStationParam(CountyDto countyDto) {
        Validate.notNull(countyDto, "countyDto is null");
        Validate.notEmpty(countyDto.getName(), "countyDto.name is empty");
        Validate.notNull(countyDto.getParentId(), "countyDto.parentId is null");
        Validate.notEmpty(countyDto.getProvince(), "countyDto.province is empty");
        Validate.notEmpty(countyDto.getCity(), "countyDto.city is empty");

        String logisticsOperator = countyDto.getLogisticsOperator();
        //筹划中县点，可能未传
        if (StringUtils.isEmpty(logisticsOperator)) {
            return;
        }
        int length = logisticsOperator.split(",").length;
        if (length > 4) {
            throw new ParamException("物流联系人不能超过4个!");
        }
    }
	
	 private void converDetail(CountyDto countyDto) {
	        String province = countyDto.getProvince();
	        String provinceDetail = countyDto.getProvinceDetail();
	        if (StringUtils.isNotEmpty(province) && StringUtils.isEmpty(provinceDetail)) {
	            DivisionVO divisionVO = chinaDivisionManager.getDivisionById(Long.parseLong(province));
	            if (divisionVO != null) {
	            	countyDto.setProvinceDetail(divisionVO.getDivisionAbbName());
	            }
	        }

	        String city = countyDto.getCity();
	        String cityDetail = countyDto.getCityDetail();
	        if (StringUtils.isNotEmpty(city) && StringUtils.isEmpty(cityDetail)) {
	            DivisionVO divisionVO = chinaDivisionManager.getDivisionById(Long.parseLong(city));
	            if (divisionVO != null) {
	            	countyDto.setCityDetail(divisionVO.getDivisionAbbName());
	            }
	        }

	        String county = countyDto.getCounty();
	        String countyDetail = countyDto.getCountyDetail();
	        if (StringUtils.isNotEmpty(county) && StringUtils.isEmpty(countyDetail)) {
	            DivisionVO divisionVO = chinaDivisionManager.getDivisionById(Long.parseLong(county));
	            if (divisionVO != null) {
	            	countyDto.setCountyDetail(divisionVO.getDivisionAbbName());
	            }
	        }
	        String town = countyDto.getTown();
	        String townDetail = countyDto.getTownDetail();
	        if (StringUtils.isNotEmpty(town) && StringUtils.isEmpty(townDetail)) {
	            DivisionVO divisionVO = chinaDivisionManager.getDivisionById(Long.parseLong(town));
	            if (divisionVO != null) {
	            	countyDto.setTownDetail(divisionVO.getDivisionName());
	            }
	        }
	    }
	 
	/**
	 * 校验大区组织下，是否已经有同名的县服务中心
	 *
	 * @param countyStationDto
	 */
	private void sameNameValidate(CountyDto countyDto) {
		CountyStationExample example = new CountyStationExample();
		Criteria c = example.createCriteria();
		c.andIsDeletedEqualTo("n").andParentIdEqualTo(countyDto.getParentId())
				.andNameEqualTo(countyDto.getName());
		List<CountyStation> countys = countyStationMapper
				.selectByExample(example);
		if (CollectionUtils.isNotEmpty(countys)) {
			boolean exist = false;
			for (CountyStation old : countys) {
				// 新增
				if (countyDto.getId() == null
						&& old.getName().equals(countyDto.getName())) {
					exist = true;
					break;
				}// 编辑
				else if (countyDto.getId() != null
						&& !old.getId().equals(countyDto.getId())) {
					exist = true;
					break;
				}
			}
			if (exist) {
				throw new AugeBusinessException("县服务中心不能重名");
			}
		}
	}

	 private void bindCuntaoCountyOrg(String operator, CountyDto countyDto) {
	        //没有绑定过组织
	        if (countyDto.getOrgId() == null || countyDto.getOrgId() <= 0) {
	            //校验父组织
	        	validateParentOrg(countyDto);
	            //查询大区下已经存在的同名县服务中心组织
	            CuntaoOrg existCuntaoOrg = findCunTaoOrg(countyDto.getName(), countyDto.getParentId());
	            //因为前面已经做过同名校验
	            if (null != existCuntaoOrg) {
	            	countyDto.setOrgId(existCuntaoOrg.getId());
	            } else {
	                Long cuntaoOrgId = createCuntaoOrg(countyDto,operator);
	                countyDto.setOrgId(cuntaoOrgId);
	            }
	        } else {
	            //更新组织树中的名称和parentId等
	        	updateCuntaoOrg(countyDto,operator);
	            //清楚缓存
	            tairCache.invalid("cuntao_orgid_1");
	        }
	    }

	/**
	 * 校验父组织是否存在
	 *
	 * @param context
	 * @param countyStationDto
	 */
	private void validateParentOrg(CountyDto countyDto) {
		CuntaoOrg org=cuntaoOrgMapper.selectByPrimaryKey(countyDto.getParentId());
		if(org==null){
			throw new AugeBusinessException("查询大区异常"+countyDto.getParentId());
		}
	}
	
	/**
     * 查询 县运营中心 组织
     *
     * @return
     */
    private CuntaoOrg findCunTaoOrg(String name, long parentId) {
    	CuntaoOrgExample example=new CuntaoOrgExample();
    	com.taobao.cun.auge.dal.domain.CuntaoOrgExample.Criteria c = example.createCriteria();
		c.andIsDeletedEqualTo("n").andNameEqualTo(name).andParentIdEqualTo(parentId);
        List<CuntaoOrg> list = cuntaoOrgMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }
    
    /**
     * 创建 县运营中心 组织节点
     *
     * @param countyStationDto
     * @return
     */
    private Long createCuntaoOrg(CountyDto countyDto,String operator) {
        CuntaoOrg addCuntaoOrg = new CuntaoOrg();
        addCuntaoOrg.setName(countyDto.getName());
        addCuntaoOrg.setDeptPro(CuntaoOrgDeptProEnum.COUNTRY_SERVICE_CENTER.getDesc());
        addCuntaoOrg.setParentId(countyDto.getParentId());
        addCuntaoOrg.setOrgRangeType("county");
        addCuntaoOrg.setTempParentId(countyDto.getSpecialTeamId());
        if (CountyStationManageModelEnum.SELF.equals(countyDto.getManageModel())) {
            addCuntaoOrg.setOrgType(CuntaoOrgTypeEnum.SELF_SUPPORT.getCode());
        } else {
            addCuntaoOrg.setOrgType(CuntaoOrgTypeEnum.NO_SELF_SUPPORT.getCode());
        }
        Long id = cuntaoOrgBO.addOrg(addCuntaoOrg, operator);
        return id;
    }
    
    private void updateCuntaoOrg(CountyDto countyDto,String operator) {
        CuntaoOrg cuntaoOrg = new CuntaoOrg();
        	cuntaoOrg.setId(countyDto.getOrgId());
        	cuntaoOrg.setName(countyDto.getName());
            //获取新的父组织
        	CuntaoOrg parentOrgDo = cuntaoOrgMapper.selectByPrimaryKey(countyDto.getParentId());
        	cuntaoOrg.setFullIdPath(parentOrgDo.getFullIdPath() + "/" + countyDto.getOrgId());
        	cuntaoOrg.setFullNamePath(parentOrgDo.getFullNamePath() + "/" + countyDto.getName());
        	cuntaoOrg.setFullOrderPath(parentOrgDo.getFullOrderPath() + "/0");
        	cuntaoOrg.setParentId(countyDto.getParentId());
        	cuntaoOrg.setGmtModified(new Date());
        	cuntaoOrg.setModifier(operator);
            
            CuntaoOrg team = cuntaoOrgMapper.selectByPrimaryKey(countyDto.getSpecialTeamId()); 
            if(team != null){
            	cuntaoOrg.setTempFullIdPath(team.getTempFullIdPath() + "/" + countyDto.getOrgId());
            	cuntaoOrg.setTempFullNamePath(team.getTempFullNamePath() + "/" + countyDto.getName());
            	cuntaoOrg.setTempParentId(countyDto.getSpecialTeamId());
            }
            cuntaoOrgMapper.updateByPrimaryKeySelective(cuntaoOrg);
    }

    private Long getTaobaoUserId(String taobaoNick) {
        if (taobaoNick != null) {
            ResultDO<BaseUserDO> baseUserDO = uicReadServiceClient.getBaseUserByNick(taobaoNick);
            if (baseUserDO == null || baseUserDO.getModule() == null || !baseUserDO.isSuccess()) {
                throw new AugeBusinessException("not find taobaoUserID: " + taobaoNick);
            }
            return baseUserDO.getModule().getUserId();
        }
        return null;
    }
    
    public static CountyStation toCountyStationDOAddNew(CountyDto countyDto,Long taobaoUserId,String operator) {
        if (countyDto.getManageModel() == null) {
        	countyDto.setManageModel(CountyStationManageModelEnum.SELF);
        }
        if (countyDto.getManageStatus() == null) {
        	countyDto.setManageStatus(CountyStationManageStatusEnum.PLANNING);
        }
        CountyStation countyStation = new CountyStation();
        BeanUtils.copyProperties(countyDto, countyStation);
        countyStation.setCreator(operator);
        countyStation.setModifier(operator);
        countyStation.setManageModel(countyDto.getManageModel().getCode());
        countyStation.setManageStatus(countyDto.getManageStatus().getCode());
        if (taobaoUserId != null) {
        	countyDto.setTaobaoUserId(taobaoUserId);
            countyStation.setTaobaoUserId(new Long(taobaoUserId.toString()));
        }
        if (null != countyDto.getOrgId()) {
            countyStation.setOrgId(countyDto.getOrgId());
        }
        countyStation.setGmtStartOperation(countyDto.getStartOperationTime());
        countyStation.setLeaseType(countyDto.getLeaseTypeEnum() == null ? null : countyDto.getLeaseTypeEnum().getCode());
        countyStation.setWaterPayment(StringUtils.isEmpty(countyDto.getWaterPayment())? "0" : countyDto.getWaterPayment());
        countyStation.setElectricPayment(StringUtils.isEmpty(countyDto.getElectricPayment())? "0" : countyDto.getElectricPayment());
        countyStation.setPropertyPayment(StringUtils.isEmpty(countyDto.getPropertyPayment())? "0" : countyDto.getPropertyPayment());
        return countyStation;
    }
    
    private void syncNewCountyStationToCainiao(String operator, CountyDto countyDto, Long taobaoUserId) {
        CuntaoCainiaoStationRel cuntaoCainiaoStationRel = cuntaoCainiaoStationRelBO.queryCuntaoCainiaoStationRel(
        		countyDto.getId(), CuntaoCainiaoStationRelTypeEnum.COUNTY_STATION);
        if (null != cuntaoCainiaoStationRel) {
            return;
        }
        CaiNiaoStationDto stationDto = toNewCaiNiaoStationDto(countyDto);
        Long caiNiaostationId = caiNiaoAdapter.addCounty(stationDto);
        if (caiNiaostationId == null) {
            throw new BusinessException("同步菜鸟驿站失败");
        } else {
        	CuntaoCainiaoStationRelDto relDO = new CuntaoCainiaoStationRelDto();
            relDO.setObjectId(countyDto.getId());
            relDO.setCainiaoStationId(caiNiaostationId);
            relDO.setType(CuntaoCainiaoStationRelTypeEnum.COUNTY_STATION);
            cuntaoCainiaoStationRelBO.insertCuntaoCainiaoStationRel(relDO);
        }
    }
    
    /**
     * StationDto 转换，新建服务中县服务中心时，物流操作人员，采用邮件方式通知菜鸟，不通过API方式
     *
     * @param countyStationDto
     * @return
     */
    private CaiNiaoStationDto toNewCaiNiaoStationDto(CountyDto countyDto) {
    	CaiNiaoStationDto stationDto = new CaiNiaoStationDto();
        stationDto.setStationName(countyDto.getName());
        stationDto.setStationAddress(convertToStationAddress(countyDto));
        stationDto.setContact(countyDto.getEmployeeName());
        stationDto.setMobile(countyDto.getLogisticsPhone());
        stationDto.setTaobaoUserId(countyDto.getTaobaoUserId());
        stationDto.setStationType(4);
        stationDto.setLoginId(countyDto.getLogisticsOperator());
        stationDto.setStationId(countyDto.getId());
        return stationDto;
    }
    
    private  Address convertToStationAddress(CountyDto countyDto) {
        Address address = new Address();
        address.setProvince(countyDto.getProvince());
        address.setProvinceDetail(countyDto.getProvinceDetail());
        address.setCity(countyDto.getCity());
        address.setCityDetail(countyDto.getCityDetail());
        address.setCounty(countyDto.getCounty());
        address.setCountyDetail(countyDto.getCountyDetail());
        address.setTown(countyDto.getTown());
        address.setTownDetail(countyDto.getTownDetail());
        address.setAddressDetail(countyDto.getAddressDetail());
        address.setLat(countyDto.getLat());
        address.setLng(countyDto.getLng());
        return address;
    }

    /**
     * 绑定村淘组织和行政地址
     *
     * @param countyStation
     * @param context
     */
    private void bindOrg2Address(CountyStation countyStation, String operator) {
        CuntaoOrgAdminAddress cuntaoOrgAdminAddressDO = buildCuntaoOrgAdminAddressDO(countyStation);
        //如果市、县都不存在，则不绑定
        if (null == cuntaoOrgAdminAddressDO) {
            return;
        }
        // 行政地址名称容易变化，只按code查询。
        CuntaoOrgAdminAddressExample example= new CuntaoOrgAdminAddressExample(); 
        com.taobao.cun.auge.dal.domain.CuntaoOrgAdminAddressExample.Criteria c=example.createCriteria();
        c.andIsDeletedEqualTo("n").andAddressCodeEqualTo(cuntaoOrgAdminAddressDO.getAddressCode());
        List<CuntaoOrgAdminAddress> addresses=cuntaoOrgAdminAddressMapper.selectByExample(example);
        //已经绑定了村淘组织,则修改为新的绑定  村淘组织 ： 行政地址=1：n，但是一个行政地址只能绑定一个组织
        if (addresses.size()>0) {
        	cuntaoOrgAdminAddressDO =addresses.get(0);
            //修改为新的村淘组织
            cuntaoOrgAdminAddressDO.setCuntaoOrgId(countyStation.getOrgId());
            cuntaoOrgAdminAddressDO.setModifier(operator);
            cuntaoOrgAdminAddressMapper.updateByPrimaryKey(cuntaoOrgAdminAddressDO);
        } else {
            cuntaoOrgAdminAddressDO.setCuntaoOrgId(countyStation.getOrgId());
            cuntaoOrgAdminAddressDO.setGmtCreate(new Date());
            cuntaoOrgAdminAddressDO.setModifier(operator);
            cuntaoOrgAdminAddressDO.setCreator(operator);
            cuntaoOrgAdminAddressDO.setGmtModified(new Date());
            cuntaoOrgAdminAddressDO.setIsDeleted("n");
            //绑定组织和行政地址
            cuntaoOrgAdminAddressMapper.insert(cuntaoOrgAdminAddressDO);

            //激活因该地区未开通的合伙人
//            activeRefusedPartner(countyStation, context);
        }
    }
    
    /**
     * 只有存在市或者县时，才自动关联
     * @param countyStation
     * @return
     */
    private CuntaoOrgAdminAddress buildCuntaoOrgAdminAddressDO(CountyStation countyStation) {
        CuntaoOrgAdminAddress cuntaoOrgAdminAddressDO = new CuntaoOrgAdminAddress();

        if (StringUtil.isNotEmpty(countyStation.getCounty())) {
            cuntaoOrgAdminAddressDO.setAddressCode(countyStation.getCounty());
            cuntaoOrgAdminAddressDO.setAddressName(countyStation.getCountyDetail());
        } else if (StringUtil.isNotEmpty(countyStation.getCity())) {
            cuntaoOrgAdminAddressDO.setAddressCode(countyStation.getCity());
            cuntaoOrgAdminAddressDO.setAddressName(countyStation.getCityDetail());
        } else {
            return null;
        }

        return cuntaoOrgAdminAddressDO;
    }
    
    private CountyStation toCountyStationDOUpdate(String operator, CountyDto countyDto,CountyStation old,Long taobaoUserId) {
        if (countyDto.getManageModel() == null) {
        	countyDto.setManageModel(CountyStationManageModelEnum.SELF);
        }
        CountyStation countyStation = new CountyStation();
        BeanUtils.copyProperties(countyDto, countyStation);
        countyStation.setModifier(operator);
        countyStation.setLeaseType(countyDto.getLeaseTypeEnum() == null ? null : countyDto.getLeaseTypeEnum().getCode());
        countyStation.setWaterPayment(StringUtils.isEmpty(countyDto.getWaterPayment())? "0" : countyDto.getWaterPayment());
        countyStation.setElectricPayment(StringUtils.isEmpty(countyDto.getElectricPayment())? "0" : countyDto.getElectricPayment());
        countyStation.setPropertyPayment(StringUtils.isEmpty(countyDto.getPropertyPayment())? "0" : countyDto.getPropertyPayment());
        countyStation.setManageModel(countyDto.getManageModel().getCode());
        if (countyDto.getManageStatus() != null) {
            countyStation.setManageStatus(countyDto.getManageStatus().getCode());
        }
        if (!CountyStationManageStatusEnum.OPERATING
                .equals(CountyStationManageStatusEnum.valueof(old.getManageStatus()))&&taobaoUserId != null) {
        	 countyStation.setTaobaoUserId(taobaoUserId);
        }
        if (null != countyDto.getOrgId()) {
            countyStation.setOrgId(countyDto.getOrgId());
        }
        countyStation.setGmtStartOperation(countyDto.getStartOperationTime());
        countyStation.setId(old.getId());
        return countyStation;
    }
    
    /**
     * 修改运营中的县运营中心数据同步至菜鸟
     *
     * @param old
     * @param countyStationDto
     */
    private void syncModifiedCountyStationToCainiao(CountyStation old,  CountyDto countyDto) {
    	CuntaoCainiaoStationRel rel= cuntaoCainiaoStationRelBO.queryCuntaoCainiaoStationRel(old.getId(), CuntaoCainiaoStationRelTypeEnum.COUNTY_STATION);
        if (rel == null) {
            throw new AugeBusinessException("cuntaoCainiaoStationRelDao.getCuntaoCainiaoStationRelByCountyStationId is null");
        }
        //地址变更了，才发邮件,即使CountyStationDto中地址变更了
        if(needToSendAddressUpdatedEmail(old, countyDto)) {
            sendUpdateAddressEmail(countyDto, old);
        }
    }
    
  //县服务中心，地址是否变化
    private boolean needToSendAddressUpdatedEmail(CountyStation old, CountyDto countyDO) {
        if (isAddressChanged(old.getProvince(), countyDO.getProvince())) {
            return true;
        }

        if (isAddressChanged(old.getCity(), countyDO.getCity())) {
            return true;
        }

        if (isAddressChanged(old.getCounty(), countyDO.getCounty())) {
            return true;
        }
        if (isAddressChanged(old.getTown(), countyDO.getTown())) {
            return true;
        }

        if (isAddressChanged(old.getAddressDetail(), countyDO.getAddressDetail())) {
            return true;
        }
        return false;
    }
    
    private static boolean isAddressChanged(String oldAddress, String newAddress) {
        return (null != oldAddress && !oldAddress.equals(newAddress)) || (null == oldAddress && null != newAddress);
    }
    
    
    private void sendUpdateAddressEmail(CountyDto countyDto, CountyStation old) {
        //最多存放8个，如果模板中参数个数变化了，注意修改
        Map<String, Object> params = new HashMap<String, Object>(8);
        params.put("originAddress", convertToStationAddressString(countyDto));
        params.put("curAddress", convertToStationAddressString(old));
        params.put("orgName", StringUtils.isEmpty(countyDto.getName()) ? old.getName() : countyDto.getName());
        params.put("orgType", "县服务中心");
//        List<String> mailList = findEmailReceiverFromResource(STATION_ADDRESS_CHANGE_RECEIVERS);
        List<String> mailList=new ArrayList<String>();
        while (mailList.size() > 50) {
            List<String> subMailList = mailList.subList(0, 49);
            messageCenterService.sendMail(subMailList, mailParam(TEMPLATE_ID, SOURCE_ID, MESSAGE_TYPE_ID), params);
            mailList.removeAll(subMailList);
        }
        messageCenterService.sendMail(mailList, mailParam(TEMPLATE_ID, SOURCE_ID, MESSAGE_TYPE_ID), params);
    }
    
    private Map<String,String> mailParam(String templateId,String sourceId,String messageTypeId){
    	Map<String,String> param = new HashMap<String, String>();
    	param.put("templateId", templateId);
    	param.put("sourceId", sourceId);
    	param.put("messageTypeId", messageTypeId);
    	return param;
    }
	private String convertToStationAddressString(CountyDto countyDto) {
		StringBuilder address = new StringBuilder();
		address.append(
				StringUtil.isBlank(countyDto.getProvinceDetail()) ? " "
						: countyDto.getProvinceDetail())
				.append(StringUtil.isBlank(countyDto.getCityDetail()) ? " "
						: countyDto.getCityDetail())
				.append(StringUtil.isBlank(countyDto.getCountyDetail()) ? " "
						: countyDto.getCountyDetail())
				.append(StringUtil.isBlank(countyDto.getTownDetail()) ? " "
						: countyDto.getTownDetail())
				.append(StringUtil.isBlank(countyDto.getAddressDetail()) ? " "
						: countyDto.getAddressDetail());
		return address.toString();
	}

	private String convertToStationAddressString(CountyStation countyStation) {
		StringBuilder address = new StringBuilder();
		address.append(
				StringUtil.isBlank(countyStation.getProvinceDetail()) ? " "
						: countyStation.getProvinceDetail())
				.append(StringUtil.isBlank(countyStation.getCityDetail()) ? " "
						: countyStation.getCityDetail())
				.append(StringUtil.isBlank(countyStation.getCountyDetail()) ? " "
						: countyStation.getCountyDetail())
				.append(StringUtil.isBlank(countyStation.getTownDetail()) ? " "
						: countyStation.getTownDetail())
				.append(StringUtil.isBlank(countyStation.getAddressDetail()) ? " "
						: countyStation.getAddressDetail());
		return address.toString();
	}
}
