package com.umssonline.dms.core.dal.repository.impl;

import com.umssonline.dms.core.dal.dao.DocBinaryInfo;
import com.umssonline.dms.core.dal.jcrutils.QueryConstants;
import com.umssonline.dms.core.utils.common.DMSUtil;
import com.umssonline.dms.core.utils.constants.DMSConstants;
import com.umssonline.dms.core.utils.constants.SchemaConstants;
import com.google.gson.Gson;
import javafx.util.Pair;
import org.apache.jackrabbit.commons.JcrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.*;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.jcr.version.Version;
import javax.jcr.version.VersionHistory;
import javax.jcr.version.VersionIterator;
import javax.jcr.version.VersionManager;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author franco.arratia@umssonline.com
 */
public class NodeManagerImpl {

    //region Fields
    private static Logger logger = LoggerFactory.getLogger(NodeManagerImpl.class);
    private static NodeManagerImpl instance = null;
    //endregion

    //region Initializers
    private NodeManagerImpl() {

    }

    public static NodeManagerImpl getInstance() {

        if (instance == null) {
            instance = new NodeManagerImpl();
        }

        return instance;
    }
    //endregion

    //region Methods
    public static <T> Node addNodeInfo(T schema, Session session) throws RepositoryException {

        Set<Field> fieldsToAnalyze = getObjectInstanceFields(schema);

        Node rootNode = getActualNode(session);
        Node dmsNode = null;

        Field idPropertyField = fieldsToAnalyze.stream().filter(field -> SchemaConstants.PROPERTY_ID.equals(field.getName()))
                                                        .findAny()
                                                        .orElse(null);

        if (idPropertyField != null) {

            try {

                idPropertyField.setAccessible(true);
                dmsNode = rootNode.addNode(idPropertyField.get(schema).toString());
                idPropertyField.setAccessible(false);

                setNodeInformation(dmsNode, schema, fieldsToAnalyze);

            } catch (IllegalAccessException e) {
                throw new RepositoryException("Properties could not be added: " + e.getMessage());
            }

            dmsNode.addMixin(DMSConstants.MIX_VERSIONABLE);
            dmsNode.addMixin(DMSConstants.MIX_LOCKABLE);
            dmsNode.setProperty(DMSConstants.JCR_LAST_MODIFIED_BY, "admin");
            dmsNode.setProperty(DMSConstants.DMS_DELETE_ATTRIBUTE, "N");
        }

        return dmsNode;
    }

    public static <T> Node addVersionInfo(T versionSchema, Node nodeVersion) throws RepositoryException {
        Set<Field> schemaFields = getObjectInstanceFields(versionSchema);

        try {

            setNodeInformation(nodeVersion, versionSchema, schemaFields);

        } catch (IllegalAccessException e) {
            throw new RepositoryException("Properties could not be added: " + e.getMessage());
        }

        return nodeVersion;
    }

    public static <T> Node updateNodeInfo(T schema, Node nodeToUpdate, Session session) throws RepositoryException {
        Set<Field> schemaFields = getObjectInstanceFields(schema);

        try {

            InputStream binaryContent = null;
            String mimeType = null;

            for (Field field : schemaFields) {

                field.setAccessible(true);

                String propertyName = field.getName();
                if (nodeToUpdate.hasProperty(propertyName)) {
                    Object fieldValue = field.get(schema);
                    setPropertyValue(field, propertyName, fieldValue, schema, nodeToUpdate);
                }

                if (propertyName.equals(SchemaConstants.PROPERTY_BINARY))
                    binaryContent = (InputStream) field.get(schema);

                if (propertyName.equals(SchemaConstants.PROPERTY_MIME_TYPE))
                    mimeType = field.get(schema).toString();

                field.setAccessible(false);
            }

            Node binaryNode = getNodeBinaryContent(nodeToUpdate);
            if (binaryNode != null && binaryContent != null && mimeType != null) {
                Binary binary = session.getValueFactory().createBinary(binaryContent);
                binaryNode.setProperty(DMSConstants.JCR_DATA, binary);
                binaryNode.setProperty(DMSConstants.JCR_MIME_TYPE, mimeType);
            }
        } catch (IllegalAccessException e) {
            throw new RepositoryException("Properties could not be updated: " + e.getMessage());
        }

        return nodeToUpdate;
    }

