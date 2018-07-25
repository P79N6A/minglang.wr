package com.taobao.cun.auge.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.aliyun.oss.OSSClient;
import com.taobao.cun.crius.oss.client.FileStoreService;
import com.taobao.cun.crius.oss.client.OSSClientFactoryBean;
import com.taobao.cun.crius.oss.client.impl.FileStoreServiceImpl;

@Configuration
public class OssConfiguration {

    @Bean
    public OSSClientFactoryBean ossClient(@Value("${oss.key}") String key,
                                          @Value("${oss.secret}") String secret,
                                          @Value("${oss.endpoint}") String endpoint) throws Exception {
        OSSClientFactoryBean ossClient = new OSSClientFactoryBean();
        ossClient.setKey(key);
        ossClient.setEndpoint(endpoint);
        ossClient.setSecret(secret);
        ossClient.afterPropertiesSet();
        return ossClient;
    }

    @Bean
    public FileStoreService ossFileStoreService(@Value("${oss.bucketName}") String bucketName,
                                                    OSSClient ossClient) throws Exception {
        FileStoreServiceImpl ossFileStoreService = new FileStoreServiceImpl();
        ossFileStoreService.setBucketName(bucketName);
        ossFileStoreService.setOssClient(ossClient);
        return ossFileStoreService;
    }
}
