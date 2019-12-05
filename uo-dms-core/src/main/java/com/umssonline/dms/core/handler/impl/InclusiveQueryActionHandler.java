package com.umssonline.dms.core.handler.impl;

import com.umssonline.dms.core.dal.dao.DocBinaryInfo;
import com.umssonline.dms.core.dal.dao.DocumentSchema;
import com.umssonline.dms.core.dal.repository.CoreMetadataManager;
import com.umssonline.dms.core.dal.repository.impl.MetadataManagerImpl;
import com.umssonline.dms.core.handler.ActionHandler;
import com.umssonline.dms.core.utils.constants.DMSConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import java.io.IOException;
import java.util.Map;

/**
 * @author franco.arratia@umssonline.com
 */
public class InclusiveQueryActionHandler implements ActionHandler {

    //region Fields
    private static Logger logger = LoggerFactory.getLogger(InclusiveQueryActionHandler.class);
    //endregion

    //region ActionHandler Members
    @Override
    public Object handle(Map<String, Object> params) throws RepositoryException, IOException, IllegalAccessException {
        logger.info("InclusiveQueryActionHandler.handle() method");

        if (params == null || params.isEmpty()) {
            String errorMessage = "Parameter '%1$s' can not be null, it is required to query in the database.";
            throw new RepositoryException(String.format(errorMessage, DMSConstants.DMS_QUERY_PARAMETERS));
        }

        CoreMetadataManager<DocumentSchema, DocBinaryInfo, String> metadataManager = new MetadataManagerImpl<>();
        try {
            return metadataManager.getInclusiveBy(params, DocumentSchema.class);
        } catch (InstantiationException e) {
            throw new RepositoryException("Could no be gotten metadata for " + DocumentSchema.class.toString());
        }
    }
    //endregion
}
