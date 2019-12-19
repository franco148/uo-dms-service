package com.umssonline.dms.api.service.impl;

import com.umssonline.dms.api.service.api.CoreServiceAdapter;
import com.google.gson.Gson;
import com.umssonline.dms.core.dal.dao.DmsSchema;
import com.umssonline.dms.core.dal.dao.DocumentSchema;
import com.umssonline.dms.core.dal.dao.VersionSchema;
import com.umssonline.dms.core.facade.DmsCoreFacade;
import com.umssonline.dms.core.facade.dto.RepositoryDTO;
import com.umssonline.dms.core.handler.factory.ActionHandlerOptions;
import com.umssonline.dms.core.utils.constants.DMSConstants;
import javafx.util.Pair;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author franco.arratia@umssonline.com
 */
public class SetJcrCoreAdapter implements CoreServiceAdapter {

    //region CoreServiceAdapter Members

    //region Document Operations
    @Override
    public Object getAllMetadata() throws RepositoryException, IllegalAccessException, IOException {
        RepositoryDTO repository = new RepositoryDTO();
        repository.setActionType(ActionHandlerOptions.DMS_ALL_SCHEMES);
        return DmsCoreFacade.getInstance().executeAction(repository);
    }

    @Override
    public Object get(String identifier) throws RepositoryException, IllegalAccessException, IOException {
        Map<String, Object> fileProperties = new HashMap<>();
        fileProperties.put(DMSConstants.DMS_ID, identifier);

        RepositoryDTO repository = new RepositoryDTO();
        repository.setParams(fileProperties);
        repository.setActionType(ActionHandlerOptions.DMS_DOWNLOAD);
        return DmsCoreFacade.getInstance().executeAction(repository);
    }

    @Override
    public Object getMetadata(String identifier) throws RepositoryException, IllegalAccessException, IOException {
        Map<String, Object> fileProperties = new HashMap<>();
        fileProperties.put(DMSConstants.DMS_ID, identifier);

        RepositoryDTO repository = new RepositoryDTO();
        repository.setParams(fileProperties);
        repository.setActionType(ActionHandlerOptions.DMS_SCHEMA);

        return DmsCoreFacade.getInstance().executeAction(repository);
    }

    @Override
    public Object getInclusiveBy(String queryParameters) throws RepositoryException, IllegalAccessException, IOException {
        Gson gsonConverter = new Gson();
        Map<String, Object> queryParams = gsonConverter.fromJson(queryParameters, Map.class);

        RepositoryDTO repository = new RepositoryDTO();
        repository.setActionType(ActionHandlerOptions.DMS_INCLUSIVE_QUERY);
        repository.setParams(queryParams);

        return DmsCoreFacade.getInstance().executeAction(repository);
    }

    @Override
    public Object upload(FormDataContentDisposition fileDetail, InputStream binary, HttpServletRequest servletRequest,
                         String jsonParameters) throws RepositoryException, IllegalAccessException, IOException {

        if (servletRequest == null) {
            return null;
        }

        Map<String, Object> parameters = new HashMap<>();
        DmsCoreFacade dmsCoreFacade = DmsCoreFacade.getInstance();
        Gson gsonConverter = new Gson();
        Map<String, Object> parametersMap = gsonConverter.fromJson(jsonParameters, Map.class);

        DmsSchema dmsSchema = new DocumentSchema();
        dmsSchema.setName(fileDetail.getFileName());
        //dmsSchema.setTitle(fileTitle);
        //dmsSchema.setDescription(fileDescription);
        dmsSchema.setSize((long)servletRequest.getContentLength());

        //TODO: Improve this block of code in order not to have hardcoded values
        parameters.put("title", dmsSchema.getTitle());
        parameters.put("name", dmsSchema.getName());
        parameters.put("mimeType", dmsSchema.getMimeType());
        parameters.put("description", dmsSchema.getDescription());
        parameters.put("createdBy", dmsSchema.getCreatedBy());
        parameters.put("modifiedBy", dmsSchema.getModifiedBy());
        parameters.put("version", 1L);
        parameters.put("extension", dmsSchema.getExtension());
        parameters.put("size", dmsSchema.getSize());
        parameters.putAll(parametersMap);

        RepositoryDTO repository = new RepositoryDTO();
        repository.setActionType(ActionHandlerOptions.DMS_UPLOAD);
        repository.setParams(parameters);
        repository.setInputStream(binary);

        return dmsCoreFacade.executeAction(repository);
    }

