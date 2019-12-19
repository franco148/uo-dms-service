package com.umssonline.dms.core.dal.repository;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.QueryManager;
import javax.jcr.version.VersionManager;

/**
 * @author franco.arratia@umssonline.com
 */
public interface RepositoryManager {

    Session login() throws RepositoryException;
    void logout(Session session);
    QueryManager getQueryManager(Session session) throws RepositoryException;
    VersionManager getVersionManager(Session session) throws RepositoryException;
}
