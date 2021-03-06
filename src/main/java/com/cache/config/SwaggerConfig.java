package com.cache.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author liuqian
 * @date 2020-09-10 15:02
 */
@Configuration
@Profile({"!prod"})
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select().apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any()).build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 标题
                .title("本地缓存测试项目文档")
                // 简介
                .description("本地缓存的几种实现方式，仅供学习参考。")
                // 服务条款
                .termsOfServiceUrl("")
                // 联系人
                .contact(new Contact("lq920320", "http://github.com/lq920320", ""))
                // 版本
                .version("1.0").build();
    }
}
