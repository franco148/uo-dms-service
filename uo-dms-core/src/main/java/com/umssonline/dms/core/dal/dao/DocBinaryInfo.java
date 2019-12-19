package com.umssonline.dms.core.dal.dao;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author franco.arratia@umssonline.com
 */
public class DocBinaryInfo {

    //region Fields

    private InputStream inputStream;
    private String mimeType;
    private String name;
    //endregion

    //region Constructors
    public DocBinaryInfo() {

    }
    //endregion

    //region Getters & Setters
    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //endregion

    //region Methods
    public static DocBinaryInfo build(Map<String, Object> properties) throws IllegalAccessException {
        DocBinaryInfo responseBinary = new DocBinaryInfo();

        Field[] fields = responseBinary.getClass().getDeclaredFields();

        for (Field field : fields) {
            String propertyName = field.getName();

            if (properties.containsKey(propertyName)) {
                field.set(responseBinary, properties.get(propertyName));
            }
        }

        return responseBinary;
    }
    //endregion
}
