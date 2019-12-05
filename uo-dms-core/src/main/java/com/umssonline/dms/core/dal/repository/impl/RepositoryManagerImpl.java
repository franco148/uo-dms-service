package com.umssonline.dms.core.dal.repository.impl;

import com.umssonline.dms.core.config.ConfigurationFactory;
import com.umssonline.dms.core.config.DmsConfiguration;
import com.umssonline.dms.core.dal.repository.CustomLoginContext;
import com.umssonline.dms.core.dal.repository.RepositoryManager;
import com.umssonline.dms.core.utils.constants.DMSConstants;
import com.umssonline.dms.core.utils.constants.LuceneConstants;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.jackrabbit.oak.Oak;
import org.apache.jackrabbit.oak.jcr.Jcr;
import org.apache.jackrabbit.oak.jcr.repository.RepositoryImpl;
import org.apache.jackrabbit.oak.plugins.document.DocumentMK;
import org.apache.jackrabbit.oak.plugins.document.DocumentNodeStore;
import org.apache.jackrabbit.oak.plugins.index.lucene.LuceneIndexEditorProvider;
import org.apache.jackrabbit.oak.spi.state.NodeStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.*;
import javax.jcr.query.QueryManager;
import javax.jcr.version.VersionManager;
import java.io.InputStream;
import java.util.Map;

/**
 * @author franco.arratia@umssonline.com
 */
public class RepositoryManagerImpl implements RepositoryManager {

    //region Fields
    private static Logger logger = LoggerFactory.getLogger(RepositoryManagerImpl.class);
    private static Repository repository;
    private static NodeStore nodeStore;
    private static CustomLoginContext loginContext;

    private static RepositoryManagerImpl instance = null;
    //endregion

    //region Initializers
    private RepositoryManagerImpl() {}

    public static RepositoryManagerImpl getInstance() {

        if (instance == null) {
            instance = new RepositoryManagerImpl();
        }

        return instance;
    }
    //endregion

    //region RepositoryManager Members
    @Override
    public Session login() throws RepositoryException {
        Session session;
        DmsConfiguration repositoryConfiguration = ConfigurationFactory.getConfiguration();
        Map<String, String> repoConfigMap = repositoryConfiguration.getConfigurationParameters();
        String userName = (String)repoConfigMap.get("rep_username");
        String password = (String)repoConfigMap.get("rep_password");
        logger.debug("Prepaeing parameters to get Session..");

        try {
            SimpleCredentials credential = new SimpleCredentials(userName, password.toCharArray());
            session = repository.login(credential);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            session = null;
        }

        if(session == null) {
            throw new javax.jcr.LoginException("Could not obtain session from DMS dal");
        } else {
            return session;
        }
    }

    @Override
    public void logout(Session session) {

        if (session != null) {
            session.logout();
        }
    }

    @Override
    public QueryManager getQueryManager(Session session) throws RepositoryException {
        QueryManager queryManager = null;

        if (session != null && session.getWorkspace() != null) {
            queryManager = session.getWorkspace().getQueryManager();
        }

        return queryManager;
    }

    @Override
    public VersionManager getVersionManager(Session session) throws RepositoryException {
        VersionManager versionManager = null;

        if (session != null && session.getWorkspace() != null) {
            versionManager = session.getWorkspace().getVersionManager();
        }

        return versionManager;
    }
    //endregion

    //region Methods

    private static void start() throws RepositoryException {

        logger.info("Starting dal.........");

        DmsConfiguration repositoryConfiguration = ConfigurationFactory.getConfiguration();
        Map<String, String> repoConfigMap = repositoryConfiguration.getConfigurationParameters();
        String userName = (String)repoConfigMap.get("rep_username");
        String encryptPassword = (String)repoConfigMap.get("rep_password");

        Session session = null;
        try {
            repository = initContentMongoRepo(repoConfigMap);
            logger.info("Repository..........." + repository);
            SimpleCredentials credntial = new SimpleCredentials(userName, encryptPassword.toCharArray());
            session = repository.login(credntial);
            createIndex(session);


        } catch (Exception var23) {
            logger.error("Exception while login to DMS dal:  " + var23);
            throw new RepositoryException("Repository Start Exception");
        } finally {
            if(session != null) {
                session.logout();
            }

        }
        logger.info("Repository started successfully");
    }

    private static void createIndex(Session session) throws RepositoryException {

        Node oakIndexNode = session.getNode(DMSConstants.OAK_INDEX);
        String[] enclosingNodeTypes = new String[]{ DMSConstants.NT_UNSTRUCTURED };
        String[] indexpfsrealnameProp;

        if(!oakIndexNode.hasNode(DMSConstants.DMSID_INDEX)) {

            indexpfsrealnameProp = new String[]{ DMSConstants.DMS_ID };
            propertyIndexDefinition(session, DMSConstants.DMSID_INDEX, indexpfsrealnameProp, true, enclosingNodeTypes);
        }

        createLuceneIndex(session);
    }

