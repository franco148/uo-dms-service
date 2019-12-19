package com.umssonline.dms.core.dal.repository.impl;

import com.umssonline.dms.core.dal.dao.DocBinaryInfo;
import com.umssonline.dms.core.dal.jcrutils.QueryConstants;
import com.umssonline.dms.core.dal.repository.CoreMetadataManager;
import com.umssonline.dms.core.dal.repository.RepositoryManager;
import com.umssonline.dms.core.utils.constants.DMSConstants;
import com.umssonline.dms.core.utils.constants.SchemaConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.QueryManager;
import javax.jcr.version.Version;
import javax.jcr.version.VersionManager;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author franco.arratia@umssonline.com
 */
public class MetadataManagerImpl<T, E extends DocBinaryInfo, ID extends Serializable> implements CoreMetadataManager<T, E, ID> {

    //region Fields
    private Logger logger = LoggerFactory.getLogger(MetadataManagerImpl.class);
    private RepositoryManager repositoryManager = RepositoryManagerImpl.getInstance();
    private QueryManager queryManager;

    //private NodeManagerImpl nodeManager = NodeManagerImpl.getInstance();
    //endregion

    //region CoreMetadataManager members

    //region Operation with Documents

    @Override
    public T createMetadata(T metadata, E dataInfo) throws RepositoryException, IOException {
        logger.debug("MetadataManagerImpl: createMetadata(metadata, inputStream)");
        Session session = null;

        try {
            session = repositoryManager.login();
            VersionManager versionManager = repositoryManager.getVersionManager(session);

            //Create DMS information Node.
            Node dmsNode = NodeManagerImpl.addNodeInfo(metadata, session);
            logger.debug("The Path of DMS node info is: " + dmsNode.getPath());

            if (dmsNode != null) {
                //Add Binary file to DMS Node
                Node nodeBinary = NodeManagerImpl.addNodeBinary(dmsNode, dataInfo, session);
                logger.debug("The path of Binary Node is: " + nodeBinary.getPath());

                session.save();
                //Track Version
                logger.debug("DMS Node: " + dmsNode.getPath());

                versionManager.checkin(dmsNode.getPath());

            }
        } finally {
            repositoryManager.logout(session);

            if (dataInfo != null) {
                if (dataInfo.getInputStream() != null)
                    dataInfo.getInputStream().close();
            }
        }

        return metadata;
    }

    @Override
    public T updateMetadata(ID dmsId, T metadata, E dataInfo) throws RepositoryException, InstantiationException, IllegalAccessException {
        logger.debug("Updating information of a node with id: " + dmsId);

        if (dmsId == null) {
            logger.debug("dmsId parameter is null, this should have a value");
            return null;
        } else {
            Session session = null;

            try {
                session = repositoryManager.login();
                Node dmsNode = NodeManagerImpl.getNode(dmsId, session);

                VersionManager versionManager = repositoryManager.getVersionManager(session);
                versionManager.checkout(dmsNode.getPath()); // open to add new version

                //update new Version File Information
                dmsNode = NodeManagerImpl.addVersionInfo(metadata, dmsNode);

                //if dataInfo does not have the inputStream, it means only metadata was updated, so we need to add the same binary
                if (dataInfo.getInputStream() == null) {
                    Class<E> dataInfoClass = (Class<E>) dataInfo.getClass();
                    dataInfo = getDataInfo(dmsId, dataInfoClass);
                }

                //getting node version
                Node nodeVersion = dmsNode.getNode(DMSConstants.JCR_CONTENT);
                nodeVersion = NodeManagerImpl.addVersionBinary(nodeVersion, dataInfo, session);

                //save changes
                session.save();

                //Applying changes
                Version version = versionManager.checkin(dmsNode.getPath());
                String versionId = version.getIdentifier();
                //TODO: Add Version ID to the versionMetadata - Improve this logic
                NodeManagerImpl.setSchemaPropertyValue(SchemaConstants.PROPERTY_VERSION, versionId, dataInfo);

                //Node updatedNode = NodeManagerImpl.updateNodeInfo(metadata, metadataNode, session);
            } finally {
                repositoryManager.logout(session);
            }
        }

        return metadata;
    }

    @Override
    public List<T> getAllMetadata(Class<T> metadataClass) throws RepositoryException, IllegalAccessException, InstantiationException {

        List<T> responseMetadataList = new ArrayList<T>();

        Session session = null;

        try {
            session = repositoryManager.login();
            Iterable<Node> nodesCollection = NodeManagerImpl.getAllNodes(session);

            for (Node dataNode : nodesCollection) {
                T metadataInfo = metadataClass.newInstance();
                responseMetadataList.add(NodeManagerImpl.nodeToDto(metadataInfo, dataNode));
            }
        } finally {
            repositoryManager.logout(session);
        }

        return responseMetadataList;
    }

