package com.roberthalf.kibana.configuration;

import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Getter
@Configuration
@ConfigurationProperties
@EnableConfigurationProperties
public class KibanaConfiguration {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        return builder.build();
    }

    //prod kibana
    //@Value("${kibana_url_source:http://__localhost:8090/api/saved_objects/_export}")
    //String srcUrl;

    //prod kibana
    @Value("${kibana_url_source}")
    String kibanaSrcUrl;

    //dev kibana 
    @Value("${kibana_url_dest}")
    String kibanaDestUrl;


}