    public static <T> void addOrEditNodeProperty(Node node, T schema, String propertyName, Object propertyValue) throws RepositoryException {

        Set<Field> fieldsToAnalyze = getObjectInstanceFields(schema);
        Field fieldToEdit = fieldsToAnalyze.stream().filter(field -> propertyName.equals(field.getName()))
                                                    .findAny()
                                                    .orElse(null);

        if (fieldToEdit != null) {
            if (fieldToEdit.getType().isAssignableFrom(Map.class)) {
                Property fieldProperty = node.getProperty(propertyName);
                Gson gson = new Gson();
                Map<String, Object> mapProperty = gson.fromJson(fieldProperty.getValue().getString(), Map.class);
                Pair<String, Object> pair = (Pair<String, Object>) propertyValue;
                mapProperty.put(pair.getKey(), pair.getValue());

                Iterator mapIterator = mapProperty.entrySet().iterator();

                while (mapIterator.hasNext()) {
                    Map.Entry mapEntry = (Map.Entry) mapIterator.next();
                    String property = mapEntry.getKey().toString();
                    String propertyVal = mapEntry.getValue().toString();

                    logger.info("Editing property: " + property + " with value: " + propertyVal);
                    node.setProperty(property, propertyVal);
                }

                String jsonProperty = gson.toJson(mapProperty);
                node.setProperty(propertyName, jsonProperty);
            }
        } else {
            throw new RepositoryException("Property: " + propertyName + " does not exists. Operation can not be performed.");
        }
    }

    public static <T extends DocBinaryInfo> Node addNodeBinary(Node node, T dataInfo, Session session) throws RepositoryException {

        Node binaryNode = node.addNode(DMSConstants.JCR_CONTENT, DMSConstants.NT_RESOURCE);
        binaryNode.setProperty(DMSConstants.JCR_ENCODING, DMSConstants.UTF_8);

        Binary binary = session.getValueFactory().createBinary(dataInfo.getInputStream());
        binaryNode.setProperty(DMSConstants.JCR_DATA, binary);

        String mimeType = DMSUtil.getMimeTypeByFileName(dataInfo.getName());
        binaryNode.setProperty(DMSConstants.JCR_MIME_TYPE, mimeType);
        logger.debug("mimeType of Input stream" + mimeType);

        session.save();
        logger.debug("DocBinaryInfo binary was added to the node successfully...");

        return binaryNode;
    }

    public static <T extends DocBinaryInfo> Node addVersionBinary(Node node, T versionInfo, Session session) throws RepositoryException {

        Binary binary = session.getValueFactory().createBinary(versionInfo.getInputStream());
        node.setProperty(DMSConstants.JCR_DATA, binary);

        String mimeType = DMSUtil.getMimeTypeByFileName(versionInfo.getName());
        node.setProperty(DMSConstants.JCR_MIME_TYPE, mimeType);
        return node;
    }

    public static Iterable<Node> getAllNodes(Session session) throws RepositoryException {

        String queryExpression = String.format(QueryConstants.SELECT_ALL_QUERY,
                                               DMSConstants.NT_UNSTRUCTURED,
                                               QueryConstants.PARAMETER_NAME_IS_DELETED,
                                               QueryConstants.PARAMETER_VALUE_IS_NOT_DELETED);

        logger.debug("Query expression: " + queryExpression);

        QueryManager queryManager = RepositoryManagerImpl.getInstance().getQueryManager(session);
        Query query = queryManager.createQuery(queryExpression, DMSConstants.DMS_CREATE_QUERY_CTTE);
        QueryResult queryResult = query.execute();

        return JcrUtils.getNodes(queryResult);
    }