    @Override
    public Object update(String fileId, InputStream fileStream, FormDataBodyPart documentSchema) throws RepositoryException,
                                                                                                    IllegalAccessException,
                                                                                                    IOException {

        Map<String, Object> parameters = new HashMap<>();
        documentSchema.setMediaType(MediaType.APPLICATION_JSON_TYPE);
        DocumentSchema dmsSchema = documentSchema.getValueAs(DocumentSchema.class);

        //TODO: Improve this block of code in order not to have hardcoded values
        parameters.put("title", dmsSchema.getTitle());
        parameters.put("name", dmsSchema.getName());
        parameters.put("mimeType", dmsSchema.getMimeType());
        parameters.put("description", dmsSchema.getDescription());
        parameters.put("createdBy", dmsSchema.getCreatedBy());
        parameters.put("modifiedBy", dmsSchema.getModifiedBy());
        parameters.put("version", 1L);
        parameters.put("extension", dmsSchema.getExtension());
        parameters.put("size", dmsSchema.getSize());

        for (Map.Entry<String, Object> entry : dmsSchema.getCustomPropertiesMap().entrySet()) {
            parameters.put(entry.getKey(), entry.getValue());
        }

        parameters.put(DMSConstants.DMS_ID, fileId);

        RepositoryDTO repository = new RepositoryDTO();
        repository.setActionType(ActionHandlerOptions.DMS_UPDATE);
        repository.setParams(parameters);
        repository.setInputStream(fileStream);

        return DmsCoreFacade.getInstance().executeAction(repository);
    }

    @Override
    public Object delete(String identifier) throws RepositoryException, IllegalAccessException, IOException {
        Map<String, Object> fileProperties = new HashMap<>();
        fileProperties.put(DMSConstants.DMS_ID, identifier);

        RepositoryDTO repository = new RepositoryDTO();
        repository.setParams(fileProperties);
        repository.setActionType(ActionHandlerOptions.DMS_REMOVE);
        return DmsCoreFacade.getInstance().executeAction(repository);
    }

    @Override
    public Object editProperty(String identifier, String propertyName, FormDataBodyPart propertyValue) throws RepositoryException, IllegalAccessException, IOException {
        Map<String, Object> parameters = new HashMap<>();
        propertyValue.setMediaType(MediaType.APPLICATION_JSON_TYPE);
        Map<String, Object> deserializedValue = propertyValue.getValueAs(Map.class);
        Map.Entry<String, Object> firstElement = deserializedValue.entrySet().iterator().next();
        Pair<String, Object> valuePair = new Pair<>(firstElement.getKey(), firstElement.getValue());

        parameters.put(DMSConstants.DMS_ID, identifier);
        parameters.put(DMSConstants.DMS_PROPERTY_NAME, propertyName);
        parameters.put(DMSConstants.DMS_PROPERTY_VALUE, valuePair);

        RepositoryDTO repository = new RepositoryDTO();
        repository.setActionType(ActionHandlerOptions.DMS_EDIT_PROPERTY);
        repository.setParams(parameters);

        return DmsCoreFacade.getInstance().executeAction(repository);
    }

    //endregion

    //region Document Version Operations
    @Override
    public Object uploadVersion(String parentId, String fileName, String fileTitle, String fileDescription, InputStream fileStream, long fileSize) throws RepositoryException, IllegalAccessException, IOException {
        Map<String, Object> parameters = new HashMap<>();

        DmsSchema dmsSchema = new VersionSchema();
        dmsSchema.setName(fileName);
        dmsSchema.setTitle(fileTitle);
        dmsSchema.setDescription(fileDescription);
        dmsSchema.setSize(fileSize);

        //TODO: Improve this block of code in order not to have hardcoded values
        parameters.put("title", dmsSchema.getTitle());
        parameters.put("name", dmsSchema.getName());
        parameters.put("mimeType", dmsSchema.getMimeType());
        parameters.put("description", dmsSchema.getDescription());
        parameters.put("createdBy", dmsSchema.getCreatedBy());
        parameters.put("modifiedBy", dmsSchema.getModifiedBy());
        parameters.put("version", 1L);
        parameters.put("extension", dmsSchema.getExtension());
        parameters.put("size", dmsSchema.getSize());
        parameters.put(DMSConstants.DMS_ID, parentId);

        RepositoryDTO repository = new RepositoryDTO();
        repository.setActionType(ActionHandlerOptions.DMS_UPLOAD_VERSION);
        repository.setParams(parameters);
        repository.setInputStream(fileStream);

        return DmsCoreFacade.getInstance().executeAction(repository);
    }

