package com.taobao.cun.auge.fence.cainiao;

import com.taobao.cun.auge.dal.domain.FenceEntity;

/**
 * 调用菜鸟围栏接口
 * 
 * @author chengyu.zhoucy
 *
 */
public interface RailServiceAdapter {
	/**
	 * 添加菜鸟围栏
	 * @param fenceEntity
	 * @return
	 */
	Long addCainiaoFence(FenceEntity fenceEntity);

	/**
	 * 更新菜鸟围栏
	 * @param fenceEntity
	 */
	void updateCainiaoFence(FenceEntity fenceEntity);

	/**
	 * 删除菜鸟围栏
	 * @param cainiaoFenceId
	 */
	void deleteCainiaoFence(Long cainiaoFenceId);
	
	/**
	 * 更新菜鸟围栏状态
	 * @param fenceEntity
	 */
	void updateCainiaoFenceState(FenceEntity fenceEntity);

	/**
	 * 检查在菜鸟是否存在
	 * @param cainianFenceId
	 * @return
	 */
	boolean isExistsCainiaoFence(Long cainianFenceId);

	/**
	 * 获取菜鸟围栏
	 * @param cainianFenceId
	 * @return
	 */
	String getCainiaoFenceJSON(Long cainianFenceId);
}
