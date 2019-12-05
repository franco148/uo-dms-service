package com.umssonline.dms.core.dal.dao;

import com.umssonline.dms.core.utils.constants.SchemaConstants;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author franco.arratia@umssonline.com
 */
public class DocumentSchema extends DmsSchema {

    //region Fields

    private Map<String, Object> customPropertiesMap = new HashMap<>();

    //endregion

    //region Constructors
    public DocumentSchema() {
    }

    public DocumentSchema(DocumentSchema documentSchema) {
        this.copy(documentSchema);
    }
    //endregion

    //region Getters & Setters

    public Map<String, Object> getCustomPropertiesMap() {
        return customPropertiesMap;
    }

    public void setCustomPropertiesMap(Map<String, Object> customPropertiesMap) {
        this.customPropertiesMap.putAll(customPropertiesMap);
    }
    //endregion

    //region Methods
    public static DmsSchema build(Map<String, Object> properties) throws IllegalAccessException {
        DocumentSchema schemaResponse = new DocumentSchema();

        Field[] parentFields = schemaResponse.getClass().getSuperclass().getDeclaredFields();
        Field[] declaredFields = schemaResponse.getClass().getDeclaredFields();

        List<String> propertyExclusions = new ArrayList<>();
        propertyExclusions.add(SchemaConstants.PROPERTY_BINARY);

        assignValuesToFields(parentFields, properties, schemaResponse);
        assignValuesToFields(declaredFields, properties, schemaResponse);

        Map<String, Object> customProperties = loadCustomProperties(Arrays.asList(parentFields), properties, propertyExclusions);
        schemaResponse.setCustomPropertiesMap(customProperties);

        return schemaResponse;
    }
    //endregion

    //region Utilities
    @Override
    protected void copy(DmsSchema entity) {
        super.copy(entity);

        if (entity != null && entity instanceof DocumentSchema) {
            this.setCustomPropertiesMap(((DocumentSchema) entity).getCustomPropertiesMap());
        }
    }

    //private void assignValuesToFields(Field[] fields, DmsSchema)
    //endregion
}
