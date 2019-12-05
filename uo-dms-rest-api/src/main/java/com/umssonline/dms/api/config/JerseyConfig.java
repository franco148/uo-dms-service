package com.umssonline.dms.api.config;

import com.umssonline.dms.api.restws.impl.FileResourceImpl;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author franco.arratia@umssonline.com
 */
@Configuration
public class JerseyConfig extends ResourceConfig {

    @Value("${spring.jersey.application-path:/}")
    private String apiPath;

    @PostConstruct
    public void init() {

        // Register components where DI is needed
        this.configureSwagger();
    }

    public JerseyConfig() {
        register(CORSResponseFilter.class);
        register(MultiPartFeature.class);
        register(FileResourceImpl.class);
    }

    private void configureSwagger() {

        // Available at localhost:port/swagger.json
        //this.register(ApiListingResource.class);
        //this.register(SwaggerSerializers.class);
        /*BeanConfig config = new BeanConfig();
        config.setTitle("DMS Service API");
        config.setVersion("v1");
        config.setContact("Digital Harbor");
        config.setSchemes(new String[] { "http", "https" });
        config.setBasePath(this.apiPath);
        config.setResourcePackage("com.dharbor.dms.resources");
        config.setPrettyPrint(true);
        config.setScan(true);*/
    }
}