    @Override
    public Object updateVersion(String parentId, String versionId, InputStream fileStream, FormDataBodyPart versionSchema) throws RepositoryException, IllegalAccessException, IOException {
        Map<String, Object> parameters = new HashMap<>();
        versionSchema.setMediaType(MediaType.APPLICATION_JSON_TYPE);
        VersionSchema dmsSchema = versionSchema.getValueAs(VersionSchema.class);

        //TODO: Improve this block of code in order not to have hardcoded values
        parameters.put("title", dmsSchema.getTitle());
        parameters.put("name", dmsSchema.getName());
        parameters.put("mimeType", dmsSchema.getMimeType());
        parameters.put("description", dmsSchema.getDescription());
        parameters.put("createdBy", dmsSchema.getCreatedBy());
        parameters.put("modifiedBy", dmsSchema.getModifiedBy());
        parameters.put("version", 1L);
        parameters.put("extension", dmsSchema.getExtension());
        parameters.put("size", dmsSchema.getSize());
        parameters.put(DMSConstants.DMS_ID, parentId);
        parameters.put(DMSConstants.DMS_VERSION_ID, versionId);

        RepositoryDTO repository = new RepositoryDTO();
        repository.setActionType(ActionHandlerOptions.DMS_UPDATE_VERSION);
        repository.setParams(parameters);
        repository.setInputStream(fileStream);

        return DmsCoreFacade.getInstance().executeAction(repository);
    }

    @Override
    public Object getVersion(String fileId, String versionId) throws RepositoryException, IllegalAccessException, IOException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(DMSConstants.DMS_ID, fileId);
        parameters.put(DMSConstants.DMS_VERSION_ID, versionId);

        RepositoryDTO repository = new RepositoryDTO();
        repository.setParams(parameters);
        repository.setActionType(ActionHandlerOptions.DMS_DOWNLOAD_VERSION);

        return DmsCoreFacade.getInstance().executeAction(repository);
    }

    @Override
    public Object getVersionMetadata(String fileId, String versionId) {
        return null;
    }

    @Override
    public Object getVersionData(String fileId, String versionId) throws RepositoryException, IllegalAccessException, IOException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(DMSConstants.DMS_ID, fileId);
        parameters.put(DMSConstants.DMS_VERSION_ID, versionId);

        RepositoryDTO repository = new RepositoryDTO();
        repository.setParams(parameters);
        repository.setActionType(ActionHandlerOptions.DMS_DOWNLOAD_VERSION_DATA);

        return DmsCoreFacade.getInstance().executeAction(repository);
    }

    @Override
    public Object getAllVersions(String fileId) throws RepositoryException, IllegalAccessException, IOException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(DMSConstants.DMS_ID, fileId);

        RepositoryDTO repository = new RepositoryDTO();
        repository.setParams(parameters);
        repository.setActionType(ActionHandlerOptions.DMS_ALL_VERSIONS);

        return DmsCoreFacade.getInstance().executeAction(repository);
    }

    @Override
    public Integer countFileVersions(String fileId, boolean includeRoot) throws RepositoryException, IllegalAccessException, IOException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(DMSConstants.DMS_ID, fileId);

        RepositoryDTO repository = new RepositoryDTO();
        repository.setParams(parameters);
        repository.setActionType(ActionHandlerOptions.DMS_ALL_VERSIONS);

        List<VersionSchema> versionsResponse = (List<VersionSchema>)DmsCoreFacade.getInstance().executeAction(repository);

        return includeRoot ? versionsResponse.size() : versionsResponse.size() - 1;
    }

    @Override
    public Object selectVersion(String fileId, String versionId) throws RepositoryException, IllegalAccessException, IOException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(DMSConstants.DMS_ID, fileId);
        parameters.put(DMSConstants.DMS_VERSION_ID, versionId);

        RepositoryDTO repository = new RepositoryDTO();
        repository.setParams(parameters);
        repository.setActionType(ActionHandlerOptions.DMS_SELECT_VERSION);

        return DmsCoreFacade.getInstance().executeAction(repository);
    }

    @Override
    public Object deleteVersion(String fileId, String versionId) throws RepositoryException, IllegalAccessException, IOException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(DMSConstants.DMS_ID, fileId);
        parameters.put(DMSConstants.DMS_VERSION_ID, versionId);

        RepositoryDTO repository = new RepositoryDTO();
        repository.setParams(parameters);
        repository.setActionType(ActionHandlerOptions.DMS_REMOVE_VERSION);

        return DmsCoreFacade.getInstance().executeAction(repository);
    }
    //endregion

    //endregion
}
