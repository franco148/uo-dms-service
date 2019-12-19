package com.umssonline.dms.core.config;

/**
 * @author franco.arratia@umssonline.com
 */
public class ConfigurationFactory {

    //region Constructors
    private ConfigurationFactory() {

    }
    //endregion

    //region Methods
    public static DmsConfiguration getConfiguration() {

        return new DmsConfiguration();
    }
    //endregion
}
