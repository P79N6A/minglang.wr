package com.taobao.cun.auge.alipay.process;

import com.alibaba.keycenter.client.extension.alipay.KeyCenterNewAlipayClient;
import com.alibaba.keycenter.client.extension.alipay.internal.security.KeyCenterAlipaySignChecker;
import com.alibaba.keycenter.client.properties.KeyCenterProperties;
import com.alipay.api.AlipayClient;
import com.taobao.common.keycenter.keystore.KeyStoreImpl;
import com.taobao.common.keycenter.security.Signer;
import com.taobao.common.keycenter.security.SignerImpl;
import com.taobao.common.keycenter.security.Verifier;
import com.taobao.common.keycenter.security.VerifierImpl;
import com.taobao.cun.auge.alipay.constants.AliPayConstants;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service("alipayClientProcessor")
public class AlipayClientProcessor {

    private static Signer signer;
    private static Verifier verifier;
    private static KeyCenterAlipaySignChecker signChecker;


    @PostConstruct
    public void init() {
            // 正式环境

            KeyCenterProperties properties = new KeyCenterProperties();
            properties.setAppPublishNum(AliPayConstants.KEYCENTER_NUM_ONLINE);
            properties.setPreferProtocal("http");
            properties.setHttpServiceAddress(AliPayConstants.KEYCENTER_ADDRESS_ONLINE);

            KeyStoreImpl keyStore = new KeyStoreImpl();
            keyStore.setKeyCenterProperties(properties);
            keyStore.init();

            signer = new SignerImpl(keyStore);
            verifier = new VerifierImpl(keyStore);
            signChecker = new KeyCenterAlipaySignChecker(verifier, AliPayConstants.CUNTAO_ALIPAY_PUBLIC_KEY_ONLINE);


    }

    public static AlipayClient getAlipayClient() {

            KeyCenterNewAlipayClient keyCenterNewAlipayClient = new KeyCenterNewAlipayClient(
                    AliPayConstants.OPENAPI_GATEWAY_ONLINE, AliPayConstants.CUNTAO_APP_ID_ONLINE, AliPayConstants.FORMAT,
                    AliPayConstants.CHARSET, AliPayConstants.SIGN_TYPE);

            // Signer: 集团应用方用于生成签名, 所以需要用到应用私钥(由KC平台生成)
            keyCenterNewAlipayClient.setSigner(signer, AliPayConstants.CUNTAO_APP_PRIVATE_KEY_ONLINE);

            // SignChecker: 集团应用方用于校验支付宝回调时携带的签名, 所以需要用到支付宝的公钥(从支付宝平台获得)
            keyCenterNewAlipayClient.setSignChecker(verifier, AliPayConstants.CUNTAO_ALIPAY_PUBLIC_KEY_ONLINE);

            return keyCenterNewAlipayClient;
    }
}