    public static Iterable<Node> getByPropertiesInclusive(Session session, Map<String, Object> queryParameters) throws RepositoryException {

        StringBuilder queryBuilder = new StringBuilder(String.format(QueryConstants.SELECT_QUERY, DMSConstants.NT_UNSTRUCTURED));
        int counter = 0;

        Iterator paramsIterator = queryParameters.entrySet().iterator();
        while (paramsIterator.hasNext()) {
            String queryString;
            Map.Entry entry = (Map.Entry)paramsIterator.next();
            String propertyName = entry.getKey().toString();
            String propertyValue = entry.getValue().toString();

            if (counter == 0) {
                queryString = String.format(QueryConstants.ADD_QUERY_CONDITION, propertyName, propertyValue);
                counter++;
            } else {
                queryString = String.format(QueryConstants.ADD_QUERY_AND_CONDITION, propertyName, propertyValue);
            }

            queryBuilder.append(queryString);
        }

        String andIsNotDeleted = String.format(QueryConstants.ADD_QUERY_AND_CONDITION, QueryConstants.PARAMETER_NAME_IS_DELETED, QueryConstants.PARAMETER_VALUE_IS_NOT_DELETED);
        queryBuilder.append(andIsNotDeleted);

        logger.debug("Query expression: " + queryBuilder.toString());

        QueryManager queryManager = RepositoryManagerImpl.getInstance().getQueryManager(session);
        Query query = queryManager.createQuery(queryBuilder.toString(), DMSConstants.DMS_CREATE_QUERY_CTTE);
        QueryResult queryResult = query.execute();

        return JcrUtils.getNodes(queryResult);
    }

    public static <T> java.util.Collection<T> getAllVersionNodes(VersionManager versionManager, Node node, Class<T> metadataClass) throws RepositoryException, IllegalAccessException, InstantiationException {
        List<T> versionsList = new ArrayList<>();

        if (versionManager != null && node != null) {
            VersionHistory versionHistory = versionManager.getVersionHistory(node.getPath());
            String baseVersionId = versionManager.getBaseVersion(node.getPath()).getIdentifier();
            VersionIterator versionIterator = versionHistory.getAllVersions();

            while (versionIterator.hasNext()) {

                //retrieving version
                Version version = versionIterator.nextVersion();
                NodeIterator nodeIterator = version.getNodes();

                //exploring node version
                while (nodeIterator.hasNext()) {
                    Node versionNode = nodeIterator.nextNode();

                    if (versionNode.hasProperty(SchemaConstants.PROPERTY_ID)) {
                        T dtoInstance = metadataClass.newInstance();
                        dtoInstance = nodeToDto(dtoInstance, versionNode);

                        String currentVersionId = version.getIdentifier();
                        setSchemaPropertyValue(SchemaConstants.PROPERTY_VERSION, currentVersionId, dtoInstance);
                        //Set flag to isSelectedVersion = true
                        if (baseVersionId.equals(currentVersionId)) {
                            setSchemaPropertyValue(SchemaConstants.PROPERTY_IS_ROOT, true, dtoInstance);
                        }
                        versionsList.add(dtoInstance);
                    }
                }
                
            }
        }

        return versionsList;
    }

    public static <ID extends Serializable> Node getNode(ID dmsId, Session session) throws RepositoryException {
        String queryExpression = String.format(QueryConstants.SELECT_NODE_QUERY,
                                               DMSConstants.NT_UNSTRUCTURED,
                                               SchemaConstants.PROPERTY_ID,
                                               dmsId);

        logger.debug("Query expression: " + queryExpression);

        QueryManager queryManager = RepositoryManagerImpl.getInstance().getQueryManager(session);
        Query query = queryManager.createQuery(queryExpression, DMSConstants.DMS_CREATE_QUERY_CTTE);
        query.setLimit(1L);
        QueryResult queryResult = query.execute();

        NodeIterator nodeIterator = queryResult.getNodes();
        session.save();

        Node response = null;
        if (nodeIterator != null && nodeIterator.hasNext()) {
            response = nodeIterator.nextNode();
        }

        return response;
    }

    public static Node getNodeBinaryContent(Node dmsNode) throws RepositoryException {
        Node binaryNode = null;

        PropertyIterator propertyIterator = dmsNode.getProperties();
        while (propertyIterator.hasNext()) {
            Property property = propertyIterator.nextProperty();
            if (property.getName().equalsIgnoreCase(SchemaConstants.PROPERTY_ID)) {

                if (dmsNode.hasNode(DMSConstants.JCR_CONTENT)) {
                    binaryNode = dmsNode.getNode(DMSConstants.JCR_CONTENT);
                    break;
                }
            }
        }

        return binaryNode;
    }

