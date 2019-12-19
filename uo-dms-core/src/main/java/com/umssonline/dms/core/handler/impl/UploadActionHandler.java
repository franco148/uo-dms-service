package com.umssonline.dms.core.handler.impl;

import com.umssonline.dms.core.dal.dao.DmsSchema;
import com.umssonline.dms.core.dal.dao.DocBinaryInfo;
import com.umssonline.dms.core.handler.ActionHandler;
import com.umssonline.dms.core.dal.dao.DocumentSchema;
import com.umssonline.dms.core.dal.repository.CoreMetadataManager;
import com.umssonline.dms.core.dal.repository.impl.MetadataManagerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import java.io.IOException;
import java.util.Map;

/**
 * @author franco.arratia@umssonline.com
 */
public class UploadActionHandler implements ActionHandler {

    //region Fields
    private static Logger logger = LoggerFactory.getLogger(UploadActionHandler.class);
    //endregion

    //region ActionHandler members
    @Override
    public Object handle(Map<String, Object> params) throws RepositoryException, IOException, IllegalAccessException {
        logger.info("UploadActionHandler.handle() method");

        // Building the document schema information
        DmsSchema documentSchema = DocumentSchema.build(params);

        // Building the document binary
        DocBinaryInfo docBinaryInfoInfo = DocBinaryInfo.build(params);

        CoreMetadataManager<DmsSchema, DocBinaryInfo, String> metadataManager = new MetadataManagerImpl<>();

        return metadataManager.createMetadata(documentSchema, docBinaryInfoInfo);
    }
    //endregion

    //region Utilities

    //endregion
}
