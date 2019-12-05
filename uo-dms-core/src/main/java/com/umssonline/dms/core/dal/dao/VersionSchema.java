package com.umssonline.dms.core.dal.dao;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author franco.arratia@umssonline.com
 */
public class VersionSchema extends DmsSchema {

    //region Fields
    private String versionId = "";
    private boolean isRoot = false;
    //endregion

    //region Constructors
    public VersionSchema() {

    }

    public VersionSchema(VersionSchema versionSchema) {
        this.copy(versionSchema);
    }
    //endregion

    //region Getters & Setters

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

    //region Methods
    public static DmsSchema build(Map<String, Object> properties) throws IllegalAccessException {
        VersionSchema responseSchema = new VersionSchema();

        Field[] parentFields = responseSchema.getClass().getSuperclass().getDeclaredFields();
        Field[] declaredFields = responseSchema.getClass().getDeclaredFields();

        assignValuesToFields(parentFields, properties, responseSchema);
        assignValuesToFields(declaredFields, properties, responseSchema);

        return responseSchema;
    }
    //endregion

    //region Utilities

    @Override
    protected void copy(DmsSchema entity) {
        super.copy(entity);

        if (entity != null && entity instanceof VersionSchema) {
            this.versionId = ((VersionSchema) entity).versionId;
            this.isRoot = ((VersionSchema) entity).isRoot;
        }
    }

    //endregion
}