    public static <ID extends Serializable> Version getVersionBinaryContent(Session session, VersionManager versionManager, Node node, ID versionId) throws RepositoryException {

        if (session != null && versionManager !=null && node != null && versionId != null) {
            logger.debug("Base of the version node is: " + versionManager.getBaseVersion(node.getPath()).getIdentifier());

            VersionHistory versionHistory = versionManager.getVersionHistory(node.getPath());
            VersionIterator versionIterator = versionHistory.getAllVersions();

            while (versionIterator.hasNext()) {
                Version version = versionIterator.nextVersion();

                if (version.getIdentifier().equalsIgnoreCase(versionId.toString())) {
                    return version;
                }
            }
        }

        return null;
    }

    public static <ID extends Serializable> void removeVersion(VersionManager versionManager, Node baseNode, ID versionId) throws RepositoryException {
        if (versionManager != null && baseNode != null && versionId != null) {

            String baseVersionId = versionManager.getBaseVersion(baseNode.getPath()).getIdentifier();

            if (baseVersionId.equalsIgnoreCase(versionId.toString()))
                throw new RepositoryException("Document selected as default can not be removed.");

            VersionHistory versionHistory = versionManager.getVersionHistory(baseNode.getPath());
            VersionIterator versionIterator = versionHistory.getAllVersions();

            while (versionIterator.hasNext()) {
                Version version = versionIterator.nextVersion();

                if (versionId.toString().equalsIgnoreCase(version.getIdentifier())) {
                    versionHistory.removeVersion(version.getName());
                    return;
                }
            }
        } else {
            throw new UnsupportedOperationException("Remove version operation can not be performed due to invalid parameters.");
        }
    }

    public static Node getNodeFromVersion(Version version) throws RepositoryException {
        NodeIterator versionNodes = version.getNodes();
        while (versionNodes.hasNext()) {
            Node versionNode = versionNodes.nextNode();
            if (versionNode.hasProperty(SchemaConstants.PROPERTY_ID)) {
                return versionNode;
            }
        }

        return null;
    }

    public static <T> T nodeToDto(T dtoInstance, Node nodeInfo) throws RepositoryException {

        Set<Field> dtoInstanceFields = getObjectInstanceFields(dtoInstance);

        try {

            for (Field field : dtoInstanceFields) {
                field.setAccessible(true);

                String propertyName = field.getName();
                if (nodeInfo.hasProperty(propertyName)) {
                    if (field.getType().isAssignableFrom(String.class)) {
                        Property fieldProperty = nodeInfo.getProperty(propertyName);
                        field.set(dtoInstance, fieldProperty.getValue().getString());
                    } else if (field.getType().isAssignableFrom(Date.class)) {
                        Property fieldProperty = nodeInfo.getProperty(propertyName);
                        field.set(dtoInstance, fieldProperty.getValue().getDate().getTime());
                    } else if (field.getType().isAssignableFrom(Long.class)) {
                        Property fieldProperty = nodeInfo.getProperty(propertyName);
                        field.set(dtoInstance, fieldProperty.getValue().getLong());
                    } else if (field.getType().isAssignableFrom(Double.class)) {
                        Property fieldProperty = nodeInfo.getProperty(propertyName);
                        field.set(dtoInstance, fieldProperty.getValue().getDouble());
                    } else if (field.getType().isAssignableFrom(Map.class)) {
                        Property fieldProperty = nodeInfo.getProperty(propertyName);
                        Gson gson = new Gson();
                        Map mapProperty = gson.fromJson(fieldProperty.getValue().getString(), Map.class);
                        field.set(dtoInstance, mapProperty);
                    } else {
                        //TODO: Complete this behavior with more functionality.
                    }
                }

                field.setAccessible(false);
            }
        } catch (IllegalAccessException e) {
            throw new RepositoryException("Properties could not be added: " + e.getMessage());
        }

        return dtoInstance;
    }

    public static InputStream getBinaryFromNode(Node binaryNode) throws RepositoryException {
        Property binaryContent = binaryNode.getProperty(DMSConstants.JCR_DATA);
        return binaryContent.getBinary().getStream();
    }

    public static InputStream getBinaryFromVersion(Version versionHistory) throws RepositoryException {

        InputStream binaryResponse = null;
        NodeIterator nodeIterator = versionHistory.getNodes();

        while (nodeIterator.hasNext()) {
            Node node = nodeIterator.nextNode();

            if (node.hasNode(DMSConstants.JCR_CONTENT)) {
                Node binaryNode = node.getNode(DMSConstants.JCR_CONTENT);

                if (binaryNode.hasProperty(DMSConstants.JCR_DATA)) {
                    Property property = binaryNode.getProperty(DMSConstants.JCR_DATA);
                    binaryResponse = property.getBinary().getStream();
                }
            }
        }

        return binaryResponse;
    }

