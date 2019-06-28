package com.taobao.cun.auge.fence.bo;

import com.github.pagehelper.PageHelper;
import com.taobao.cun.auge.dal.domain.FenceEntityExample;
import com.taobao.cun.auge.dal.mapper.FenceEntityMapper;

import javax.annotation.Resource;

public class FenceChecker {
    @Resource
    FenceEntityMapper fenceEntityMapper;

    void check(){
        FenceEntityExample example = new FenceEntityExample();
        example.createCriteria().andIsDeletedEqualTo("n");

        PageHelper.startPage(1, 200); // 每次查询20条
        fenceEntityMapper.selectByExample(example);
    }
}