    @Override
    public List<T> getInclusiveBy(Map<String, Object> parameters, Class<T> metadataClass) throws RepositoryException, IllegalAccessException, InstantiationException {

        List<T> responseList = new ArrayList<T>();

        Session session = null;
        try {
            session = repositoryManager.login();
            Iterable<Node> nodesCollection = NodeManagerImpl.getByPropertiesInclusive(session, parameters);

            for (Node dataNode : nodesCollection) {
                T metadataInfo = metadataClass.newInstance();
                responseList.add(NodeManagerImpl.nodeToDto(metadataInfo, dataNode));
            }
        } finally {
            repositoryManager.logout(session);
        }

        return responseList;
    }

    @Override
    public T getMetadata(ID dmsId, Class<T> metadataClass) throws IllegalAccessException, InstantiationException, RepositoryException {
        logger.debug("Getting document metadata with id: " + dmsId);

        T metadataResponse = metadataClass.newInstance();

        if (dmsId == null) {
            logger.debug("dmsId parameter is null, this should have a value");
            return null;
        } else {

            Session session = null;

            try {
                session = repositoryManager.login();
                Node metadataNode = NodeManagerImpl.getNode(dmsId, session);
                metadataResponse = NodeManagerImpl.nodeToDto(metadataResponse, metadataNode);
            } finally {
                repositoryManager.logout(session);
            }
        }

        return metadataResponse;
    }

    @Override
    public E getDataInfo(ID dmsId, Class<E> dataInfoClass) throws IllegalAccessException, InstantiationException, RepositoryException {
        logger.debug("Getting document metadata with id: " + dmsId);

        E metadataResponse = dataInfoClass.newInstance();

        if (dmsId == null) {
            logger.debug("dmsId parameter is null, this should have a value");
            return null;
        } else {

            Session session = null;

            try {
                session = repositoryManager.login();
                Node metadataNode = NodeManagerImpl.getNode(dmsId, session);
                metadataResponse = NodeManagerImpl.nodeToDto(metadataResponse, metadataNode);
                Node binaryNode = NodeManagerImpl.getNodeBinaryContent(metadataNode);
                metadataResponse.setInputStream(NodeManagerImpl.getBinaryFromNode(binaryNode));
            } finally {
                repositoryManager.logout(session);
            }
        }

        return metadataResponse;
    }

    @Override
    public boolean removeDocument(ID dmsId) throws RepositoryException {

        if (dmsId == null) {
            logger.debug("dmsId parameter is null, the operation could not be completed.");
            return false;
        }
        boolean response;

        Session session = null;

        try {
            session = repositoryManager.login();
            Node dmsNode = NodeManagerImpl.getNode(dmsId, session);
            dmsNode.checkout();

            dmsNode.setProperty(QueryConstants.PARAMETER_NAME_IS_DELETED, QueryConstants.PARAMETER_VALUE_IS_DELETED);
            session.save();
            response = true;
        } finally {
            repositoryManager.logout(session);
        }

        return response;
    }

    @Override
    public boolean addOrEditProperty(ID dmsId, String propertyName, Object propertyValue, Class<T> metadataClass) throws RepositoryException, IllegalAccessException, InstantiationException {
        if (dmsId == null || propertyName == null || propertyValue == null) {
            logger.error("One required parameters are empty or null, operation can not be performed.");
            throw new RepositoryException("One required parameters are empty or null, operation can not be performed.");
        }

        boolean response;
        Session session = null;

        try {
            session = repositoryManager.login();
            T metadataSchema = metadataClass.newInstance();
            Node dmsNode = NodeManagerImpl.getNode(dmsId, session);
            dmsNode.checkout();
            NodeManagerImpl.addOrEditNodeProperty(dmsNode, metadataSchema, propertyName, propertyValue);

            session.save();
            response = true;
        } finally {
            repositoryManager.logout(session);
        }

        return response;
    }

    //endregion

