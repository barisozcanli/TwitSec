package com.peace.twitsec.config;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.models.dto.ApiInfo;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableSwagger
public class SwaggerConfig {

    @Value("${app.version}")
    private String appVersion;

    @Autowired
    private SpringSwaggerConfig springSwaggerConfig;

    @Bean
    public SwaggerSpringMvcPlugin customImplementation() {
        return new SwaggerSpringMvcPlugin(this.springSwaggerConfig)
                .apiInfo(apiInfo())
                .includePatterns("/"+appVersion+"/.*")
                .swaggerGroup(appVersion);
    }

    private ApiInfo apiInfo() {
        return new ApiInfo("TwitSec API","TwitSec REST API","http://", "info@twitsec.com",null,null);
    }
}
