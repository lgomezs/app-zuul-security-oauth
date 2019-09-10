package com.example.zuul.appsecurityzuul.gateway.client;


import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@FeignClient(name = "OAuthServer",
    url = "${security.checktoken.oauth.endpoint}",
    configuration = OAuthClient.OAuthClientConfig.class)
public interface OAuthClient {

    @PostMapping(value = "${security.checktoken.oauth.path}",
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
        headers = {
            "Content-Type=application/x-www-form-urlencoded",
            "authorization=Basic ${security.checktoken.oauth.header.authorization}"
        })
    void checkToken(Map<String, String> token);


    class OAuthClientConfig {
        @Bean
        public Encoder encoder(ObjectFactory<HttpMessageConverters> converters) {
            return new SpringFormEncoder(new SpringEncoder(converters));
        }
    }
}