    public static <ID extends Serializable> boolean changeDefaultVersion(Session session, VersionManager versionManager, Node node, ID versionId) throws RepositoryException {

        if (session != null && versionManager != null && node != null && versionId != null) {
            VersionHistory versionHistory = versionManager.getVersionHistory(node.getPath());
            VersionIterator versionIterator = versionHistory.getAllVersions();

            while (versionIterator.hasNext()) {
                Version version = versionIterator.nextVersion();
                String currentVersionId = version.getIdentifier();

                if (currentVersionId.equals(versionId)) {
                    versionManager.restore(version, false);
                    session.save();
                    return true;
                }
            }
        }

        return false;
    }
    //endregion

    //region Utilities
    private static Node getActualNode(Session session) throws RepositoryException {
        Node rootNode = session.getRootNode();
        return rootNode;
    }

    private static <T> Node setPropertyValue(Field field, String propertyName, Object value, T objectSchema, Node node) throws IllegalAccessException, RepositoryException {

        if (value instanceof String) {
            node.setProperty(propertyName, field.get(objectSchema).toString());
        } else if (value instanceof Date) {
            Date createdTime = (Date)field.get(objectSchema);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(createdTime);
            node.setProperty(propertyName, calendar);
        } else if(value instanceof Integer) {
            node.setProperty(propertyName, field.getInt(objectSchema));
        } else if (value instanceof Long) {
            node.setProperty(propertyName, (Long)field.get(objectSchema));
        } else if (value instanceof Double) {
            node.setProperty(propertyName, (Double) field.get(objectSchema));
        } else if (value instanceof Map) {
            Map customPropertiesMap = (Map) field.get(objectSchema);
            Iterator mapIterator = customPropertiesMap.entrySet().iterator();

            while (mapIterator.hasNext()) {
                Map.Entry mapEntry = (Map.Entry) mapIterator.next();
                String property = mapEntry.getKey().toString();
                String propertyValue = mapEntry.getValue().toString();

                node.setProperty(property, propertyValue);
            }

            Gson gson = new Gson();
            String jsonProperty = gson.toJson(customPropertiesMap);
            node.setProperty(propertyName, jsonProperty);

        } else {
            //TODO: Add more functionality based on new types
        }
        return node;
    }

    public static  <T> void setSchemaPropertyValue(String propertyName, Object propertyValue, T objectSchema) throws IllegalAccessException {
        Set<Field> objectSchemaFields = getObjectInstanceFields(objectSchema);

        for (Field field : objectSchemaFields) {
            String fieldName = field.getName();

            if (fieldName.equals(propertyName)) {
                field.setAccessible(true);
                if (propertyValue instanceof Boolean) {
                    boolean value = (boolean)propertyValue;
                    field.set(objectSchema, value);
                } else if (propertyValue instanceof String) {
                    String value = (String)propertyValue;
                    field.set(objectSchema, value);
                } else if (propertyValue instanceof InputStream) {
                    InputStream value = (InputStream)propertyValue;
                    field.set(objectSchema, value);
                }
                field.setAccessible(false);

                break;
            }
        }
    }

    private static <T> Set<Field> getObjectInstanceFields(T objectInstance) {
        Set<Field> listOfFields = new HashSet<>();

        if (objectInstance.getClass().getSuperclass() != null) {
            Field[] parentFields = objectInstance.getClass().getSuperclass().getDeclaredFields();
            listOfFields.addAll(Arrays.asList(parentFields));
        }

        Field[] objectFields = objectInstance.getClass().getDeclaredFields();
        listOfFields.addAll(Arrays.asList(objectFields));

        return listOfFields;
    }

    private static <T> void setNodeInformation(Node node, T schema, Set<Field> objectSchemaFields) throws RepositoryException, IllegalAccessException {
        for (Field field : objectSchemaFields) {

            field.setAccessible(true);

            String propertyName = field.getName();
            Object fieldValue = field.get(schema);
            setPropertyValue(field, propertyName, fieldValue, schema, node);

            field.setAccessible(false);
        }
    }

    //endregion
}