    private static Node propertyIndexDefinition(Session session, String indexDefinitionName, String[] propertyNames,
                                         boolean unique, String[] enclosingNodeTypes) throws RepositoryException {

        Node root = session.getRootNode();
        Node indexDefRoot = JcrUtils.getOrAddNode(root, DMSConstants.OAK_INDEX_PARAM, DMSConstants.NT_UNSTRUCTURED);

        Node indexDef = JcrUtils.getOrAddNode(indexDefRoot, indexDefinitionName, DMSConstants.OAK_QUERY_INDEX_DEFINITION);
        indexDef.setProperty(LuceneConstants.PROPERTY_TYPE, LuceneConstants.VALUE_PROPERTY);
        indexDef.setProperty(LuceneConstants.PROPERTY_RE_INDEX, true);
        indexDef.setProperty(LuceneConstants.PROPERTY_PROPERTY_NAMES, propertyNames, 7);
        indexDef.setProperty(LuceneConstants.PROPERTY_UNIQUE, unique);

        if(enclosingNodeTypes != null && enclosingNodeTypes.length != 0) {
            indexDef.setProperty(LuceneConstants.PROPERTY_DECLARING_NODE_TYPES, enclosingNodeTypes, 7);
        }

        session.save();
        return indexDef;
    }

    private static void createLuceneIndex(Session session) throws RepositoryException {

        Node lucene = JcrUtils.getNodeIfExists(DMSConstants.LUCENE_INDEX_PATH, session);

        if(lucene == null) {
            InputStream is = RepositoryManager.class.getClassLoader().getResourceAsStream(DMSConstants.TIKA_CONFIG_PATH);

            if(is == null) {
                logger.info("Tika configurations not found.");
                return;
            }

            lucene = JcrUtils.getOrCreateByPath(DMSConstants.LUCENE_INDEX_PATH, DMSConstants.OAK_UNSTRUCTURED,
                                                DMSConstants.OAK_QUERY_INDEX_DEFINITION, session, false);

            lucene.setProperty(LuceneConstants.PROPERTY_COMPAT_VERSION, 2L);
            lucene.setProperty(LuceneConstants.PROPERTY_TYPE, LuceneConstants.VALUE_LUCENE);
            lucene.setProperty(LuceneConstants.PROPERTY_ASYNC, LuceneConstants.VALUE_ASYNC);

            Node tika = JcrUtils.getOrAddNode(lucene, LuceneConstants.VALUE_TIKA, DMSConstants.NT_UNSTRUCTURED);
            Node tikaConfig = JcrUtils.getOrAddNode(tika, LuceneConstants.VALUE_CONFIG_FILE, LuceneConstants.PROPERTY_LUCENE_FILE);
            Node config = JcrUtils.getOrAddNode(tikaConfig, DMSConstants.JCR_CONTENT, DMSConstants.NT_RESOURCE);

            Binary binary = session.getValueFactory().createBinary(is);
            config.setProperty(DMSConstants.JCR_DATA, binary);
            session.save();

            logger.info("Lucene index created");
        }
    }

    private static Repository initContentMongoRepo( Map<String, String> repoConfigMap){
        logger.debug("Mongo config parameters :" + (String)repoConfigMap.get("db_host"), repoConfigMap.get("db_port"), repoConfigMap.get("db_name"));

        Repository resp = null;

        String host = (String)repoConfigMap.get("db_host");
        String port = (String)repoConfigMap.get("db_port");
        String dbName = (String)repoConfigMap.get("db_name");
        int mongoDbPort = Integer.parseInt(port);

        DocumentMK.Builder docMKBuilder = new DocumentMK.Builder();
        DocumentMK.Builder builder = null;


        MongoClient e = new MongoClient(host, mongoDbPort);
        DB oakInstaqnce = e.getDB(dbName);
        builder = docMKBuilder.setMongoDB(oakInstaqnce);
        DocumentNodeStore e2 = builder.getNodeStore();
        Oak oakInstaqnce1 = new Oak(e2);
        Jcr jcrInstance = (new Jcr(oakInstaqnce1)).withAsyncIndexing().with(new LuceneIndexEditorProvider());
        resp = jcrInstance.createRepository();

        return resp;
    }

    //endregion

    //region Utilities
    public static void createIndexForMigrationNode(String mongoUri) throws RepositoryException {
        start();
    }

    public static void disposeRepository() {

        logger.info("Disposing the dal....");
        if (nodeStore != null && nodeStore instanceof DocumentNodeStore) {
            ((DocumentNodeStore)nodeStore).dispose();
        }

        if (repository != null && repository instanceof RepositoryImpl) {
            ((RepositoryImpl)repository).shutdown();
        }

        //Start()
    }

    //endregion
}
