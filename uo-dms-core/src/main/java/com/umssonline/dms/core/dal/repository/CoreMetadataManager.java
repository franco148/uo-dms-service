package com.umssonline.dms.core.dal.repository;

import com.umssonline.dms.core.dal.dao.DocBinaryInfo;

import javax.jcr.RepositoryException;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author franco.arratia@umssonline.com
 */
public interface CoreMetadataManager<T, E extends DocBinaryInfo, ID extends Serializable> {

    //region Operations with Documents
    T createMetadata(T metadata, E dataInfo) throws RepositoryException, IOException;
    T updateMetadata(ID dmsId, T metadata, E dataInfo) throws RepositoryException, InstantiationException, IllegalAccessException;
    List<T> getAllMetadata(Class<T> metadataClass) throws RepositoryException, IllegalAccessException, InstantiationException;
    List<T> getInclusiveBy(Map<String, Object> parameters, Class<T> metadataClass) throws RepositoryException, IllegalAccessException, InstantiationException;
    T getMetadata(ID dmsId, Class<T> metadataClass) throws IllegalAccessException, InstantiationException, RepositoryException;
    E getDataInfo(ID dmsId, Class<E> dataInfoClass) throws IllegalAccessException, InstantiationException, RepositoryException;
    boolean removeDocument(ID dmsId) throws RepositoryException;
    boolean addOrEditProperty(ID dmsId, String propertyName, Object propertyValue, Class<T> metadataClass) throws RepositoryException, IllegalAccessException, InstantiationException;
    //endregion

    //region Operations with DocBinaryInfo Versions
    T addVersion(ID dmsId, T versionMetadata, E versionDataInfo) throws RepositoryException, IllegalAccessException;
    T updateVersion(ID dmsId, ID versionId, T versionMetadata, E versionDataInfo) throws RepositoryException, InstantiationException, IllegalAccessException;
    E getVersionDataInfo(ID dmsId, ID versionId, Class<E> metadataClass) throws IllegalAccessException, InstantiationException, RepositoryException;
    T getVersionMetadata(ID dmsId, ID versionId, Class<T> metadataClass) throws IllegalAccessException, InstantiationException, RepositoryException;
    T getVersionData(ID dmsId, ID versionId, Class<T> metadataClass) throws IllegalAccessException, InstantiationException, RepositoryException;
    List<T> getAllVersionsMetadata(ID dmsId, Class<T> metadataClass) throws RepositoryException, InstantiationException, IllegalAccessException;
    boolean selectDefaultVersion(ID dmsId, ID versionId) throws RepositoryException;
    boolean removeVersion(ID dmsId, ID versionId) throws RepositoryException;
    //endregion
}
