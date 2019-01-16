package com.taobao.cun.auge.station.service.impl;

import com.taobao.cun.auge.api.service.station.NewCustomerUnitQueryService;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ResultUtils;
import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import com.taobao.cun.auge.dal.domain.StationNewCustomer;
import com.taobao.cun.auge.dal.domain.StationNewCustomerExample;
import com.taobao.cun.auge.dal.domain.StationNewCustomerExample.Criteria;
import com.taobao.cun.auge.dal.mapper.StationNewCustomerMapper;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.convert.StationNewCustomerConverter;
import com.taobao.cun.auge.station.dto.StationNewCustomerDailyTaskDto;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.service.StationNewCustomerService;
import com.taobao.cun.auge.tag.UserTag;
import com.taobao.cun.auge.tag.service.UserTagService;
import com.taobao.cun.auge.validator.BeanValidator;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Service("stationNewCustomerService")
@HSFProvider(serviceInterface = StationNewCustomerService.class, clientTimeout = 10000)
public class StationNewCustomerServiceImpl implements StationNewCustomerService {

    private static final Logger logger = LoggerFactory.getLogger(StationNewCustomerService.class);

    @Autowired
    private NewCustomerUnitQueryService newCustomerUnitQueryService;

    @Autowired
    private StationNewCustomerMapper stationNewCustomerMapper;

    @Resource
    private UserTagService userTagService;

    @Autowired
    private DiamondConfiguredProperties diamondConfiguredProperties;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void syncNewCustomer(StationNewCustomerDailyTaskDto taskDto) {
        BeanValidator.validate(taskDto);

        Long taobaoUserId = taskDto.getTaobaoUserId();

        boolean isExist = isExistNewCustomer(taobaoUserId);

        if (isExist) {
            updateNewCustomer(taskDto);
        } else {
            addNewCustomer(taskDto);
        }
    }

    /**
     * 是否存在新站点新购用户
     *
     * @param taobaoUserId
     * @return
     */
    private boolean isExistNewCustomer(Long taobaoUserId) {
        StationNewCustomer stationNewCustomer = getNewCustomer(taobaoUserId);
        return null != stationNewCustomer;
    }

    /**
     * 查询新站点新购用户
     *
     * @param taobaoUserId
     * @return
     */
    private StationNewCustomer getNewCustomer(Long taobaoUserId) {
        StationNewCustomerExample example = new StationNewCustomerExample();
        Criteria criteria = example.createCriteria();

        criteria.andIsDeletedEqualTo("n");
        criteria.andTaobaoUserIdEqualTo(taobaoUserId);

        List<StationNewCustomer> customers = stationNewCustomerMapper.selectByExample(example);
        return ResultUtils.selectOne(customers);
    }

    /**
     * 修改新站点新购用户
     *
     * @param taskDto
     */
    private void updateNewCustomer(StationNewCustomerDailyTaskDto taskDto) {
        Long taobaoUserId = taskDto.getTaobaoUserId();

        StationNewCustomer newCustomer = StationNewCustomerConverter.convertToUpdate(taskDto);
        updateNewCustomer(taobaoUserId, newCustomer);

        //风险用户，去标，去缓存
        if ("y".equalsIgnoreCase(taskDto.getRisk())) {
            //去标
            removeNewCustomerUserTag(taobaoUserId);
            //更新去标时间
            updateTagRemoveTime(taobaoUserId);
            newCustomerUnitQueryService.invalidNewCustomerCache(taobaoUserId);
        } else {
            //先转换首登时间，如果时间，抛异常，跳出去
            Date realInterestTime = buildRealInterestTime(taskDto.getRealInterestTime());

            //只有首登时间，解析成功后，再去打标，并更新打标时间
            addNewCustomerUserTag(taobaoUserId);

            //更新打标时间
            updateTagAddTime(taobaoUserId, realInterestTime);

            newCustomerUnitQueryService.invalidNewCustomerCache(taobaoUserId);
            newCustomerUnitQueryService.getNewCustomer(taobaoUserId);
        }
    }

