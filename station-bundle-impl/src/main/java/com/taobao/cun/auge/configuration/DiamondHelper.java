package com.taobao.cun.auge.configuration;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.taobao.diamond.client.Diamond;
import com.taobao.diamond.manager.ManagerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * Created by xiao on 18/10/30.
 */
public class DiamondHelper {

    private static final Logger logger = LoggerFactory.getLogger(DiamondHelper.class);

    private static ConcurrentMap<String, String> data = new ConcurrentHashMap<>(4);

    private static ConcurrentMap<String, ManagerListener> listeners = Maps.newConcurrentMap();

    /**
     * 获取配置内容
     * @param groupId
     * @param dataId
     * @return
     */
    public static String getConfig(String groupId, String dataId) {
        String key = dataId + '_' + groupId;
        if (data.containsKey(key)) {
            return data.get(key);
        }
        ManagerListener listener = new ManagerListener() {
            @Override
            public Executor getExecutor() {
                return null;
            }

            @Override
            public void receiveConfigInfo(String configInfo) {
                data.put(key, Strings.nullToEmpty(configInfo));
            }
        };
        Diamond.addListener(dataId, groupId, listener);
        listeners.put(key, listener);
        return getConfigFromDiamond(groupId, dataId);
    }

    /**
     * 从diaomnd获取配置
     * @param groupId
     * @param dataId
     * @return
     */
    private static String getConfigFromDiamond(String groupId, String dataId) {
        try {
            String key = dataId + '_' + groupId;
            String configInfo = Strings.nullToEmpty(Diamond.getConfig(dataId, groupId, 3000L));
            data.put(key, StringUtils.isEmpty(configInfo) ? "" : configInfo);
            return configInfo;
        } catch (IOException e) {
            logger.error("DiamondError get config from diamond {groupId}|{dataId}", groupId, dataId);
            return "";
        }
    }

    /**
     * 移除配置
     *
     * @param groupId
     * @param dataId
     */
    public static void remove(final String groupId, final String dataId) {
        final String key = dataId + '_' + groupId;
        Diamond.removeListener(dataId, groupId, listeners.get(key));
        data.remove(key);
        listeners.remove(key);
        Diamond.remove(dataId, groupId);
    }

}
