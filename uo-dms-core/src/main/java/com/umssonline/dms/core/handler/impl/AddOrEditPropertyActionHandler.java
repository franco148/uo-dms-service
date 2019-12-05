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
 * @author franco.arratia@dharbor.com.
 */
public class AddOrEditPropertyActionHandler implements ActionHandler {

    //region Fields
    private static Logger logger = LoggerFactory.getLogger(AddOrEditPropertyActionHandler.class);
    //endregion

    //region ActionHandler Members
    @Override
    public Object handle(Map<String, Object> params) throws RepositoryException, IOException, IllegalAccessException {
        logger.info("AddOrEditPropertyActionHandler.handle() method");

        if (params == null || params.isEmpty() ||
            (!params.containsKey(DMSConstants.DMS_ID) && params.get(DMSConstants.DMS_ID) != null) ||
            (!params.containsKey(DMSConstants.DMS_PROPERTY_NAME) && params.get(DMSConstants.DMS_PROPERTY_NAME) != null) ||
            (!params.containsKey(DMSConstants.DMS_PROPERTY_VALUE) && params.get(DMSConstants.DMS_PROPERTY_VALUE) != null)) {
            String errorMessage = "Parameters '%1$s', '%2$s', '%3$s' can not be null, they are required to perform the operation.";
            throw new RepositoryException(String.format(errorMessage, DMSConstants.DMS_ID, DMSConstants.DMS_PROPERTY_NAME, DMSConstants.DMS_PROPERTY_VALUE));
        }

        String dmsId = params.get(DMSConstants.DMS_ID).toString();
        String propertyName = params.get(DMSConstants.DMS_PROPERTY_NAME).toString();
        Object propertyValue = params.get(DMSConstants.DMS_PROPERTY_VALUE);

        CoreMetadataManager<DocumentSchema, DocBinaryInfo, String> metadataManager = new MetadataManagerImpl<>();
        try {
            return metadataManager.addOrEditProperty(dmsId, propertyName, propertyValue, DocumentSchema.class);
        } catch (InstantiationException e) {
            throw new RepositoryException("Could no be gotten metadata for " + DocumentSchema.class.toString());
        }
    }
    //endregion
}
