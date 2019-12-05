package com.umssonline.dms.core.handler.impl;

import com.umssonline.dms.core.dal.dao.DmsSchema;
import com.umssonline.dms.core.dal.dao.DocBinaryInfo;
import com.umssonline.dms.core.dal.dao.VersionSchema;
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
public class UploadVersionActionHandler implements ActionHandler {

    //region Fields
    private static Logger logger = LoggerFactory.getLogger(UploadVersionActionHandler.class);
    //endregion

    //region ActionHandler members
    @Override
    public Object handle(Map<String, Object> params) throws RepositoryException, IOException, IllegalAccessException {
        logger.info("UploadVersionActionHandler.handle() method");

        if (params == null || params.isEmpty() || !params.containsKey(DMSConstants.DMS_ID)) {
            String errorMessage = "Parameter '%1$s' can not be null, it is required to query in the database.";
            throw new RepositoryException(String.format(errorMessage, DMSConstants.DMS_ID));
        }

        String dmsId = params.get(DMSConstants.DMS_ID).toString();

        // Building the document schema information
        DmsSchema documentSchema = VersionSchema.build(params);
        documentSchema.setId(dmsId);

        // Building the document binary
        DocBinaryInfo docBinaryInfoInfo = new DocBinaryInfo().build(params);

        CoreMetadataManager<DmsSchema, DocBinaryInfo, String> metadataManager = new MetadataManagerImpl<>();

        return metadataManager.addVersion(documentSchema.getId(), documentSchema, docBinaryInfoInfo);
    }
    //endregion
}
