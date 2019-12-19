package com.umssonline.dms.core.handler.factory;

import com.umssonline.dms.core.handler.ActionHandler;
import com.umssonline.dms.core.handler.impl.*;

/**
 * @author franco.arratia@umssonline.com
 */
public class ActionHandlerFactory {

    //region Constructors
    private ActionHandlerFactory() {

    }
    //endregion

    //region Methods
    public static ActionHandler getActionHandler(final ActionHandlerOptions action) {

        ActionHandler actionHandler;

        switch (action) {
            case DMS_UPLOAD:
                actionHandler = new UploadActionHandler();
                break;

            case DMS_ALL_SCHEMES:
                actionHandler = new GetSchemesHandler();
                break;

            case DMS_DOWNLOAD:
                actionHandler = new DownloadActionHandler();
                break;

            case DMS_REMOVE:
                actionHandler = new DeleteActionHandler();
                break;

            case DMS_REMOVE_VERSION:
                actionHandler = new DeleteVersionActionHandler();
                break;

            case DMS_UPLOAD_VERSION:
                actionHandler = new UploadVersionActionHandler();
                break;

            case DMS_DOWNLOAD_VERSION:
                actionHandler = new DownloadVersionActionHandler();
                break;

            case DMS_ALL_VERSIONS:
                actionHandler = new GetVersionSchemesHandler();
                break;

            case DMS_SELECT_VERSION:
                actionHandler = new SelectVersionHandler();
                break;

            case DMS_SCHEMA:
                actionHandler = new GetSchemaActionHandler();
                break;

            case DMS_UPDATE:
                actionHandler = new UpdateActionHandler();
                break;

            case DMS_DOWNLOAD_VERSION_DATA:
                actionHandler = new GetVersionDataActionHandler();
                break;

            case DMS_UPDATE_VERSION:
                actionHandler = new UpdateVersionActionHandler();
                break;

            case DMS_INCLUSIVE_QUERY:
                actionHandler = new InclusiveQueryActionHandler();
                break;

            case DMS_EDIT_PROPERTY:
                actionHandler = new AddOrEditPropertyActionHandler();
                break;

            default:
                actionHandler = null;
                break;
        }

        return actionHandler;
    }
//endregion
}
