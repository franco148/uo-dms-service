package com.umssonline.dms.core.dal.dao;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author franco.arratia@umssonline.com
 */
public class DocumentDao extends DmsSchema {

    //region Fields
    private InputStream inputStream;

    private Map<String, Object> customPropertiesMap = new HashMap<>();

    private String versionId = "";
    private boolean isRoot = false;
    //endregion

    //region Constructors
    public DocumentDao() {

    }
    //endregion

    //region Getters & Setters
    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public Map<String, Object> getCustomPropertiesMap() {
        return customPropertiesMap;
    }

    public void setCustomPropertiesMap(Map<String, Object> customPropertiesMap) {
        this.customPropertiesMap.putAll(customPropertiesMap);
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public void setIsRoot(boolean isRoot) {
        this.isRoot = isRoot;
    }
    //endregion
}
