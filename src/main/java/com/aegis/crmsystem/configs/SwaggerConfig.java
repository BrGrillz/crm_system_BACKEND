package com.aegis.crmsystem.configs;

import com.aegis.crmsystem.constants.SwaggerConst;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static springfox.documentation.builders.PathSelectors.regex;

@Slf4j
@EnableSwagger2
@Configuration
public class SwaggerConfig {

//    @Bean
//    public Docket api() {
//        List<SecurityScheme> schemeList = new ArrayList<>();
//        schemeList.add(new ApiKey(HttpHeaders.AUTHORIZATION, "JWT", "header"));
//        return new Docket(DocumentationType.SWAGGER_2)
//                .produces(Collections.singleton("application/json"))
//                .consumes(Collections.singleton("application/json"))
//                .ignoredParameterTypes(Authentication.class)
//                .securitySchemes(schemeList)
//                .useDefaultResponseMessages(false)
//                .select()
//                .paths(PathSelectors.any())
//                .build()
//                .securityContexts(Arrays.asList(actuatorSecurityContext()))
//                .securitySchemes(Arrays.asList(basicAuthScheme()));
//    }

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(SwaggerConst.PATH))
                .paths(regex(SwaggerConst.PATH_REGEX))
                .build()
                .apiInfo(metaData())
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()));
    }

    private ApiInfo metaData() {
        return new ApiInfoBuilder()
                .title(SwaggerConst.MetaData.TITLE)
                .description(SwaggerConst.MetaData.DESCRIPTION)
                .version(SwaggerConst.MetaData.VERSION)
                .license(SwaggerConst.MetaData.LICENCE)
                .licenseUrl(SwaggerConst.MetaData.LICENCE_URL)
                .contact(SwaggerConst.MetaData.CONTACT)
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("/api/v1/tasks.*|/api/v1/admin.*"))
                .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");

        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;

        return Arrays.asList(
                new SecurityReference("JWT", authorizationScopes));
    }

//    @Bean
//    public Docket actuatorApi() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .groupName("Spring Actuator")
//                .select()
//                .apis(RequestHandlerSelectors.any())
//                .paths(PathSelectors.ant("/api/**"))
//                .build()
//                .securityContexts(Arrays.asList(actuatorSecurityContext()));
////                .securitySchemes(Arrays.asList(basicAuthScheme()));
//    }
//
//    private SecurityContext actuatorSecurityContext() {
//        return SecurityContext.builder()
//                .securityReferences(Arrays.asList(basicAuthReference()))
//                .forPaths(PathSelectors.ant("/api/**"))
//                .build();
//    }
//
//    private SecurityScheme basicAuthScheme() {
//        return new BasicAuth("basicAuth");
//    }
//
//    private SecurityReference basicAuthReference() {
//        return new SecurityReference("basicAuth", new AuthorizationScope[0]);
//    }
}
