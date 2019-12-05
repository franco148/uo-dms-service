package com.umssonline.dms.core.dal.dao;

import com.umssonline.dms.core.utils.common.DMSUtil;
import com.umssonline.dms.core.utils.common.DmsIdGenerator;
import org.apache.commons.io.FilenameUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author franco.arratia@umssonline.com
 */
public class DmsSchema {

    //region Fields
    protected String id = DmsIdGenerator.createDmsUniqueId();
    protected String title = "";
    protected String name = "";
    protected String mimeType = "";
    protected String description = "";
    protected Date createdDate = Calendar.getInstance().getTime();
    protected Date modifiedDate = Calendar.getInstance().getTime();
    protected String createdBy = "";
    protected String modifiedBy = "";
    protected Long version = 1L;
    protected String extension = "";
    protected Long size = 0L;
    //private boolean editable;
    //private Thumbnail thumbnail;
    //private Set<String> labels ----- starred, hidden, trashed, restricted, viewed

    //endregion

    //region Constructors
    public DmsSchema() {

    }

    public DmsSchema(DmsSchema dmsSchema) {
        this.copy(dmsSchema);
    }
    //endregion

    //region Getters & Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = FilenameUtils.getBaseName(name);
        this.extension = FilenameUtils.getExtension(name);
        this.mimeType = DMSUtil.getMimeTypeByFileName(name);
        //this.mimeType = Files.probeContentType(name);
        //this.mimeType = MIMETypeUtil
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long fileSize) {
        this.size = fileSize;
    }
    //endregion

    //region Utilities
    protected void copy(DmsSchema entity) {

        if (entity != null) {
            this.id = entity.id;
            this.title = entity.title;
            this.name = entity.name;
            this.mimeType = entity.mimeType;
            this.description = entity.description;
            this.createdDate = entity.createdDate;
            this.modifiedDate = entity.modifiedDate;
            this.createdBy = entity.createdBy;
            this.modifiedBy = entity.modifiedBy;
            this.version = entity.version;
            this.extension = entity.extension;
            this.size = entity.size;
        }
    }

    public static DmsSchema build(Map<String, Object> properties) throws IllegalAccessException {
        DmsSchema responseSchema = new DmsSchema();

        Field[] fields = responseSchema.getClass().getDeclaredFields();

        assignValuesToFields(fields, properties, responseSchema);

        return responseSchema;
    }

    public static Map<String, Object> loadCustomProperties(List<Field> fields, Map<String, Object> properties, List<String> propertyExclusions) {
        Map<String, Object> propertiesResponse = new HashMap<>();

        for (Map.Entry<String, Object> property : properties.entrySet()) {

            String propertyName = property.getKey();
            if (!propertyExclusions.contains(propertyName) && !existsInList(fields, propertyName)) {
                propertiesResponse.put(propertyName, property.getValue());
            }
        }
        
        return propertiesResponse;
    }

    public static void assignValuesToFields(Field[] fields, Map<String, Object> propertyValues, DmsSchema dmsSchema) throws IllegalAccessException {

        for (Field field : fields) {
            String propertyName = field.getName();

            if (propertyValues.containsKey(propertyName)) {
                field.set(dmsSchema, propertyValues.get(propertyName));
            }
        }
    }
    
    private static boolean existsInList(List<Field> fields, String propertyName) {
        boolean existsProperty = fields.stream().anyMatch(f -> f.getName().equals(propertyName));
        return existsProperty;
    }
    //endregion
}
