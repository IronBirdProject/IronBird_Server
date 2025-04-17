package com.trip.IronBird_Server.config;


import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.cbor.MappingJackson2CborHttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        //List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();

        //XML 처리용 메시지 컨버터 추가
        messageConverters.add(new MappingJackson2XmlHttpMessageConverter(new XmlMapper()));


        messageConverters.add(new FormHttpMessageConverter());
        restTemplate.setMessageConverters(messageConverters);


        return restTemplate;
    }

}
