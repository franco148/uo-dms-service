package com.umssonline.dms.api.service.api;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author franco.arratia@umssonline.com
 */
public interface CoreServiceAdapter {

    //region Document Operations
    Object getAllMetadata() throws RepositoryException, IllegalAccessException, IOException;
    Object get(String identifier) throws RepositoryException, IllegalAccessException, IOException;
    Object getMetadata(String identifier) throws RepositoryException, IllegalAccessException, IOException;
    Object getInclusiveBy(String queryParameters) throws RepositoryException, IllegalAccessException, IOException;
    Object upload(FormDataContentDisposition fileDetail, InputStream binary, HttpServletRequest servletRequest, String jsonParameters) throws RepositoryException, IllegalAccessException, IOException;
    Object update(String fileId, InputStream fileStream, FormDataBodyPart documentSchema) throws RepositoryException, IllegalAccessException, IOException;
    Object delete(String identifier) throws RepositoryException, IllegalAccessException, IOException;
    Object editProperty(String identifier, String propertyName, FormDataBodyPart propertyValue) throws RepositoryException, IllegalAccessException, IOException;
    //endregion

    //region Document Version Operations
    Object uploadVersion(String parentId, String fileName, String fileTitle, String fileDescription, InputStream fileStream, long fileSize) throws RepositoryException, IllegalAccessException, IOException;
    Object updateVersion(String parentId, String versionId, InputStream fileStream, FormDataBodyPart versionSchema) throws RepositoryException, IllegalAccessException, IOException;
    Object getVersion(String fileId, String versionId) throws RepositoryException, IllegalAccessException, IOException;
    Object getVersionMetadata(String fileId, String versionId);
    Object getVersionData(String fileId, String versionId) throws RepositoryException, IllegalAccessException, IOException;
    Object getAllVersions(String fileId) throws RepositoryException, IllegalAccessException, IOException;
    Object selectVersion(String fileId, String versionId) throws RepositoryException, IllegalAccessException, IOException;
    Object deleteVersion(String fileId, String versionId) throws RepositoryException, IllegalAccessException, IOException;
    //endregion
}
