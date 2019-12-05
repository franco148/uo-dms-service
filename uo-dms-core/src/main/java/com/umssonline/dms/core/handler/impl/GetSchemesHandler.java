package com.umssonline.dms.core.handler.impl;

import com.umssonline.dms.core.dal.dao.DocBinaryInfo;
import com.umssonline.dms.core.dal.repository.CoreMetadataManager;
import com.umssonline.dms.core.dal.repository.impl.MetadataManagerImpl;
import com.umssonline.dms.core.handler.ActionHandler;
import com.umssonline.dms.core.dal.dao.DocumentSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import java.io.IOException;
import java.util.Map;

/**
 * @author franco.arratia@umssonline.com
 */
public class GetSchemesHandler implements ActionHandler {

    //region Fields
    private static Logger logger = LoggerFactory.getLogger(GetSchemesHandler.class);
    //endregion

    //region ActionHandler Members
    @Override
    public Object handle(Map<String, Object> params) throws RepositoryException, IOException, IllegalAccessException {
        logger.info("GetSchemesHandler.handle() method");

        CoreMetadataManager<DocumentSchema, DocBinaryInfo, String> metadataManager = new MetadataManagerImpl<>();
        try {
            return metadataManager.getAllMetadata(DocumentSchema.class);
        } catch (InstantiationException e) {
            throw new RepositoryException("Could no be gotten metadata for " + DocumentSchema.class.toString());
        }
    }
    //endregion
}
