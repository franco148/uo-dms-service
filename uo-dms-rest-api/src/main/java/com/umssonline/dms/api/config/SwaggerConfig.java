package com.umssonline.dms.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

/**
 * @author franco.arratia@umssonline.com
 */

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    // CONTACT INFO
    private String contactName = "Franco Arratia";
    private String contactUrl = "www.franco-arratia.com";
    private String contactEmail = "franco.arratia@eextreme.com";

    // API INFO
    private String apiInfoTitle = "Umss Online - Document Management System Api Documentation";
    private String apiInfoDescription = "API Rest Docs for Document Management service - Umss Online Project";
    private String apiInfoVersion = "1.0";
    private String apiInfoTermsOfServices = "All rights reserved.";
    private String apiInfoLicense = "Apache 2.0";
    private String apiInfoLicenseUrl = "http://www.apache.org/licenses/LICENSE-2.0";

    @Bean
    public Docket api() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                   .select()
//                   .apis(RequestHandlerSelectors.any())
//                   .paths(PathSelectors.any())
//                   .build();
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.umssonline.dms.api.restws"))
                //.paths(PathSelectors.ant("/users/*"))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfo
                (
                    apiInfoTitle,
                    apiInfoDescription,
                    apiInfoVersion,
                    apiInfoTermsOfServices,
                    contact(),
                    apiInfoLicense,
                    apiInfoLicenseUrl
                );
    }

    private Contact contact() {
        return new Contact(contactName, contactUrl, contactEmail);
    }
}