    //region Operations with DocBinaryInfo versions
    @Override
    public T addVersion(ID dmsId, T versionMetadata, E versionDataInfo) throws RepositoryException, IllegalAccessException {

        if (dmsId == null) {
            logger.debug("dmsId parameter is null, addVersion operation can not be performed.");
            return null;
        } else {
            Session session = null;

            try {
                session = repositoryManager.login();
                Node dmsNode = NodeManagerImpl.getNode(dmsId, session);

                VersionManager versionManager = repositoryManager.getVersionManager(session);
                versionManager.checkout(dmsNode.getPath()); // open to add new version

                //update new Version File Information
                dmsNode = NodeManagerImpl.addVersionInfo(versionMetadata, dmsNode);

                //getting node version
                Node nodeVersion = dmsNode.getNode(DMSConstants.JCR_CONTENT);
                nodeVersion = NodeManagerImpl.addVersionBinary(nodeVersion, versionDataInfo, session);

                //save changes
                session.save();

                //Applying changes
                Version version = versionManager.checkin(dmsNode.getPath());
                String versionId = version.getIdentifier();
                //TODO: Add Version ID to the versionMetadata - Improve this logic
                NodeManagerImpl.setSchemaPropertyValue(SchemaConstants.PROPERTY_VERSION, versionId, versionMetadata);
            } finally {
                repositoryManager.logout(session);
            }
        }

        return versionMetadata;
    }

    @Override
    public T updateVersion(ID dmsId, ID versionId, T versionMetadata, E versionDataInfo) throws RepositoryException, InstantiationException, IllegalAccessException {

        if (dmsId == null || versionId == null) {
            logger.debug("dmsId or versionId parameter is null, updateVersion operation can not be performed.");
            return null;
        } else {
            Session session = null;

            try {
                session = repositoryManager.login();
                Node dmsNode = NodeManagerImpl.getNode(dmsId, session);

                VersionManager versionManager = repositoryManager.getVersionManager(session);
                versionManager.checkout(dmsNode.getPath()); // open to add new version

                //update new Version File Information
                dmsNode = NodeManagerImpl.addVersionInfo(versionMetadata, dmsNode);

                //if dataInfo does not have the inputStream, it means only metadata was updated, so we need to add the same binary
                if (versionDataInfo.getInputStream() == null) {
                    Class<E> dataInfoClass = (Class<E>) versionDataInfo.getClass();
                    versionDataInfo = getVersionDataInfo(dmsId, versionId, dataInfoClass);
                }

                //getting node version
                Node nodeVersion = dmsNode.getNode(DMSConstants.JCR_CONTENT);
                nodeVersion = NodeManagerImpl.addVersionBinary(nodeVersion, versionDataInfo, session);

                //save changes
                session.save();

                //Applying changes
                Version version = versionManager.checkin(dmsNode.getPath());
                //TODO: Add Version ID to the versionMetadata - Improve this logic
                NodeManagerImpl.setSchemaPropertyValue(SchemaConstants.PROPERTY_VERSION, versionId, versionMetadata);
            } finally {
                repositoryManager.logout(session);
            }
        }

        return versionMetadata;
    }

    @Override
    public E getVersionDataInfo(ID dmsId, ID versionId, Class<E> metadataClass) throws IllegalAccessException, InstantiationException, RepositoryException {
        logger.debug("Getting document data information with [id]: " + dmsId + " and [version id]: " + versionId);

        E docInfoResponse = metadataClass.newInstance();

        if (dmsId == null || versionId == null) {
            logger.debug("dmsId and versionId parameters can not be null");
            return null;
        } else {

            Session session = null;

            try {
                session = repositoryManager.login();
                VersionManager versionManager = repositoryManager.getVersionManager(session);

                Node metadataNode = NodeManagerImpl.getNode(dmsId, session);

                if (metadataNode.getIdentifier().equals(versionId)) {
                    docInfoResponse = NodeManagerImpl.nodeToDto(docInfoResponse, metadataNode);
                    docInfoResponse.setInputStream(NodeManagerImpl.getBinaryFromNode(metadataNode));
                } else {
                    Version binaryVersion = NodeManagerImpl.getVersionBinaryContent(session, versionManager, metadataNode, versionId);
                    Node versionNode = NodeManagerImpl.getNodeFromVersion(binaryVersion);
                    docInfoResponse = NodeManagerImpl.nodeToDto(docInfoResponse, versionNode);
                    docInfoResponse.setInputStream(NodeManagerImpl.getBinaryFromVersion(binaryVersion));
                }

            } finally {
                repositoryManager.logout(session);
            }
        }
        return docInfoResponse;
    }