    /**
     * 新增新站点新购用户
     *
     * @param taskDto
     */
    private void addNewCustomer(StationNewCustomerDailyTaskDto taskDto) {
        Long taobaoUserId = taskDto.getTaobaoUserId();

        StationNewCustomer newCustomer = StationNewCustomerConverter.convert(taskDto);

        DomainUtils.beforeInsert(newCustomer, "system");
        stationNewCustomerMapper.insertSelective(newCustomer);

        //非风险用户，打标，新增缓存
        if ("n".equalsIgnoreCase(taskDto.getRisk())) {
            //先转换首登时间，如果时间，抛异常，跳出去
            Date realInterestTime = buildRealInterestTime(taskDto.getRealInterestTime());
            //打标
            addNewCustomerUserTag(taobaoUserId);

            //更新打标时间
            updateTagAddTime(taobaoUserId, realInterestTime);

            //初始化缓存
            newCustomerUnitQueryService.getNewCustomer(taobaoUserId);
        }
    }

    /**
     * 转换首登时间
     *
     * @param realInterestTime
     * @return
     */
    private Date buildRealInterestTime(String realInterestTime) {
        try {
            SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return TIME_FORMAT.parse(realInterestTime);
        } catch (ParseException e) {
            logger.error("Failed to parse realInterestTime.realInterestTime = " + realInterestTime, e);
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE, "Failed to parse realInterestTime.realInterestTime = " + realInterestTime);
        }
    }

    /**
     * 打新用户标
     *
     * @param taobaoUserId
     */
    private void addNewCustomerUserTag(Long taobaoUserId) {
        if (!userTagService.hasTag(taobaoUserId, UserTag.STATION_NEW_CUSTOMER_TAG.getTag())) {
            boolean tagResult = userTagService.addTag(taobaoUserId, UserTag.STATION_NEW_CUSTOMER_TAG.getTag());
            if (!tagResult) {
                logger.error("Failed to add tag。taobaoUserId=" + taobaoUserId);
                throw new AugeBusinessException(AugeErrorCodes.USER_TAG_ERROR_CODE, "Failed to add tag。taobaoUserId=" + taobaoUserId);
            }

        }
    }

    /**
     * 去新用户标
     *
     * @param taobaoUserId
     */
    private void removeNewCustomerUserTag(Long taobaoUserId) {
        if (userTagService.hasTag(taobaoUserId, UserTag.STATION_NEW_CUSTOMER_TAG.getTag())) {
            boolean tagResult = userTagService.removeTag(taobaoUserId, UserTag.STATION_NEW_CUSTOMER_TAG.getTag());
            if (!tagResult) {
                logger.error("Failed to remove tag。taobaoUserId=" + taobaoUserId);
                throw new AugeBusinessException(AugeErrorCodes.USER_TAG_ERROR_CODE, "Failed to remove tag。taobaoUserId=" + taobaoUserId);
            }
        }
    }

    /**
     * 修改打标时间
     *
     * @param taobaoUserId
     */
    private void updateTagAddTime(Long taobaoUserId, Date realInterestTime) {
        StationNewCustomer newCustomer = new StationNewCustomer();

        newCustomer.setTagAddTime(new Date());
        buildRateTime(newCustomer, realInterestTime);

        updateNewCustomer(taobaoUserId, newCustomer);
    }

    /**
     * 组装权益开始时间，结束时间
     *
     * @param newCustomer
     */
    private void buildRateTime(StationNewCustomer newCustomer, Date realInterestTime) {
        Calendar c = Calendar.getInstance();
        c.setTime(realInterestTime);

        //首登时间+2天
        c.add(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 2);

        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);

        newCustomer.setRateBeginTime(c.getTime());

        //结束时间为开始时间+180天
        Integer stationNewCustomerRateTime = diamondConfiguredProperties.getStationNewCustomerRateTime();
        c.add(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + stationNewCustomerRateTime);

        newCustomer.setRateEndTime(c.getTime());
    }

    /**
     * 修改打标时间
     *
     * @param taobaoUserId
     */
    private void updateTagRemoveTime(Long taobaoUserId) {
        StationNewCustomer newCustomer = new StationNewCustomer();

        newCustomer.setTagRmTime(new Date());

        updateNewCustomer(taobaoUserId, newCustomer);
    }

    private void updateNewCustomer(Long taobaoUserId, StationNewCustomer newCustomer) {
        DomainUtils.beforeUpdate(newCustomer, "system");

        StationNewCustomerExample example = new StationNewCustomerExample();

        Criteria criteria = example.createCriteria();

        criteria.andTaobaoUserIdEqualTo(taobaoUserId);
        criteria.andIsDeletedEqualTo("n");

        stationNewCustomerMapper.updateByExampleSelective(newCustomer, example);
    }
}
