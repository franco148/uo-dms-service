package com.umssonline.dms.core.config;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author franco.arratia@umssonline.com
 */
public class DmsConfiguration {
    private static final String CONFIG_FILE_NAME = "dms_config.yml";
    private static final String MAIN_CONFIG_PREFIX = "dms";
    //region Methods
    public Map<String, String> getConfigurationParameters() {
        Map< String, String> repositoryMap = null;
        Yaml yaml = new Yaml();

        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        try {
            InputStream resourceStream = loader.getResourceAsStream(CONFIG_FILE_NAME);
            Map< String, Object> result = (Map< String, Object>) yaml.load(resourceStream);
            repositoryMap = (Map<String, String>) result.get(MAIN_CONFIG_PREFIX);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return repositoryMap;
    }
    //endregion
}
