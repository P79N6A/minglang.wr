package com.taobao.cun.auge.station.service.impl;

import com.taobao.cun.auge.api.service.station.NewCustomerUnitQueryService;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ResultUtils;
import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import com.taobao.cun.auge.dal.domain.StationNewCustomer;
import com.taobao.cun.auge.dal.domain.StationNewCustomerExample;
import com.taobao.cun.auge.dal.domain.StationNewCustomerExample.Criteria;
import com.taobao.cun.auge.dal.mapper.StationNewCustomerMapper;
import com.taobao.cun.auge.station.convert.StationNewCustomerConverter;
import com.taobao.cun.auge.station.dto.StationNewCustomerDailyTaskDto;
import com.taobao.cun.auge.station.service.StationNewCustomerService;
import com.taobao.cun.auge.tag.UserTag;
import com.taobao.cun.auge.tag.service.UserTagService;
import com.taobao.cun.auge.validator.BeanValidator;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Service("stationNewCustomerService")
@HSFProvider(serviceInterface = StationNewCustomerService.class,clientTimeout =10000)
public class StationNewCustomerServiceImpl implements StationNewCustomerService {

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
        //风险用户
        Long taobaoUserId = taskDto.getTaobaoUserId();

        if ("y".equalsIgnoreCase(taskDto.getRisk())) {
            removeNewCustomerUserTag(taobaoUserId);
            newCustomerUnitQueryService.invalidNewCustomerCache(taobaoUserId);
        }
        StationNewCustomer record = StationNewCustomerConverter.convert(taskDto);
        DomainUtils.beforeUpdate(record, "system");

        StationNewCustomerExample example = new StationNewCustomerExample();

        Criteria criteria = example.createCriteria();

        criteria.andTaobaoUserIdEqualTo(taobaoUserId);
        criteria.andIsDeletedEqualTo("n");

        stationNewCustomerMapper.updateByExampleSelective(record, example);

        //非风险用户，信息变更，更新缓存
        if ("n".equalsIgnoreCase(taskDto.getRisk())) {
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
        buildRateTime(newCustomer);

        DomainUtils.beforeInsert(newCustomer, "system");
        stationNewCustomerMapper.insertSelective(newCustomer);

        //非风险用户，打标，新增缓存
        if ("n".equalsIgnoreCase(taskDto.getRisk())) {
            //打标
            addNewCustomerUserTag(taobaoUserId);

            //初始化缓存
            newCustomerUnitQueryService.getNewCustomer(taobaoUserId);
        }
    }

    /**
     * 组装权益开始时间，结束时间
     *
     * @param newCustomer
     */
    private void buildRateTime(StationNewCustomer newCustomer) {
        //开始时间为插入时间
        newCustomer.setRateBeginTime(new Date());

        Integer stationNewCustomerRateTime = diamondConfiguredProperties.getStationNewCustomerRateTime();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + stationNewCustomerRateTime);
        //结束时间为插入时间+180天
        newCustomer.setRateEndTime(c.getTime());
    }

    /**
     * 打新用户标
     *
     * @param taobaoUserId
     */
    private void addNewCustomerUserTag(Long taobaoUserId) {
        if (!userTagService.hasTag(taobaoUserId, UserTag.STATION_NEW_CUSTOMER_TAG.getTag())) {
            userTagService.addTag(taobaoUserId, UserTag.STATION_NEW_CUSTOMER_TAG.getTag());
        }
    }

    /**
     * 去新用户标
     *
     * @param taobaoUserId
     */
    private void removeNewCustomerUserTag(Long taobaoUserId) {
        if (userTagService.hasTag(taobaoUserId, UserTag.STATION_NEW_CUSTOMER_TAG.getTag())) {
            userTagService.removeTag(taobaoUserId, UserTag.STATION_NEW_CUSTOMER_TAG.getTag());
        }
    }
}
