package com.taobao.cun.auge.store.bo.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Strings;
import com.taobao.biz.common.division.impl.DefaultDivisionAdapterManager;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.FeatureUtil;
import com.taobao.cun.auge.common.utils.POIUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.company.bo.EmployeeWriteBO;
import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import com.taobao.cun.auge.dal.domain.*;
import com.taobao.cun.auge.dal.mapper.CuntaoStoreMapper;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.adapter.CaiNiaoAdapter;
import com.taobao.cun.auge.station.adapter.UicReadAdapter;
import com.taobao.cun.auge.station.bo.CuntaoCainiaoStationRelBO;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.condition.PartnerInstanceCondition;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.service.PartnerInstanceQueryService;
import com.taobao.cun.auge.store.bo.InventoryStoreWriteBo;
import com.taobao.cun.auge.store.bo.StoreReadBO;
import com.taobao.cun.auge.store.bo.StoreWriteV2BO;
import com.taobao.cun.auge.store.dto.*;
import com.taobao.cun.auge.store.dto.StoreStatus;
import com.taobao.cun.auge.store.service.StoreException;
import com.taobao.cun.auge.tag.UserTag;
import com.taobao.cun.auge.tag.service.UserTagService;
import com.taobao.cun.mdjxc.api.CtMdJxcWarehouseApi;
import com.taobao.cun.mdjxc.common.result.DataResult;
import com.taobao.cun.mdjxc.enums.BooleanStatusEnum;
import com.taobao.cun.mdjxc.model.CtMdJxcWarehouseDTO;
import com.taobao.place.client.domain.ResultDO;
import com.taobao.place.client.domain.dataobject.StandardAreaDO;
import com.taobao.place.client.domain.dataobject.StoreGroupDO;
import com.taobao.place.client.domain.enumtype.*;
import com.taobao.place.client.domain.enumtype.v2.StoreExtendsTypeV2;
import com.taobao.place.client.domain.extend.StoreAlbumDO;
import com.taobao.place.client.service.GroupBindService;
import com.taobao.place.client.service.StoreGroupService;
import com.taobao.place.client.service.area.StandardAreaService;
import com.taobao.place.client.service.v2.StoreCreateServiceV2;
import com.taobao.place.client.service.v2.StoreExtendServiceV2;
import com.taobao.place.client.service.v2.StoreUpdateServiceV2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.taobao.place.client.domain.dto.StoreDTO;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class StoreWriteV2BOImpl implements StoreWriteV2BO {

    private static final Logger logger = LoggerFactory.getLogger(StoreWriteV2BOImpl.class);

    @Resource
    private DiamondConfiguredProperties diamondConfiguredProperties;

    @Autowired
    private StoreCreateServiceV2 storeCreateServiceV2;

    @Autowired
    private StoreUpdateServiceV2 storeUpdateServiceV2;

    @Autowired
    private StoreExtendServiceV2 storeExtendServiceV2;

    @Autowired
    private StoreGroupService storeGroupService;

    @Autowired
    private GroupBindService groupBindService;

    @Resource
    private StoreReadBO storeReadBO;

    @Resource
    private CuntaoStoreMapper cuntaoStoreMapper;

    @Autowired
    private PartnerBO partnerBO;

    @Autowired
    private StationBO stationBO;

    @Autowired
    private PartnerInstanceBO partnerInstanceBO;

    @Autowired
    CuntaoCainiaoStationRelBO cuntaoCainiaoStationRelBO;

    @Autowired
    CaiNiaoAdapter caiNiaoAdapter;

    @Autowired
    private StandardAreaService standardAreaService;

    @Autowired
    private DefaultDivisionAdapterManager defaultDivisionAdapterManager;

    @Resource
    private PartnerInstanceQueryService partnerInstanceQueryService;

    @Autowired
    private EmployeeWriteBO employeeWriteBO;

    @Autowired
    CtMdJxcWarehouseApi ctMdJxcWarehouseApi;

    @Resource
    private UserTagService userTagService;
    @Resource
    private InventoryStoreWriteBo inventoryStoreWriteBo;

    @Autowired
    private UicReadAdapter uicReadAdapter;


    @Override
    public Long createByStationId(Long stationId) {
        PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceByStationId(stationId);
        if (rel == null) {
            throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE, "当前合伙人不存在");
        }
        Station station = stationBO.getStationById(stationId);
        Partner partner = partnerBO.getPartnerById(rel.getPartnerId());
        StoreDto store = storeReadBO.getStoreDtoByStationId(station.getId());

        Map<String, String> feature = FeatureUtil.toMap(station.getFeature());
        if (store != null) {
            modifyStationInfoForStore(rel.getId());
            return  store.getShareStoreId();
        }
        StoreDTO storeDTO = initStoreDTO(station, partner);
        storeDTO.setCategoryId(diamondConfiguredProperties.getStoreCategoryId());
        // 仓库的区域CODE，取叶子节点
        String areaId = bulidStoreAddress(station, storeDTO);
        // 如果areaId为空，则无法创建仓库，这里直接终止以下流程
        if (Strings.isNullOrEmpty(areaId)) {
            logger.error("createByDto.error:areaId is null,param:" + stationId);
            throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE, "areaId is null:" + stationId);
        }
        addTagForStoreDTO(StoreCategory.valueOf(feature.get("storeCategory")), storeDTO);
        ResultDO<Long> result = storeCreateServiceV2.create(storeDTO, diamondConfiguredProperties.getStoreMainUserId(),
                StoreBizType.STORE_ITEM_BIZ.getValue());
        if (result.isFailured()) {
            logger.error("createSupplyStore error[" + stationId + "]:" + result.getFullErrorMsg());
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, stationId + result.getFullErrorMsg() + result.getResult());
        }

        String scmCode = "";
        try {
            scmCode = createInventoryStore(station.getName(), station.getTaobaoUserId(), areaId);
        } catch (StoreException e) {
            logger.error("createSupplyStore createInventoryStore[" + stationId + "]:",e);
        }
        // 打标
        if (!userTagService.hasTag(station.getTaobaoUserId(), UserTag.TPS_USER_TAG.getTag())) {
            userTagService.addTag(station.getTaobaoUserId(), UserTag.TPS_USER_TAG.getTag());
        }
        if (!userTagService.hasTag(station.getTaobaoUserId(), UserTag.SAMPLE_SELLER_TAG.getTag())) {
            userTagService.addTag(station.getTaobaoUserId(), UserTag.SAMPLE_SELLER_TAG.getTag());
        }
        // 本地存储
        insertLocalStore(station, scmCode, result.getResult(), station.getName(), stationId, feature.get("storeCategory"));
        initStoreWarehouse(station.getId());
        initStoreEmployees(station.getId());
        return result.getResult();
    }

    private String createInventoryStore(String stationName, Long userId, String areaId)
            throws StoreException {
        InventoryStoreCreateDto inventoryStoreCreateDto = new InventoryStoreCreateDto();
        inventoryStoreCreateDto.setName(stationName);
        inventoryStoreCreateDto.setAlias(stationName);
        inventoryStoreCreateDto.setUserId(userId);
        inventoryStoreCreateDto.setAreaId(Long.parseLong(areaId));
        return inventoryStoreWriteBo.create(inventoryStoreCreateDto);
    }

    private void addTagForStoreDTO(StoreCategory category, StoreDTO storeDTO) {
        switch (category) {
            case FMCG:
                storeDTO.addTag(StoreTags.FMCG_TAG);
                break;
            case MOMBABY:
                storeDTO.addTag(StoreTags.MOMBABY_TAG);
                break;
            case ELEC:
                storeDTO.addTag(StoreTags.ELEC_TAG);
                break;
        }
    }

    private StoreDTO initStoreDTO(Station station, Partner partner) {
        StoreDTO storeDTO = new StoreDTO();
        storeDTO.setName(station.getName());
        storeDTO.setAddress(station.getAddress());
        storeDTO.setBusinessTime("10:00-19:00");
        storeDTO.setOuterId(String.valueOf(station.getId()));
        storeDTO.addTag(diamondConfiguredProperties.getStoreTag());
        storeDTO.addTag(StoreTags.NEED_OPERATE_PHYSICAL_STORE);


        if (!Strings.isNullOrEmpty(station.getLat())) {
            storeDTO.setPosy(POIUtils.toStanardPOI(station.getLat()));
        }
        if (!Strings.isNullOrEmpty(station.getLng())) {
            storeDTO.setPosx(POIUtils.toStanardPOI(fixLng(station.getLng())));
        }
        storeDTO.setStatus(com.taobao.place.client.domain.enumtype.StoreStatus.NORMAL.getValue());
        storeDTO.setCheckStatus(StoreCheckStatus.CHECKED.getValue());
        storeDTO.setAuthenStatus(StoreAuthenStatus.PASS.getValue());

        storeDTO.addContact(partner.getMobile());
        storeDTO.addAttribute(StoreAttribute.SHOPPING_GUIDE_USER_ID.getKey(), String.valueOf(partner.getTaobaoUserId()));
        storeDTO.addAttribute(StoreAttribute.SHOPPING_GUIDE_USER_NAME.getKey(), partner.getName());
        storeDTO.addAttribute(StoreAttribute.SHOPPING_GUIDE_TITLE.getKey(), "店长");
        storeDTO.setPic(diamondConfiguredProperties.getStoreMainImage());
        return storeDTO;
    }

    @Override
    public Long createSupplyStoreByStationId(Long stationId) {

        PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceByStationId(stationId);
        if (rel == null) {
            throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE, "当前合伙人不存在");
        }
        Station station = stationBO.getStationById(stationId);
        Partner partner = partnerBO.getPartnerById(rel.getPartnerId());
        StoreDto store = storeReadBO.getStoreDtoByStationId(stationId);
        if (store != null) {
            //同步修改门店信息
            this.modifyStationInfoForStore(rel.getId());
            return store.getShareStoreId();
        }
        StoreDTO storeDTO = new StoreDTO();
        storeDTO.setName(station.getName());
        storeDTO.setAddress(station.getAddress());
        storeDTO.setOuterId(String.valueOf(stationId));
        storeDTO.setBusinessTime("10:00-19:00");
        storeDTO.addContact(partner.getMobile());
        storeDTO.addAttribute(StoreAttribute.SHOPPING_GUIDE_USER_ID.getKey(), String.valueOf(partner.getTaobaoUserId()));
        storeDTO.addAttribute(StoreAttribute.SHOPPING_GUIDE_USER_NAME.getKey(), partner.getName());
        storeDTO.addAttribute(StoreAttribute.SHOPPING_GUIDE_TITLE.getKey(), "店长");
        storeDTO.setPic(diamondConfiguredProperties.getStoreMainImage());

        storeDTO.setCategoryId(diamondConfiguredProperties.getStoreCategoryId());
        String areaId = bulidStoreAddress(station, storeDTO);


        // 如果areaId为空，则无法创建仓库，这里直接终止以下流程
        if (Strings.isNullOrEmpty(areaId)) {
            logger.error("createSupplyStore error[" + stationId + "]: areaId is null");
            throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE, "areaId is null:" + stationId);
        }
        if (!Strings.isNullOrEmpty(station.getLat())) {
            storeDTO.setPosy(POIUtils.toStanardPOI(station.getLat()));
        }
        if (!Strings.isNullOrEmpty(station.getLng())) {
            storeDTO.setPosx(POIUtils.toStanardPOI(fixLng(station.getLng())));
        }

        storeDTO.addTag(diamondConfiguredProperties.getStoreTag());
        storeDTO.addTag(StoreTags.SUPPLY_STATION_TAG);// 村点补货
        storeDTO.addTag(StoreTags.NEED_OPERATE_PHYSICAL_STORE);
        storeDTO.setStatus(com.taobao.place.client.domain.enumtype.StoreStatus.NORMAL.getValue());
        storeDTO.setCheckStatus(StoreCheckStatus.CHECKED.getValue());
        storeDTO.setAuthenStatus(StoreAuthenStatus.PASS.getValue());
        ResultDO<Long> result = storeCreateServiceV2.create(storeDTO, diamondConfiguredProperties.getStoreMainUserId(),
                StoreBizType.STORE_ITEM_BIZ.getValue());
        if (result.isFailured()) {
            logger.error("createSupplyStore error[" + station.getId() + "]:" + result.getFullErrorMsg());
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, station.getId() + result.getFullErrorMsg());
        }
        uploadStoreSubImage(result.getResult());
        insertLocalStore(station, "", result.getResult(), station.getName(), station.getId(), StoreCategoryConstants.FMCG);
        initGoodSupplyFeature(station.getId());
        initStoreWarehouse(station.getId());
        initStoreEmployees(station.getId());
        return result.getResult();

    }

    public void initStoreEmployees(Long stationId) {
        OperatorDto operator = new OperatorDto();
        operator.setOperator("system");
        operator.setOperatorType(OperatorTypeEnum.HAVANA);
        PartnerInstanceDto partnerInstance = queryInfo(stationId, operator);
        if (partnerInstance != null && partnerInstance.getPartnerDto() != null) {
            CuntaoEmployee employee = new CuntaoEmployee();
            employee.setName(partnerInstance.getPartnerDto().getName());
            employee.setTaobaoNick(partnerInstance.getPartnerDto().getTaobaoNick());
            employee.setTaobaoUserId(partnerInstance.getPartnerDto().getTaobaoUserId());
            employee.setCreator("system");
            employeeWriteBO.createStoreEndorUser(stationId, employee);
        } else {
            logger.error("add storeEmployee error! can not find PartnerInstance By stationId[" + stationId + "]");
        }
    }

    public PartnerInstanceDto queryInfo(Long stationId, OperatorDto operator) {
        ValidateUtils.notNull(stationId);

        Long instanceId = partnerInstanceBO.findPartnerInstanceIdByStationId(stationId);

        PartnerInstanceCondition condition = new PartnerInstanceCondition();
        condition.setInstanceId(instanceId);
        condition.setNeedPartnerInfo(Boolean.TRUE);
        condition.setNeedStationInfo(Boolean.TRUE);
        condition.setNeedDesensitization(Boolean.FALSE);
        condition.setNeedPartnerLevelInfo(Boolean.FALSE);
        condition.copyOperatorDto(operator);

        return partnerInstanceQueryService.queryInfo(condition);
    }

    /**
     * 初始化门店库存
     */
    public boolean initCtMdJxcWarehouse(BooleanStatusEnum buyerIden, String storeId, Long userId) {
        CtMdJxcWarehouseDTO ctMdJxcWarehouseDTO = new CtMdJxcWarehouseDTO();
        ctMdJxcWarehouseDTO.setBuyerIden(buyerIden);
        ctMdJxcWarehouseDTO.setStoreId(storeId);
        ctMdJxcWarehouseDTO.setUserId(userId);
        DataResult<Boolean> result = ctMdJxcWarehouseApi.createWarehouse(ctMdJxcWarehouseDTO);
        if (!result.isSuccess()) {
            logger.error("initCtMdJxcWarehouse error[" + storeId + "]:" + result.getMessage());
        }
        return result.isSuccess();
    }


    public Boolean initStoreWarehouse(Long stationId) {
        try {
            StoreDto storeDto = storeReadBO.getStoreDtoByStationId(stationId);
            if (storeDto == null || storeDto.getShareStoreId() == null) {
                logger.error("initStoreWarehouse error storeDto or sharedStoreId is Null");
                return false;
            }
            return initCtMdJxcWarehouse(BooleanStatusEnum.YES, storeDto.getShareStoreId() + "",
                    storeDto.getTaobaoUserId());
        } catch (Exception e) {
            logger.error("initStoreWarehouse error[" + stationId + "]", e);
            return false;
        }
    }


    public void initGoodSupplyFeature(Long stationId) {
        Long cainiaoStationId = cuntaoCainiaoStationRelBO.getCainiaoStationId(stationId);
        if (cainiaoStationId != null) {
            LinkedHashMap<String, String> features = new LinkedHashMap<String, String>();
            features.put("goodsSupply", "y");
            caiNiaoAdapter.updateStationFeatures(cainiaoStationId, features);
        }
    }

    private void insertLocalStore(Station station, String scmCode, Long result2, String name, Long
            stationId, String category) {
        CuntaoStore cuntaoStore = new CuntaoStore();
        cuntaoStore.setShareStoreId(result2);
        cuntaoStore.setName(name);
        cuntaoStore.setStationId(stationId);
        cuntaoStore.setStatus(StoreStatus.NORMAL.getStatus());
        cuntaoStore.setStoreCategory(category);
        cuntaoStore.setTaobaoUserId(station.getTaobaoUserId());
        cuntaoStore.setScmCode(scmCode);
        cuntaoStore.setEndorOrgId(3L);
        DomainUtils.beforeInsert(cuntaoStore, "system");
        cuntaoStoreMapper.insert(cuntaoStore);
    }

    @Override
    public void modifyStationInfoForStore(Long instanceId) {
        PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(instanceId);
        if (rel == null) {
            throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE, "当前合伙人不存在");
        }
        CuntaoStore cuntaoStore = storeReadBO.getCuntaoStoreByStationId(rel.getStationId());
        if (cuntaoStore == null) {
            return;
        }
        Station station = stationBO.getStationById(rel.getStationId());
        Partner partner = partnerBO.getPartnerById(rel.getPartnerId());
        StoreDTO storeDTO = new StoreDTO();
        storeDTO.setName(station.getName());
        storeDTO.setAddress(station.getAddress());
        storeDTO.addContact(partner.getMobile());
        storeDTO.addAttribute(StoreAttribute.SHOPPING_GUIDE_USER_ID.getKey(), String.valueOf(partner.getTaobaoUserId()));
        storeDTO.addAttribute(StoreAttribute.SHOPPING_GUIDE_USER_NAME.getKey(), partner.getName());
        storeDTO.addAttribute(StoreAttribute.SHOPPING_GUIDE_TITLE.getKey(), "店长");
        if (!Strings.isNullOrEmpty(station.getLat())) {
            storeDTO.setPosy(POIUtils.toStanardPOI(station.getLat()));
        }
        if (!Strings.isNullOrEmpty(station.getLng())) {
            storeDTO.setPosx(POIUtils.toStanardPOI(fixLng(station.getLng())));
        }
        storeDTO.setStoreId(cuntaoStore.getShareStoreId());
        // 更新
        ResultDO<Boolean> result = storeUpdateServiceV2.update(storeDTO,
                diamondConfiguredProperties.getStoreMainUserId(), StoreBizType.STORE_ITEM_BIZ.getValue());
        if (result.isFailured()) {
            logger.error("modifyStationInfoForStore error[" + station.getId() + "]:" + result.getFullErrorMsg());
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, station.getId() + result.getFullErrorMsg());
        }
        cuntaoStore.setName(station.getName());
        cuntaoStore.setTaobaoUserId(station.getTaobaoUserId());
        cuntaoStoreMapper.updateByPrimaryKey(cuntaoStore);
    }

    public static String fixLng(String lng) {
        if (lng != null && lng.length() == 8 && lng.endsWith("0") && lng.startsWith("9")) {
            return lng.substring(0, lng.length() - 1);
        }
        return lng;
    }

    @Override
    public void closeStore(Long stationId) {
        CuntaoStore cuntaoStore = storeReadBO.getCuntaoStoreByStationId(stationId);
        if (cuntaoStore == null) {
            return;
        }
        StoreDTO storeDTO = new StoreDTO();
        storeDTO.setStoreId(cuntaoStore.getShareStoreId());
        storeDTO.setStatus(com.taobao.place.client.domain.enumtype.StoreStatus.CLOSE.getValue());
        // 更新
        ResultDO<Boolean> result = storeUpdateServiceV2.update(storeDTO,
                diamondConfiguredProperties.getStoreMainUserId(), StoreBizType.STORE_ITEM_BIZ.getValue());
        if (result.isFailured()) {
            logger.error("closeStore error[" + stationId + "]:" + result.getFullErrorMsg());
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, stationId + result.getFullErrorMsg());
        }

        CuntaoStore cs = new CuntaoStore();
        cs.setId(cuntaoStore.getId());
        cs.setStatus(StoreStatus.CLOSE.getStatus());
        cs.setGmtModified(new Date());
        cs.setModifier("system");
        cuntaoStoreMapper.updateByPrimaryKeySelective(cs);
    }

    @Override
    public void uploadStoreImage(Long shareStoreId) {
        this.uploadStoreMainImage(shareStoreId);
        this.uploadStoreSubImage(shareStoreId);
    }

    @Override
    public void uploadStoreMainImage(Long shareStoreId) {
        StoreDTO storeDTO = new StoreDTO();
        storeDTO.setStoreId(shareStoreId);
        storeDTO.setPic(diamondConfiguredProperties.getStoreMainImage());
        // 更新
        ResultDO<Boolean> res = storeUpdateServiceV2.update(storeDTO,
                diamondConfiguredProperties.getStoreMainUserId(), StoreBizType.STORE_ITEM_BIZ.getValue());
        if (res.isFailured()) {
            logger.error("uploadStoreMainImage error[" + shareStoreId + "]:" + res.getFullErrorMsg());
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, shareStoreId + res.getFullErrorMsg());
        }
    }

    @Override
    public void uploadStoreSubImage(Long shareStoreId) {
        StoreAlbumDO albumDO = new StoreAlbumDO();
        albumDO.setPicCdnDomainName(diamondConfiguredProperties.getStoreImagePerfix());
        String subImageList = diamondConfiguredProperties.getStoreSubImage();
        List<Map<String, String>> l = JSON.parseObject(subImageList, new TypeReference<List<Map<String, String>>>() {
        });
        if (CollectionUtils.isEmpty(l)) {
            return;
        }
        List<StoreAlbumDO.PictureDO> pictures = l.stream().map(l1 -> bulidPic(l1)).collect(Collectors.toList());
        albumDO.setPictures(pictures);

        ResultDO<Boolean> res = storeExtendServiceV2.updateExtends(shareStoreId, StoreExtendsTypeV2.STORE_ENVIRONMENT_PICS, albumDO);
        if (res.isFailured()) {
            logger.error("uploadStoreSubImage error[" + shareStoreId + "]:" + res.getFullErrorMsg());
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, shareStoreId + res.getFullErrorMsg());
        }

    }

    private StoreAlbumDO.PictureDO bulidPic(Map<String, String> p) {
        StoreAlbumDO.PictureDO r = new StoreAlbumDO.PictureDO();
        r.setUrl(p.get("url"));
        r.setName(p.get("name"));
        r.setSequence(Integer.parseInt(p.get("seq")));
        return r;
    }

    private String bulidStoreAddress(com.taobao.cun.auge.dal.domain.Station station, com.taobao.place.client.domain.dto.StoreDTO storeDTO) {
        // 仓库的区域CODE，取叶子节点
        String areaId = null;
        // 省
        if (!Strings.isNullOrEmpty(station.getProvince())) {
            Long cityCode = tb2gbCode(Long.parseLong(station.getCity()));
            if (cityCode == null) {
                cityCode = Long.parseLong(station.getCity());
            }
            StandardAreaDO standardAreaDO = standardAreaService.getStandardAreaDOById(cityCode);
            if (standardAreaDO != null && standardAreaDO.getParentId() != null) {
                storeDTO.setProv(standardAreaDO.getParentId().intValue());
            } else {
                storeDTO.setProv(Integer.parseInt(station.getProvince()));
            }
            storeDTO.setProvName(station.getProvinceDetail());
            areaId = station.getProvince();
        }
        // 市
        if (!Strings.isNullOrEmpty(station.getCity())) {
            Long gbCode = tb2gbCode(Long.parseLong(station.getCity()));
            if (gbCode != null) {
                storeDTO.setCity(gbCode.intValue());
            } else {
                // 重庆市特殊处理，共享需要500200标准code
                if ("500100".equals(station.getCity())) {
                    Long countyCode = tb2gbCode(Long.parseLong(station.getCounty()));
                    if (countyCode == null) {
                        countyCode = Long.parseLong(station.getCounty());
                    }
                    StandardAreaDO standardAreaDO = standardAreaService.getStandardAreaDOById(countyCode);
                    storeDTO.setCity(standardAreaDO.getParentId().intValue());
                } else {
                    storeDTO.setCity(Integer.parseInt(station.getCity()));
                }
            }
            storeDTO.setCityName(station.getCityDetail());
            areaId = station.getCity();
        }
        // 区/县
        // 区/县
        if (!Strings.isNullOrEmpty(station.getCounty())) {
            Integer district = getCountyCode(station.getCounty(), station.getCountyDetail(), station.getCity());
            storeDTO.setDistrict(district);
            storeDTO.setDistrictName(station.getCountyDetail());
            areaId = station.getCounty();
        }
        if (!Strings.isNullOrEmpty(station.getTown())) {
            StandardAreaDO area = this.standardAreaService.getStandardAreaDOById(Long.parseLong(station.getTown()));
            if (area != null) {
                storeDTO.setTown(Integer.parseInt(station.getTown()));
                storeDTO.setTownName(station.getTownDetail());
            }
            areaId = station.getTown();
        }
        return areaId;
    }

    public Long tb2gbCode(Long taobaocode) {
        if (taobaocode == null) {
            return null;
        }
        // 地址库接口转不出来，人工转一下
        // 江苏淮安清江浦
        if (taobaocode == 320802L) {
            return 320812L;
        }
        // 江西九江庐山
        if (taobaocode == 360427L) {
            return 360483L;
        }
        if (taobaocode != null) {
            return defaultDivisionAdapterManager.tbCodeToGbCode(taobaocode);
        }
        return null;
    }

    public Integer getCountyCode(String countyCode, String countyDetail, String cityCode) {
        Long gbCode = tb2gbCode(Long.parseLong(countyCode));
        if (gbCode == null) {
            gbCode = Long.parseLong(countyCode);
        }
        StandardAreaDO area = standardAreaService.getStandardAreaDOById(gbCode);
        if (area != null) {
            return area.getAreaCode().intValue();
        } else {
            if (cityCode == null) {
                return Integer.parseInt(countyCode);
            }
            List<StandardAreaDO> areaList = standardAreaService.getChildrenByCityId(Long.parseLong(cityCode));
            if (CollectionUtils.isEmpty(areaList)) {
                return Integer.parseInt(countyCode);
            }
            Optional<StandardAreaDO> areaDO = areaList.stream().filter(ar -> ar.getAreaName().startsWith(countyDetail)).findFirst();
            if (areaDO.isPresent()) {
                return areaDO.get().getAreaCode().intValue();
            }
            return Integer.parseInt(countyCode);
        }

    }

    @Override
    public StoreGroupInfoDto createStoreGroup(String title,String comment) {
        StoreGroupDO storeGroupDO = new StoreGroupDO();
        Long mainUserId = diamondConfiguredProperties.getStoreMainUserId();
        storeGroupDO.setUserId(mainUserId);
        storeGroupDO.setUserNick(uicReadAdapter.getTaobaoNickByTaobaoUserId(mainUserId));
        storeGroupDO.setTitle(title);
        storeGroupDO.setBizType(StoreGroupBizType.ETICKET.getValue());
        storeGroupDO.setComment(comment);
        storeGroupDO.setStatus(0);
        ResultDO<StoreGroupDO> storeGroupDOResultDO = storeGroupService.create(storeGroupDO);
        if(storeGroupDOResultDO.isSuccess()){
            StoreGroupDO g = storeGroupDOResultDO.getResult();
            StoreGroupInfoDto  res  = new StoreGroupInfoDto();
            res.setId(g.getId());
            res.setAttributes(g.getAttributes());
            res.setBizType(g.getBizType());
            res.setComment(g.getComment());
            res.setTitle(g.getTitle());
            res.setGmtCreate(g.getGmtCreate());
            res.setUserId(g.getUserId());
            res.setStatus(g.getStatus());
            res.setVersion(g.getVersion());
            res.setStoreCount(g.getStoreCount());
            res.setStoreIds(g.getStoreIds());
            return res;
        }else {
            logger.error("createStoreGroup error:" + storeGroupDOResultDO.getFullErrorMsg());
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,storeGroupDOResultDO.getFullErrorMsg());
        }
    }

    @Override
    public Boolean bindStoreGroup(Long groupId, List<Long> shareStoreIds) {
        Objects.requireNonNull(groupId, "groupId不能为空");
        Objects.requireNonNull(groupId, "shareStoreIds不能为空");
        if (CollectionUtils.isNotEmpty(shareStoreIds)) {
            ResultDO<Boolean> booleanResultDO = groupBindService.batchBindStore(groupId, shareStoreIds);
            if(booleanResultDO.isSuccess()){
                if(booleanResultDO.getResult()) {
                    shareStoreIds.forEach(storeId->updatGroupByShareStoreId(groupId,storeId,"y"));
                }
                return booleanResultDO.getResult();
            } else {
                logger.error("bindStoreGroup error:" + booleanResultDO.getFullErrorMsg());
                throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,booleanResultDO.getFullErrorMsg());
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean unBindStoreGroup(Long groupId, List<Long> shareStoreIds) {
        Objects.requireNonNull(groupId, "groupId不能为空");
        Objects.requireNonNull(groupId, "shareStoreIds不能为空");
        if (CollectionUtils.isNotEmpty(shareStoreIds)) {
            ResultDO<Boolean> booleanResultDO = groupBindService.batchUnBindStore(groupId, shareStoreIds);
        if(booleanResultDO.isSuccess()){
            if(booleanResultDO.getResult()) {
                shareStoreIds.forEach(storeId->updatGroupByShareStoreId(groupId,storeId,"n"));
            }
            return booleanResultDO.getResult();
        } else {
            logger.error("unBindStoreGroup error:" + booleanResultDO.getFullErrorMsg());
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,booleanResultDO.getFullErrorMsg());
        }
        }
        return Boolean.TRUE;
    }

    private void updatGroupByShareStoreId(Long groupId,Long shareStoreId,String isBind) {
        CuntaoStore cuntaoStore = storeReadBO.getCuntaoStoreBySharedStoreId(shareStoreId);
        if (cuntaoStore == null) {
            return;
        }
        String  gIds = cuntaoStore.getStoreGroupIds();
        List<Long> sList = null;
        if (StringUtils.isNotEmpty(gIds)){
            sList = JSON.parseObject(gIds, new TypeReference<List<Long>>(){});
        }else{
            sList = new ArrayList<>();
        }
        if ("y".equals(isBind)) {
            sList.add(groupId);
        }else if ("n".equals(isBind)){
            if (CollectionUtils.isNotEmpty(sList)) {
                sList.remove(groupId);
            }
        }
        CuntaoStore cs = new CuntaoStore();
        cs.setId(cuntaoStore.getId());
        cs.setStoreGroupIds(JSONObject.toJSONString(sList));
        cs.setGmtModified(new Date());
        cs.setModifier("system");
        cuntaoStoreMapper.updateByPrimaryKeySelective(cs);
    }
}
