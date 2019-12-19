package com.umssonline.dms.core.dal.dao;

import java.io.InputStream;

/**
 * @author franco.arratia@umssonline.com
 */
public class Thumbnail {

    //region Fields
    private InputStream image;
    private String mimeType;
    //endregion

    //region Getters & Setters
    public InputStream getImage() {
        return image;
    }

    public void setImage(InputStream image) {
        this.image = image;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
    //endregion
}