    @Override
    public T getVersionMetadata(ID dmsId, ID versionId, Class<T> metadataClass) throws IllegalAccessException, InstantiationException, RepositoryException {
        logger.debug("Getting document data information with [id]: " + dmsId + " and [version id]: " + versionId);

        T versionMetadataResponse = metadataClass.newInstance();

        if (dmsId == null || versionId == null) {
            logger.debug("dmsId and versionId parameters can not be null");
            return null;
        } else {
            Session session = null;

            try {
                session = repositoryManager.login();
                VersionManager versionManager = repositoryManager.getVersionManager(session);

                Node metadataNode = NodeManagerImpl.getNode(dmsId, session);

                if (metadataNode.getIdentifier().equals(versionId)) {
                    versionMetadataResponse = NodeManagerImpl.nodeToDto(versionMetadataResponse, metadataNode);
                } else {
                    Version binaryVersion = NodeManagerImpl.getVersionBinaryContent(session, versionManager, metadataNode, versionId);
                    Node versionNode = NodeManagerImpl.getNodeFromVersion(binaryVersion);
                    versionMetadataResponse = NodeManagerImpl.nodeToDto(versionMetadataResponse, versionNode);
                }

            } finally {
                repositoryManager.logout(session);
            }
        }

        return versionMetadataResponse;
    }

    @Override
    public T getVersionData(ID dmsId, ID versionId, Class<T> metadataClass) throws IllegalAccessException, InstantiationException, RepositoryException {
        logger.debug("Getting document data information with [id]: " + dmsId + " and [version id]: " + versionId);

        T versionMetadataResponse = metadataClass.newInstance();

        if (dmsId == null || versionId == null) {
            logger.debug("dmsId and versionId parameters can not be null");
            return null;
        } else {
            Session session = null;

            try {
                session = repositoryManager.login();
                VersionManager versionManager = repositoryManager.getVersionManager(session);

                Node metadataNode = NodeManagerImpl.getNode(dmsId, session);

                if (metadataNode.getIdentifier().equals(versionId)) {
                    versionMetadataResponse = NodeManagerImpl.nodeToDto(versionMetadataResponse, metadataNode);
                    NodeManagerImpl.setSchemaPropertyValue(SchemaConstants.PROPERTY_VERSION, versionId, versionMetadataResponse);
                    //NodeManagerImpl.setSchemaPropertyValue(SchemaConstants.PROPERTY_BINARY, NodeManagerImpl.getBinaryFromNode(metadataNode), versionMetadataResponse);
                } else {
                    Version binaryVersion = NodeManagerImpl.getVersionBinaryContent(session, versionManager, metadataNode, versionId);
                    Node versionNode = NodeManagerImpl.getNodeFromVersion(binaryVersion);
                    versionMetadataResponse = NodeManagerImpl.nodeToDto(versionMetadataResponse, versionNode);
                    NodeManagerImpl.setSchemaPropertyValue(SchemaConstants.PROPERTY_VERSION, binaryVersion.getIdentifier(), versionMetadataResponse);
                    //NodeManagerImpl.setSchemaPropertyValue(SchemaConstants.PROPERTY_BINARY, NodeManagerImpl.getBinaryFromVersion(binaryVersion), versionMetadataResponse);
                }

            } finally {
                repositoryManager.logout(session);
            }
        }

        return versionMetadataResponse;
    }

    @Override
    public List<T> getAllVersionsMetadata(ID dmsId, Class<T> metadataClass) throws RepositoryException, InstantiationException, IllegalAccessException {
        List<T> responseVersionsList = new ArrayList<T>();

        Session session = null;
        try {
            session = repositoryManager.login();
            VersionManager versionManager = repositoryManager.getVersionManager(session);
            Node baseNode = NodeManagerImpl.getNode(dmsId, session);
            responseVersionsList.addAll(NodeManagerImpl.getAllVersionNodes(versionManager, baseNode, metadataClass));

        } finally {
            repositoryManager.logout(session);
        }

        return responseVersionsList;
    }

    @Override
    public boolean selectDefaultVersion(ID dmsId, ID versionId) throws RepositoryException {

        boolean response = false;
        Session session = null;
        try {
            session = repositoryManager.login();
            VersionManager versionManager = repositoryManager.getVersionManager(session);
            //Getting required node
            Node dmsNode = NodeManagerImpl.getNode(dmsId, session);
            response = NodeManagerImpl.changeDefaultVersion(session, versionManager, dmsNode, versionId);

        } finally {
            repositoryManager.logout(session);
        }

        return response;
    }

    @Override
    public boolean removeVersion(ID dmsId, ID versionId) throws RepositoryException {

        if (dmsId == null && versionId == null) {
            logger.debug("dmsId or versionId parameter is null, the operation could not be completed.");
            return false;
        }

        boolean response;

        Session session = null;

        try {
            session = repositoryManager.login();
            VersionManager versionManager = repositoryManager.getVersionManager(session);

            Node dmsNode = NodeManagerImpl.getNode(dmsId, session);

            NodeManagerImpl.removeVersion(versionManager, dmsNode, versionId);

            response = true;
        } finally {
            repositoryManager.logout(session);
        }

        return response;
    }
    //endregion

    //endregion
}
