package com.cdh.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by cdh
 * @description:Swagger2文档配置类
 * @Date: Created in 19:20 on 2021/4/18
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.cdh.server.controller"))
                .paths(PathSelectors.any())
                .build()
                .securityContexts(securityContexts())
                .securitySchemes(securitySchemes());
    }

    private List<SecurityContext> securityContexts() {
        List<SecurityContext> res = new ArrayList<>();
        // 设置需要登录认证的路径
        res.add(getContextByPath("/hello/*"));
        return res;
    }

    private SecurityContext getContextByPath(String pathRegex) {
        return SecurityContext.builder().securityReferences(defaultAuthPath())
                .forPaths(PathSelectors.regex(pathRegex))
                .build();
    }

    private List<SecurityReference> defaultAuthPath() {
        List<SecurityReference> res = new ArrayList<>();
        AuthorizationScope scope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] scopes = new AuthorizationScope[1];
        scopes[0] = scope;
        res.add(new SecurityReference("Authorization",scopes));
        return res;
    }

    private List<ApiKey> securitySchemes() {
        List<ApiKey> res = new ArrayList<>();
        // 设置请求头信息
        ApiKey apiKey = new ApiKey("Auth", "Authorization", "Header");
        res.add(apiKey);
        return res;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("云E办接口文档")
                .description("云E办接口文档")
                .contact(new Contact("cdh", "http://localhost:8081/doc.html","2361367057@qq.com"))
                .version("1.0")
                .build();
    }
}
