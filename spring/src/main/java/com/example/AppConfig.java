package com.example;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

@Configuration
public class AppConfig {

//    @Bean
//    public ObjectMapper jackson2ObjectMapperBuilder(){
//        Jackson2ObjectMapperBuilder builder = Jackson2ObjectMapperBuilder.json()
//                .featuresToEnable(INDENT_OUTPUT)
//                .featuresToDisable(WRITE_DATES_AS_TIMESTAMPS);
//
//        return builder.build();
//    }


}
