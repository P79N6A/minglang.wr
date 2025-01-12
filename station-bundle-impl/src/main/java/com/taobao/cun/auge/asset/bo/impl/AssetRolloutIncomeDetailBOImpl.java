package com.taobao.cun.auge.asset.bo.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.taobao.cun.auge.asset.bo.AssetIncomeBO;
import com.taobao.cun.auge.asset.bo.AssetRolloutBO;
import com.taobao.cun.auge.asset.bo.AssetRolloutIncomeDetailBO;
import com.taobao.cun.auge.asset.convert.AssetRolloutIncomeDetailConverter;
import com.taobao.cun.auge.asset.dto.AssetCategoryCountDto;
import com.taobao.cun.auge.asset.dto.AssetRolloutIncomeDetailDto;
import com.taobao.cun.auge.asset.dto.AssetRolloutIncomeDetailExtDto;
import com.taobao.cun.auge.asset.enums.AssetRolloutIncomeDetailStatusEnum;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.PageDtoUtil;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.AssetRolloutIncomeDetail;
import com.taobao.cun.auge.dal.domain.AssetRolloutIncomeDetailExample;
import com.taobao.cun.auge.dal.domain.AssetRolloutIncomeDetailExample.Criteria;
import com.taobao.cun.auge.dal.domain.AssetRolloutIncomeDetailExtExample;
import com.taobao.cun.auge.dal.mapper.AssetRolloutIncomeDetailExtMapper;
import com.taobao.cun.auge.dal.mapper.AssetRolloutIncomeDetailMapper;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AssetRolloutIncomeDetailBOImpl implements
    AssetRolloutIncomeDetailBO {

    @Autowired
    private AssetRolloutIncomeDetailMapper assetRolloutIncomeDetailMapper;

    @Autowired
    private AssetRolloutIncomeDetailExtMapper assetRolloutIncomeDetailExtMapper;

    @Autowired
    private AssetRolloutBO assetRolloutBO;

    @Autowired
    private AssetIncomeBO assetIncomeBO;

    @Override
    public List<AssetCategoryCountDto> queryCountByIncomeId(Long incomeId, List<String> statusList) {
        ValidateUtils.notNull(incomeId);
        AssetRolloutIncomeDetailExtExample example = new AssetRolloutIncomeDetailExtExample();
        Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("n");
        criteria.andIncomeIdEqualTo(incomeId);
        criteria.andStatusIn(statusList);
        return assetRolloutIncomeDetailExtMapper.queryCountGroupByCategory(example);
    }

    @Override
    public List<AssetCategoryCountDto> queryCountByRolloutId(Long rolloutId,
                                                             AssetRolloutIncomeDetailStatusEnum status) {
        ValidateUtils.notNull(rolloutId);
        AssetRolloutIncomeDetailExtExample example = new AssetRolloutIncomeDetailExtExample();
        Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("n");
        criteria.andRolloutIdEqualTo(rolloutId);
        if (status != null) {
            criteria.andStatusEqualTo(status.getCode());
        }
        criteria.andStatusNotEqualTo(AssetRolloutIncomeDetailStatusEnum.CANCEL.getCode());
        return assetRolloutIncomeDetailExtMapper.queryCountGroupByCategory(example);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
    public void signAsset(Long id, String operator) {
        ValidateUtils.notNull(id);
        AssetRolloutIncomeDetail record = new AssetRolloutIncomeDetail();
        record.setId(id);
        record.setStatus(AssetRolloutIncomeDetailStatusEnum.HAS_SIGN.getCode());
        record.setOperatorTime(new Date());
        DomainUtils.beforeUpdate(record, operator);
        assetRolloutIncomeDetailMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Boolean isAllSignByRolloutId(Long rolloutId) {
        AssetRolloutIncomeDetailExample example = new AssetRolloutIncomeDetailExample();
        Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("n");
        criteria.andStatusEqualTo(AssetRolloutIncomeDetailStatusEnum.WAIT_SIGN.getCode());
        criteria.andRolloutIdEqualTo(rolloutId);
        List<AssetRolloutIncomeDetail> resList = assetRolloutIncomeDetailMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(resList)) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean isAllSignByIncomeId(Long incomeId) {
        AssetRolloutIncomeDetailExample example = new AssetRolloutIncomeDetailExample();
        Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("n");
        criteria.andStatusEqualTo(AssetRolloutIncomeDetailStatusEnum.WAIT_SIGN.getCode());
        criteria.andIncomeIdEqualTo(incomeId);
        List<AssetRolloutIncomeDetail> resList = assetRolloutIncomeDetailMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(resList)) {
            return true;
        }
        return false;
    }

    @Override
    public Long addDetail(AssetRolloutIncomeDetailDto param) {
        ValidateUtils.notNull(param);
        AssetRolloutIncomeDetail record = AssetRolloutIncomeDetailConverter.toAssetRolloutIncomeDetail(param);
        record.setOperatorTime(new Date());
        DomainUtils.beforeInsert(record, param.getOperator());
        assetRolloutIncomeDetailMapper.insert(record);
        return record.getId();
    }

    @Override
    public List<AssetRolloutIncomeDetail> queryListByRolloutId(Long rolloutId) {
        AssetRolloutIncomeDetailExample example = new AssetRolloutIncomeDetailExample();
        Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("n");
        criteria.andRolloutIdEqualTo(rolloutId);
        return assetRolloutIncomeDetailMapper.selectByExample(example);
    }

    @Override
    public AssetRolloutIncomeDetail queryWaitSignByAssetId(Long assetId) {
        ValidateUtils.notNull(assetId);
        AssetRolloutIncomeDetailExample example = new AssetRolloutIncomeDetailExample();
        Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("n");
        criteria.andStatusEqualTo(AssetRolloutIncomeDetailStatusEnum.WAIT_SIGN.getCode());
        criteria.andAssetIdEqualTo(assetId);
        List<AssetRolloutIncomeDetail> resList = assetRolloutIncomeDetailMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(resList)) {
            return null;
        }
        if (resList.size() > 1) {
            throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,
                "操作失败，当前资产多条数据，请核对资产信息！如有疑问，请联系资产管理员。");
        }
        return resList.get(0);
    }

    @Override
    public List<AssetRolloutIncomeDetail> queryWaitSignByAssetIdList(Long assetId) {
        ValidateUtils.notNull(assetId);
        AssetRolloutIncomeDetailExample example = new AssetRolloutIncomeDetailExample();
        Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("n");
        criteria.andStatusEqualTo(AssetRolloutIncomeDetailStatusEnum.WAIT_SIGN.getCode());
        criteria.andAssetIdEqualTo(assetId);
        List<AssetRolloutIncomeDetail> resList = assetRolloutIncomeDetailMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(resList)) {
            return null;
        }
        return resList;
    }

    @Override
    public AssetRolloutIncomeDetail cancel(Long assetId, String operator) {
        ValidateUtils.notNull(assetId);
        AssetRolloutIncomeDetail detail = queryWaitSignByAssetId(assetId);
        if (detail == null) {
            throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,
                "操作失败，当前资产不是待签收资产，请核对资产信息！如有疑问，请联系资产管理员。");
        }
        AssetRolloutIncomeDetail record = new AssetRolloutIncomeDetail();
        record.setStatus(AssetRolloutIncomeDetailStatusEnum.CANCEL.getCode());
        record.setOperatorTime(new Date());
        DomainUtils.beforeUpdate(record, operator);

        AssetRolloutIncomeDetailExample example = new AssetRolloutIncomeDetailExample();
        Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("n");
        criteria.andAssetIdEqualTo(assetId);
        criteria.andStatusEqualTo(AssetRolloutIncomeDetailStatusEnum.WAIT_SIGN.getCode());

        assetRolloutIncomeDetailMapper.updateByExampleSelective(record, example);
        return detail;

    }

    @Override
    public List<AssetRolloutIncomeDetail> queryListByIncomeId(Long incomeId) {
        AssetRolloutIncomeDetailExample example = new AssetRolloutIncomeDetailExample();
        example.createCriteria().andIsDeletedEqualTo("n").andIncomeIdEqualTo(incomeId);
        return assetRolloutIncomeDetailMapper.selectByExample(example);
    }

    @Override
    public Page<AssetRolloutIncomeDetailExtDto> queryPageByIncomeId(Long incomeId,
                                                                    AssetRolloutIncomeDetailStatusEnum status,
                                                                    int pageNum, int pageSize) {
        ValidateUtils.notNull(incomeId);
        AssetRolloutIncomeDetailExtExample example = new AssetRolloutIncomeDetailExtExample();
        example.setIncomeId(incomeId);

        if (status != null) {
            example.setDetailStatus(status.getCode());
        }
        PageHelper.startPage(pageNum, pageSize);
        return assetRolloutIncomeDetailExtMapper.queryFullDetailInfo(example);
    }

    @Override
    public Boolean hasCancelAssetByIncomeId(Long incomeId) {
        AssetRolloutIncomeDetailExample example = new AssetRolloutIncomeDetailExample();
        Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("n");
        criteria.andIncomeIdEqualTo(incomeId);
        criteria.andStatusEqualTo(AssetRolloutIncomeDetailStatusEnum.CANCEL.getCode());
        int i = assetRolloutIncomeDetailMapper.countByExample(example);
        if (i > 0) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public Page<AssetRolloutIncomeDetailExtDto> queryPageByRolloutId(Long rolloutId,
                                                                     AssetRolloutIncomeDetailStatusEnum status,
                                                                     int pageNum, int pageSize) {
        ValidateUtils.notNull(rolloutId);
        AssetRolloutIncomeDetailExtExample example = new AssetRolloutIncomeDetailExtExample();
        example.setRolloutId(rolloutId);
        if (status != null) {
            example.setDetailStatus(status.getCode());
        }
        PageHelper.startPage(pageNum, pageSize);
        return assetRolloutIncomeDetailExtMapper.queryFullDetailInfo(example);
    }

    @Override
    public Boolean hasCancelAssetByRolloutId(Long rolloutId) {
        AssetRolloutIncomeDetailExample example = new AssetRolloutIncomeDetailExample();
        Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("n");
        criteria.andRolloutIdEqualTo(rolloutId);
        criteria.andStatusEqualTo(AssetRolloutIncomeDetailStatusEnum.CANCEL.getCode());
        int i = assetRolloutIncomeDetailMapper.countByExample(example);
        if (i > 0) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public PageDto<AssetRolloutIncomeDetailDto> queryAssetRiDetailByPage(
        Long assetId, int pageNum, int pageSize) {
        ValidateUtils.notNull(assetId);
        AssetRolloutIncomeDetailExample example = new AssetRolloutIncomeDetailExample();
        Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("n");
        criteria.andAssetIdEqualTo(assetId);
        example.setOrderByClause("id desc");
        PageHelper.startPage(pageNum, pageSize);
        Page<AssetRolloutIncomeDetail> page = (Page<AssetRolloutIncomeDetail>)assetRolloutIncomeDetailMapper
            .selectByExample(example);
        List<AssetRolloutIncomeDetailDto> targetList = page.getResult().stream().map(
            source -> AssetRolloutIncomeDetailConverter.toAssetRolloutIncomeDetailDto(source)).collect(
            Collectors.toList());
        return PageDtoUtil.success(page, targetList);
    }

	@Override
	public void deleteWaitSignDetail(Long assetId,String operator) {
        ValidateUtils.notNull(assetId);
        AssetRolloutIncomeDetail detail = queryWaitSignByAssetId(assetId);
        if (detail == null) {
            throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,
                "操作失败，当前资产不是待签收资产，请核对资产信息！如有疑问，请联系资产管理员。");
        }
        AssetRolloutIncomeDetail record = new AssetRolloutIncomeDetail();
        record.setId(detail.getId());
        DomainUtils.beforeDelete(record, operator);
        assetRolloutIncomeDetailMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public void addIncomeIdByRolloutId(Long rolloutId, Long incomeId,String operator) {
		AssetRolloutIncomeDetailExample example = new AssetRolloutIncomeDetailExample();
        Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("n");
        criteria.andRolloutIdEqualTo(rolloutId);
        AssetRolloutIncomeDetail record = new AssetRolloutIncomeDetail();
        DomainUtils.beforeUpdate(record, operator);
        record.setIncomeId(incomeId);
        assetRolloutIncomeDetailMapper.updateByExampleSelective(record, example);
	}

	@Override
	public void signAssetByIncomeId(Long incomeId, String operator) {
		AssetRolloutIncomeDetailExample example = new AssetRolloutIncomeDetailExample();
		Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("n");
        criteria.andIncomeIdEqualTo(incomeId);
        criteria.andStatusEqualTo(AssetRolloutIncomeDetailStatusEnum.WAIT_SIGN.getCode());
        AssetRolloutIncomeDetail record = new AssetRolloutIncomeDetail();
        DomainUtils.beforeUpdate(record, operator);
        record.setStatus(AssetRolloutIncomeDetailStatusEnum.HAS_SIGN.getCode());
        assetRolloutIncomeDetailMapper.updateByExampleSelective(record, example);
		
	}

	@Override
	public void cancelByRolloutId(Long rolloutId,
		String operator) {
	 	ValidateUtils.notNull(rolloutId);
        AssetRolloutIncomeDetail record = new AssetRolloutIncomeDetail();
        record.setStatus(AssetRolloutIncomeDetailStatusEnum.CANCEL.getCode());
        record.setOperatorTime(new Date());
        DomainUtils.beforeUpdate(record, operator);

        AssetRolloutIncomeDetailExample example = new AssetRolloutIncomeDetailExample();
        Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("n");
        criteria.andRolloutIdEqualTo(rolloutId);

        assetRolloutIncomeDetailMapper.updateByExampleSelective(record, example);
	}

	@Override
	public List<AssetRolloutIncomeDetail> queryWaitSignListByIncomeId(
			Long incomeId) {
		 AssetRolloutIncomeDetailExample example = new AssetRolloutIncomeDetailExample();
	     example.createCriteria().andIsDeletedEqualTo("n").andIncomeIdEqualTo(incomeId).andStatusEqualTo(AssetRolloutIncomeDetailStatusEnum.WAIT_SIGN.getCode());
	     return assetRolloutIncomeDetailMapper.selectByExample(example);
	}
}
