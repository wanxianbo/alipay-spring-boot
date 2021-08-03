package com.example.alipay.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.FileCopyUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;


@Configuration
@EnableConfigurationProperties(AliPayBean.class)
public class AliConfig {

    // 暂时不用
//    @Value("${custom.http.proxyHost}")
//    private String proxyHost;
//    @Value("${custom.http.proxyPort}")
//    private int proxyPort;
    @Value("${spring.profiles.active}")
    private String activeEnv;

    private static final String PROD_ENV ="prod";

    private static final String STAGE_ENV ="stage";

    private static final String TEST_ENV ="test";

    private final AliPayBean aliPayBean;

    public AliConfig(AliPayBean aliPayBean) {
        this.aliPayBean = aliPayBean;
    }

    @Bean(name = {"alipayClient"})
    public AlipayClient alipayClientService() throws Exception {
        CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
        //设置网关地址
        certAlipayRequest.setServerUrl(aliPayBean.getServerUrl());
        //设置应用Id
        certAlipayRequest.setAppId(aliPayBean.getAppId());
        //设置应用私钥
        certAlipayRequest.setPrivateKey(aliPayBean.getPrivateKey());
        //设置请求格式，固定值json
        certAlipayRequest.setFormat(aliPayBean.getFormat());
        //设置字符集
        certAlipayRequest.setCharset(aliPayBean.getCharset());
        //设置签名类型
        certAlipayRequest.setSignType(aliPayBean.getSignType());
        //如果是生产环境或者预演环境，则使用代理模式
        if (PROD_ENV.equals(activeEnv) || STAGE_ENV.equals(activeEnv) || TEST_ENV.equals(activeEnv)) {
            //设置应用公钥证书路径
            certAlipayRequest.setCertContent(getCertContentByPath(aliPayBean.getAppCertPath()));
            //设置支付宝公钥证书路径
            certAlipayRequest.setAlipayPublicCertContent(getCertContentByPath(aliPayBean.getAlipayCertPath()));
            //设置支付宝根证书路径
            certAlipayRequest.setRootCertContent(getCertContentByPath(aliPayBean.getAlipayRootCertPath()));
//            certAlipayRequest.setProxyHost(proxyHost);
//            certAlipayRequest.setProxyPort(proxyPort);

        } else {
            //local
            String serverPath = Objects.requireNonNull(this.getClass().getResource("/")).getPath();
            //设置应用公钥证书路径
            certAlipayRequest.setCertPath(serverPath + aliPayBean.getAppCertPath());
            //设置支付宝公钥证书路径
            certAlipayRequest.setAlipayPublicCertPath(serverPath + aliPayBean.getAlipayCertPath());
            //设置支付宝根证书路径
            certAlipayRequest.setRootCertPath(serverPath + aliPayBean.getAlipayRootCertPath());
        }
        return new DefaultAlipayClient(certAlipayRequest);
    }

    private String getCertContentByPath(String name) {
        InputStream inputStream = null;
        String content = null;
        try {
            inputStream = this.getClass().getClassLoader().getResourceAsStream(name);
            content = new String(FileCopyUtils.copyToByteArray(inputStream), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

}