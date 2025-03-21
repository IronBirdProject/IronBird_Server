package com.trip.IronBird_Server.config;


import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.cbor.MappingJackson2CborHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();

        //XML 처리용 메시지 컨버터 추가
//        List<HttpMessageConverter<?>> messageConverters1 = restTemplate.getMessageConverters();
//        messageConverters1.add(new MappingJackson2CborHttpMessageConverter(new XmlMapper()));

        // JSON 처리용 메시지 컨버터 추가
//        List<HttpMessageConverter<?>> messageConverters2 = restTemplate.getMessageConverters();
//        messageConverters2.removeIf(converter -> converter instanceof MappingJackson2CborHttpMessageConverter);
//        messageConverters2.add(new MappingJackson2JsonHttpMessageConverter());

        messageConverters.add(new FormHttpMessageConverter());
        restTemplate.setMessageConverters(messageConverters);
        return restTemplate;
    }
}
