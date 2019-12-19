package com.umssonline.dms.core.facade;

import com.umssonline.dms.core.handler.ActionHandler;
import com.umssonline.dms.core.handler.factory.ActionHandlerFactory;
import com.umssonline.dms.core.handler.factory.ActionHandlerOptions;
import com.umssonline.dms.core.dal.repository.impl.RepositoryManagerImpl;
import com.umssonline.dms.core.utils.common.ObjStatusChecker;
import com.umssonline.dms.core.facade.dto.RepositoryDTO;
import com.umssonline.dms.core.utils.constants.SchemaConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import java.io.IOException;
import java.util.Map;

/**
 * @author franco.arratia@umssonline.com
 */
public class DmsCoreFacade {

    //region Fields
    private static Logger logger = LoggerFactory.getLogger(DmsCoreFacade.class);

    private static final String FILE_CONTENT = "fileContent";
    private static DmsCoreFacade instance = null;
    //endregion

    //region Constructor
    private DmsCoreFacade() {

    }
    //endregion

    //region Methods

    public static DmsCoreFacade getInstance() {
        if (instance == null) {
            instance = new DmsCoreFacade();
        }

        return instance;
    }

    public Object executeAction(RepositoryDTO repositoryDTO) throws IOException, RepositoryException, IllegalAccessException {
        Object response = null;

        ActionHandlerOptions actionType = repositoryDTO.getActionType();
        Map<String, Object> parameters = repositoryDTO.getParams();

        if (repositoryDTO.getInputStream() != null)
            parameters.put(SchemaConstants.PROPERTY_BINARY, repositoryDTO.getInputStream());

        ActionHandler actionHandler = getActionHandler(actionType);
        if (ObjStatusChecker.isNotNull(actionHandler)) {
            response = actionHandler.handle(parameters);
        }

        return response;
    }

    public ActionHandler getActionHandler(ActionHandlerOptions actionType) {

        return ActionHandlerFactory.getActionHandler(actionType);
    }

    //endregion

    //region Utilities
    public static void createIndexForMigration(String mongoUri) throws RepositoryException {
        RepositoryManagerImpl.createIndexForMigrationNode(mongoUri);
    }

    public static void disposeRepository() {
        RepositoryManagerImpl.disposeRepository();
    }

    static {

        try {
            createIndexForMigration(null);
        } catch (RepositoryException e) {
            logger.error("Exception while starting DMS Repository: " + e.getMessage());
        }
    }
    //endregion
}
